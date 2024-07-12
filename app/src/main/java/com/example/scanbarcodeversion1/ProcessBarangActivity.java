package com.example.scanbarcodeversion1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ProcessBarangActivity extends AppCompatActivity {
    private RecyclerView recyclerViewProcessBarang;
    private ProcessBarangAdapter processBarangAdapter;
    private List<Barang> barangList = new ArrayList<>();
    private List<String> selectedIds = new ArrayList<>();

    private DatabaseReference databaseBarang;
    private Button buttonProsesBarang;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_barang);

        recyclerViewProcessBarang = findViewById(R.id.recyclerViewProcessBarang);
        buttonProsesBarang = findViewById(R.id.buttonProsesBarang);
        buttonBack = findViewById(R.id.buttonBack);

        // Initialize Firebase Database
        databaseBarang = FirebaseDatabase.getInstance().getReference("barang1");

        // Initialize RecyclerView
        recyclerViewProcessBarang.setLayoutManager(new LinearLayoutManager(this));
        processBarangAdapter = new ProcessBarangAdapter(barangList, new ProcessBarangAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(String id) {
                selectedIds.add(id);
            }

            @Override
            public void onItemUncheck(String id) {
                selectedIds.remove(id);
            }
        });
        recyclerViewProcessBarang.setAdapter(processBarangAdapter);

        buttonProsesBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prosesBarang();
            }
        });

        fetchBarangData();
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProcessBarangActivity.this, PembuatanRO.class));
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
                    if ("diminta".equals(barang.getStatus())) {
                        barangList.add(barang);
                    }
                }
                processBarangAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProcessBarangActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prosesBarang() {
        for (String id : selectedIds) {
            databaseBarang.child(id).child("status").setValue("diproses");
        }
        Toast.makeText(this, "Barang berhasil diproses", Toast.LENGTH_SHORT).show();
        selectedIds.clear();
    }
}
