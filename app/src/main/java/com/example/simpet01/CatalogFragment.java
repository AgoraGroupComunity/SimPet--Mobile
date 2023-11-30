package com.example.simpet01;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simpet01.ApiController.FormattedCurrency;
import com.example.simpet01.Produk.ProdukModel;
import com.example.simpet01.Produk.RecyclerViewCatalogAdapter;
import com.example.simpet01.Produk.createProduk;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CatalogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CatalogFragment extends Fragment {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    RecyclerViewCatalogAdapter catalogAdapter;
    List<ProdukModel> listProduk;
    ProdukModel produkModel;
    TextView errorText;

    CardView btn_addProduk;

    FormattedCurrency formattedCurrency;
    SessionManager sessionManager ;
    ShimmerFrameLayout shimmerFrameLayout;




//    int []arr={R.drawable.pic1, R.drawable.pic2, R.drawable.pic3,R.drawable.pic4, R.drawable.pic5,R.drawable.pic6,
//            R.drawable.pic7, R.drawable.pic8, R.drawable.pic9, R.drawable.pic10, R.drawable.pic11,
//            R.drawable.pic12};

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CatalogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CatalogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CatalogFragment newInstance(String param1, String param2) {
        CatalogFragment fragment = new CatalogFragment();
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
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);

        formattedCurrency = new FormattedCurrency();
        sessionManager= new SessionManager(requireContext());
        btn_addProduk= view.findViewById(R.id.btn_addProduk);


        // Inflate the layout for this fragment
        recyclerView= view.findViewById(R.id.recylcerView);

        //loading effect
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view);
        shimmerFrameLayout.startShimmerAnimation();


        if(sessionManager.getIdUser().equals("65652a2e6ea647e40be542fb")){
            btn_addProduk.setVisibility(View.VISIBLE);
        } else{
            btn_addProduk.setVisibility(View.GONE);
        }



        btn_addProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), createProduk.class);
                startActivity(i);
            }
        });


        recyclerView.setHasFixedSize(true);
        String url="https://us-east-1.aws.data.mongodb-api.com/app/application-0-exinc/endpoint/getProducts";
        //String url="https://reqres.in/api/users?page=2";


        OkHttpClient client = new OkHttpClient();
        HttpUrl httpUrlLoginUser = new HttpUrl.Builder()

                .scheme("https")
                .host("us-east-1.aws.data.mongodb-api.com")
                .addPathSegment("app")
                .addPathSegment("application-0-exinc")
                .addPathSegment("endpoint")
                .addPathSegment("getProducts")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("Okhttp API Error");
                    }
                });

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            //showToast("respon sukses");
                            listProduk = new ArrayList<>();
                            try {
                                //showToast("jalan awal");
                                String responseDataCheckUser = response.body().string();
                                //JSONObject hasil= new JSONObject(responseDataCheckUser);
                                //showToast(responseDataCheckUser);
                                //errorText.setText(responseDataCheckUser);
                                JSONArray jsonArray = new JSONArray(responseDataCheckUser);
                                //errorText.setText(jsonArray.toString());

                                if (jsonArray.length() > 0) {
                                    //showToast("lebih dari 1, ada"+ jsonArray.length());

                                    for (int i=0; i< jsonArray.length();i++){
                                        produkModel = new ProdukModel();

                                        JSONObject productObject = jsonArray.getJSONObject(i); // Use index i here
                                        String jsonId = productObject.getString("_id");
                                        String jsonName = productObject.getString("name");
                                        //showToast("loop"+jsonName);
                                        Integer jsonPrice = productObject.getInt("price");
                                        String jsonDescriptions = productObject.getString("descriptions"); // Correct typo here
                                        JSONObject objectImages = productObject.getJSONObject("images");
                                        String firstImage = objectImages.getString("image0"); // Correct typo here

                                        //unused images, only for update
                                        String secondImage = objectImages.getString("image1"); // Correct typo here
                                        String thirdImage = objectImages.getString("image2"); // Correct typo here
                                        String fourthImage = objectImages.getString("image3"); // Correct typo here


                                        produkModel.set_id(jsonId);
                                        produkModel.setName(jsonName);
                                        produkModel.setPrice(jsonPrice);
                                        produkModel.setDescriptions(jsonDescriptions);
                                        produkModel.setFirstImages(firstImage);
                                        produkModel.setSecondImage(secondImage);
                                        produkModel.setThirdImage(thirdImage);
                                        produkModel.setFourthImage(fourthImage);

                                        listProduk.add(produkModel);
                                        //showToast(produkModel.get_id()+produkModel.getName()+produkModel.getPrice()+""+produkModel.getDescriptions());
//                                        showToast(produkModel.getName());
//                                        showToast(produkModel.getPrice()+"");
//                                        showToast(produkModel.getDescriptions());

                                        layoutManager = new GridLayoutManager(requireContext(),2);
                                        recyclerView.setLayoutManager(layoutManager);
                                        catalogAdapter = new RecyclerViewCatalogAdapter(requireContext(), listProduk);
                                        recyclerView.setAdapter(catalogAdapter);

                                    }
                                    shimmerFrameLayout.stopShimmerAnimation();
                                    shimmerFrameLayout.setVisibility(View.INVISIBLE);

                                } else {
                                    // Data tidak ditemukan
                                    showToast("Tidak ada produk");
                                }
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                                showToast("Terjadi kesalahan saat mengambil atau mengurai data dari server");
                            }
                        } else{
                            showToast("Gagal response");
                        }

                    }
                });
            }
        });


        return view;
    }

    public void getProduk(){
    }
    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }




}