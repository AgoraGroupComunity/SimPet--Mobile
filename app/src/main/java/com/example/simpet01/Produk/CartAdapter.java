package com.example.simpet01.Produk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpet01.ApiController.FormattedCurrency;
import com.example.simpet01.Cart;
import com.example.simpet01.R;
import com.example.simpet01.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {


    Context context;
    List<CartModel> cartModels;
    LayoutInflater inflater;
    SessionManager sessionManager;
    FormattedCurrency formattedCurrency;

    public CartAdapter(Context context, List<CartModel> cartModels) {
        this.context = context;
        this.cartModels = cartModels;
        this.inflater= LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_cart_view,parent,false);
        CartAdapter.MyViewHolder myViewHolder = new CartAdapter.MyViewHolder(view);
        return myViewHolder;
        //return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.single_cart_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull  MyViewHolder holder, int position) {

        formattedCurrency = new FormattedCurrency();

        String input = cartModels.get(position).getCartImage();
        String[] substrings = {"data:image/png;base64,", "data:image/jpeg;base64,"};
        String avatarStringForMobile = checkUrlImage(input, substrings);
        Bitmap avatarBitmap =  decodeImage(avatarStringForMobile);

        holder.cartImage.setImageBitmap(avatarBitmap);
        holder.productId.setText(cartModels.get(position).getProductId());
        holder.cartName.setText(cartModels.get(position).getCartName());
        holder.cartPrice.setText(formattedCurrency.formatCurrency(cartModels.get(position).getCartPrice()));
        holder.cartAmount.setText(cartModels.get(position).getCartAmount()+"");


        holder.cartDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showToast("delete button");
                OkHttpClient client = new OkHttpClient();
                HttpUrl httpUrlDeleteProducts = new HttpUrl.Builder()
                        .scheme("https")
                        .host("us-east-1.aws.data.mongodb-api.com")
                        .addPathSegment("app")
                        .addPathSegment("application-0-exinc")
                        .addPathSegment("endpoint")
                        .addPathSegment("deleteCartMobile")
                        .addQueryParameter("user_id",sessionManager.getIdUser())
                        .addQueryParameter("product_id",cartModels.get(holder.getAdapterPosition()).getProductId())
                        .build();

                Request request = new Request.Builder()
                        .url(httpUrlDeleteProducts)
                        .delete()  // Use the DELETE method for deletion
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("error api");
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (response.isSuccessful()) {
                                    showToast("berhasil hapus");
                                    Intent intent = new Intent(context, Cart.class);
                                    ((Activity) context).startActivity(intent);



                                }else{
                                    showToast("gagal respon");
                                }
                            }
                        });
                    }
                });


            }
        });

        holder.cartPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showToast("tambah data "+cartModels.get(holder.getAdapterPosition()).getName()+ " id ->" + sessionManager.getIdUser());
                addCartAPI(cartModels.get(holder.getAdapterPosition()).getProductId(),1);
            }
        });

        holder.cartMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showToast("minus data");
                addCartAPI(cartModels.get(holder.getAdapterPosition()).getProductId(),-1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView cartImage, cartDelete,cartPlus,cartMinus;
        TextView cartName,cartPrice,cartAmount,productId;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sessionManager = new SessionManager(itemView.getContext());

            productId = itemView.findViewById(R.id.productId);
            cartImage = itemView.findViewById(R.id.cartImage);
            cartName = itemView.findViewById(R.id.cartName);
            cartPrice = itemView.findViewById(R.id.cartPrice);
            cartAmount = itemView.findViewById(R.id.cartAmount);

            cartDelete= itemView.findViewById(R.id.cartDelete);
            cartPlus = itemView.findViewById(R.id.cartPlus);
            cartMinus =itemView.findViewById(R.id.cartMinus);


        }
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }



    private void addCartAPI(String productId, Integer plusMinusButton){
        OkHttpClient client = new OkHttpClient();
        HttpUrl httpUrlAddToCart = new HttpUrl.Builder()
                .scheme("https")
                .host("us-east-1.aws.data.mongodb-api.com")
                .addPathSegment("app")
                .addPathSegment("application-0-exinc")
                .addPathSegment("endpoint")
                .addPathSegment("updateCart")
                .build();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("user_id", sessionManager.getIdUser());
            jsonBody.put("product_id", productId);
            jsonBody.put("amounts", plusMinusButton);
        } catch (JSONException e) {
            e.printStackTrace();
        };

        // Convert the JSONObject to a JSON string
        String jsonString = jsonBody.toString();

        // Create a RequestBody from the JSON string
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody requestBody = RequestBody.create(JSON, jsonString);

        Request request = new Request.Builder()
                .url(httpUrlAddToCart)
                .put(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {


            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("Error API");
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            //showToast("Berhasil Menambahkan Keranjang");
                            Intent intent = new Intent(context, Cart.class);
                            ((Activity) context).startActivity(intent);
                        } else {
                            showToast("Gagal mengubah keranjang");
                        }
                    }
                });

            }
        });
    }
    public Bitmap decodeImage(String sImage) {

        // Decode base64 string
        byte[] bytes = Base64.decode(sImage, Base64.DEFAULT);
        // Initialize bitmap
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

    }
    public String checkUrlImage(String input, String[] substrings) {
        for (String substring : substrings) {
            if (input.contains(substring)) {
                input = input.replace(substring, "");
            }
        }
        return input;
    }
}

