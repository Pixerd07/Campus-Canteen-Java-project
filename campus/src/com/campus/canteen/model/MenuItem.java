package com.campus.canteen.model;
// this file will conatain menu Information
public class MenuItem {

    private long id;
    private String name;
    private double price;
    private boolean available;

    public MenuItem(long id , String name, double price, boolean available) {
        if (id <= 0) {
            throw new IllegalArgumentException("Menu item ID must be greater than zero.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Menu item name cannot be empty.");
        }
        if (!Double.isFinite(price) || price <= 0) {
            throw new IllegalArgumentException("Menu item price must be greater than zero.");
        }
        this.id=id;
        this.name=name.trim();
        this.price=price;
        this.available=available;

    }

    public long getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public double getPrice(){
        return price;
    }
    public boolean isAvailable(){
        return available;
    }

    public void setName(String name){
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Menu item name cannot be empty.");
        }
        this.name=name.trim();
    }
    public void setPrice(double price){
        if (!Double.isFinite(price) || price <= 0) {
            throw new IllegalArgumentException("Menu item price must be greater than zero.");
        }
        this.price=price;
    }
    public void setAvailable(boolean available){
        this.available=available;
    }


}
