package com.example.scanbarcodeversion1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AddSupplierActivity extends AppCompatActivity {

    private EditText editTextNewSupplier;
    private Button buttonAddSupplier;
    private Button buttonBack;
    private RecyclerView recyclerViewSuppliers;
    private SupplierAdapter supplierAdapter;
    private List<String> supplierList = new ArrayList<>();

    private DatabaseReference databaseSuppliers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_supplier);

        editTextNewSupplier = findViewById(R.id.editTextNewSupplier);
        buttonAddSupplier = findViewById(R.id.buttonAddSupplier);
        buttonBack = findViewById(R.id.buttonBack);
        recyclerViewSuppliers = findViewById(R.id.recyclerViewSuppliers);

        // Initialize Firebase Database
        databaseSuppliers = FirebaseDatabase.getInstance().getReference("suppliers");

        // Initialize RecyclerView
        recyclerViewSuppliers.setLayoutManager(new LinearLayoutManager(this));
        supplierAdapter = new SupplierAdapter(supplierList);
        recyclerViewSuppliers.setAdapter(supplierAdapter);

        fetchSupplierData();

        buttonAddSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSupplier();
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddSupplierActivity.this, CreatePOActivity.class));
            }
        });
    }

    private void addSupplier() {
        String supplierName = editTextNewSupplier.getText().toString().trim();
        if (!supplierName.isEmpty()) {
            String id = databaseSuppliers.push().getKey();
            if (id != null) {
                databaseSuppliers.child(id).setValue(supplierName);
                Toast.makeText(this, "Supplier added successfully", Toast.LENGTH_SHORT).show();
                editTextNewSupplier.setText("");
            }
        } else {
            Toast.makeText(this, "Supplier name cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchSupplierData() {
        databaseSuppliers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                supplierList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String supplierName = snapshot.getValue(String.class);
                    if (supplierName != null) {
                        supplierList.add(supplierName);
                    }
                }
                supplierAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddSupplierActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
