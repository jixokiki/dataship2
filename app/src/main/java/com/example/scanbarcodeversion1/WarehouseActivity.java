package com.example.scanbarcodeversion1;

import androidx.appcompat.app.AppCompatActivity;
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

public class WarehouseActivity extends AppCompatActivity {

    private Spinner spinnerNamaBarang;
    private Spinner spinnerUkuranBarang;
    private EditText editTextJumlahBarang;
    private Button buttonTambahBarang;
    private Button buttonBack;
    private Button buttonAddNewItem;
    private Button buttonAddNewSize;
    private DatabaseReference databaseBarang;
    private DatabaseReference databaseItems;
    private DatabaseReference databaseSizes;
    private List<String> namaBarangList;
    private List<String> ukuranBarangList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);

        spinnerNamaBarang = findViewById(R.id.spinnerNamaBarang);
        spinnerUkuranBarang = findViewById(R.id.spinnerUkuranBarang);
        editTextJumlahBarang = findViewById(R.id.editTextJumlahBarang);
        buttonTambahBarang = findViewById(R.id.buttonTambahBarang);
        buttonBack = findViewById(R.id.buttonBack);
        buttonAddNewItem = findViewById(R.id.buttonAddNewItem);
        buttonAddNewSize = findViewById(R.id.buttonAddNewSize);

        // Initialize Firebase Database
        databaseBarang = FirebaseDatabase.getInstance().getReference("barang1");
        databaseItems = FirebaseDatabase.getInstance().getReference("items");
        databaseSizes = FirebaseDatabase.getInstance().getReference("sizes");

        // Fetch items from Firebase
        namaBarangList = new ArrayList<>();
        ukuranBarangList = new ArrayList<>();
        fetchItemsFromFirebase();
        fetchSizesFromFirebase();

        buttonTambahBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahBarang();
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WarehouseActivity.this, PembuatanPO.class));
            }
        });

        buttonAddNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WarehouseActivity.this, AddItemActivity.class));
            }
        });

        buttonAddNewSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WarehouseActivity.this, AddSizeActivity.class));
            }
        });
    }

    private void fetchItemsFromFirebase() {
        databaseItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                namaBarangList.clear();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    String itemName = itemSnapshot.getValue(String.class);
                    namaBarangList.add(itemName);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(WarehouseActivity.this, android.R.layout.simple_spinner_item, namaBarangList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerNamaBarang.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(WarehouseActivity.this, "Failed to load items", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchSizesFromFirebase() {
        databaseSizes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ukuranBarangList.clear();
                for (DataSnapshot sizeSnapshot : dataSnapshot.getChildren()) {
                    String sizeName = sizeSnapshot.getValue(String.class);
                    ukuranBarangList.add(sizeName);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(WarehouseActivity.this, android.R.layout.simple_spinner_item, ukuranBarangList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerUkuranBarang.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(WarehouseActivity.this, "Failed to load sizes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void tambahBarang() {
        String namaBarang = spinnerNamaBarang.getSelectedItem().toString().trim();
        String ukuranBarang = spinnerUkuranBarang.getSelectedItem().toString().trim();
        String jumlahBarang = editTextJumlahBarang.getText().toString().trim();

        if (!namaBarang.isEmpty() && !ukuranBarang.isEmpty() && !jumlahBarang.isEmpty()) {
            String id = databaseBarang.push().getKey();
            Barang barang = new Barang(id, namaBarang, ukuranBarang, jumlahBarang,  "diminta");
            if (id != null) {
                databaseBarang.child(id).setValue(barang);
                Toast.makeText(this, "Barang berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                clearFields();
            }
        } else {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        editTextJumlahBarang.setText("");
        spinnerNamaBarang.setSelection(0);
        spinnerUkuranBarang.setSelection(0);
    }
}