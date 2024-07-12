package com.example.scanbarcodeversion1;

public class Item2 {
    private String itemName;

    public Item2() {
        // Default constructor required for Firebase
    }

    public Item2(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
