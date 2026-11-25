package com.campus.canteen.model;
//this file will contain item ID and quantity
public class OrderItem {
    private long itemId;
    private int qty;
    private double price;


    public OrderItem(long itemId ,int qty , double price){
        if (itemId <= 0) {
            throw new IllegalArgumentException("Item ID must be greater than zero.");
        }
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (!Double.isFinite(price) || price < 0) {
            throw new IllegalArgumentException("Price must be a valid non-negative number.");
        }
        this.itemId=itemId;
        this.qty=qty;
        this.price=price;
    }

    public long getItemId() { return itemId; }
    public int getQty() { return qty; }
    public double getPrice(){return price;}
    public void setQty(int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        this.qty = qty;
    }
}


// Only create setters when that field is supposed to be modified.