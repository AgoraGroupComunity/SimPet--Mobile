package com.example.simpet01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.simpet01.databinding.ActivityMainBinding;

public class Home extends AppCompatActivity {

    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        // Tangkap data ekstra dari Intent
        Intent intent = getIntent();
        String fragmentIdentifier = intent.getStringExtra("FRAGMENT_IDENTIFIER");

        // Tentukan fragment yang akan ditampilkan berdasarkan identifier

//        if (fragmentIdentifier != null) {
//            // Pindah ke fragment catalog
//            if(fragmentIdentifier.equals("catalog")){
//                loadCatalogFragment();
//            }
//        }


        if (fragmentIdentifier != null && fragmentIdentifier.equals("catalog")) {
            // Pindah ke fragment catalog
            loadCatalogFragment();
        }
        if (fragmentIdentifier != null && fragmentIdentifier.equals("monitoring")) {
            // Pindah ke fragment catalog
            loadMonitoringFragment();
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.catalog) {
                replaceFragment(new CatalogFragment());
            } else if (item.getItemId() == R.id.monitoring) {
                replaceFragment(new KandangkuFragment());
            } else if (item.getItemId() == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }

            return true;
        });

    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();

    }

    private void loadCatalogFragment() {
        // Tambahkan fragment catalog ke fragment container di layout Home
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, new CatalogFragment());
        fragmentTransaction.commit();
        // Set tombol navbar "catalog" terklik
        binding.bottomNavigationView.getMenu().findItem(R.id.catalog).setChecked(true);
    }

    private void loadMonitoringFragment() {
        // Tambahkan fragment catalog ke fragment container di layout Home
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, new KandangkuFragment());
        fragmentTransaction.commit();
        // Set tombol navbar "catalog" terklik
        binding.bottomNavigationView.getMenu().findItem(R.id.monitoring).setChecked(true);
    }


}