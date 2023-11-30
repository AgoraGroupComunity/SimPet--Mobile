package com.example.simpet01.Produk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simpet01.ApiController.ImageConverter;
import com.example.simpet01.Home;
import com.example.simpet01.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class createProduk extends AppCompatActivity {

    EditText add_namaProduk, add_hargaProduk, add_deskripsiProduk;
    TextView urlImage;
    Button btn_uploadImage;
    String stringImage;

    ImageConverter imageConverter;

    ImageView addImageProduk,btn_backFromCreateProduct;

    CardView btn_tambahProduk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_produk);

        add_namaProduk = findViewById(R.id.add_namaProduk);
        add_hargaProduk = findViewById(R.id.add_hargaProduk);
        add_deskripsiProduk = findViewById(R.id.add_deskripsiProduk);
        btn_uploadImage = findViewById(R.id.btn_uploadImage);
        urlImage = findViewById(R.id.urlImage);
        addImageProduk = findViewById(R.id.addImageProduk);
        btn_tambahProduk = findViewById(R.id.btn_tambahProduk);
        btn_backFromCreateProduct = findViewById(R.id.btn_backFromCreateProduct);
        urlImage.setText("");

        imageConverter = new ImageConverter(this, urlImage, addImageProduk);
        btn_backFromCreateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                Intent intent = new Intent(getApplicationContext(), Home.class);
//                intent.putExtra("FRAGMENT_IDENTIFIER", "catalog"); // Ganti dengan identifier yang sesuai
//                startActivity(intent);
            }
        });


        btn_uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start
                imageConverter.selectImage();
            }
        });

        btn_tambahProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showToast("klik");

                String namaProduk = add_namaProduk.getText().toString();
                String stringHargaProduk = add_hargaProduk.getText().toString();
                String deskripsiProduk = add_deskripsiProduk.getText().toString();
                String stringImage = urlImage.getText().toString();
                //api add product
                urlImage.setText(stringImage);
                //showToast(namaProduk+stringHargaProduk+deskripsiProduk+stringImage);
                if(namaProduk.isEmpty()||stringHargaProduk.isEmpty()||deskripsiProduk.isEmpty()||stringImage.isEmpty()){
                    showToast("Lengkapi semua data");
                } else{
                    //showToast("data terisi");
                    try {
                        addProductAPI(namaProduk,stringHargaProduk,deskripsiProduk,stringImage);
                    } catch (JSONException | UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the result to the ImageConverter
        imageConverter.onActivityResult(requestCode, resultCode, data);
        // Set the 'urlImage' variable with the value from ImageConverter
        stringImage = imageConverter.getSImage();
        urlImage.setText(stringImage);
    }

    private void showToast(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void addProductAPI( String name, String price, String descriptions, String image0) throws JSONException, UnsupportedEncodingException {
        OkHttpClient client = new OkHttpClient();


        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("us-east-1.aws.data.mongodb-api.com")
                .addPathSegment("app")
                .addPathSegment("application-0-exinc")
                .addPathSegment("endpoint")
                .addPathSegment("addProduct")
                .addQueryParameter("name", name)
                .addQueryParameter("price",price+"")
                .addQueryParameter("descriptions", descriptions)
                .addQueryParameter("image0", "data:image/jpeg;base64,"+image0)
                .addQueryParameter("image1", "")
                .addQueryParameter("image2", "")
                .addQueryParameter("image3", "")
                .build();

        //https://us-east-1.aws.data.mongodb-api.com/app/application-0-exinc/endpoint/registerUser
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", name);
            jsonBody.put("price", price);
            jsonBody.put("descriptions", descriptions);
            jsonBody.put("image0", "data:image/jpeg;base64,"+image0);
            jsonBody.put("image1", "");
            jsonBody.put("image2", "");
            jsonBody.put("image3", "");
        } catch (JSONException e) {
            e.printStackTrace();
        };

        // Convert the JSONObject to a JSON string
        String jsonString = jsonBody.toString();

        // Create a RequestBody from the JSON string
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody requestBody = RequestBody.create(JSON, jsonString);

        Request request = new Request.Builder()
                .url(httpUrl)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // Tangani kesalahan yang terjadi saat permintaan gagal
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error okhttp", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // Tangani respon dari permintaan
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Berhasil menambah produk", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Home.class);
                            intent.putExtra("FRAGMENT_IDENTIFIER", "catalog"); // Ganti dengan identifier yang sesuai
                            startActivity(intent);

                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal menambah produk", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

}