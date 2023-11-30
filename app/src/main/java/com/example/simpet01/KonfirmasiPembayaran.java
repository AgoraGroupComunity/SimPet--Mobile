package com.example.simpet01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class KonfirmasiPembayaran extends AppCompatActivity {

    ImageView btn_backFromPembayaran;
    Button btn_belanjaLagi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_pembayaran);

        btn_backFromPembayaran=findViewById(R.id.btn_backFromPembayaran);
        btn_belanjaLagi=findViewById(R.id.btn_belanjaLagi);

        btn_backFromPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_belanjaLagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KonfirmasiPembayaran.this, Home.class);
                intent.putExtra("FRAGMENT_IDENTIFIER", "catalog"); // Ganti dengan identifier yang sesuai
                startActivity(intent);
            }
        });

    }
}