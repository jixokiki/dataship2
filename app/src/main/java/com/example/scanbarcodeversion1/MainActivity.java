package com.example.scanbarcodeversion1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                if (username.equals("supervisor") && password.equals("123")) {
                    Toast.makeText(MainActivity.this, "Login successful as Supervisor", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(MainActivity.this, SupervisorActivity.class));
                    startActivity(new Intent(MainActivity.this, TampilanSupervisor.class));
                } else if (username.equals("purchasing") && password.equals("456")) {
                    Toast.makeText(MainActivity.this, "Login successful as Purchasing", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(MainActivity.this, PurchasingActivity.class));
                    startActivity(new Intent(MainActivity.this, TampilanPembuatanPO.class));
                } else if (username.equals("warehouse") && password.equals("789")) {
                    Toast.makeText(MainActivity.this, "Login successful as Warehouse", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, PembuatanPO.class));
                } else {
                    Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}