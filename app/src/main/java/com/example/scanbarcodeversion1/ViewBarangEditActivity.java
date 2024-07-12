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

public class ViewBarangEditActivity extends AppCompatActivity {
    private RecyclerView recyclerViewViewBarang;
    private BarangAdapter barangAdapter;
    private List<Barang> barangList = new ArrayList<>();

    private DatabaseReference databaseBarang;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_barang4);

        recyclerViewViewBarang = findViewById(R.id.recyclerViewViewBarang);
        buttonBack = findViewById(R.id.buttonBack);

        // Initialize Firebase Database
        databaseBarang = FirebaseDatabase.getInstance().getReference("items");
//        databaseBarang = FirebaseDatabase.getInstance().getReference("sizes");
//        databaseBarang = FirebaseDatabase.getInstance().getReference("suppliers");

        // Initialize RecyclerView
        recyclerViewViewBarang.setLayoutManager(new LinearLayoutManager(this));
        barangAdapter = new BarangAdapter(barangList);
        recyclerViewViewBarang.setAdapter(barangAdapter);

        fetchBarangData();

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewBarangEditActivity.this, AddItemActivity.class));
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
                Toast.makeText(ViewBarangEditActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
