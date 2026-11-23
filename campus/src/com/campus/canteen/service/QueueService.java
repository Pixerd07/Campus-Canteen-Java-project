package com.campus.canteen.service;

import com.campus.canteen.model.Order;
import com.campus.canteen.policy.QueuePolicy;

import java.util.ArrayList;
import java.util.List;

public class QueueService {

    private QueuePolicy queuePolicy;

    public QueueService() {
        queuePolicy = new QueuePolicy();
    }

    // get all active orders
    public List<Order> getActiveOrders(List<Order> orders) {

        List<Order> activeOrders = new ArrayList<>();

        for (Order order : orders) {
            if (queuePolicy.isActive(order.getStatus())) {
                activeOrders.add(order);
            }
        }

        return activeOrders;
    }

    // calculate orders ahead of a particular order
    public int getOrdersAhead(List<Order> orders, long orderId) {

        int count = 0;

        for (Order order : orders) {

            if (order.getOrderId() == orderId) {
                if (!queuePolicy.isActive(order.getStatus())) {
                    return -1;
                }

                return count;
            }

            if (queuePolicy.isActive(order.getStatus())) {
                count++;
            }
        }

        return -1;
    }

    // get total active orders
    public int getQueueSize(List<Order> orders) {
        return getActiveOrders(orders).size();
    }
}
