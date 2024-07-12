package com.example.scanbarcodeversion1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CreatePOActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPOBarang;
    private POBarangAdapter poBarangAdapter;
    private List<Barang> barangList = new ArrayList<>();
    private List<String> selectedIds = new ArrayList<>();

    private DatabaseReference databaseBarang;
    private DatabaseReference databaseSuppliers;
    private EditText editTextHarga;
    private  EditText editTextAlamat;
    private Spinner spinnerSupplier;
    private Button buttonCreatePO;
    private Button buttonBack;
    private Button buttonAddSupplier;
    private List<String> supplierList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_po);

        recyclerViewPOBarang = findViewById(R.id.recyclerViewPOBarang);
        editTextHarga = findViewById(R.id.editTextHarga);
        spinnerSupplier = findViewById(R.id.spinnerSupplier);
        buttonCreatePO = findViewById(R.id.buttonCreatePO);
        buttonBack = findViewById(R.id.buttonBack);
        buttonAddSupplier = findViewById(R.id.buttonAddSupplier);
        editTextAlamat = findViewById(R.id.editTextAlamat);

        // Initialize Firebase Database
        databaseBarang = FirebaseDatabase.getInstance().getReference("barang1");
        databaseSuppliers = FirebaseDatabase.getInstance().getReference("suppliers");

        // Initialize RecyclerView
        recyclerViewPOBarang.setLayoutManager(new LinearLayoutManager(this));
        poBarangAdapter = new POBarangAdapter(barangList, new POBarangAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(String id) {
                selectedIds.add(id);
            }

            @Override
            public void onItemUncheck(String id) {
                selectedIds.remove(id);
            }
        });
        recyclerViewPOBarang.setAdapter(poBarangAdapter);

        // Fetch suppliers from Firebase
        supplierList = new ArrayList<>();
        fetchSuppliersFromFirebase();

        buttonCreatePO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPO();
            }
        });

        fetchBarangData();

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreatePOActivity.this, PembuatanRO.class));
            }
        });

        buttonAddSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreatePOActivity.this, AddSupplierActivity.class));
            }
        });
    }

    private void fetchBarangData() {
        databaseBarang.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                barangList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Barang barang = snapshot.getValue(Barang.class);
                    if ("diproses".equals(barang.getStatus())) {
                        barangList.add(barang);
                    }
                }
                poBarangAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CreatePOActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchSuppliersFromFirebase() {
        databaseSuppliers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                supplierList.clear();
                for (DataSnapshot supplierSnapshot : dataSnapshot.getChildren()) {
                    String supplierName = supplierSnapshot.getValue(String.class);
                    supplierList.add(supplierName);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(CreatePOActivity.this, android.R.layout.simple_spinner_item, supplierList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerSupplier.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CreatePOActivity.this, "Failed to load suppliers", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createPO() {
        String harga = editTextHarga.getText().toString().trim();
        String supplier = spinnerSupplier.getSelectedItem().toString().trim();
        String alamat = editTextAlamat.getText().toString().trim();

        if (!harga.isEmpty() && !supplier.isEmpty() && !selectedIds.isEmpty()) {
            for (String id : selectedIds) {
                databaseBarang.child(id).child("harga").setValue(harga);
                databaseBarang.child(id).child("supplier").setValue(supplier);
                databaseBarang.child(id).child("alamat").setValue(alamat);
                databaseBarang.child(id).child("status").setValue("PO dibuat");
            }
            Toast.makeText(this, "PO berhasil dibuat", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(this, "Semua field harus diisi dan minimal satu barang dipilih", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        editTextHarga.setText("");
        editTextAlamat.setText("");
        spinnerSupplier.setSelection(0);
        selectedIds.clear();
        poBarangAdapter.notifyDataSetChanged();
    }
}