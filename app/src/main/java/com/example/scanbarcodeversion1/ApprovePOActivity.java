package com.example.scanbarcodeversion1;

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

public class ApprovePOActivity extends AppCompatActivity {

    private RecyclerView recyclerViewApprovePO;
    private POApproveAdapter poApproveAdapter;
    private List<Barang> barangList = new ArrayList<>();
    private List<String> selectedIds = new ArrayList<>();

    private DatabaseReference databaseBarang;
    private Button buttonApprove;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_po);

        recyclerViewApprovePO = findViewById(R.id.recyclerViewApprovePO);
        buttonApprove = findViewById(R.id.buttonApprove);
        buttonBack = findViewById(R.id.buttonBack);

        // Initialize Firebase Database
        databaseBarang = FirebaseDatabase.getInstance().getReference("barang1");

        // Initialize RecyclerView
        recyclerViewApprovePO.setLayoutManager(new LinearLayoutManager(this));
        poApproveAdapter = new POApproveAdapter(barangList, new POApproveAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(String id) {
                selectedIds.add(id);
            }

            @Override
            public void onItemUncheck(String id) {
                selectedIds.remove(id);
            }
        });
        recyclerViewApprovePO.setAdapter(poApproveAdapter);

        buttonApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approvePO();
            }
        });

        fetchPOBarangData();
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ApprovePOActivity.this, TampilanSupervisor.class));
            }
        });
    }

    private void fetchPOBarangData() {
        databaseBarang.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                barangList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Barang barang = snapshot.getValue(Barang.class);
                    if ("PO dibuat".equals(barang.getStatus())) {
                        barangList.add(barang);
                    }
                }
                poApproveAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ApprovePOActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void approvePO() {
        for (String id : selectedIds) {
            databaseBarang.child(id).child("status").setValue("disetujui");
        }
        Toast.makeText(this, "PO berhasil disetujui", Toast.LENGTH_SHORT).show();
        selectedIds.clear();
        poApproveAdapter.notifyDataSetChanged();
    }
}