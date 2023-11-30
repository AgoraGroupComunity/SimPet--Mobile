package com.example.simpet01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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


public class EditPassword extends AppCompatActivity {
    EditText edit_passwordLama,edit_passwordBaru,edit_ulangiPassword;
    Button btn_backFromEditPassword, btn_simpanPassword;
    SessionManager sessionManager;
    HashMd5 hashMd5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn()) {
            // Pengguna belum login, arahkan ke halaman login
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            Toast.makeText(this, "kosong", Toast.LENGTH_SHORT).show();
            finish(); // Ini akan mengakhiri aktivitas saat ini agar pengguna tidak dapat kembali ke halaman beranda dengan tombol "Back"
        }

        edit_passwordLama = findViewById(R.id.edit_passwordLama);
        edit_passwordBaru = findViewById(R.id.edit_passwordBaru);
        edit_ulangiPassword = findViewById(R.id.edit_ulangiPassword);
        btn_backFromEditPassword = findViewById(R.id.btn_backFromEditPassword);
        btn_simpanPassword = findViewById(R.id.btn_simpanPassword);


        btn_simpanPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String passwordLama = edit_passwordLama.getText().toString();
               String passwordBaru = edit_passwordBaru.getText().toString();
               String ulangiPassword = edit_ulangiPassword.getText().toString();
               String idUser = sessionManager.getIdUser();

               if (passwordLama.isEmpty()||passwordBaru.isEmpty() ||ulangiPassword.isEmpty()){
                   showToast("tidak boleh kosong");
               }
               else{
                   String cekPassword = HashMd5.hashMD5(HashMd5.hashMD5(passwordLama));
                   if (cekPassword.equals(sessionManager.getPassword()) ){
                       if (passwordBaru.equals(ulangiPassword)){
                           showToast("Lanjut update password api");
                           OkHttpClient client = new OkHttpClient();
                           HttpUrl httpUrl = new HttpUrl.Builder()
                                   .scheme("https")
                                   .host("us-east-1.aws.data.mongodb-api.com")
                                   .addPathSegment("app")
                                   .addPathSegment("application-0-exinc")
                                   .addPathSegment("endpoint")
                                   .addPathSegment("updateImageMobile")
                                   .addQueryParameter("id", idUser)
                                   .build();
                           JSONObject jsonBody = new JSONObject();
                           try {
                               jsonBody.put("password",HashMd5.hashMD5(HashMd5.hashMD5(ulangiPassword)));

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
                                          showToast("Okhttp API Error");
                                       }
                                   });
                               }

                               @Override
                               public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                   runOnUiThread(new Runnable() {
                                       @Override
                                       public void run() {
                                           if (response.isSuccessful()){
                                               showToast("Sukses mengubah password. Silahkan masuk kembali.");
                                              //sessionManager.updaDateTanpaUsername(new_namaLengkap,new_nohp,new_email,new_alamat);
                                               sessionManager.logout();
                                               Intent i = new Intent(EditPassword.this,Home.class);
                                               startActivity(i);
                                               finish();
                                               //finish();
                                           } else {
                                               showToast("Kesalahan pada respon API. Coba lagi");
                                           }
                                       }
                                   });

                               }
                           });


                       } else {
                           showToast("Password baru tidak sesuai");
                       }
                   } else {
                       showToast("Password lama Anda salah");
                   }
               }

            }
        });

        btn_backFromEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(EditPassword.this,Home.class);
//                startActivity(i);
                finish();
            }
        });




    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}