package com.campus.canteen.model;
//this file will contain item ID and quantity
public class OrderItem {
    private long itemId;
    private int qty;
    private double price;


    public OrderItem(long itemId ,int qty , double price){
        this.itemId=itemId;
        this.qty=qty;
        this.price=price;
    }

    public long getItemId() { return itemId; }
    public int getQty() { return qty; }
    public double getPrice(){return price;}
    public void setQty(int qty) { this.qty = qty; }
}


// Only create setters when that field is supposed to be modified.