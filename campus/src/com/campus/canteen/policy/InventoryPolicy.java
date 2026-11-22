package com.campus.canteen.policy;
public class InventoryPolicy {

    public boolean isValidQuantity(int quantity) {
        return quantity > 0;
    }

    public boolean hasEnoughStock(int availableStock, int requestedQuantity) {
        return availableStock >= requestedQuantity;
    }
}
