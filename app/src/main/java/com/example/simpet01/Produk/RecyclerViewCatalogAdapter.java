package com.example.simpet01.Produk;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpet01.ApiController.FormattedCurrency;
import com.example.simpet01.ApiController.ImageConverter;
import com.example.simpet01.Home;
import com.example.simpet01.R;
import com.example.simpet01.SessionManager;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecyclerViewCatalogAdapter extends RecyclerView.Adapter<RecyclerViewCatalogAdapter.MyViewHolder> {

    List<ProdukModel> listProduk;
    LayoutInflater inflater;
    ImageConverter imageConverter;
    Context context;

    FormattedCurrency formattedCurrency;
    SessionManager sessionManager;

    public RecyclerViewCatalogAdapter (Context context, List<ProdukModel> listProduk){
        this.listProduk = listProduk;
        this.inflater= LayoutInflater.from(context);
        this.context= context;
        //this.imageConverter = new ImageConverter(); // Initialize ImageConverter


    }

//    int []arr;

//    public RecyclerViewCatalogAdapter(int[] arr) {
//        this.arr = arr;
//    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_catalog_view,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        holder.catalogImage.setImageResource(arr[position]);
//        holder.catalogJudul.setText("Image No. "+position);

        formattedCurrency = new FormattedCurrency();

        String input = listProduk.get(position).getFirstImages();
        String[] substrings = {"data:image/png;base64,", "data:image/jpeg;base64,"};
        String avatarStringForMobile = checkUrlImage(input, substrings);
        Bitmap avatarBitmap =  decodeImage(avatarStringForMobile);
        //imageView.setImageBitmap(avatarBitmap);
       // Toast.makeText(EditProfil.this,"Ada foto profil",Toast.LENGTH_SHORT).show();

        holder.catalogImage.setImageBitmap(avatarBitmap);
        holder.catalogJudul.setText(listProduk.get(position).getName());
        //holder.catalogHarga.setText((listProduk.get(position).getPrice()));
        holder.catalogHarga.setText(formattedCurrency.formatCurrency(listProduk.get(position).getPrice()));


        holder.catalogCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, detailProduk.class);
                intent.putExtra("extraId", listProduk.get(holder.getAdapterPosition()).get_id());
                intent.putExtra("extraImage", listProduk.get(holder.getAdapterPosition()).getFirstImages());
                intent.putExtra("extraName",listProduk.get(holder.getAdapterPosition()).getName());
                intent.putExtra("extraPrice", listProduk.get(holder.getAdapterPosition()).getPrice());
                intent.putExtra("extraDescriptions", listProduk.get(holder.getAdapterPosition()).getDescriptions());
                context.startActivity(intent);
            }
        });

        if(sessionManager.getIdUser().equals("65652a2e6ea647e40be542fb")){
            holder.catalogCard.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final Dialog dialog= new Dialog(context);
                    dialog.setContentView(R.layout.dialog_product);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_view);
                    dialog.setTitle("Pilih Aksi");
                    dialog.show();

                    //final Barang b = (Barang) lv.getAdapter().getItem(position);

                    Button btnEdit = dialog.findViewById(R.id.btn_editProduct);
                    Button btnDelete = dialog.findViewById(R.id.btn_deleteProduct);



                    btnEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, editProduk.class);
                            intent.putExtra("extraId", listProduk.get(holder.getAdapterPosition()).get_id());
                            intent.putExtra("extraImage", listProduk.get(holder.getAdapterPosition()).getFirstImages());
                            intent.putExtra("secondImage", listProduk.get(holder.getAdapterPosition()).getSecondImage());
                            intent.putExtra("thirdImage", listProduk.get(holder.getAdapterPosition()).getThirdImage());
                            intent.putExtra("fourthImage", listProduk.get(holder.getAdapterPosition()).getFourthImage());
                            intent.putExtra("extraName",listProduk.get(holder.getAdapterPosition()).getName());
                            intent.putExtra("extraPrice", listProduk.get(holder.getAdapterPosition()).getPrice());
                            intent.putExtra("extraDescriptions", listProduk.get(holder.getAdapterPosition()).getDescriptions());
                            context.startActivity(intent);

                        }
                    });

                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(context, "Hapus", Toast.LENGTH_SHORT).show();


                            OkHttpClient client = new OkHttpClient();
                            HttpUrl httpUrlDeleteProducts = new HttpUrl.Builder()
                                    .scheme("https")
                                    .host("us-east-1.aws.data.mongodb-api.com")
                                    .addPathSegment("app")
                                    .addPathSegment("application-0-exinc")
                                    .addPathSegment("endpoint")
                                    .addPathSegment("deleteProducts")
                                    .addQueryParameter("id",listProduk.get(holder.getAdapterPosition()).get_id())
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
                                                Intent intent = new Intent(context, Home.class);
                                                intent.putExtra("FRAGMENT_IDENTIFIER", "catalog"); // Ganti dengan identifier yang sesuai
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

                    return true;
                }
            });
        } else{
            //showToast("Fitur Admin");
        }



    }


    @Override
    public int getItemCount() {
        //return 0;
        return listProduk.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView catalogImage;
        TextView catalogHarga,catalogJudul;
        CardView catalogCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            catalogImage = itemView.findViewById(R.id.catalogImage);
            catalogJudul = itemView.findViewById(R.id.catalogJudul);
            catalogHarga = itemView.findViewById(R.id.catalogHarga);
            catalogCard = itemView.findViewById(R.id.catalogCard);
            sessionManager = new SessionManager(context);


        }
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
    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }



}
