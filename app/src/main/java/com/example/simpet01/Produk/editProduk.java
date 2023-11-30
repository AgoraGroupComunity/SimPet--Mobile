package com.example.simpet01.Produk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class editProduk extends AppCompatActivity {

    private ImageConverter imageConverter;

    EditText edit_namaBarang,edit_kategoriBarang,edit_hargaBarang,edit_deskripsiBarang;
    CardView btn_simpanBarang;

    ImageView btn_deleteBarang;

    TextView idBarang;

    ImageView editImageProduct;
    TextView urlImage;

    Button btn_editImage;
    String stringImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_barang);

        edit_namaBarang =findViewById(R.id.edit_namaBarang);
        edit_hargaBarang =findViewById(R.id.edit_hargaBarang);
        edit_deskripsiBarang =findViewById(R.id.edit_deskripsiBarang);
        editImageProduct =findViewById(R.id.editImageProduct);
        idBarang=findViewById(R.id.idBarang);
        urlImage=findViewById(R.id.urlImage);
        btn_editImage=findViewById(R.id.btn_editImage);


        btn_deleteBarang =findViewById(R.id.btn_deleteBarang);
        btn_simpanBarang =findViewById(R.id.btn_simpanBarang);

        imageConverter = new ImageConverter(this, urlImage, editImageProduct);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            //Toast.makeText(detailProduk.this,"images "+bundle.getString("extraImage"),Toast.LENGTH_SHORT).show();

            String input = bundle.getString("extraImage");
            String[] substrings = {"data:image/png;base64,", "data:image/jpeg;base64,"};
            String avatarStringForMobile = checkUrlImage(input, substrings);
            Bitmap avatarBitmap =  decodeImage(avatarStringForMobile);

            urlImage.setText(avatarStringForMobile);
            editImageProduct.setImageBitmap(avatarBitmap);
            idBarang.setText(bundle.getString("extraId"));
            edit_namaBarang.setText(bundle.getString("extraName"));
            edit_hargaBarang.setText(bundle.getInt("extraPrice")+"");
            edit_deskripsiBarang.setText(bundle.getString("extraDescriptions"));




        }else {
            showToast("Error bundle");
        }

        btn_editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("pilih gambar");
                imageConverter.selectImage();
            }
        });

        btn_simpanBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //urlImage.setText(stringImage);

                String checkUrlImage = urlImage.getText().toString();
                String keyId = idBarang.getText().toString();
                String new_namaBarang = edit_namaBarang.getText().toString();
                String new_hargaBarang = edit_hargaBarang.getText().toString();
                String new_deskripsiBarang = edit_deskripsiBarang.getText().toString();

                String image1 =bundle.getString("secondImage");
                String image2 =bundle.getString("thirdImage");
                String image3 =bundle.getString("fourthImage");

                if (checkUrlImage.isEmpty()||keyId.isEmpty()||new_namaBarang.isEmpty()||new_hargaBarang.isEmpty()||new_deskripsiBarang.isEmpty()){
                    showToast("Lengkapi semua data produk");
                } else{
                    //update data
                    updateProductAPI( keyId,checkUrlImage, new_namaBarang,  new_hargaBarang,  new_deskripsiBarang,  image1, image2, image3 );

                }

            }
        });

        btn_deleteBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient client = new OkHttpClient();
                HttpUrl httpUrlDeleteProducts = new HttpUrl.Builder()
                        .scheme("https")
                        .host("us-east-1.aws.data.mongodb-api.com")
                        .addPathSegment("app")
                        .addPathSegment("application-0-exinc")
                        .addPathSegment("endpoint")
                        .addPathSegment("deleteProducts")
                        .addQueryParameter("id", idBarang.getText().toString())
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
                                    showToast("berhasil hapus");
                                    Intent intent = new Intent(getApplicationContext(), Home.class);
                                    intent.putExtra("FRAGMENT_IDENTIFIER", "catalog"); // Ganti dengan identifier yang sesuai
                                    startActivity(intent);
                                }else{
                                    showToast("gagal respon");
                                }
                            }
                        });
                    }
                });

            }
        });

    }
    public void btn_backeditBarang(View v){
        Intent intent = new Intent(this, Home.class);
        intent.putExtra("FRAGMENT_IDENTIFIER", "catalog"); // Ganti dengan identifier yang sesuai
        startActivity(intent);

    }

    public Bitmap decodeImage(String sImage) {

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

    private void updateProductAPI(String keyId,String urlImage,String new_namaBarang, String new_hargaBarang, String new_deskripsiBarang, String image1,String image2,String image3){
        OkHttpClient client = new OkHttpClient();
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("us-east-1.aws.data.mongodb-api.com")
                .addPathSegment("app")
                .addPathSegment("application-0-exinc")
                .addPathSegment("endpoint")
                .addPathSegment("updateProductsMobile")
                .addQueryParameter("id", keyId)
                .build();
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name",new_namaBarang);
            jsonBody.put("price",new_hargaBarang);
            jsonBody.put("descriptions",new_deskripsiBarang);

            JSONObject imagesObject = new JSONObject();
            imagesObject.put("image0","data:image/jpeg;base64,"+urlImage);
            imagesObject.put("image1", image1);
            imagesObject.put("image2",image2);
            imagesObject.put("image3",image3);
            jsonBody.put("images", imagesObject);


        } catch (JSONException e) {
            e.printStackTrace();
        };
        //Toast.makeText(EditProfil.this,"jsonobject",Toast.LENGTH_LONG).show();

        // Convert the JSONObject to a JSON string
        String jsonString = jsonBody.toString();

        // Create a RequestBody from the JSON string
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody requestBody = RequestBody.create(JSON, jsonString);
        //Toast.makeText(EditProfil.this,"request body",Toast.LENGTH_LONG).show();

        Request request = new Request.Builder()
                .url(httpUrl)
                .put(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("error api okhttp");
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()){
                            showToast("Sukses update produk");
                            Intent intent = new Intent(getApplicationContext(), Home.class);
                            intent.putExtra("FRAGMENT_IDENTIFIER", "catalog"); // Ganti dengan identifier yang sesuai
                            startActivity(intent);

                        } else {
                            showToast("error respon api");

                        }
                    }
                });

            }
        });

    }


}