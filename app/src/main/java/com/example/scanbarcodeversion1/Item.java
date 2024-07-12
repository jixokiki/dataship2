package com.example.scanbarcodeversion1;

public class Item {
    private String itemName;

    public Item() {
        // Default constructor required for Firebase
    }

    public Item(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}