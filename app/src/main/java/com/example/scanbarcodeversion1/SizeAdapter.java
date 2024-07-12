package com.example.scanbarcodeversion1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.SizeViewHolder> {

    private List<String> sizeList;
    private DatabaseReference databaseSizes;

    public SizeAdapter(List<String> sizeList) {
        this.sizeList = sizeList;
        this.databaseSizes = FirebaseDatabase.getInstance().getReference("sizes");
    }

    @NonNull
    @Override
    public SizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_size_layout, parent, false);
        return new SizeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeViewHolder holder, int position) {
        String sizeName = sizeList.get(position);
        holder.textViewSizeName.setText(sizeName);

        holder.buttonEdit.setOnClickListener(v -> {
            String newSizeName = holder.editTextItemName.getText().toString().trim();
            if (!newSizeName.isEmpty()) {
                updateSize(sizeName, newSizeName, v.getContext());
            } else {
                Toast.makeText(v.getContext(), "Size name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        holder.buttonDelete.setOnClickListener(v -> {
            String deleteSizeName = holder.editTextItemName.getText().toString().trim();
            if (!deleteSizeName.isEmpty()) {
                deleteSize(deleteSizeName, v.getContext());
            } else {
                Toast.makeText(v.getContext(), "Size name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return sizeList.size();
    }

    private void updateSize(String oldSizeName, String newSizeName, Context context) {
        databaseSizes.orderByValue().equalTo(oldSizeName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().getChildrenCount() > 0) {
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String key = snapshot.getKey();
                    if (key != null) {
                        databaseSizes.child(key).setValue(newSizeName);
                    }
                }
                Toast.makeText(context, "Size updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Size to update not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteSize(String sizeName, Context context) {
        databaseSizes.orderByValue().equalTo(sizeName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().getChildrenCount() > 0) {
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String key = snapshot.getKey();
                    if (key != null) {
                        databaseSizes.child(key).removeValue();
                    }
                }
                Toast.makeText(context, "Size deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Size not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class SizeViewHolder extends RecyclerView.ViewHolder {
        TextView textViewSizeName;
        EditText editTextItemName;
        Button buttonEdit;
        Button buttonDelete;

        public SizeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSizeName = itemView.findViewById(R.id.textViewSizeName);
            editTextItemName = itemView.findViewById(R.id.editTextItemName);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
