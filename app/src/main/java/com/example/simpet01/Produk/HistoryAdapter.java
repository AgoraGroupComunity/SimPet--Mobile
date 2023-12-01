package com.example.simpet01.Produk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpet01.ApiController.FormattedCurrency;
import com.example.simpet01.R;
import com.example.simpet01.SessionManager;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {


    Context context;
    List<HistoryModel> historyModels;
    LayoutInflater inflater;
    SessionManager sessionManager;
    FormattedCurrency formattedCurrency;

    public HistoryAdapter(Context context, List<HistoryModel> historyModels) {
        this.context = context;
        this.historyModels = historyModels;
        this.inflater= LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_history_view,parent,false);
        HistoryAdapter.MyViewHolder myViewHolder = new HistoryAdapter.MyViewHolder(view);
        return myViewHolder;
        //return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.single_cart_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull  MyViewHolder holder, int position) {

        formattedCurrency = new FormattedCurrency();


        holder.invoiceOrder.setText("INV/XXIII/"+historyModels.get(position).getInvoiceOrder()+"");
        holder.totalOrder.setText(formattedCurrency.formatCurrency(historyModels.get(position).getTotalOrder()));
        holder.dateOrder.setText((historyModels.get(position).getDateOrder()));

        if(historyModels.get(position).getStatus().equals("Diproses")){
            holder.statusDiproses.setVisibility(View.VISIBLE);
        }
        if(historyModels.get(position).getStatus().equals("Selesai")){
            holder.statusSelesai.setVisibility(View.VISIBLE);
        }
        if(historyModels.get(position).getStatus().equals("Dibatalkan")){
            holder.statusDibatalkan.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return historyModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView invoiceOrder,totalOrder,dateOrder;
        CardView statusSelesai,statusDibatalkan,statusDiproses;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sessionManager = new SessionManager(itemView.getContext());

            invoiceOrder = itemView.findViewById(R.id.invoiceOrder);
            totalOrder = itemView.findViewById(R.id.totalOrder);
            dateOrder = itemView.findViewById(R.id.dateOrder);
            statusSelesai = itemView.findViewById(R.id.statusSelesai);
            statusDibatalkan = itemView.findViewById(R.id.statusDibatalkan);
            statusDiproses = itemView.findViewById(R.id.statusDiproses);

        }
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }



}

