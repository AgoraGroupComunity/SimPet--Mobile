package com.example.simpet01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Login extends AppCompatActivity {
    SessionManager sessionManager;
    TextView btn_daftarDisni;
    Button btn_login;
    EditText login_username,login_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(this);


        //        Span Text

        btn_daftarDisni=findViewById(R.id.btn_daftarDisini);
        String text = "Belum punya akun? Daftar disini.";
        SpannableString spannableString = new SpannableString(text);

        // Menambahkan tautan ke teks
        ClickableSpan spanDaftarDisini = new ClickableSpan() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this,Register.class);
                startActivity(i);
            }
        };
        spannableString.setSpan(spanDaftarDisini, text.indexOf("Daftar disini"), text.length(), 0);

        // Atur teks yang telah dibuat ke TextView
        btn_daftarDisni.setText(spannableString);

        // Aktifkan perpindahan ke tautan saat diklik
        btn_daftarDisni.setMovementMethod(LinkMovementMethod.getInstance());

        //       END Span Text

        // Validation Input
        btn_login = (Button) findViewById(R.id.btn_login);
        login_username = findViewById(R.id.login_username);
        login_password = findViewById(R.id.login_password);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username =login_username.getText().toString();
                String password =login_password.getText().toString();
                if (username.isEmpty() || password.isEmpty()){
                    Toast.makeText(Login.this, "Mohon isi Username dan Password Anda", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Toast.makeText(Login.this, "username: "+username+"pw: "+password, Toast.LENGTH_SHORT).show();
                    // RUN USER LOGIN API
                    OkHttpClient client = new OkHttpClient();
                    HttpUrl httpUrlLoginUser = new HttpUrl.Builder()
                            .scheme("https")
                            .host("us-east-1.aws.data.mongodb-api.com")
                            .addPathSegment("app")
                            .addPathSegment("application-0-exinc")
                            .addPathSegment("endpoint")
                            .addPathSegment("loginUser")
                            .addQueryParameter("username", username)
                            .addQueryParameter("password", HashMd5.hashMD5(HashMd5.hashMD5(password)))
                            .build();

                    Request request = new Request.Builder()
                            .url(httpUrlLoginUser)
                            .get()
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            showToast("Okhttp API Error");

                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String responseDataCheckUser = response.body().string();
                                        JSONArray jsonArray = new JSONArray(responseDataCheckUser);
                                        if (jsonArray.length() > 0) {
                                            JSONObject userObject = jsonArray.getJSONObject(0);
                                            String jsonId = userObject.getString("_id");
                                            String jsonAvatar = userObject.getString("avatar");
                                            String jsonUsername = userObject.getString("username");
                                            String jsonNama = userObject.getString("name");
                                            String jsonAlamat = userObject.getString("address");
                                            String jsonEmail = userObject.getString("email");
                                            String jsonNohp = userObject.getString("phone");
                                            String jsonPassword = userObject.getString("password");
                                            sessionManager.login(jsonId,jsonAvatar, jsonNama, jsonUsername, jsonAlamat, jsonNohp, jsonEmail, jsonPassword);

                                            // Data ada di dalam array, lakukan sesuatu dengan data
                                            Intent i = new Intent(Login.this, Home.class);
                                            startActivity(i);
                                            //showToast("mau finish");
                                            finish();
                                            //showToast("udah finsih");
                                        } else {
                                            // Data tidak ditemukan
                                            showToast("Username atau password Anda salah.");
                                        }
                                    } catch (JSONException | IOException e) {
                                        e.printStackTrace();
                                        showToast("Terjadi kesalahan saat mengambil atau mengurai data dari server");
                                    }
                                }
                            });
                        }
                    });

                    //END SECTION LOGIN API
                }
            }
        });
        // END Validation Input
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}