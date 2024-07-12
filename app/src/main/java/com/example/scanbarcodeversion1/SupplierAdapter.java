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

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.SupplierViewHolder> {

    private List<String> supplierList;
    private DatabaseReference databaseSuppliers;

    public SupplierAdapter(List<String> supplierList) {
        this.supplierList = supplierList;
        this.databaseSuppliers = FirebaseDatabase.getInstance().getReference("suppliers");
    }

    @NonNull
    @Override
    public SupplierViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_supplier_layout, parent, false);
        return new SupplierViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupplierViewHolder holder, int position) {
        String supplierName = supplierList.get(position);
        holder.textViewSupplierName.setText(supplierName);

        holder.buttonEdit.setOnClickListener(v -> {
            String newSupplierName = holder.editTextItemName.getText().toString().trim();
            if (!newSupplierName.isEmpty()) {
                updateSupplier(supplierName, newSupplierName, v.getContext());
            } else {
                Toast.makeText(v.getContext(), "Supplier name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        holder.buttonDelete.setOnClickListener(v -> {
            String deleteSupplierName = holder.editTextItemName.getText().toString().trim();
            if (!deleteSupplierName.isEmpty()) {
                deleteSupplier(deleteSupplierName, v.getContext());
            } else {
                Toast.makeText(v.getContext(), "Supplier name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return supplierList.size();
    }

    private void updateSupplier(String oldSupplierName, String newSupplierName, Context context) {
        databaseSuppliers.orderByValue().equalTo(oldSupplierName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().getChildrenCount() > 0) {
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String key = snapshot.getKey();
                    if (key != null) {
                        databaseSuppliers.child(key).setValue(newSupplierName);
                    }
                }
                Toast.makeText(context, "Supplier updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Supplier to update not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteSupplier(String supplierName, Context context) {
        databaseSuppliers.orderByValue().equalTo(supplierName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().getChildrenCount() > 0) {
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String key = snapshot.getKey();
                    if (key != null) {
                        databaseSuppliers.child(key).removeValue();
                    }
                }
                Toast.makeText(context, "Supplier deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Supplier not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class SupplierViewHolder extends RecyclerView.ViewHolder {
        TextView textViewSupplierName;
        EditText editTextItemName;
        Button buttonEdit;
        Button buttonDelete;

        public SupplierViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSupplierName = itemView.findViewById(R.id.textViewSupplierName);
            editTextItemName = itemView.findViewById(R.id.editTextItemName);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
