package com.example.scanbarcodeversion1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddSizeActivity extends AppCompatActivity {

    private EditText editTextNewSize;
    private Button buttonAddSize;
    private Button buttonBack;
    private RecyclerView recyclerViewSizes;
    private SizeAdapter sizeAdapter;
    private List<String> sizeList = new ArrayList<>();

    private DatabaseReference databaseSizes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_size);

        editTextNewSize = findViewById(R.id.editTextNewSize);
        buttonAddSize = findViewById(R.id.buttonAddSize);
        buttonBack = findViewById(R.id.buttonBack);
        recyclerViewSizes = findViewById(R.id.recyclerViewSizes);

        // Initialize Firebase Database
        databaseSizes = FirebaseDatabase.getInstance().getReference("sizes");

        // Initialize RecyclerView
        recyclerViewSizes.setLayoutManager(new LinearLayoutManager(this));
        sizeAdapter = new SizeAdapter(sizeList);
        recyclerViewSizes.setAdapter(sizeAdapter);

        fetchSizeData();

        buttonAddSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSize();
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddSizeActivity.this, WarehouseActivity.class));
            }
        });
    }

    private void addSize() {
        String sizeName = editTextNewSize.getText().toString().trim();
        if (!sizeName.isEmpty()) {
            String id = databaseSizes.push().getKey();
            if (id != null) {
                databaseSizes.child(id).setValue(sizeName);
                Toast.makeText(this, "Size added successfully", Toast.LENGTH_SHORT).show();
                editTextNewSize.setText("");
            }
        } else {
            Toast.makeText(this, "Size name cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchSizeData() {
        databaseSizes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sizeList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String sizeName = snapshot.getValue(String.class);
                    if (sizeName != null) {
                        sizeList.add(sizeName);
                    }
                }
                sizeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddSizeActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
