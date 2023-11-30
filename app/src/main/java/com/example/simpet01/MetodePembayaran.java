package com.example.simpet01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simpet01.ApiController.FormattedCurrency;

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

public class MetodePembayaran extends AppCompatActivity {
    Button btn_bayar;
    ImageView btn_backFromMetodePembayaran;
    TextView totalTagihan;
    FormattedCurrency formattedCurrency;
    TextView resultBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metode_pembayaran);

        totalTagihan=findViewById(R.id.totalTagihan);

        formattedCurrency=new FormattedCurrency();

        btn_bayar=findViewById(R.id.btn_bayar);
        btn_backFromMetodePembayaran=findViewById(R.id.btn_backFromMetodePembayaran);
        resultBundle=findViewById(R.id.resultBundle);

        Bundle bundleMetodePembayaran = getIntent().getExtras();

        String totalBayar= formattedCurrency.formatCurrency(Integer.parseInt(bundleMetodePembayaran.getString("total_tagihan")));
        totalTagihan.setText(totalBayar);

        String user_id = bundleMetodePembayaran.getString("user_id");
        String name = bundleMetodePembayaran.getString("name");
        String phone = bundleMetodePembayaran.getString("phone");
        String address = bundleMetodePembayaran.getString("address");
        String total_price = bundleMetodePembayaran.getString("total_price");
        String unique_code = bundleMetodePembayaran.getString("unique_code");
        String total_tagihan = bundleMetodePembayaran.getString("total_tagihan");
        String receipt_number =  bundleMetodePembayaran.getString("receipt_number");
        String courier =  bundleMetodePembayaran.getString("courier");
        String date = bundleMetodePembayaran.getString("date");
        String status = bundleMetodePembayaran.getString("status");
        String products = bundleMetodePembayaran.getString("products");

        //resultBundle.setText(products+"--->"+name+total_price);

        btn_backFromMetodePembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(MetodePembayaran.this, KonfirmasiPembayaran.class);
//                startActivity(i);
                //showToast(total_price);
                orderAPI( user_id,name, phone,  address, total_price, unique_code,
                         receipt_number, courier, date, status,products);
            }
        });
    }

    public void orderAPI(String user_id, String name,String phone, String address,String total_price,String unique_code,
                         String receipt_number,String courier,String date,String status, String products){



        OkHttpClient client = new OkHttpClient();
        String urlOrder= "https://us-east-1.aws.data.mongodb-api.com/app/application-0-exinc/endpoint/addShop?user_id="+user_id+"&name="+name+
                "&phone="+phone+"&address="+address+products+"&total_price="+total_price+"&unique_code="+unique_code+"&receipt_number="+
                receipt_number+"&courier="+courier+"&date="+date+"&status="+status;


        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", name);

        } catch (JSONException e) {
            e.printStackTrace();
        };

        // Convert the JSONObject to a JSON string
        String jsonString = jsonBody.toString();

        // Create a RequestBody from the JSON string
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody requestBody = RequestBody.create(JSON, jsonString);

        Request request = new Request.Builder()
                .url(urlOrder)
                .post(requestBody)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("Okhttp API error");
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            showToast("Berhasil order");
                            deleteAllCart( user_id);
//                            Intent i = new Intent(MetodePembayaran.this, KonfirmasiPembayaran.class);
//                            startActivity(i);

                        }else {
                            showToast("Gagal order");
                        }
                    }
                });
            }
        });

    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void deleteAllCart(String user_id){
        OkHttpClient client = new OkHttpClient();
        HttpUrl httpUrlDeleteProducts = new HttpUrl.Builder()
                .scheme("https")
                .host("us-east-1.aws.data.mongodb-api.com")
                .addPathSegment("app")
                .addPathSegment("application-0-exinc")
                .addPathSegment("endpoint")
                .addPathSegment("deleteCartMobile")
                .addQueryParameter("user_id", user_id)
                .build();

        Request request = new Request.Builder()
                .url(httpUrlDeleteProducts)
                .delete()  // Use the DELETE method for deletion
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
               runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("error api");
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
             runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            showToast("Keranjang sudah kosong");
                            Intent intent = new Intent(MetodePembayaran.this, KonfirmasiPembayaran.class);
                            startActivity(intent);

                        }else{
                            showToast("gagal respon");
                        }
                    }
                });
            }
        });
    }
}