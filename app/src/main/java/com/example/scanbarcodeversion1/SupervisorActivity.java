package com.example.scanbarcodeversion1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SupervisorActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 101;

    private ListView listViewBarang;

    private Button buttonUploadImage;
    private Button buttonAcc;
    private Button buttonUploadImage2;

    private Button buttonReplaceImage;

    private Button buttonBack;

    private DatabaseReference databaseBarang;
    private StorageReference storageReference;
    private List<Barang> listBarang;
    private BarangAdapter barangAdapter;

    private int selectedPosition = -1;
//    private Uri imageUri;
//    private int selectedImageView = 0; // 0 for imageViewBarang, 1 for imageViewBarang2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor);

        // Check for storage permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }

        listViewBarang = findViewById(R.id.listViewBarang);
//        buttonUploadImage = findViewById(R.id.buttonUploadImage);
        buttonAcc = findViewById(R.id.buttonAcc);
        buttonBack = findViewById(R.id.buttonBack);
//        buttonUploadImage2 = findViewById(R.id.buttonUploadImage2);
//        buttonReplaceImage = findViewById(R.id.buttonReplaceImage);

        databaseBarang = FirebaseDatabase.getInstance().getReference("barang1");
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        listBarang = new ArrayList<>();
        barangAdapter = new BarangAdapter(listBarang);

        listViewBarang.setAdapter(barangAdapter);

//        buttonUploadImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (selectedPosition != -1) {
//                    selectedImageView = 0; // Set selectedImageView to indicate imageViewBarang
//                    openFileChooser();
//                } else {
//                    Toast.makeText(SupervisorActivity.this, "Select an item first", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

//        buttonUploadImage2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (selectedPosition != -1) {
//                    selectedImageView = 1; // Set selectedImageView to indicate imageViewBarang2
//                    openFileChooser();
//                } else {
//                    Toast.makeText(SupervisorActivity.this, "Select an item first", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

//        buttonReplaceImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (selectedPosition != -1) {
//                    if (selectedImageView == 0) {
//                        uploadImage();
//                    } else if (selectedImageView == 1) {
//                        uploadImage2();
//                    }
//                } else {
//                    Toast.makeText(SupervisorActivity.this, "Select an item first", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SupervisorActivity.this, TampilanPembuatanPO.class));
            }
        });

        // Set listener untuk tombol Acc
        buttonAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition != -1) {
                    createAndSavePdf();
                } else {
                    Toast.makeText(SupervisorActivity.this, "Select an item first", Toast.LENGTH_SHORT).show();
                }
            }
        });
        loadData();
    }

//    private void openFileChooser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, PICK_IMAGE_REQUEST);
//    }


    // Metode untuk membuat PDF dari item barang yang dipilih dan menyimpannya
