package com.example.simpet01;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simpet01.ApiController.ImageConverter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    ImageConverter imageConverter;

    SessionManager sessionManager;
    TextView namaUser,textView,get_avatar;
    CardView btn_ayocek,btn_ayobuat, btn_kandangku,btn_tanyaternak,btn_metapet;
    Button btn_katalog,btn_logout,btn_pageProfil,btn_pageUbahPassword;
    ImageView home_profilImage,imageView, btn_cart;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false);



        sessionManager = new SessionManager(getContext());
        if (!sessionManager.isLoggedIn()) {
            Intent i = new Intent(requireContext(), Login.class);
            startActivity(i);
            //Toast.makeText(requireContext(), "kosng", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
        }
        //Toast.makeText(this, "udah login", Toast.LENGTH_SHORT).show();

        // Inisialisasi objek SessionManager dan ambil nama dari preferences
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        btn_ayocek = view.findViewById(R.id.btn_ayocek);
        btn_ayobuat = view.findViewById(R.id.btn_ayobuat);
        btn_kandangku = view.findViewById(R.id.btn_kandangku);
        btn_tanyaternak = view.findViewById(R.id.btn_tanyaternak);
        btn_metapet = view.findViewById(R.id.btn_metapet);
        btn_cart = view.findViewById(R.id.btn_cart);

        namaUser = view.findViewById(R.id.namaUser);
        namaUser.setText(sessionManager.getNama());


        home_profilImage = view.findViewById(R.id.home_profilImage);

        //get_avatar=findViewById(R.id.get_avatar);
        String avatarString = sessionManager.getAvatar();
        //get_avatar.setText(avatarString);
        // Inisialisasi objek imageConverter
        imageConverter = new ImageConverter(getContext(), textView, imageView);

        if (avatarString.isEmpty()||avatarString.equals("{}")){
            home_profilImage.setImageResource(R.drawable.profile_picture);
            //Toast.makeText(Home.this,"Profil kosong",Toast.LENGTH_SHORT).show();
        }
        else {
            //String avatarStringForMobile = avatarString.replace("data:image/jpeg;base64,","");
            String input = avatarString;
            String[] substrings = {"data:image/png;base64,", "data:image/jpeg;base64,"};
            String avatarStringForMobile = imageConverter.checkUrlImage(input, substrings);
            Bitmap avatarBitmap =  imageConverter.decodeImage(avatarStringForMobile);

            home_profilImage.setImageBitmap(avatarBitmap);
            //Toast.makeText(Home.this,"Ada foto profil",Toast.LENGTH_SHORT).show();
        }




        btn_ayobuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(requireContext(),ayoBuat.class);
                startActivity(i);
            }
        });

        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(requireContext(),Cart.class);
                startActivity(i);
            }
        });

        btn_ayocek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(requireContext(),ayoCek.class);
                startActivity(i);
            }
        });

        btn_kandangku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), Home.class);
                intent.putExtra("FRAGMENT_IDENTIFIER", "monitoring"); // Ganti dengan identifier yang sesuai
                startActivity(intent);
            }
        });

        btn_tanyaternak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(requireContext(), TanyaTernakBot.class);
                startActivity(i);
            }
        });

        btn_metapet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(requireContext(),metaPet.class);
                startActivity(i);
            }
        });

        return view;
    }
}