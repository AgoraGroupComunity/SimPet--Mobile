package com.example.simpet01;

public class ApiService {
    //private static final String API_CHECK_USER = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-exinc/endpoint/checkUser"; // Ganti dengan URL API Anda



//    public static String checkUsername(String username) {
//        OkHttpClient client = new OkHttpClient();
//        String responseData = null;
//
//        try {
//            HttpUrl httpUrlCheckUser = new HttpUrl.Builder()
//                    .scheme("https")
//                    .host("us-east-1.aws.data.mongodb-api.com")
//                    .addPathSegment("app")
//                    .addPathSegment("application-0-exinc")
//                    .addPathSegment("endpoint")
//                    .addPathSegment("checkUser")
//                    .addQueryParameter("username", username)
//                    .build();
//
//            Request request = new Request.Builder()
//                    .url(httpUrlCheckUser) // Ganti dengan endpoint API Anda
//                    .get()
//                    .build();
//
//            Response response = client.newCall(request).execute();
//            ResponseBody body = response.body();
//
//            if (body != null) {
//                responseData = body.string();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return responseData;
//    }
}

//INI METHOD JALAN BANG

//    private void checkUsername(String username) {
//        OkHttpClient client = new OkHttpClient();
//        HttpUrl httpUrlCheckUser = new HttpUrl.Builder()
//                .scheme("https")
//                .host("us-east-1.aws.data.mongodb-api.com")
//                .addPathSegment("app")
//                .addPathSegment("application-0-exinc")
//                .addPathSegment("endpoint")
//                .addPathSegment("checkUser")
//                .addQueryParameter("username", username)
//                .build();
//
//        Request request = new Request.Builder()
//                .url(httpUrlCheckUser)
//                .get()
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                // Tangani kesalahan yang terjadi saat permintaan gagal
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(Register.this, "Gagal999 mendaftarkan akun. Silahkan coba lagi.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                // Tangani respon dari permintaan
//                if (response.isSuccessful()) {
//                    String responseDataCheckUser = response.body().string();
//                    try {
//                        JSONArray jsonArray = new JSONArray(responseDataCheckUser);
//
//                        if (jsonArray.length() > 0) {
//                            // Data ada di dalam array, lakukan sesuatu dengan data
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(Register.this, "Username sudah digunakan", Toast.LENGTH_SHORT).show();
//                                    Log.d("API Response", responseDataCheckUser);
//                                }
//                            });
//                        } else {
//                            // Data dalam array kosong
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(Register.this, "Username belum digunakan", Toast.LENGTH_SHORT).show();
//                                    // Lakukan proses penyimpanan data baru disini
//
//                                }
//                            });
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        // Tangani kesalahan saat mengurai JSON
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(Register.this, "Terjadi kesalahan saat mengurai JSON", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                } else {
//                    // Data gagal diambil dari API, tangani kesalahan
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(Register.this, "Gagal Ambil API mendaftarkan akun. Silahkan coba lagi.", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//        });
//    }
