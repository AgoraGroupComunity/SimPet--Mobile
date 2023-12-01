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

import com.example.simpet01.R;

import java.util.List;

public class SelesaiiAdapter extends RecyclerView.Adapter<SelesaiiAdapter.MyViewHolder> {

    private Context context;
    private List<SelesaiiModel> selesaiiList;
    private LayoutInflater inflater;

    public SelesaiiAdapter(Context context, List<SelesaiiModel> selesaiiList) {
        this.context = context;
        this.selesaiiList = selesaiiList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_history_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SelesaiiModel selesaiiModel = selesaiiList.get(position);

        // Bind your SelesaiiModel data to the view elements in the ViewHolder
        holder.dateOrder.setText(selesaiiModel.getDateOrder());
        holder.invoiceOrder.setText(String.valueOf(selesaiiModel.getInvoiceOrder()));
        holder.totalOrder.setText(String.valueOf(selesaiiModel.getTotalOrder()));
    }

    @Override
    public int getItemCount() {
        return selesaiiList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView dateOrder, invoiceOrder, totalOrder;
        CardView statusSelesai,statusDibatalkan,statusDiproses;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            dateOrder = itemView.findViewById(R.id.dateOrder);
            invoiceOrder = itemView.findViewById(R.id.invoiceOrder);
            totalOrder = itemView.findViewById(R.id.totalOrder);
            statusSelesai = itemView.findViewById(R.id.statusSelesai);
            statusDibatalkan = itemView.findViewById(R.id.statusDibatalkan);
            statusDiproses = itemView.findViewById(R.id.statusDiproses);
        }
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
