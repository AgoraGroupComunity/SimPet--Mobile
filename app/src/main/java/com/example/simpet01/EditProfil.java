package com.example.simpet01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simpet01.ApiController.ImageConverter;

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

public class EditProfil extends AppCompatActivity {
    SessionManager sessionManager;
    Button btn_uploadImage,btn_updateImage,btn_simpanProfil,btn_backFromEditProfil;
    private TextView textView,tes_id;
    private ImageView imageView;
    private ImageConverter imageConverter;
    String urlImage;

    EditText edit_namaLengkap,edit_username,edit_nohp,edit_alamat,edit_email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn()) {
            // Pengguna belum login, arahkan ke halaman login
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            //Toast.makeText(this, "kosong", Toast.LENGTH_SHORT).show();
            finish(); // Ini akan mengakhiri aktivitas saat ini agar pengguna tidak dapat kembali ke halaman beranda dengan tombol "Back"
        }

        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        btn_uploadImage=findViewById(R.id.btn_uploadImage);
        tes_id = findViewById(R.id.tes_id);
        btn_updateImage = findViewById(R.id.btn_updateImage);
        edit_namaLengkap = findViewById(R.id.edit_namaLengkap);
        edit_username = findViewById(R.id.edit_username);
        edit_nohp = findViewById(R.id.edit_nohp);
        edit_alamat = findViewById(R.id.edit_alamat);
        edit_email = findViewById(R.id.edit_email);
        btn_simpanProfil = findViewById(R.id.btn_simpanProfil);
        btn_backFromEditProfil = findViewById(R.id.btn_backFromEditProfil);


        String avatarString = sessionManager.getAvatar();
        imageConverter = new ImageConverter(this, textView, imageView);

        if (avatarString.isEmpty()||avatarString.equals("{}")){
            imageView.setImageResource(R.drawable.profile_picture);
            //Toast.makeText(EditProfil.this,"Profil kosong",Toast.LENGTH_SHORT).show();
        }
        else {
//            String avatarStringForMobile = avatarString.replace("data:image/jpeg;base64,","");
//            Bitmap avatarBitmap = imageConverter.decodeImage(avatarStringForMobile);
            String input = avatarString;
            String[] substrings = {"data:image/png;base64,", "data:image/jpeg;base64,"};
            String avatarStringForMobile = imageConverter.checkUrlImage(input, substrings);
            Bitmap avatarBitmap =  imageConverter.decodeImage(avatarStringForMobile);
            imageView.setImageBitmap(avatarBitmap);
            //Toast.makeText(EditProfil.this,"Ada foto profil",Toast.LENGTH_SHORT).show();
        }

        edit_namaLengkap.setText(sessionManager.getNama());
        edit_username.setText(sessionManager.getUsername());
        edit_nohp.setText(sessionManager.getNohp());
        edit_alamat.setText(sessionManager.getAlamat());
        edit_email.setText(sessionManager.getEmail());


        imageConverter = new ImageConverter(this, textView, imageView);
        String idUser= sessionManager.getIdUser();

        btn_uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use the imageConverter object to select and convert images
                imageConverter.selectImage();
                tes_id.setText(idUser);

            }
        });

        btn_updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tes_id.getText().equals("")|| urlImage==null){
                    Toast.makeText(EditProfil.this,"Gagal mendapatkan Id dan urlImage",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(EditProfil.this,"Mengirim data...",Toast.LENGTH_LONG).show();
                    OkHttpClient client = new OkHttpClient();
                    HttpUrl httpUrl = new HttpUrl.Builder()
                            .scheme("https")
                            .host("us-east-1.aws.data.mongodb-api.com")
                            .addPathSegment("app")
                            .addPathSegment("application-0-exinc")
                            .addPathSegment("endpoint")
                            .addPathSegment("updateImageMobile")
                            .addQueryParameter("id", idUser)
//                        .addQueryParameter("avatar", hasilBaseImage)
                            .build();
                    //Toast.makeText(EditProfil.this,"baru client jalan" +urlImage,Toast.LENGTH_LONG).show();

                    JSONObject jsonBody = new JSONObject();
                    try {
                        //jsonBody.put("id", idUser);
                        jsonBody.put("avatar","data:image/jpeg;base64,"+urlImage.toString());
                        //Toast.makeText(EditProfil.this,"try avatar "+urlImage,Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(EditProfil.this, "Okhttp API Error", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (response.isSuccessful()){
                                        sessionManager.editImage(urlImage);
                                        Toast.makeText(EditProfil.this, "Sukses update foto profil", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(EditProfil.this, "Kesalahan pada respon API", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                        }
                    });
                }
            }
        });

        btn_simpanProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_namaLengkap = edit_namaLengkap.getText().toString();
                String new_username = edit_username.getText().toString();
                String new_nohp = edit_nohp.getText().toString();
                String new_email = edit_email.getText().toString();
                String new_alamat = edit_alamat.getText().toString();

                if (new_namaLengkap.isEmpty()|| new_namaLengkap.charAt(0)==' '||new_username.contains(" ")|| new_username.isEmpty()||new_username.charAt(0)==' '||
                        new_nohp.isEmpty()|| new_nohp.charAt(0)==' '||new_email.isEmpty()|| new_email.charAt(0)==' '||new_alamat.isEmpty()|| new_alamat.charAt(0)==' '){
                    showToast("Lengkapi semua data, tidak boleh diawali dengan spasi.");
                } else{
                    //showToast("Terisi semua");

                    if (new_username.equals(sessionManager.getUsername())){
                        //update tanpa mengirin username
                        showToast("proses tanpa update username");
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
                            jsonBody.put("name",new_namaLengkap);
                            jsonBody.put("email",new_email);
                            jsonBody.put("phone",new_nohp);
                            jsonBody.put("address",new_alamat);
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
                                        Toast.makeText(EditProfil.this, "Okhttp API Error", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (response.isSuccessful()){
                                            //sessionManager.editImage(urlImage);
                                            Toast.makeText(EditProfil.this, "Sukses update data profil", Toast.LENGTH_SHORT).show();
                                            sessionManager.updaDateTanpaUsername(new_namaLengkap,new_nohp,new_email,new_alamat);
                                        } else {
                                            Toast.makeText(EditProfil.this, "Kesalahan pada respon API. Coba lagi", Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });

                            }
                        });
                    } else{
                        //update dengan username yang baru
                        //cek ketersediaan username
                        OkHttpClient client = new OkHttpClient();
                        HttpUrl httpUrlCheckUser = new HttpUrl.Builder()
                                .scheme("https")
                                .host("us-east-1.aws.data.mongodb-api.com")
                                .addPathSegment("app")
                                .addPathSegment("application-0-exinc")
                                .addPathSegment("endpoint")
                                .addPathSegment("checkUser")
                                .addQueryParameter("username", new_username)
                                .build();

                        Request request = new Request.Builder()
                                .url(httpUrlCheckUser)
                                .get()
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showToast("Okhtttp API Error");

                                    }
                                });
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                if (response.isSuccessful()){
                                    String responseDataCheckUser = response.body().string();
                                    try {
                                        JSONArray jsonArray = new JSONArray(responseDataCheckUser);

                                        if (jsonArray.length() > 0) {
                                            // Data ada di dalam array, lakukan sesuatu dengan data
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(EditProfil.this, "Username sudah digunakan", Toast.LENGTH_SHORT).show();
                                                    //Log.d("API Response", responseDataCheckUser);

                                                }
                                            });
                                        } else {
                                            // Data dalam array kosong
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    showToast("Username belum digunakan");
                                                    // Lakukan proses penyimpanan data baru disini
                                                    //update DATA
                                                    Toast.makeText(EditProfil.this,"proses username baru",Toast.LENGTH_SHORT).show();
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
                                                        jsonBody.put("name",new_namaLengkap);
                                                        jsonBody.put("username",new_username);
                                                        jsonBody.put("email",new_email);
                                                        jsonBody.put("phone",new_nohp);
                                                        jsonBody.put("address",new_alamat);
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
                                                                    Toast.makeText(EditProfil.this, "Okhttp API Error", Toast.LENGTH_SHORT).show();

                                                                }
                                                            });
                                                        }

                                                        @Override
                                                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    if (response.isSuccessful()){
                                                                        //sessionManager.editImage(urlImage);
                                                                        Toast.makeText(EditProfil.this, "Sukses update data profil", Toast.LENGTH_SHORT).show();
                                                                        sessionManager.updaDateDenganUsername(new_namaLengkap,new_username,new_nohp,new_email,new_alamat);
                                                                    } else {
                                                                        Toast.makeText(EditProfil.this, "Kesalahan pada respon API", Toast.LENGTH_SHORT).show();

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
                                                Toast.makeText(EditProfil.this, "Terjadi kesalahan saat mengurai JSON", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }
                                }
                            }
                        });



                    }

                }
            }
        });
        btn_backFromEditProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(EditProfil.this,Home.class);
//                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the result to the ImageConverter
        imageConverter.onActivityResult(requestCode, resultCode, data);
        // Set the 'urlImage' variable with the value from ImageConverter
        urlImage = imageConverter.getSImage();

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
