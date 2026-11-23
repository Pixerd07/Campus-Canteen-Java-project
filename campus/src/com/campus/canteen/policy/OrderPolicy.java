package com.campus.canteen.policy;

import com.campus.canteen.model.OrderStatus;

public class OrderPolicy {

    public boolean canModify(String status) {
        return OrderStatus.PLACED.equals(status);
    }

    public boolean canCancel(String status) {
        return canModify(status);
    }

    public boolean canTransition(String currentStatus, String newStatus) {
        String nextStatus = OrderStatus.next(currentStatus);

        return nextStatus != null && nextStatus.equals(newStatus);
    }
}