package com.example.scanbarcodeversion1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class TampilanSupervisor extends AppCompatActivity {
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonPembuatanPO;

    private Button buttonViewBarang;
//    private  Button buttonAcc;

    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampilan_supervisor);

        buttonPembuatanPO = findViewById(R.id.buttonPembuatanPO);
        buttonViewBarang = findViewById(R.id.buttonViewBarang);
//        buttonAcc = findViewById(R.id.buttonAcc);
        buttonBack = findViewById(R.id.buttonBack);

        buttonPembuatanPO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TampilanSupervisor.this, ApprovePOActivity.class));
            }
        });

        buttonViewBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TampilanSupervisor.this, ListRO.class));
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TampilanSupervisor.this, MainActivity.class));
            }
        });
//        buttonAcc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(TampilanSupervisor.this, PrintInvoiceActivity.class));
//            }
//        });
    }
}
