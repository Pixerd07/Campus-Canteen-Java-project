package com.campus.canteen.policy;
import com.campus.canteen.model.OrderStatus;

public class QueuePolicy {

    public boolean isActive(String status) {

        if (OrderStatus.PLACED.equals(status) ||
                OrderStatus.PREPARING.equals(status) ||
                OrderStatus.READY.equals(status)) {

            return true;
        }

        return false;
    }
}
