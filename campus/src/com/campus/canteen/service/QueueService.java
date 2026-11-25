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

        if (orders == null) {
            return activeOrders;
        }

        for (Order order : orders) {
            if (order != null && queuePolicy.isActive(order.getStatus())) {
                activeOrders.add(order);
            }
        }

        return activeOrders;
    }

    // calculate orders ahead of a particular order
    public int getOrdersAhead(List<Order> orders, long orderId) {

        if (orders == null) {
            return -1;
        }

        int count = 0;

        for (Order order : orders) {
            if (order == null) {
                continue;
            }

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

    // calculate orders ahead of a particular order using token number
    public int getOrdersAheadByToken(List<Order> orders, int tokenNumber) {

        if (orders == null || tokenNumber <= 0) {
            return -1;
        }

        int targetIndex = -1;
        for (int i = orders.size() - 1; i >= 0; i--) {
            Order order = orders.get(i);
            if (order != null && order.getTokenNumber() == tokenNumber) {
                targetIndex = i;
                break;
            }
        }

        if (targetIndex == -1 || !queuePolicy.isActive(orders.get(targetIndex).getStatus())) {
            return -1;
        }

        int count = 0;
        for (int i = 0; i < targetIndex; i++) {
            Order order = orders.get(i);
            if (order != null && queuePolicy.isActive(order.getStatus())) {
                count++;
            }
        }

        return count;
    }

    // get total active orders
    public int getQueueSize(List<Order> orders) {
        return getActiveOrders(orders).size();
    }
}
