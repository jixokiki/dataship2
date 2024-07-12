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

public class PrintInvoiceActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPrintInvoice;
    private PrintInvoiceAdapter printInvoiceAdapter;
    private List<Barang> barangList = new ArrayList<>();
    private List<String> selectedIds = new ArrayList<>();

    private DatabaseReference databaseBarang;
    private Button buttonPrintPDF;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_invoice);

        recyclerViewPrintInvoice = findViewById(R.id.recyclerViewPrintInvoice);
        buttonPrintPDF = findViewById(R.id.buttonPrintPDF);
        buttonBack = findViewById(R.id.buttonBack);

        // Initialize Firebase Database
        databaseBarang = FirebaseDatabase.getInstance().getReference("barang1");

        // Initialize RecyclerView
        recyclerViewPrintInvoice.setLayoutManager(new LinearLayoutManager(this));
        printInvoiceAdapter = new PrintInvoiceAdapter(barangList, new PrintInvoiceAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(String id) {
                selectedIds.add(id);
            }

            @Override
            public void onItemUncheck(String id) {
                selectedIds.remove(id);
            }
        });
        recyclerViewPrintInvoice.setAdapter(printInvoiceAdapter);

        buttonPrintPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printPDF();
            }
        });

        fetchPrintInvoiceData();

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PrintInvoiceActivity.this, TampilanPembuatanPO.class));
            }
        });
    }

    private void fetchPrintInvoiceData() {
        databaseBarang.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                barangList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Barang barang = snapshot.getValue(Barang.class);
                    if ("disetujui".equals(barang.getStatus())) {
                        barangList.add(barang);
                    }
                }
                printInvoiceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PrintInvoiceActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void printPDF() {
        for (String id : selectedIds) {
            databaseBarang.child(id).child("status").setValue("PO Tercetak");
        }
        Toast.makeText(this, "Status diubah dan PDF siap untuk dicetak", Toast.LENGTH_SHORT).show();
        selectedIds.clear();
        printInvoiceAdapter.notifyDataSetChanged();

        // Implement PDF generation functionality here
        // Pindah ke SupervisorActivity
        Intent intent = new Intent(PrintInvoiceActivity.this, SupervisorActivity.class);
        startActivity(intent);
    }
}
