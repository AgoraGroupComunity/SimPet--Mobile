package com.example.simpet01;

import static com.example.simpet01.ApiController.FormattedCurrency.formatCurrency;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simpet01.ApiController.FormattedCurrency;

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

public class ayoBuat extends AppCompatActivity {

    TextView opsiHewan, hasilJenisPakan,hasilPanjangKandang,hasilLebarKandang,hasilTinggiKandang,hasilVitamin,hasilVaksin,hasilVolumeAir,hasilSDM,hasilEstimasiBiaya;
    EditText jumlahHewan,durasiTernak;
    LinearLayout viewHasil;
    Button btn_hitungRancangan;

    ImageView btn_backFromAyoBuat;

    RadioGroup radioGroup;

    FormattedCurrency formattedCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayo_buat);

        formattedCurrency = new FormattedCurrency();
        radioGroup=findViewById(R.id.radioGroup);
        opsiHewan=findViewById(R.id.opsiHewan);
        jumlahHewan=findViewById(R.id.jumlahHewan);
        durasiTernak=findViewById(R.id.durasiTernak);

        btn_backFromAyoBuat=findViewById(R.id.btn_backFromAyoBuat);
        btn_hitungRancangan=findViewById(R.id.btn_hitungRancangan);
        viewHasil=findViewById(R.id.viewHasil);
        viewHasil.setVisibility(View.GONE);
        hasilJenisPakan=findViewById(R.id.hasilJenisPakan);
        hasilPanjangKandang=findViewById(R.id.hasilPanjangKandang);
        hasilLebarKandang=findViewById(R.id.hasilLebarKandang);
        hasilTinggiKandang=findViewById(R.id.hasilTinggiKandang);
        hasilVitamin=findViewById(R.id.hasilVitamin);
        hasilVaksin=findViewById(R.id.hasilVaksin);
        hasilVolumeAir=findViewById(R.id.hasilVolumeAir);
        hasilSDM=findViewById(R.id.hasilSDM);
        hasilEstimasiBiaya=findViewById(R.id.hasilEstimasiBiaya);

        btn_backFromAyoBuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Home.class);
                startActivity(i);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // Mendapatkan radio button yang dipilih
                RadioButton radioButton = findViewById(checkedId);

                if (radioButton != null) {
                    // Mendapatkan nilai tag dari RadioButton yang dipilih
                    String selectedValue = radioButton.getTag().toString();

                    // Menetapkan teks sesuai dengan nilai tag
                    switch (selectedValue) {
                        case "Sapi Potong":
                            opsiHewan.setText("Sapi Potong");
                            break;
                        case "Sapi Perah":
                            opsiHewan.setText("Sapi Perah");
                            break;
                        case "Kambing":
                            opsiHewan.setText("Kambing");
                            break;
                        case "Kerbau":
                            opsiHewan.setText("Kerbau");
                            break;
                    }
                }

            }
        });

        btn_hitungRancangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showToast("klik");
                String inputOpsiHewan = opsiHewan.getText().toString();
                String inputJumlahHewan = jumlahHewan.getText().toString();
                String inputDurasiternak = durasiTernak.getText().toString();



                if(inputOpsiHewan.isEmpty()||inputJumlahHewan.isEmpty()||inputDurasiternak.isEmpty()){
                    showToast("Lengkapi data"+inputOpsiHewan+" "+inputDurasiternak+" "+inputDurasiternak);
                } else{
                    Integer intJumlahHewan = Integer.parseInt(inputJumlahHewan);
                    Integer intDurasiTernak = Integer.parseInt(inputDurasiternak);
                    //showToast("Lenkgap terisi");
                    ayoBuatAPI( viewHasil, inputOpsiHewan, intJumlahHewan, intDurasiTernak,  hasilJenisPakan, hasilPanjangKandang,  hasilLebarKandang, hasilTinggiKandang,  hasilVitamin, hasilVaksin,  hasilVolumeAir,  hasilSDM,hasilEstimasiBiaya);
                    //viewHasil.setVisibility(View.VISIBLE);
                }



            }
        });
    }

    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void ayoBuatAPI(LinearLayout viewHasil,String inputOpsiHewan,Integer intJumlahHewan,Integer intDurasiTernak, TextView hasilJenisPakan,TextView hasilPanjangKandang, TextView hasilLebarKandang,TextView hasilTinggiKandang, TextView hasilVitamin,TextView hasilVaksin, TextView hasilVolumeAir, TextView hasilSDM,TextView hasilEstimasiBiaya){
        //showToast(inputOpsiHewan);
        OkHttpClient client = new OkHttpClient();
        HttpUrl httpUrlGetCart = new HttpUrl.Builder()

                .scheme("https")
                .host("us-east-1.aws.data.mongodb-api.com")
                .addPathSegment("app")
                .addPathSegment("application-0-exinc")
                .addPathSegment("endpoint")
                .addPathSegment("getAnimal")
                .addQueryParameter("hewan",inputOpsiHewan)
                .build();

        Request request = new Request.Builder()
                .url(httpUrlGetCart)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("Error okhttp API");
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            //showToast("start");
                            try {
                                String responseAnimal = response.body().string();
                                JSONArray jsonArray = new JSONArray(responseAnimal);
                                if(jsonArray.length()>0){
                                    JSONObject animalObject = jsonArray.getJSONObject(0); // Use index i here
                                    String jsonId = animalObject.getString("_id");
                                    String jsonHewan = animalObject.getString("hewan");
                                    Integer jsonHargaHewan = animalObject.getInt("harga_hewan");

                                    JSONObject pakanObject = animalObject.getJSONObject("pakan");
                                    String jsonJenisPakan = pakanObject.getString("jenis_pakan");
                                    Integer jsonHargaPakan = pakanObject.getInt("harga_pakan");
                                    //batas aman

                                    JSONObject kandangObject = animalObject.getJSONObject("kandang");
                                    Integer jsonPanjang = kandangObject.getInt("minPanjang");
                                    Integer jsonLebar = kandangObject.getInt("minLebar");
                                    Integer jsonTinggi = kandangObject.getInt("minTinggi");
                                    //showToast(jsonPanjang+""+jsonLebar+""+jsonTinggi+"");

                                    JSONObject vitaminObject = animalObject.getJSONObject("vitamin");
                                    String jsonVitamin = vitaminObject.getString("jenis_vitamin"); // Correct typo here
                                    Integer jsonHargaVitamin = vitaminObject.getInt("harga_vitamin"); // Correct typo here
                                    //showToast(jsonVitamin+" "+jsonHargaVitamin+"");

                                    JSONObject vaksinObject = animalObject.getJSONObject("vaksin");
                                    String jsonVaksin = vaksinObject.getString("jenis_vaksin"); // Correct typo here
                                    Integer jsonHargaVaksin = vaksinObject.getInt("harga_vaksin"); // Correct typo here
                                    //showToast(jsonVaksin+" "+jsonHargaVaksin+"");

                                    Integer jsonAirBersih = animalObject.getInt("air_bersih"); // Correct typo here
                                    //showToast(jsonAirBersih+"");

                                    Double jsonSDM = animalObject.getDouble("SDM"); // Correct typo here
                                    Integer intSDM = (int) Math.ceil(jsonSDM)*intJumlahHewan;
                                    //showToast(intSDM+"");

                                    //Integer hargaHewan = 3000000;
                                    Integer hargaMeterKandang = 350000;

                                    Integer totalBiaya = (jsonLebar*jsonPanjang*hargaMeterKandang)+(intJumlahHewan*jsonHargaHewan)+(intJumlahHewan*jsonHargaVaksin)+(intJumlahHewan*jsonHargaVitamin)+(intJumlahHewan*jsonHargaPakan*+intDurasiTernak) ;

                                    hasilJenisPakan.setText(jsonJenisPakan);
                                    hasilPanjangKandang.setText(jsonPanjang+" Meter");
                                    hasilLebarKandang.setText(jsonLebar+" Meter");
                                    hasilTinggiKandang.setText(jsonTinggi+" Meter");
                                    hasilVitamin.setText(jsonVitamin);
                                    hasilVaksin.setText(jsonVaksin);
                                    hasilVolumeAir.setText(jsonAirBersih+" L/ hari");
                                    hasilSDM.setText(intSDM+" orang");
                                    //hasilEstimasiBiaya.setText("Rp"+totalBiaya+"");
                                    hasilEstimasiBiaya.setText(formatCurrency(totalBiaya)+"");


                                    //Integer hitungBiaya


                                    viewHasil.setVisibility(View.VISIBLE);



                                }

                            } catch (IOException |JSONException e) {
                                showToast("gagal try ");
                                throw new RuntimeException(e);
                            }

                        } else {
                            showToast("Gagal mengambil response");
                        }
                    }
                });
            }
        });

    }
}