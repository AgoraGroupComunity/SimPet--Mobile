package com.example.simpet01;
import android.content.Context;
import android.content.SharedPreferences;
public class  SessionManager {

    private static final String PREFERENCE_NAME = "LoginPreference";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_NAMA = "nama";
    private static final String KEY_ALAMAT = "alamat";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NOHP = "nohp";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IDUSER = "id";
    private static final String KEY_AVATAR = "avatar";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

//    public SessionManager(Context context) {
//        this.context = context;
//        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//    }

        public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        }

    // Fungsi untuk login
    public void login(String id,String avatar, String nama, String username, String alamat,String nohp, String email, String password) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_IDUSER, id);
        editor.putString(KEY_AVATAR, avatar);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_NAMA, nama);
        editor.putString(KEY_ALAMAT, alamat);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NOHP, nohp);
        editor.putString(KEY_PASSWORD, password);

        editor.apply();
    }

    public void editImage(String avatar){
        editor.putString(KEY_AVATAR, avatar);
        editor.apply();
    }

    public void updaDateTanpaUsername(String nama, String nohp, String email, String alamat) {
        editor.putString(KEY_NAMA, nama);
        editor.putString(KEY_ALAMAT, alamat);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NOHP, nohp);
        editor.apply();
    }

    public void updaDateDenganUsername(String nama, String username, String nohp, String email, String alamat) {
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_NAMA, nama);
        editor.putString(KEY_ALAMAT, alamat);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NOHP, nohp);
        editor.apply();
    }

    // Fungsi untuk logout
    public void logout() {
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        // Hapus data tambahan
        editor.remove(KEY_IDUSER);
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_AVATAR);
        editor.remove(KEY_NAMA);
        editor.remove(KEY_ALAMAT);
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_NOHP);
        editor.remove(KEY_PASSWORD);
        ;
        editor.apply();
    }

    // Fungsi untuk memeriksa status login
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Mendapatkan data tambahan
    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    public String getNama() {
        return sharedPreferences.getString(KEY_NAMA, "");
    }

    public String getAvatar() { return sharedPreferences.getString(KEY_AVATAR, ""); }

    public String getAlamat() {
        return sharedPreferences.getString(KEY_ALAMAT, "");
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    public String getPassword() {
        return sharedPreferences.getString(KEY_PASSWORD, "");
    }

    public String getNohp() {
        return sharedPreferences.getString(KEY_NOHP, "");
    }

    public String getIdUser() {
        return sharedPreferences.getString(KEY_IDUSER, "");
    }


}


