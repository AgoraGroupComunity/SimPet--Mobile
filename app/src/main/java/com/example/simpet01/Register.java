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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Register extends AppCompatActivity {

    EditText register_namaLengkap,register_username,register_nohp,register_alamat,register_email,register_password,register_ulangiPassword ;
    Button btn_daftar;

    TextView btn_masukDisini;


    String sImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // START SPAN CLICKABLE
        btn_masukDisini=findViewById(R.id.btn_masukDisini);
        String text = "Sudah punya akun? Masuk disini.";
        SpannableString spannableString = new SpannableString(text);

        // Menambahkan tautan ke teks
        ClickableSpan spanDaftarDisini = new ClickableSpan() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register.this,Login.class);
                startActivity(i);
            }
        };
        spannableString.setSpan(spanDaftarDisini, text.indexOf("Masuk disini"), text.length(), 0);

        // Atur teks yang telah dibuat ke TextView
        btn_masukDisini.setText(spannableString);

        // Aktifkan perpindahan ke tautan saat diklik
        btn_masukDisini.setMovementMethod(LinkMovementMethod.getInstance());

        // END SPAN CLICKABLE

        // START REGISTER VALIDATION
        register_namaLengkap = findViewById(R.id.register_namaLengkap);
        register_username = findViewById(R.id.register_username);
        register_nohp = findViewById(R.id.register_nohp);
        register_alamat = findViewById(R.id.register_alamat);
        register_email = findViewById(R.id.register_email);
        register_password = findViewById(R.id.register_password);
        register_ulangiPassword = findViewById(R.id.register_ulangiPassword);
        btn_daftar= findViewById(R.id.btn_daftar);







        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaLengkap = register_namaLengkap.getText().toString();
                String username = register_username.getText().toString();
                String nohp = register_nohp.getText().toString();
                String alamat = register_alamat.getText().toString();
                String email = register_email.getText().toString();
                String password = register_password.getText().toString();
                String ulangiPassword = register_ulangiPassword.getText().toString();

                if (namaLengkap.isEmpty() || username.isEmpty()|| nohp.isEmpty()|| email.isEmpty()|| alamat.isEmpty()|| password.isEmpty()|| ulangiPassword.isEmpty()){

                    Toast.makeText(Register.this, "Mohon lengkapi data Anda", Toast.LENGTH_SHORT).show();
                }
                else{
//                    if (username.contains(" ")){
//                        showToast("Username tidak boleh menggunakan spasi.");
//                    } else if (!username.contains(" ") && password.equals(ulangiPassword)) {
//
//                    }

                    //PASSWORD CHECKED, TRUE CONDITION -> INSERT DATA TO DATABASE
                    if (!username.contains(" ") && password.equals(ulangiPassword)){

                        // Toast.makeText(Register.this, "data: "+namaLengkap+" | "+username+" | "+nohp+" | "+alamat+" | "+password+" | "+ulangiPassword, Toast.LENGTH_SHORT).show();
                        Toast.makeText(Register.this, "Mengirim data...", Toast.LENGTH_SHORT).show();


                        OkHttpClient client = new OkHttpClient();
                        HttpUrl httpUrlCheckUser = new HttpUrl.Builder()
                                .scheme("https")
                                .host("us-east-1.aws.data.mongodb-api.com")
                                .addPathSegment("app")
                                .addPathSegment("application-0-exinc")
                                .addPathSegment("endpoint")
                                .addPathSegment("checkUser")
                                .addQueryParameter("username", username)
                                .build();

                        Request request = new Request.Builder()
                                .url(httpUrlCheckUser)
                                .get()
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                // Tangani kesalahan yang terjadi saat permintaan gagal
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Register.this, "Okhttp API Error", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                // Tangani respon dari permintaan
                                if (response.isSuccessful()) {
                                    String responseDataCheckUser = response.body().string();
                                    try {
                                        JSONArray jsonArray = new JSONArray(responseDataCheckUser);

                                        if (jsonArray.length() > 0) {
                                            // Data ada di dalam array, lakukan sesuatu dengan data
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(Register.this, "Username sudah digunakan", Toast.LENGTH_SHORT).show();
                                                    //Log.d("API Response", responseDataCheckUser);

                                                }
                                            });
                                        } else {
                                            // Data dalam array kosong
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //Toast.makeText(Register.this, "Username belum digunakan", Toast.LENGTH_SHORT).show();
                                                    // Lakukan proses penyimpanan data baru disini
                                                    //INSERT DATA
                                                    OkHttpClient client = new OkHttpClient();
                                                    HttpUrl httpUrl = new HttpUrl.Builder()
                                                            .scheme("https")
                                                            .host("us-east-1.aws.data.mongodb-api.com")
                                                            .addPathSegment("app")
                                                            .addPathSegment("application-0-exinc")
                                                            .addPathSegment("endpoint")
                                                            .addPathSegment("registerUser")
                                                            .addQueryParameter("name", namaLengkap)
                                                            .addQueryParameter("username",username )
                                                            .addQueryParameter("email", email)
                                                            .addQueryParameter("phone", nohp)
                                                            .addQueryParameter("address", alamat)
                                                            .addQueryParameter("password", HashMd5.hashMD5(HashMd5.hashMD5(ulangiPassword)))
                                                            .build();

                                                    //https://us-east-1.aws.data.mongodb-api.com/app/application-0-exinc/endpoint/registerUser
                                                    JSONObject jsonBody = new JSONObject();
                                                    try {
                                                        jsonBody.put("name", namaLengkap);
                                                        jsonBody.put("username", username);
                                                        jsonBody.put("email", email);
                                                        jsonBody.put("phone", nohp);
                                                        jsonBody.put("address", alamat);
                                                        jsonBody.put("password",HashMd5.hashMD5(HashMd5.hashMD5(ulangiPassword)));
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
                                                                    Toast.makeText(Register.this, "Gagal mendaftarkan akun. Silahkan coba lagi.", Toast.LENGTH_SHORT).show();

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
                                                                        Toast.makeText(Register.this, "Berhasil mendaftarkan akun. Silahkan masuk dengan akun Anda.", Toast.LENGTH_SHORT).show();
                                                                        register_namaLengkap.setText("");
                                                                        register_email.setText("");
                                                                        register_nohp.setText("");
                                                                        register_password.setText("");
                                                                        register_ulangiPassword.setText("");
                                                                        register_username.setText("");
                                                                        Intent i = new Intent(Register.this, Login.class);
                                                                        startActivity(i);
                                                                        finish();
                                                                    } else {
                                                                        Toast.makeText(Register.this, "Gagal mendaftarkan akun. Silahkan coba lagi.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        // Tangani kesalahan saat mengurai JSON
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(Register.this, "Terjadi kesalahan saat mengurai JSON", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }
                                } else {
                                    // Data gagal diambil dari API, tangani kesalahan
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(Register.this, "Gagal Ambil API mendaftarkan akun. Silahkan coba lagi.", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                            }
                        });

                    }
                    //END (IF CONDITION FOR INSERT NEW DATA)
                    else{
                        if (username.contains(" ")){
                            showToast("Username tidak boleh mengandung spasi.");
                        }
                        else {
                            //CHECKER CONFIRMATION PASSWORD
                            Toast.makeText(Register.this, "Password Anda tidak sama", Toast.LENGTH_SHORT).show();
                        }


                    }
                }
            }
        });

        // END REGISTER VALIDATION

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}