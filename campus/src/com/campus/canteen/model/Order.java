package com.campus.canteen.model;

import java.util.*;
// this file wiil contain Complete order information

public class Order {
    private long orderId;
    private long studentId;
    private String status;

    private List<OrderItem> items;

    public Order(long orderId ,long studentId){
        this.orderId=orderId;
        this.studentId=studentId;
        this.status=OrderStatus.PLACED;
        this.items = new ArrayList<>();
    }

    public long getOrderId() {
        return orderId;
    }
    public long getStudentId() {
        return studentId;
    }
    public String getStatus() {
        return status;
    }
    public List<OrderItem> getItems() {
        return items;
    }

    public void addItem(OrderItem item){
        items.add(item);
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
    }

    public int getTotalQuantity() {
        int total = 0;

        for (OrderItem item : items) {
            total += item.getQty();
        }

        return total;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPrice() {
        double total = 0;

        for (OrderItem item : items) {
            total += item.getPrice() * item.getQty();
        }

        return total;
    }
}
