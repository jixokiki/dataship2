package com.example.scanbarcodeversion1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ProcessBarangAdapter extends RecyclerView.Adapter<ProcessBarangAdapter.BarangViewHolder> {

    private List<Barang> barangList;
    private OnItemCheckListener onItemCheckListener;

    public interface OnItemCheckListener {
        void onItemCheck(String id);
        void onItemUncheck(String id);
    }

    public ProcessBarangAdapter(List<Barang> barangList, OnItemCheckListener onItemCheckListener) {
        this.barangList = barangList;
        this.onItemCheckListener = onItemCheckListener;
    }

    @NonNull
    @Override
    public BarangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barang_process, parent, false);
        return new BarangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarangViewHolder holder, int position) {
        Barang barang = barangList.get(position);
        holder.textViewNamaBarang.setText(barang.getNamaBarang());
        holder.textViewJumlahBarang.setText(barang.getJumlahBarang());
        holder.textViewStatus.setText(barang.getStatus());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        if (barang.getServerTimeStamp() != null) {
            holder.textViewTimestamp.setText(sdf.format(barang.getServerTimeStamp()));
        } else {
            holder.textViewTimestamp.setText("N/A");
        }

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(false);

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                onItemCheckListener.onItemCheck(barang.getId());
            } else {
                onItemCheckListener.onItemUncheck(barang.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return barangList.size();
    }

    public static class BarangViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNamaBarang, textViewJumlahBarang, textViewStatus, textViewTimestamp;
        CheckBox checkBox;

        public BarangViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNamaBarang = itemView.findViewById(R.id.textViewNamaBarang);
            textViewJumlahBarang = itemView.findViewById(R.id.textViewJumlahBarang);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
