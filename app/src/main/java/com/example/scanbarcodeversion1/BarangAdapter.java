package com.example.scanbarcodeversion1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class BarangAdapter extends RecyclerView.Adapter<BarangAdapter.BarangViewHolder> {

    private List<Barang> barangList;

    public BarangAdapter(List<Barang> barangList) {
        this.barangList = barangList;
    }

    @NonNull
    @Override
    public BarangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barang2, parent, false);
        return new BarangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarangViewHolder holder, int position) {
        Barang barang = barangList.get(position);
        holder.textViewNamaBarang.setText(barang.getNamaBarang());
        holder.textViewJumlahBarang.setText(barang.getJumlahBarang());
        holder.textViewUkuranBarang.setText(barang.getUkuranBarang());
        holder.textViewStatus.setText(barang.getStatus());
        holder.textViewAlamatSuplier.setText(barang.getAlamatSuplier());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        if (barang.getServerTimeStamp() != null) {
            holder.textViewTimestamp.setText(sdf.format(barang.getServerTimeStamp()));
        } else {
            holder.textViewTimestamp.setText("N/A");
        }
    }

    @Override
    public int getItemCount() {
        return barangList.size();
    }

    public static class BarangViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNamaBarang, textViewJumlahBarang, textViewUkuranBarang,textViewStatus, textViewAlamatSuplier, textViewTimestamp;

        public BarangViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNamaBarang = itemView.findViewById(R.id.textViewNamaBarang);
            textViewJumlahBarang = itemView.findViewById(R.id.textViewJumlahBarang);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewAlamatSuplier = itemView.findViewById(R.id.textViewAlamatSuplier);
            textViewUkuranBarang = itemView.findViewById(R.id.textViewUkuranBarang);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
        }
    }
}
