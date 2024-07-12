package com.example.scanbarcodeversion1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class ListPO extends AppCompatActivity {
    private RecyclerView recyclerViewViewBarang;
    private BarangAdapter barangAdapter;
    private List<Barang> barangList = new ArrayList<>();

    private DatabaseReference databaseBarang;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_barang2);

        recyclerViewViewBarang = findViewById(R.id.recyclerViewViewBarang);

        // Initialize Firebase Database
        databaseBarang = FirebaseDatabase.getInstance().getReference("barang1");

        // Initialize RecyclerView
        recyclerViewViewBarang.setLayoutManager(new LinearLayoutManager(this));
        barangAdapter = new BarangAdapter(barangList);
        recyclerViewViewBarang.setAdapter(barangAdapter);

        fetchBarangData();

        buttonBack = findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListPO.this, TampilanPembuatanPO.class));
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
                    barangList.add(barang);
                }
                barangAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ListPO.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
