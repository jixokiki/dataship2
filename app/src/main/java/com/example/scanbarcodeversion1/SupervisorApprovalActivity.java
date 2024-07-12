package com.example.scanbarcodeversion1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SupervisorApprovalActivity extends AppCompatActivity {
    private DatabaseReference preorderRef;
    private TextView preorderDetailsText;
    private Button approveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_approval);

        preorderRef = FirebaseDatabase.getInstance().getReference().child("preorders");
        preorderDetailsText = findViewById(R.id.preorder_details_text);
        approveButton = findViewById(R.id.approve_button);

        // Retrieve preorder information from Firebase
        preorderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Preorder preorder = snapshot.getValue(Preorder.class);
                        displayPreorderDetails(preorder);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Error handling
                Toast.makeText(SupervisorApprovalActivity.this, "Terjadi kesalahan: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approvePreorder();
            }
        });
    }

    private void displayPreorderDetails(Preorder preorder) {
        String details = "Item Name: " + preorder.getItemName() + "\n" +
                "Item Count: " + preorder.getItemCount() + "\n" +
                "Item Price: " + preorder.getItemPrice() + "\n" +
                "Requested Quantity: " + preorder.getRequestedQuantity();
        preorderDetailsText.setText(details);
    }

    private void approvePreorder() {
        // Generate PDF with preorder details
        String pdfFileName = "preorder.pdf";
        File pdfFile = new File(getFilesDir(), pdfFileName);

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            document.add(new Paragraph(preorderDetailsText.getText().toString()));
            document.close();

            Toast.makeText(this, "PDF generated at: " + pdfFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to generate PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
