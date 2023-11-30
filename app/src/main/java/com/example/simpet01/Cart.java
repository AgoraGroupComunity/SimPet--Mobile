package com.example.simpet01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simpet01.ApiController.FormattedCurrency;
import com.example.simpet01.ApiController.ProductAPI;
import com.example.simpet01.Produk.CartAdapter;
import com.example.simpet01.Produk.CartModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Cart extends AppCompatActivity {
    CartModel cartModel;
    Button btn_pesan;

    SessionManager sessionManager;

    TextView errorCart,subHarga,ongkosKirim,kodeTransaksi,totalHarga;

    ProductAPI productAPI;
    String allProducts;
    int receiptNumber;
    int randomNumber;
    int totalHargaItem;
    int SubTotalHarga = 0;
    String products="";
    String amounts;
    ShimmerFrameLayout shimmerFrameLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        errorCart=findViewById(R.id.errorCart);
        btn_pesan=findViewById(R.id.btn_pesan);
        subHarga=findViewById(R.id.subHarga);
        ongkosKirim=findViewById(R.id.ongkosKirim);
        kodeTransaksi=findViewById(R.id.kodeTransaksi);
        totalHarga=findViewById(R.id.totalHarga);
        //loading effect

        shimmerFrameLayout = findViewById(R.id.shimmer_view);
        shimmerFrameLayout.startShimmerAnimation();


        errorCart.setText(allProducts);

        sessionManager=new SessionManager(getApplicationContext());

        RecyclerView recyclerView = findViewById(R.id.recylcerViewCart);
        Random random = new Random();


        List<CartModel> listCart = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        HttpUrl httpUrlGetCart = new HttpUrl.Builder()

                .scheme("https")
                .host("us-east-1.aws.data.mongodb-api.com")
                .addPathSegment("app")
                .addPathSegment("application-0-exinc")
                .addPathSegment("endpoint")
                .addPathSegment("getCartMobile")
                .addEncodedQueryParameter("user_id",sessionManager.getIdUser())
                .build();
        //showToast(sessionManager.getIdUser());

        Request request = new Request.Builder()
                .url(httpUrlGetCart)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("Okhttp API Error");

                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            try {
                                String responseCart = response.body().string();
                                JSONArray jsonArray = new JSONArray(responseCart);
                                // Inisialisasi total harga


                                if (jsonArray.length() > 0) {
                                    for (int iCart = 0; iCart < jsonArray.length(); iCart++) {
                                        JSONObject cartObject = jsonArray.getJSONObject(iCart);
                                        String jsonId = cartObject.getString("_id");
                                        String jsonUserId = cartObject.getString("user_id");
                                        String jsonProductId = cartObject.getString("product_id");
                                        String jsonProductName = cartObject.getString("product_name");
                                        Integer jsonProductPrice = cartObject.getInt("product_price");
                                        String jsonProductImage = cartObject.getString("product_image");
                                        Integer jsonAmounts = cartObject.getInt("amounts");

                                        products += "&product"+iCart+"="+jsonProductName+"&amounts_product"+iCart+"="+jsonAmounts;                                      ;
//                                        amounts += "&amount"+iCart+"="+jsonAmounts;


                                        // Menghitung total harga untuk item ini
                                        totalHargaItem = jsonProductPrice * jsonAmounts;

                                        // Menambahkan total harga item ke totalHarga keseluruhan
                                        SubTotalHarga += totalHargaItem;

                                        cartModel = new CartModel();

                                        cartModel.setProductId(jsonProductId);
                                        cartModel.setCartImage(jsonProductImage);
                                        cartModel.setCartName(jsonProductName);
                                        cartModel.setCartPrice(jsonProductPrice);
                                        cartModel.setCartAmount(jsonAmounts);

                                        listCart.add(cartModel);

                                        // Atur RecyclerView setelah loop selesai
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                        recyclerView.setAdapter(new CartAdapter(Cart.this, listCart));

                                    }

//
                                    // Menampilkan total harga di TextView
                                    subHarga.setText(FormattedCurrency.formatCurrency(SubTotalHarga));
                                    ongkosKirim.setText(FormattedCurrency.formatCurrency(SubTotalHarga/10));


                                    randomNumber = random.nextInt(900) + 100;
                                    kodeTransaksi.setText(randomNumber+"");
                                    totalHarga.setText(FormattedCurrency.formatCurrency(SubTotalHarga+(SubTotalHarga/10)+randomNumber));

                                    receiptNumber = random.nextInt(9000000) + 1000000;

                                    //showToast("Selesai mengambil data");

                                } else {
                                    showToast("Keranjang Anda kosong");
                                }
                                shimmerFrameLayout.stopShimmerAnimation();
                                shimmerFrameLayout.setVisibility(View.INVISIBLE);

                            } catch (JSONException | IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });

            }
        }); //end api cart




        btn_pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SubTotalHarga>0){
                    //showToast(SubTotalHarga+"");



                    Intent i = new Intent(Cart.this, MetodePembayaran.class);
                    Bundle bundleCart = new Bundle();
                    bundleCart.putString("user_id",sessionManager.getIdUser());
                    bundleCart.putString("name",sessionManager.getUsername());
                    bundleCart.putString("phone",sessionManager.getNohp());
                    bundleCart.putString("address",sessionManager.getAlamat());
                    bundleCart.putString("total_price",SubTotalHarga+"");
                    bundleCart.putString("unique_code",randomNumber+"");
                    bundleCart.putString("total_tagihan",(SubTotalHarga+(SubTotalHarga/10)+randomNumber)+"");
                    bundleCart.putString("receipt_number",receiptNumber+"");
                    bundleCart.putString("courier","SiCepat");
                    bundleCart.putString("date",getDate());
                    bundleCart.putString("status","Diproses");
                    bundleCart.putString("products",products);

                    // Add the list of CartItemParcelable objects to the bundle
                    //bundleCart.putParcelableArrayList("cart_items", cartList);
                    i.putExtras(bundleCart);
                    startActivity(i);
                }else {
                    showToast("Sedang memuat data");
                }


            }
        });




    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public String getDate(){
        // Get the current date and time
        Date currentDate = new Date();

        // Define the date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        // Format the current date and time using the specified format
        String formattedDateTime = dateFormat.format(currentDate);

        return formattedDateTime;
    }

    public void btn_backFromKeranjang(View v){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

}