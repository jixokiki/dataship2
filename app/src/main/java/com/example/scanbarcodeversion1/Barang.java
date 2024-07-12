package com.example.scanbarcodeversion1;

import com.google.firebase.database.ServerValue;
public class Barang {
    private String id;
    private String namaBarang;
    private String jumlahBarang;
    private String ukuranBarang;
    private String alamatSuplier;
    private String status;
    private Object serverTimeStamp;
    private String supplier;
    private String quantity;
    private String harga;

    private String imageUrl; // URL gambar pertama
    private String imageUrl2; // URL gambar kedua
    private String localImageUri; // Local URI for immediate image update
    private String localImageUri2; // Local URI for immediate image update

//    public Barang(String id, String namaBarang, String ukuranBarang, String jumlahBarang, String diminta) {
//        // Default constructor required for calls to DataSnapshot.getValue(Barang.class)
//    }

    public Barang(){}

    public Barang(String id, String namaBarang,  String ukuranBarang,String jumlahBarang, String status) {
        this.id = id;
        this.namaBarang = namaBarang;
        this.jumlahBarang = jumlahBarang;
        this.ukuranBarang = ukuranBarang;
//        this.alamatSuplier =  alamatSuplier;
        this.status = status;
        this.serverTimeStamp = ServerValue.TIMESTAMP;
        this.imageUrl = "";
        this.imageUrl2 = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getAlamatSuplier() {
        return alamatSuplier;
    }

    public void setAlamatSuplier(String alamatSuplier) {
        this.alamatSuplier = alamatSuplier;
    }

    public String getJumlahBarang() {
        return jumlahBarang;
    }

    public void setJumlahBarang(String jumlahBarang) {
        this.jumlahBarang = jumlahBarang;
    }

    public String getUkuranBarang() {
        return ukuranBarang;
    }

    public void setUkuranBarang(String ukuranBarang) {
        this.ukuranBarang = ukuranBarang;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getServerTimeStamp() {
        return serverTimeStamp;
    }

    public void setServerTimeStamp(Object serverTimeStamp) {
        this.serverTimeStamp = serverTimeStamp;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setSuplier(String suplier) {
        this.supplier = supplier;
    }
    public String getSuplier() {
        return supplier;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl2() {
        return imageUrl2;
    }

    public void setImageUrl2(String imageUrl2) {
        this.imageUrl2 = imageUrl2;
    }

    public String getLocalImageUri() {
        return localImageUri;
    }

    public void setLocalImageUri(String localImageUri) {
        this.localImageUri = localImageUri;
    }

    public String getLocalImageUri2() {
        return localImageUri2;
    }

    public void setLocalImageUri2(String localImageUri2) {
        this.localImageUri2 = localImageUri2;
    }
}

