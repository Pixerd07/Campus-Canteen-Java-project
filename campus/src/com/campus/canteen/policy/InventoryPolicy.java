package com.campus.canteen.policy;

public class InventoryPolicy {

    public boolean isValidQuantity(int quantity) {
        return quantity >= 1;
    }

    public boolean hasEnoughStock(int availableStock, int requestedQuantity) {
        return requestedQuantity <= availableStock;
    }
}