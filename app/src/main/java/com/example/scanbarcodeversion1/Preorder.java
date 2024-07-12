package com.example.scanbarcodeversion1;

public class Preorder {
    private String itemName;
    private int itemCount;
    private String itemPrice;
    private int requestedQuantity;

    public Preorder() {
    }

    public Preorder(String itemName, int itemCount, String itemPrice, int requestedQuantity) {
        this.itemName = itemName;
        this.itemCount = itemCount;
        this.itemPrice = itemPrice;
        this.requestedQuantity = requestedQuantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getRequestedQuantity() {
        return requestedQuantity;
    }

    public void setRequestedQuantity(int requestedQuantity) {
        this.requestedQuantity = requestedQuantity;
    }

//    public String getId() {
//    }
}