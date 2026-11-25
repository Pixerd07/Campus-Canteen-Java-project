
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

    // Set initial stock
    public void setInitialStock(long itemId, int quantity) {

        if (itemId > 0 && quantity >= 0) {
            stock.put(itemId, quantity);
        }
    }

    // Get available stock (for Admin use)
    public int getStock(long itemId) {
        return stock.getOrDefault(itemId, 0);
    }

    // Check stock availability
    public boolean hasEnoughStock(long itemId, int quantity) {

        if (!inventoryPolicy.isValidQuantity(quantity)) {
            return false;
        }

        return inventoryPolicy.hasEnoughStock(
                getStock(itemId), quantity
        );
    }

    // Validate stock and throw exception if unavailable
    public void validateStock(long itemId, int requestedQuantity) {

        if (!inventoryPolicy.isValidQuantity(requestedQuantity)) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero."
            );
        }

        int availableStock = getStock(itemId);

        if (availableStock == 0) {
            throw new StockUnavailableException(
                    "OUT OF STOCK!"
            );
        }

        if (requestedQuantity > availableStock) {
            throw new StockUnavailableException(
                    "Only " + availableStock
                            + " left in stock. Please reduce your quantity."
            );
        }
    }

    // Deduct stock after order
    public boolean deductStock(long itemId, int quantity) {

        if (!hasEnoughStock(itemId, quantity)) {
            return false;
        }

        stock.put(itemId, getStock(itemId) - quantity);

        return true;
    }

    // Restore stock after cancellation
    public void restoreStock(long itemId, int quantity) {

        if (quantity > 0 && stock.containsKey(itemId)) {
            int currentStock = getStock(itemId);
            if (quantity > Integer.MAX_VALUE - currentStock) {
                stock.put(itemId, Integer.MAX_VALUE);
            } else {
                stock.put(itemId, currentStock + quantity);
            }
        }
    }

    // Update stock manually
    public boolean updateStock(long itemId, int newStock) {

        if (newStock < 0 || !stock.containsKey(itemId)) {
            return false;
        }

        stock.put(itemId, newStock);

        return true;
    }
}
