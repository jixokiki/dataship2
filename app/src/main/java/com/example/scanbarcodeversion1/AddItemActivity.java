package com.example.scanbarcodeversion1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AddItemActivity extends AppCompatActivity {

    private EditText editTextNewItem;
    private Button buttonAddItem;
    private Button buttonBack;
    private RecyclerView recyclerViewItems;
    private ItemAdapter itemAdapter;
    private List<String> itemList = new ArrayList<>();

    private DatabaseReference databaseItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        editTextNewItem = findViewById(R.id.editTextNewItem);
        buttonAddItem = findViewById(R.id.buttonAddItem);
        buttonBack = findViewById(R.id.buttonBack);
        recyclerViewItems = findViewById(R.id.recyclerViewItems);

        // Initialize Firebase Database
        databaseItems = FirebaseDatabase.getInstance().getReference("items");

        // Initialize RecyclerView
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new ItemAdapter(itemList);
        recyclerViewItems.setAdapter(itemAdapter);

        fetchBarangData();

        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddItemActivity.this, WarehouseActivity.class));
            }
        });
    }

    private void addItem() {
        String itemName = editTextNewItem.getText().toString().trim();
        if (!itemName.isEmpty()) {
            String id = databaseItems.push().getKey();
            if (id != null) {
                databaseItems.child(id).setValue(itemName);
                Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show();
                editTextNewItem.setText("");
            }
        } else {
            Toast.makeText(this, "Item name cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchBarangData() {
        databaseItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String itemName = snapshot.getValue(String.class);
                    if (itemName != null) {
                        itemList.add(itemName);
                    }
                }
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddItemActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

