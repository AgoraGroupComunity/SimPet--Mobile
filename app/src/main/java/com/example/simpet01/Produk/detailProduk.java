package com.example.simpet01.Produk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simpet01.ApiController.FormattedCurrency;
import com.example.simpet01.R;
import com.example.simpet01.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class detailProduk extends AppCompatActivity {

    ImageView detailImage,btn_backFromDetailProduct;
    TextView detailName,detailDescriptions,detailPrice,detailId;

    CardView btnAddToCart;
    SessionManager sessionManager;
    FormattedCurrency formattedCurrency;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);

        btnAddToCart=findViewById(R.id.btnAddToCart);
        btn_backFromDetailProduct=findViewById(R.id.btn_backFromDetailProduct);
        detailImage=findViewById(R.id.detailImage);
        detailName=findViewById(R.id.detailName);
        detailPrice=findViewById(R.id.detailPrice);
        detailId=findViewById(R.id.detailId);
        detailDescriptions=findViewById(R.id.detailDescriptions);
        sessionManager = new SessionManager(this);
        formattedCurrency = new FormattedCurrency();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            //Toast.makeText(detailProduk.this,"images "+bundle.getString("extraImage"),Toast.LENGTH_SHORT).show();

            String input = bundle.getString("extraImage");
            String[] substrings = {"data:image/png;base64,", "data:image/jpeg;base64,"};
            String avatarStringForMobile = checkUrlImage(input, substrings);
            Bitmap avatarBitmap =  decodeImage(avatarStringForMobile);


            detailImage.setImageBitmap(avatarBitmap);
            detailId.setText(bundle.getString("extraId"));
            detailName.setText(bundle.getString("extraName"));
            //detailPrice.setText("Rp"+bundle.getInt("extraPrice")+"");
            detailPrice.setText(formattedCurrency.formatCurrency(bundle.getInt("extraPrice")));
            detailDescriptions.setText(bundle.getString("extraDescriptions"));




        }else {
            showToast("Error bundle");
        }

        btn_backFromDetailProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String addCart = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-exinc/endpoint/addCart";

                addCartAPI(bundle.getString("extraId"),bundle.getString("extraImage"),bundle.getString("extraName"),bundle.getInt("extraPrice"),1);
            }
        });


    }

    public Bitmap decodeImage(String sImage) {

//        // decode base64 string
//        byte[] bytes=Base64.decode(sImage,Base64.DEFAULT);
//        // Initialize bitmap
//        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//        // set bitmap on imageView
//        imageView.setImageBitmap(bitmap);

        // Decode base64 string
        byte[] bytes = Base64.decode(sImage, Base64.DEFAULT);
        // Initialize bitmap
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

    }

    public String checkUrlImage(String input, String[] substrings) {
        for (String substring : substrings) {
            if (input.contains(substring)) {
                input = input.replace(substring, "");
            }
        }
        return input;
    }

    private void showToast(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();

    }


    private void addCartAPI(String productId,String productImage,String productName,Integer productPrice, Integer plusMinusButton){
        OkHttpClient client = new OkHttpClient();
        HttpUrl httpUrlAddToCart = new HttpUrl.Builder()
                .scheme("https")
                .host("us-east-1.aws.data.mongodb-api.com")
                .addPathSegment("app")
                .addPathSegment("application-0-exinc")
                .addPathSegment("endpoint")
                .addPathSegment("updateCart")
                .build();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("user_id", sessionManager.getIdUser());
            jsonBody.put("product_id", productId);
            jsonBody.put("product_image", productImage);
            jsonBody.put("product_name", productName);
            jsonBody.put("product_price", productPrice);
            jsonBody.put("amounts", plusMinusButton);
        } catch (JSONException e) {
            e.printStackTrace();
        };

        // Convert the JSONObject to a JSON string
        String jsonString = jsonBody.toString();

        // Create a RequestBody from the JSON string
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody requestBody = RequestBody.create(JSON, jsonString);

        Request request = new Request.Builder()
                .url(httpUrlAddToCart)
                .put(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {


            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //showToast("Error API");
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
              runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            showToast("Berhasil Menambahkan Keranjang");
                        } else {
                            showToast("Gagal Menambahkan Keranjang");
                        }
                    }
                });

            }
        });
    }


}