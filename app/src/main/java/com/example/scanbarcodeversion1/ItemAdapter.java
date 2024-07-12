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

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<String> itemList;
    private DatabaseReference databaseItems;

    public ItemAdapter(List<String> itemList) {
        this.itemList = itemList;
        this.databaseItems = FirebaseDatabase.getInstance().getReference("items");
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        String itemName = itemList.get(position);
        holder.textViewItemName.setText(itemName);

        holder.buttonEdit.setOnClickListener(v -> {
            String newItemName = holder.editTextItemName.getText().toString().trim();
            if (!newItemName.isEmpty()) {
                updateItem(itemName, newItemName, v.getContext());
            } else {
                Toast.makeText(v.getContext(), "Item name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        holder.buttonDelete.setOnClickListener(v -> {
            String deleteItemName = holder.editTextItemName.getText().toString().trim();
            if (!deleteItemName.isEmpty()) {
                deleteItem(deleteItemName, v.getContext());
            } else {
                Toast.makeText(v.getContext(), "Item name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    private void updateItem(String oldItemName, String newItemName, Context context) {
        databaseItems.orderByValue().equalTo(oldItemName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().getChildrenCount() > 0) {
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String key = snapshot.getKey();
                    if (key != null) {
                        databaseItems.child(key).setValue(newItemName);
                    }
                }
                Toast.makeText(context, "Item updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Item to update not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteItem(String itemName, Context context) {
        databaseItems.orderByValue().equalTo(itemName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().getChildrenCount() > 0) {
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String key = snapshot.getKey();
                    if (key != null) {
                        databaseItems.child(key).removeValue();
                    }
                }
                Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Item not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItemName;
        EditText editTextItemName;
        Button buttonEdit;
        Button buttonDelete;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            editTextItemName = itemView.findViewById(R.id.editTextItemName);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
