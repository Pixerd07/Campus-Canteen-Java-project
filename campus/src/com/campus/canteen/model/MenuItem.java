package com.campus.canteen.model;
// this file will conatain menu Information
public class MenuItem {

    private long id;
    private String name;
    private double price;
    private boolean available;

    public MenuItem(long id , String name, double price, boolean available) {
        this.id=id;
        this.name=name;
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
        this.name=name;
    }
    public void setPrice(double price){
        this.price=price;
    }
    public void setAvailable(boolean available){
        this.available=available;
    }


}
