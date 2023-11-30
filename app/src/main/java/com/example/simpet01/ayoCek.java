package com.example.simpet01;

import static com.example.simpet01.ApiController.FormattedCurrency.formatCurrency;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class ayoCek extends AppCompatActivity {

    TextView opsiHewan, hasilJenisPakan,hasilPanjangKandang,hasilLebarKandang,hasilTinggiKandang,hasilVitamin,hasilVaksin,hasilVolumeAir,hasilSDM,hasilEstimasiBiaya;
    EditText jumlahHewan,durasiTernak,jenisPakan,jenisVaksin,jenisVitamin,panjangKandang,lebarKandang,airBersih,jumlahModal;
    LinearLayout viewHasil;
    Button btn_cekPeternakan;

    ImageView btn_backFromAyoCek;

    RadioGroup radioGroup;

    FormattedCurrency formattedCurrency;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayo_cek);

        formattedCurrency = new FormattedCurrency();

        viewHasil=findViewById(R.id.viewHasil);
        viewHasil.setVisibility(View.GONE);
        radioGroup=findViewById(R.id.radioGroup);
        opsiHewan=findViewById(R.id.opsiHewan);

        jumlahHewan=findViewById(R.id.jumlahHewan);
        durasiTernak=findViewById(R.id.durasiTernak);
        jenisPakan = findViewById(R.id.jenisPakan);
        jenisVaksin = findViewById(R.id.jenisVaksin);
        jenisVitamin = findViewById(R.id.jenisVitamin);
        panjangKandang = findViewById(R.id.panjangKandang);
        lebarKandang = findViewById(R.id.lebarKandang);
        airBersih = findViewById(R.id.airBersih);
        jumlahModal = findViewById(R.id.jumlahModal);

        btn_backFromAyoCek=findViewById(R.id.btn_backFromAyoCek);
        btn_cekPeternakan=findViewById(R.id.btn_cekPeternakan);

        hasilJenisPakan=findViewById(R.id.hasilJenisPakan);
        hasilPanjangKandang=findViewById(R.id.hasilPanjangKandang);
        hasilLebarKandang=findViewById(R.id.hasilLebarKandang);
        hasilTinggiKandang=findViewById(R.id.hasilTinggiKandang);
        hasilVitamin=findViewById(R.id.hasilVitamin);
        hasilVaksin=findViewById(R.id.hasilVaksin);
        hasilVolumeAir=findViewById(R.id.hasilVolumeAir);
        hasilSDM=findViewById(R.id.hasilSDM);
        hasilEstimasiBiaya=findViewById(R.id.hasilEstimasiBiaya);

        btn_backFromAyoCek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

        btn_cekPeternakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputOpsiHewan = opsiHewan.getText().toString();
                String inputJumlahHewan = jumlahHewan.getText().toString();
                String inputDurasiternak = durasiTernak.getText().toString();

                String inputJenisPakan = jenisPakan.getText().toString();
                String inputJenisVaksin = jenisVaksin.getText().toString();
                String inputJenisVitamin = jenisVitamin.getText().toString();
                String inputPanjangKandang = panjangKandang.getText().toString();
                String inputLebarKandang = lebarKandang.getText().toString();
                String inputAirBersih = airBersih.getText().toString();
                String inputJumlahModal = jumlahModal.getText().toString();



                if(inputOpsiHewan.isEmpty()||inputJumlahHewan.isEmpty()||inputDurasiternak.isEmpty()||inputJenisPakan.isEmpty()||
                        inputJenisVaksin.isEmpty()||inputJenisVitamin.isEmpty()||inputPanjangKandang.isEmpty()||inputLebarKandang.isEmpty()||inputAirBersih.isEmpty()||inputJumlahModal.isEmpty()){
                    showToast("Lengkapi data");
                } else{

                    Integer intJumlahHewan = Integer.parseInt(inputJumlahHewan);
                    Integer intDurasiTernak = Integer.parseInt(inputDurasiternak);
                    Integer intPanjangKandang = Integer.parseInt(inputPanjangKandang);
                    Integer intLebarKandang = Integer.parseInt(inputLebarKandang);
                    Integer intAirBersih = Integer.parseInt(inputAirBersih);
                    Integer intJumlahModal = Integer.parseInt(inputJumlahModal );

                    //ayoCekAPI( viewHasil, inputOpsiHewan, intJumlahHewan, intDurasiTernak,  hasilJenisPakan, hasilPanjangKandang,  hasilLebarKandang, hasilTinggiKandang,  hasilVitamin, hasilVaksin,  hasilVolumeAir,  hasilSDM,hasilEstimasiBiaya);
                    //viewHasil.setVisibility(View.VISIBLE);
                    ayoCekAPI( viewHasil, inputOpsiHewan, intJumlahHewan, intDurasiTernak,
                            inputJenisPakan, inputJenisVaksin,  inputJenisVitamin,  intAirBersih,  intPanjangKandang, intLebarKandang,intJumlahModal,
                             hasilJenisPakan, hasilPanjangKandang,  hasilLebarKandang,
                             hasilTinggiKandang,  hasilVitamin, hasilVaksin,  hasilVolumeAir,
                             hasilSDM, hasilEstimasiBiaya);

                }

            }
        });

    }
    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void ayoCekAPI(LinearLayout viewHasil,String inputOpsiHewan,Integer intJumlahHewan,Integer intDurasiTernak,
            String jenisPakan,String jenisVaksin, String jenisVitamin, Integer airBersih, Integer panjangKandang,Integer lebarKandang, Integer intJumlahModal ,
                          TextView hasilJenisPakan,TextView hasilPanjangKandang, TextView hasilLebarKandang,
                          TextView hasilTinggiKandang, TextView hasilVitamin,TextView hasilVaksin, TextView hasilVolumeAir,
                          TextView hasilSDM,TextView hasilEstimasiBiaya){

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

                                    JSONObject kandangObject = animalObject.getJSONObject("kandang");
                                    Integer jsonPanjang = kandangObject.getInt("minPanjang");
                                    Integer jsonLebar = kandangObject.getInt("minLebar");
                                    Integer jsonTinggi = kandangObject.getInt("minTinggi");

                                    JSONObject vitaminObject = animalObject.getJSONObject("vitamin");
                                    String jsonVitamin = vitaminObject.getString("jenis_vitamin"); // Correct typo here
                                    Integer jsonHargaVitamin = vitaminObject.getInt("harga_vitamin"); // Correct typo here

                                    JSONObject vaksinObject = animalObject.getJSONObject("vaksin");
                                    String jsonVaksin = vaksinObject.getString("jenis_vaksin"); // Correct typo here
                                    Integer jsonHargaVaksin = vaksinObject.getInt("harga_vaksin"); // Correct typo here

                                    Integer jsonAirBersih = animalObject.getInt("air_bersih"); // Correct typo here

                                    Double jsonSDM = animalObject.getDouble("SDM"); // Correct typo here
                                    Integer intSDM = (int) Math.ceil(jsonSDM)*intJumlahHewan;

                                    Integer hargaMeterKandang = 350000;

                                    Integer totalBiaya = (jsonLebar*jsonPanjang*hargaMeterKandang)+(intJumlahHewan*jsonHargaHewan)+(intJumlahHewan*jsonHargaVaksin)+(intJumlahHewan*jsonHargaVitamin)+(intJumlahHewan*jsonHargaPakan*+intDurasiTernak) ;

                                    String cekJenisPakan, cekJenisVaksin,cekJenisVitamin,cekAirBersih,cekPanjangKandang,cekLebarKandang,cekJumlahModal;


                                    // validation data
                                    if (jenisPakan.equals(jsonJenisPakan)) {
                                        cekJenisPakan = jsonJenisPakan;
                                    } else {
                                        cekJenisPakan = "Ganti pakan ternak Anda dengan " + jsonJenisPakan;
                                    }

                                    if (jenisVaksin.equals(jsonVaksin)) {
                                        cekJenisVaksin = jsonVaksin +" cocok untuk hewan ternak Anda";
                                    } else {
                                        cekJenisVaksin = "Ganti jenis vaksin ternak Anda dengan " + jsonVaksin;
                                    }

                                    if (jenisVitamin.equals(jsonVitamin)) {
                                        cekJenisVitamin = jsonVitamin + " cocok untuk hewan ternak Anda";
                                    } else {
                                        cekJenisVitamin = "Ganti jenis vitamin ternak Anda dengan " + jsonVitamin;
                                    }

                                    if (airBersih>(jsonAirBersih*intJumlahHewan)) {
                                        cekAirBersih = airBersih +" Liter/ hari sudah mencukupi";
                                    } else {
                                        cekAirBersih = "Sediakan air bersih minimal " + (jsonAirBersih*intJumlahHewan) +" Liter/ hari" ;
                                    }

                                    if (panjangKandang>jsonPanjang) {
                                        cekPanjangKandang = "Panjang "+panjangKandang +" Meter sudah sesuai";
                                    } else {
                                        cekPanjangKandang = "Panjang kandang ternak minimal " + jsonPanjang +" Meter";
                                    }

                                    if (lebarKandang>jsonLebar) {
                                        cekLebarKandang ="Lebar "+lebarKandang +" Meter sudah sesuai";
                                    } else {
                                        cekLebarKandang = "Lebar kandang ternak minimal " + jsonLebar +" Meter";
                                    }

                                    if(intJumlahModal>totalBiaya){
                                        cekJumlahModal = formatCurrency(intJumlahModal)+" mencukupi untuk membuat peternakan";
                                    } else{
                                        cekJumlahModal = "Modal anda kurang " + formatCurrency(totalBiaya-intJumlahModal);
                                    }

                                    hasilJenisPakan.setText(cekJenisPakan);
                                    hasilPanjangKandang.setText(cekPanjangKandang);
                                    hasilLebarKandang.setText(cekLebarKandang);
                                    hasilTinggiKandang.setText("Tinggi kandang minimal "+jsonTinggi+" Meter");
                                    hasilVitamin.setText(cekJenisVitamin);
                                    hasilVaksin.setText(cekJenisVaksin);
                                    hasilVolumeAir.setText(cekAirBersih);
                                    hasilSDM.setText(intSDM+" orang");
                                    //hasilEstimasiBiaya.setText("Rp"+totalBiaya+"");
                                    hasilEstimasiBiaya.setText(cekJumlahModal);

                                    viewHasil.setVisibility(View.VISIBLE);



                                }

                            } catch (IOException | JSONException e) {
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