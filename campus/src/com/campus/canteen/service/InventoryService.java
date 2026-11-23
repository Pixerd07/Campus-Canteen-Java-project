package com.campus.canteen.service;

import com.campus.canteen.policy.InventoryPolicy;

import java.util.HashMap;
import java.util.Map;

public class InventoryService {

    private Map<Long, Integer> stock;
    private InventoryPolicy inventoryPolicy;

    public InventoryService() {
        stock = new HashMap<>();
        inventoryPolicy = new InventoryPolicy();
    }

    // set initial stock
    public void setInitialStock(long itemId, int quantity) {

        if (quantity >= 0) {
            stock.put(itemId, quantity);
        }
    }

    // get available stock
    public int getStock(long itemId) {
        return stock.getOrDefault(itemId, 0);
    }

    // check stock availability
    public boolean hasEnoughStock(long itemId, int quantity) {

        if (!inventoryPolicy.isValidQuantity(quantity)) {
            return false;
        }

        return inventoryPolicy.hasEnoughStock(
                getStock(itemId), quantity);
    }

    // deduct stock after order
    public boolean deductStock(long itemId, int quantity) {

        if (!hasEnoughStock(itemId, quantity)) {
            return false;
        }

        stock.put(itemId, getStock(itemId) - quantity);

        return true;
    }

    // restore stock after cancellation
    public void restoreStock(long itemId, int quantity) {

        if (quantity > 0 && stock.containsKey(itemId)) {
            stock.put(itemId, getStock(itemId) + quantity);
        }
    }

    // update stock manually
    public boolean updateStock(long itemId, int newStock) {

        if (newStock < 0 || !stock.containsKey(itemId)) {
            return false;
        }

        stock.put(itemId, newStock);

        return true;
    }
}