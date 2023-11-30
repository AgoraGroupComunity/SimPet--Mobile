package com.example.simpet01.ApiController;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProductAPI {
    private String allProducts;
    Context context;

    public ProductAPI(Context context){
        this.context=context;
    }



    public String fetchAllProducts() {
        OkHttpClient client = new OkHttpClient();
        String url="https://us-east-1.aws.data.mongodb-api.com/app/application-0-exinc/endpoint/getProducts";
        HttpUrl httpUrlLoginUser = new HttpUrl.Builder()

                .scheme("https")
                .host("us-east-1.aws.data.mongodb-api.com")
                .addPathSegment("app")
                .addPathSegment("application-0-exinc")
                .addPathSegment("endpoint")
                .addPathSegment("getProducts")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("Okhttp API Error");
                    }
                });

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                ((Activity) context).runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            //showToast("respon sukses");
                            try {
                                //showToast("jalan awal");
                                String responseDataCheckUser = response.body().string();
                                allProducts= responseDataCheckUser;

                            } catch ( IOException e) {
                                e.printStackTrace();
                                //showToast("Terjadi kesalahan saat mengambil atau mengurai data dari server");
                            }
                        } else{
                            //showToast("Gagal response");
                        }

                    }
                });
            }
        });
        return allProducts;

    }

    public String getAllProducts() {
        return allProducts;
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