//    private void createAndSavePdf() {
//        // Memilih item yang dipilih
//        final Barang barang = listBarang.get(selectedPosition);
//
//        // Gunakan AsyncTask untuk menghindari NetworkOnMainThreadException
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    // Membuat direktori untuk menyimpan PDF di External Storage
//                    File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "MyPDFs");
//                    if (!pdfFolder.exists()) {
//                        pdfFolder.mkdirs();
//                    }
//
//                    // Path untuk menyimpan file PDF
//                    String pdfFilePath = pdfFolder.getAbsolutePath() + "/" + barang.getNamaBarang() + ".pdf";
//                    File pdfFile = new File(pdfFilePath);
//
//                    // Membuat dokumen PDF
//                    Document document = new Document();
//                    PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
//                    document.open();
//
//                    // Menambahkan gambar dari drawable
//                    Drawable drawable = getResources().getDrawable(R.drawable.logo); // Ganti 'your_drawable' dengan id drawable Anda
//                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                    Image imageFromDrawable = Image.getInstance(stream.toByteArray());
//                    imageFromDrawable.setAbsolutePosition(-1, 680); // Ubah posisi gambar sesuai kebutuhan
//                    document.add(imageFromDrawable);
//
//                    // Menambahkan teks biasa
//                    Paragraph textParagraph = new Paragraph("PURCHASE ORDER");
//                    textParagraph.setAlignment(Element.ALIGN_CENTER);
//                    textParagraph.setSpacingBefore(50f); // Menambahkan padding atas
////                    textParagraph.setSpacingAfter(70f);  // Menambahkan padding bawah
//                    document.add(textParagraph);
//
//
//                    Paragraph textParagraph2 = new Paragraph("PT. TOYOTA TSUSHO LOGISTIC \n" +
//                            "JI. Irian V Blok KK-8, Kawasan Industri MM2100, Cikarang Barat, bekasi 17530\n" +
//                            "Phone No. : (021) 8998-1701 (Hunting)\n" +
//                            "Fax No. : (021) 8998-1681");
//                    textParagraph2.setAlignment(Element.ALIGN_CENTER);
//                    textParagraph2.setSpacingBefore(10f); // Menambahkan padding atas
////                    textParagraph2.setSpacingAfter(80f);  // Menambahkan padding bawah
//                    document.add(textParagraph2);
////                    // Menambahkan informasi barang ke dokumen PDF
////                    document.add(new Paragraph("Nama Barang: " + barang.getNamaBarang()));
////                    document.add(new Paragraph("Jumlah Barang: " + barang.getJumlahBarang()));
////                    document.add(new Paragraph("Kode Barang: " + barang.getKodeBarang()));
////                    document.add(new Paragraph("Harga Barang: " + barang.getHarga()));
////                    document.add(new Paragraph("Nama Suplier: " + barang.getSuplier()));
//
//                    Paragraph textParagraph4 = new Paragraph("To : " + barang.getSuplier());
//                    textParagraph4.setAlignment(Element.ALIGN_LEFT);
//                    textParagraph4.setSpacingBefore(20f); // Menambahkan padding atas
//                    textParagraph4.setSpacingAfter(20f);  // Menambahkan padding bawah
//                    document.add(textParagraph4);
//
//                    // Menambahkan informasi barang ke dokumen PDF dengan teks yang terpusat
////                    Paragraph namaBarang = new Paragraph("Nama Barang: " + barang.getNamaBarang());
//                    Paragraph textParagraph5 = new Paragraph("Nama Barang: " + barang.getNamaBarang());
//                    textParagraph5.setAlignment(Element.ALIGN_LEFT);
////                    textParagraph5.setSpacingBefore(20f); // Menambahkan padding atas
////                    textParagraph5.setSpacingAfter(20f);  // Menambahkan padding bawah
//                    document.add(textParagraph5);
////                    namaBarang.setAlignment(Element.ALIGN_LEFT);
////                    document.add(namaBarang);
//
//                    Paragraph jumlahBarang = new Paragraph("Jumlah Barang: " + barang.getJumlahBarang());
//                    jumlahBarang.setAlignment(Element.ALIGN_LEFT);
//                    document.add(jumlahBarang);
//
//                    Paragraph kodeBarang = new Paragraph("Status: " + barang.getStatus());
//                    kodeBarang.setAlignment(Element.ALIGN_LEFT);
//                    document.add(kodeBarang);
//
//                    Paragraph hargaBarang = new Paragraph("Harga Barang: " + barang.getHarga());
//                    hargaBarang.setAlignment(Element.ALIGN_LEFT);
//                    document.add(hargaBarang);
//
////                    Paragraph namaSuplier = new Paragraph("To : " + barang.getSuplier());
////                    namaSuplier.setAlignment(Element.ALIGN_LEFT);
////                    document.add(namaSuplier);
//
////                    Paragraph supervisor = new Paragraph("Supervisor: " + barang.getSupervisor());
////                    supervisor.setAlignment(Element.ALIGN_LEFT);
////                    document.add(supervisor);
//
//                    // Menambahkan gambar pertama ke PDF jika tersedia
//                    if (barang.getImageUrl() != null && !barang.getImageUrl().isEmpty()) {
//                        try {
//                            Image image = Image.getInstance(new URL(barang.getImageUrl()));
//                            // Mendapatkan ukuran asli gambar
//                            float originalWidth = image.getWidth();
//                            float originalHeight = image.getHeight();
//
//                            // Menentukan ukuran maksimum yang diizinkan (misalnya 300x300 pixels)
//                            float maxWidth = 100f;
//                            float maxHeight = 100f;
//
//                            // Menghitung rasio skala untuk menyesuaikan ukuran
//                            float widthScale = maxWidth / originalWidth;
//                            float heightScale = maxHeight / originalHeight;
//                            float scale = Math.min(widthScale, heightScale);
//
//                            // Mengatur ukuran gambar sesuai dengan skala
//                            image.scaleToFit(image.getWidth() * scale, image.getHeight() * scale);
//
//                            // Menentukan ukuran gambar
//                            image.scaleToFit(100, 100);
//                            // Menempatkan gambar di posisi paling bawah halaman
//                            image.setAbsolutePosition(20, 200); // Ubah 100, 50 sesuai dengan posisi yang diinginkan
//
//
//                            document.add(image);
//                        } catch (MalformedURLException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    Paragraph textParagraph3 = new Paragraph("Note : \n" +
//                            "•\u2060  \u2060Payment 30 days after receiving invoice\n" +
//                            "•\u2060  \u2060Order Requirement should be delivered to PT Toyota Tsusho Logistic Center, Blok KK-8 MM2100 Cibitung Bekasi\n" +
//                            "•\u2060  \u2060Delivery schedule should be arranged by PT. Toyota Tsusho Logistic Center");
//                    textParagraph3.setAlignment(Element.ALIGN_LEFT);
//                    textParagraph3.setSpacingBefore(50f); // Menambahkan padding atas
//                    textParagraph3.setSpacingAfter(50f);  // Menambahkan padding bawah
//                    document.add(textParagraph3);
//
//                    // Menambahkan teks biasa
//                    textParagraph = new Paragraph("Madinah");
//                    textParagraph.setAlignment(Element.ALIGN_LEFT);
//                    textParagraph.setSpacingBefore(140f); // Menambahkan padding atas
//                    textParagraph.setSpacingAfter(170f);  // Menambahkan padding bawah
//                    document.add(textParagraph);
//
//
//                    // Menutup dokumen PDF
//                    document.close();
//
//                    // Menampilkan pesan PDF berhasil dibuat di UI Thread
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(SupervisorActivity.this, "PDF created: " + pdfFilePath, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                } catch (IOException | DocumentException e) {
//                    e.printStackTrace();
//                    // Menampilkan pesan kesalahan di UI Thread
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(SupervisorActivity.this, "Failed to create PDF", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//        });
//    }






    // Metode untuk membuat PDF dari item barang yang dipilih dan menyimpannya
    private void createAndSavePdf() {
        // Memilih item yang dipilih
        final Barang barang = listBarang.get(selectedPosition);

        // Gunakan AsyncTask untuk menghindari NetworkOnMainThreadException
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // Membuat direktori untuk menyimpan PDF di External Storage
                    File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "MyPDFs");
                    if (!pdfFolder.exists()) {
                        pdfFolder.mkdirs();
                    }

                    // Path untuk menyimpan file PDF
                    String pdfFilePath = pdfFolder.getAbsolutePath() + "/" + barang.getNamaBarang() + ".pdf";
                    File pdfFile = new File(pdfFilePath);

                    // Membuat dokumen PDF
                    Document document = new Document();
                    PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
                    document.open();

                    // Menambahkan gambar dari drawable
                    Drawable drawable = getResources().getDrawable(R.drawable.logod); // Ganti 'your_drawable' dengan id drawable Anda
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Image imageFromDrawable = Image.getInstance(stream.toByteArray());
                    imageFromDrawable.setAbsolutePosition(380, 730); // Ubah posisi gambar sesuai kebutuhan
                    document.add(imageFromDrawable);

                    // Menambahkan teks biasa
                    Paragraph textParagraph = new Paragraph("PT. TOYOTA TSUSHO LOGISTIC CENTER \n" +
                            "JI. Irian V Blok KK-8, Kawasan Industri MM2100 \n" +
                            "Cikarang Barat, Bekasi 17530\n" +
                            "Phone No. : (021) 8998-1701 (Hunting)\n" +
                            "Fax No. : (021) 8998-1681");
                    textParagraph.setAlignment(Element.ALIGN_LEFT);
                    textParagraph.setSpacingBefore(0f); // Menambahkan padding atas
//                    textParagraph.setSpacingAfter(0f);  // Menambahkan padding bawah
                    document.add(textParagraph);


                    Paragraph textParagraph2 = new Paragraph("PURCHASE ORDER");
                    textParagraph2.setAlignment(Element.ALIGN_CENTER);
                    textParagraph2.setSpacingBefore(10f); // Menambahkan padding atas
//                    textParagraph2.setSpacingAfter(80f);  // Menambahkan padding bawah
                    document.add(textParagraph2);
//                    // Menambahkan informasi barang ke dokumen PDF
//                    document.add(new Paragraph("Nama Barang: " + barang.getNamaBarang()));
//                    document.add(new Paragraph("Jumlah Barang: " + barang.getJumlahBarang()));
//                    document.add(new Paragraph("Kode Barang: " + barang.getKodeBarang()));
//                    document.add(new Paragraph("Harga Barang: " + barang.getHarga()));
//                    document.add(new Paragraph("Nama Suplier: " + barang.getSuplier()));

                    Paragraph textParagraph4 = new Paragraph("To : " + barang.getSuplier());
                    textParagraph4.setAlignment(Element.ALIGN_LEFT);
                    textParagraph4.setSpacingBefore(20f); // Menambahkan padding atas
                    textParagraph4.setSpacingAfter(20f);  // Menambahkan padding bawah
                    document.add(textParagraph4);

                    // Menambahkan informasi barang ke dokumen PDF dengan teks yang terpusat
//                    Paragraph namaBarang = new Paragraph("Nama Barang: " + barang.getNamaBarang());
                    Paragraph textParagraph5 = new Paragraph("Nama Barang: " + barang.getNamaBarang());
                    textParagraph5.setAlignment(Element.ALIGN_LEFT);
//                    textParagraph5.setSpacingBefore(20f); // Menambahkan padding atas
//                    textParagraph5.setSpacingAfter(20f);  // Menambahkan padding bawah
                    document.add(textParagraph5);
//                    namaBarang.setAlignment(Element.ALIGN_LEFT);
//                    document.add(namaBarang);

                    Paragraph jumlahBarang = new Paragraph("Jumlah Barang: " + barang.getJumlahBarang());
                    jumlahBarang.setAlignment(Element.ALIGN_LEFT);
                    document.add(jumlahBarang);

                    Paragraph kodeBarang = new Paragraph("Status: " + barang.getStatus());
                    kodeBarang.setAlignment(Element.ALIGN_LEFT);
                    document.add(kodeBarang);

                    Paragraph hargaBarang = new Paragraph("Harga Barang: " + barang.getHarga());
                    hargaBarang.setAlignment(Element.ALIGN_LEFT);
                    document.add(hargaBarang);

                    Paragraph ukuranBarang = new Paragraph("Size Barang: " + barang.getUkuranBarang());
                    ukuranBarang.setAlignment(Element.ALIGN_LEFT);
                    document.add(ukuranBarang);

//                    Paragraph namaSuplier = new Paragraph("To : " + barang.getSuplier());
//                    namaSuplier.setAlignment(Element.ALIGN_LEFT);
//                    document.add(namaSuplier);

//                    Paragraph supervisor = new Paragraph("Supervisor: " + barang.getSupervisor());
//                    supervisor.setAlignment(Element.ALIGN_LEFT);
//                    document.add(supervisor);

                    // Menambahkan gambar pertama ke PDF jika tersedia
                    if (barang.getImageUrl() != null && !barang.getImageUrl().isEmpty()) {
                        try {
                            Image image = Image.getInstance(new URL(barang.getImageUrl()));
                            // Mendapatkan ukuran asli gambar
                            float originalWidth = image.getWidth();
                            float originalHeight = image.getHeight();

                            // Menentukan ukuran maksimum yang diizinkan (misalnya 300x300 pixels)
                            float maxWidth = 100f;
                            float maxHeight = 100f;

                            // Menghitung rasio skala untuk menyesuaikan ukuran
                            float widthScale = maxWidth / originalWidth;
                            float heightScale = maxHeight / originalHeight;
                            float scale = Math.min(widthScale, heightScale);

                            // Mengatur ukuran gambar sesuai dengan skala
                            image.scaleToFit(image.getWidth() * scale, image.getHeight() * scale);

                            // Menentukan ukuran gambar
                            image.scaleToFit(100, 100);
                            // Menempatkan gambar di posisi paling bawah halaman
                            image.setAbsolutePosition(20, 200); // Ubah 100, 50 sesuai dengan posisi yang diinginkan


                            document.add(image);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }

                    Paragraph textParagraph3 = new Paragraph("Note : \n" +
                            "•\u2060  \u2060Payment 30 days after receiving invoice\n" +
                            "•\u2060  \u2060Order Requirement should be delivered to PT Toyota Tsusho Logistic Center, Blok KK-8 MM2100 Cibitung Bekasi\n" +
                            "•\u2060  \u2060Delivery schedule should be arranged by PT. Toyota Tsusho Logistic Center");
                    textParagraph3.setAlignment(Element.ALIGN_LEFT);
                    textParagraph3.setSpacingBefore(50f); // Menambahkan padding atas
                    textParagraph3.setSpacingAfter(50f);  // Menambahkan padding bawah
                    document.add(textParagraph3);

                    // Menambahkan teks biasa
                    textParagraph = new Paragraph("Proposed By");
                    textParagraph.setAlignment(Element.ALIGN_LEFT);
                    textParagraph.setSpacingBefore(50f); // Menambahkan padding atas
                    textParagraph.setSpacingAfter(50f);  // Menambahkan padding bawah
                    document.add(textParagraph);

                    // Menambahkan teks biasa
                    textParagraph = new Paragraph("Checked By");
                    textParagraph.setAlignment(Element.ALIGN_CENTER);
                    textParagraph.setIndentationRight(-65f); // Mengatur margin kanan untuk menggeser teks ke kanan
                    textParagraph.setSpacingBefore(-65f); // Menambahkan padding atas
                    textParagraph.setSpacingAfter(-65f);  // Menambahkan padding bawah
                    document.add(textParagraph);


                    // Membuat tabel dengan 2 kolom
                    PdfPTable table = new PdfPTable(2);
                    table.setWidthPercentage(100); // Mengatur lebar tabel 100% dari halaman

                    Drawable drawable2 = getResources().getDrawable(R.drawable.logod); // Ganti 'your_drawable' dengan id drawable Anda
                    Bitmap bitmap2 = ((BitmapDrawable) drawable2).getBitmap();
                    ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                    bitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream2);
                    Image imageFromDrawable2 = Image.getInstance(stream2.toByteArray());
                    imageFromDrawable2.setIndentationRight(-135f); // Mengatur margin kanan untuk menggeser teks ke kanan
                    imageFromDrawable2.setAbsolutePosition(0, 140); // Ubah posisi gambar sesuai kebutuhan
                    document.add(imageFromDrawable2);

// Membuat sel untuk teks kiri (Applicant dan Andri Wijaya)
                    PdfPCell leftCell = new PdfPCell();
                    leftCell.addElement(new Paragraph("Applicant"));
                    leftCell.addElement(new Paragraph("Andri Wijaya"));
                    leftCell.setBorder(Rectangle.NO_BORDER); // Menghilangkan border


                    Drawable drawable3 = getResources().getDrawable(R.drawable.logod); // Ganti 'your_drawable' dengan id drawable Anda
                    Bitmap bitmap3 = ((BitmapDrawable) drawable3).getBitmap();
                    ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
                    bitmap3.compress(Bitmap.CompressFormat.PNG, 100, stream3);
                    Image imageFromDrawable3 = Image.getInstance(stream3.toByteArray());
                    imageFromDrawable3.setIndentationRight(-65f); // Mengatur margin kanan untuk menggeser teks ke kanan
                    imageFromDrawable3.setAbsolutePosition(175, 145); // Ubah posisi gambar sesuai kebutuhan
                    document.add(imageFromDrawable3);

// Membuat sel untuk teks kanan (Supervisor dan Madinah)
                    PdfPCell rightCell = new PdfPCell();
                    rightCell.addElement(new Paragraph("Supervisor"));
                    rightCell.addElement(new Paragraph("Madinah"));
                    rightCell.setBorder(Rectangle.NO_BORDER); // Menghilangkan border
                    rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT); // Mengatur teks ke kanan


// Menambahkan sel ke tabel
                    table.addCell(leftCell);
                    table.addCell(rightCell);

// Menambahkan padding atas dan bawah untuk tabel
                    table.setSpacingBefore(220f);
                    table.setSpacingAfter(220f);

// Menambahkan tabel ke dokumen
                    document.add(table);



                    // Menutup dokumen PDF
                    document.close();

                    // Menampilkan pesan PDF berhasil dibuat di UI Thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SupervisorActivity.this, "PDF created: " + pdfFilePath, Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (IOException | DocumentException e) {
                    e.printStackTrace();
                    // Menampilkan pesan kesalahan di UI Thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SupervisorActivity.this, "Failed to create PDF", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            imageUri = data.getData();
//            Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show();
//
//            // Update the local image URI of the selected Barang object
//            if (selectedPosition != -1) {
//                Barang barang = listBarang.get(selectedPosition);
//
//                // Update the appropriate local image URI field
//                if (selectedImageView == 0) {
//                    barang.setLocalImageUri(imageUri.toString());
//                } else if (selectedImageView == 1) {
//                    barang.setLocalImageUri2(imageUri.toString());
//                }
//
//                // Notify the adapter to refresh the view
//                barangAdapter.notifyDataSetChanged();
//
////                // Load the image into ImageView using Glide if the view is not null
////                View view = listViewBarang.getChildAt(selectedPosition);
////                if (view != null) {
////                    ImageView imageViewToUpdate;
////                    if (selectedImageView == 0) {
////                        imageViewToUpdate = view.findViewById(R.id.imageViewBarang);
////                    } else if (selectedImageView == 1) {
////                        imageViewToUpdate = view.findViewById(R.id.imageViewBarang2);
////                    } else {
////                        return; // Handle unexpected selectedImageView value
////                    }
////                    if (imageViewToUpdate != null) {
////                        Glide.with(this).load(imageUri).into(imageViewToUpdate);
////                    }
////                }
//            }
//        }
//    }





//    private void uploadImage() {
//        if (imageUri != null) {
//            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");
//
//            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            String imageUrl = uri.toString();
//                            Barang barang = listBarang.get(selectedPosition);
//                            barang.setImageUrl(imageUrl); // menyimpan URL gambar pertama
////                            barang.setDisetujui(true); // menandai bahwa barang sudah disetujui
//                            databaseBarang.child(barang.getId()).setValue(barang)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            Toast.makeText(SupervisorActivity.this, "Data updated", Toast.LENGTH_SHORT).show();
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            Toast.makeText(SupervisorActivity.this, "Failed to update data", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                        }
//                    });
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(SupervisorActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void uploadImage2() {
//        if (imageUri != null) {
//            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");
//
//            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            String imageUrl = uri.toString();
//                            Barang barang = listBarang.get(selectedPosition);
//                            barang.setImageUrl2(imageUrl); // menyimpan URL gambar kedua
////                            barang.setDisetujui(true); // menandai bahwa barang sudah disetujui
//                            databaseBarang.child(barang.getId()).setValue(barang)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            Toast.makeText(SupervisorActivity.this, "Data updated", Toast.LENGTH_SHORT).show();
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            Toast.makeText(SupervisorActivity.this, "Failed to update data", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                        }
//                    });
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(SupervisorActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void loadData() {
        databaseBarang.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listBarang.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Barang barang = postSnapshot.getValue(Barang.class);
                    if (barang != null) {
                        listBarang.add(barang);
                    }
                }
                barangAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SupervisorActivity.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private class BarangAdapter extends BaseAdapter {

        private List<Barang> listBarang;

        public BarangAdapter(List<Barang> listBarang) {
            this.listBarang = listBarang;
        }

        @Override
        public int getCount() {
            return listBarang.size();
        }

        @Override
        public Object getItem(int position) {
            return listBarang.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barang_supervisor, parent, false);
                holder = new ViewHolder();
                holder.textViewNama = convertView.findViewById(R.id.textViewNama);
                holder.textViewJumlah = convertView.findViewById(R.id.textViewJumlah);
                holder.textViewKode = convertView.findViewById(R.id.textViewKode);
                holder.textViewUkuran = convertView.findViewById(R.id.textViewUkuran);
                holder.textViewHarga = convertView.findViewById(R.id.textViewHarga);
                holder.textViewSuplier = convertView.findViewById(R.id.textViewSuplier);
//                holder.imageViewBarang = convertView.findViewById(R.id.imageViewBarang);
//                holder.imageViewBarang2 = convertView.findViewById(R.id.imageViewBarang2);
//                holder.namaSupervisor = convertView.findViewById(R.id.namaSupervisor);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Barang barang = listBarang.get(position);

            holder.textViewNama.setText(barang.getNamaBarang());
            holder.textViewJumlah.setText(barang.getJumlahBarang());
            holder.textViewUkuran.setText(barang.getUkuranBarang());
            holder.textViewKode.setText(barang.getStatus());
            holder.textViewHarga.setText(barang.getHarga());
            holder.textViewSuplier.setText(barang.getSuplier());
//            holder.namaSupervisor.setText(barang.getSupervisor());

//            // Load images into ImageViews using Glide
//            if (barang.getImageUrl() != null && !barang.getImageUrl().isEmpty()) {
//                Glide.with(SupervisorActivity.this)
//                        .load(barang.getImageUrl())
//                        .into(holder.imageViewBarang);
//            } else {
//                holder.imageViewBarang.setImageResource(R.drawable.ic_placeholder); // set a placeholder image
//            }
//
//            if (barang.getImageUrl2() != null && !barang.getImageUrl2().isEmpty()) {
//                Glide.with(SupervisorActivity.this)
//                        .load(barang.getImageUrl2())
//                        .into(holder.imageViewBarang2);
//            } else {
//                holder.imageViewBarang2.setImageResource(R.drawable.ic_placeholder); // set a placeholder image
//            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = position;
                }
            });

            return convertView;
        }

        private class ViewHolder {
            TextView textViewNama;
            TextView textViewJumlah;
            TextView textViewUkuran;
            TextView textViewKode;
            TextView textViewHarga;
            TextView textViewSuplier;
            ImageView imageViewBarang;
            ImageView imageViewBarang2;
            TextView namaSupervisor;
        }
    }

}
