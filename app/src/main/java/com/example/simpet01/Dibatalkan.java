package com.example.simpet01;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simpet01.Produk.HistoryAdapter;
import com.example.simpet01.Produk.HistoryModel;
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

public class Dibatalkan extends Fragment {
    HistoryModel historyModel;
    HistoryAdapter historyAdapter;
    RecyclerView recyclerViewHistory;
    SessionManager sessionManager;
    TextView errorText;

    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diproses, container, false);
        recyclerViewHistory= view.findViewById(R.id.recyclerViewHistory);
        errorText= view.findViewById(R.id.errorText);

        //loading effect
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view);
        shimmerFrameLayout.startShimmerAnimation();

        sessionManager = new SessionManager(getContext());

        List<HistoryModel> listHistory = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        HttpUrl httpUrlGetCart = new HttpUrl.Builder()

                .scheme("https")
                .host("us-east-1.aws.data.mongodb-api.com")
                .addPathSegment("app")
                .addPathSegment("application-0-exinc")
                .addPathSegment("endpoint")
                .addPathSegment("getHistory")
                .addEncodedQueryParameter("user_id",sessionManager.getIdUser())
                .addEncodedQueryParameter("status","Dibatalkan")
                .build();
        //showToast(sessionManager.getIdUser());

        Request request = new Request.Builder()
                .url(httpUrlGetCart)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("Okhttp API Error");

                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            try {
                                String responseCart = response.body().string();
                                JSONArray jsonArray = new JSONArray(responseCart);
                                // Inisialisasi total harga


                                if (jsonArray.length() > 0) {
                                    for (int iCart = 0; iCart < jsonArray.length(); iCart++) {
                                        JSONObject cartObject = jsonArray.getJSONObject(iCart);
                                        String jsonId = cartObject.getString("_id");
                                        Integer jsonInvoice = cartObject.getInt("receipt_number");
                                        String jsonDate = cartObject.getString("date");
                                        Integer jsonTotalPrice = cartObject.getInt("total_price");
                                        //showToast(jsonInvoice+"");
                                        historyModel = new HistoryModel();
                                        historyModel.setInvoiceOrder(jsonInvoice);
                                        historyModel.setTotalOrder(jsonTotalPrice);
                                        historyModel.setDateOrder(jsonDate);
                                        historyModel.setStatus("Dibatalkan");
                                        //errorText.setText(jsonInvoice+"->"+jsonTotalPrice+"->"+jsonDate);

                                        listHistory.add(historyModel);

                                        // Atur RecyclerView setelah loop selesai
                                        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
                                        recyclerViewHistory .setAdapter(new HistoryAdapter(getActivity(), listHistory));

                                    }
                                    // Menampilkan total harga di TextView

                                    //showToast("Selesai mengambil data");

                                } else {
                                    showToast("history Anda kosong");
                                }
                                shimmerFrameLayout.stopShimmerAnimation();
                                shimmerFrameLayout.setVisibility(View.INVISIBLE);


                            } catch (JSONException | IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });

            }
        }); //end api cart




        return view;
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}