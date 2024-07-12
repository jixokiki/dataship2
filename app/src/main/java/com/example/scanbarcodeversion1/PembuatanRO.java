package com.example.scanbarcodeversion1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PembuatanRO extends AppCompatActivity {

//    private Button buttonPembuatanPO;

    private Button buttonViewDimintaPO;
    private  Button buttonCreatePO;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembuatanpo);

        buttonCreatePO = findViewById(R.id.buttonCreatePO);
        buttonViewDimintaPO = findViewById(R.id.buttonViewDimintaPO);
        buttonBack = findViewById(R.id.buttonBack);

        buttonCreatePO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PembuatanRO.this, CreatePOActivity.class));
            }
        });

        buttonViewDimintaPO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PembuatanRO.this, ProcessBarangActivity.class));
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PembuatanRO.this, TampilanPembuatanPO.class));
            }
        });
    }
}
