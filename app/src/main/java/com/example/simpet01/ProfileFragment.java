package com.example.simpet01;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.simpet01.ApiController.ImageConverter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    LinearLayout btn_ubahProfile,btn_gantiSandi,btn_riwayatPesanan,btn_keranjangBelanja,btn_logOut,btn_cekKeranjang,btn_transaksi;
    SessionManager sessionManager;

    ImageView profilePicture;
    TextView profileName;

    ImageConverter imageConverter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        sessionManager = new SessionManager(getContext());

        btn_ubahProfile= view.findViewById(R.id.btn_ubahProfile);
        btn_gantiSandi= view.findViewById(R.id.btn_gantiSandi);
        btn_keranjangBelanja= view.findViewById(R.id.btn_keranjangBelanja);
        btn_riwayatPesanan= view.findViewById(R.id.btn_riwayatPesanan);
        btn_logOut= view.findViewById(R.id.btn_logOut);
        profileName= view.findViewById(R.id.profileName);
        profilePicture= view.findViewById(R.id.profilePicture);

        profileName.setText(sessionManager.getNama());
        String avatarString = sessionManager.getAvatar();

        if (avatarString.isEmpty()||avatarString.equals("{}")){
            profilePicture.setImageResource(R.drawable.profile_picture);
            //Toast.makeText(Home.this,"Profil kosong",Toast.LENGTH_SHORT).show();
        }
        else {
            //String avatarStringForMobile = avatarString.replace("data:image/jpeg;base64,","");
            String input = avatarString;
            String[] substrings = {"data:image/png;base64,", "data:image/jpeg;base64,"};
            String avatarStringForMobile = checkUrlImage(input, substrings);
            Bitmap avatarBitmap =  decodeImage(avatarStringForMobile);

            profilePicture.setImageBitmap(avatarBitmap);
            //Toast.makeText(Home.this,"Ada foto profil",Toast.LENGTH_SHORT).show();
        }


        btn_ubahProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(requireContext(),EditProfil.class);
                startActivity(i);

            }
        });

        btn_gantiSandi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(requireContext(),EditPassword.class);
                startActivity(i);

            }
        });

        btn_keranjangBelanja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "klik", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(requireContext(),Cart.class);
                startActivity(i);



            }
        });

        btn_riwayatPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),StatusPesanan.class);
                startActivity(i);
            }
        });

        btn_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sessionManager.logout();
                Intent i = new Intent(getContext(), Login.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        return view;
    }

    public String checkUrlImage(String input, String[] substrings) {
        for (String substring : substrings) {
            if (input.contains(substring)) {
                input = input.replace(substring, "");
            }
        }
        return input;
    }

    public Bitmap decodeImage(String sImage) {
        // Decode base64 string
        byte[] bytes = Base64.decode(sImage, Base64.DEFAULT);
        // Initialize bitmap
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }





}