package com.campus.canteen.service;

import com.campus.canteen.model.Order;
import com.campus.canteen.model.OrderItem;
import com.campus.canteen.model.OrderStatus;
import com.campus.canteen.policy.OrderPolicy;

import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private List<Order> orders;
    private OrderPolicy orderPolicy;
    private long nextOrderId;

    public OrderService() {
        orders = new ArrayList<>();
        orderPolicy = new OrderPolicy();
        nextOrderId = 1;
    }

    // place a new order
    public Order placeOrder(long studentId, List<OrderItem> items) {

        if (studentId <= 0 || items == null || items.isEmpty()) {
            return null;
        }

        for (OrderItem item : items) {
            if (item == null || item.getQty() <= 0) {
                return null;
            }
        }

        Order order = new Order(nextOrderId, studentId);

        for (OrderItem item : items) {
            order.addItem(item);
        }

        orders.add(order);
        nextOrderId++;

        return order;
    }

    // modify an existing order
    public boolean modifyOrder(long orderId, long studentId,
                               List<OrderItem> newItems) {

        Order order = findOrder(orderId);

        if (order == null || order.getStudentId() != studentId) {
            return false;
        }

        if (!orderPolicy.canModify(order.getStatus())) {
            return false;
        }

        if (newItems == null || newItems.isEmpty()) {
            return false;
        }

        for (OrderItem item : newItems) {
            if (item == null || item.getQty() <= 0) {
                return false;
            }
        }

        order.getItems().clear();

        for (OrderItem item : newItems) {
            order.addItem(item);
        }

        return true;
    }

    // cancel an order
    public boolean cancelOrder(long orderId, long studentId) {

        Order order = findOrder(orderId);

        if (order == null || order.getStudentId() != studentId) {
            return false;
        }

        if (!orderPolicy.canCancel(order.getStatus())) {
            return false;
        }

        order.setStatus(OrderStatus.CANCELLED);

        return true;
    }

    // find order using order ID
    public Order findOrder(long orderId) {

        for (Order order : orders) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }

        return null;
    }

    // find the latest order using token number
    public Order findOrderByToken(int tokenNumber) {

        if (tokenNumber <= 0) {
            return null;
        }

        for (int i = orders.size() - 1; i >= 0; i--) {
            Order order = orders.get(i);
            if (order.getTokenNumber() == tokenNumber) {
                return order;
            }
        }

        return null;
    }

    // view orders of a student
    public List<Order> getStudentOrders(long studentId) {

        List<Order> studentOrders = new ArrayList<>();

        for (Order order : orders) {
            if (order.getStudentId() == studentId) {
                studentOrders.add(order);
            }
        }

        return studentOrders;
    }

    // return all orders
    public List<Order> getAllOrders() {
        return orders;
    }

}
