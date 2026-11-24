
package com.campus.canteen.ui;

import com.campus.canteen.model.*;
import com.campus.canteen.service.*;

import java.util.ArrayList;
import java.util.List;

public class StudentUI {

    private MenuService menuService;
    private OrderService orderService;
    private QueueService queueService;
    private InventoryService inventoryService;

    public StudentUI(MenuService menuService,
                     OrderService orderService,
                     QueueService queueService,
                     InventoryService inventoryService) {

        this.menuService = menuService;
        this.orderService = orderService;
        this.queueService = queueService;
        this.inventoryService = inventoryService;
    }

    public void showMenu() {

        long studentId = InputHelper.readLong("Enter Student ID: ");
        while (studentId <= 0) {
            System.out.println("Student ID must be a positive number.");
            studentId = InputHelper.readLong("Enter Student ID: ");
        }

        int choice = -1;

        do {
            System.out.println("\n--- STUDENT MENU ---");
            System.out.println("1. Browse Menu");
            System.out.println("2. Place Order");
            System.out.println("3. View Order History");
            System.out.println("4. Cancel Order");
            System.out.println("5. Track Queue");
            System.out.println("6. View Order Status");
            System.out.println("0. Back");

            choice = InputHelper.readInt("Enter choice: ");

            try {
                switch (choice) {
                    case 1:
                        browseMenu();
                        break;

                    case 2:
                        placeOrder(studentId);
                        break;

                    case 3:
                        viewHistory(studentId);
                        break;

                    case 4:
                        cancelOrder(studentId);
                        break;

                    case 5:
                        trackQueue(studentId);
                        break;

                    case 6:
                        viewStatus(studentId);
                        break;

                    case 0:
                        System.out.println("Returning to main menu.");
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (IllegalStateException e) {
                throw e;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (choice != 0);
    }

    private void browseMenu() {

        System.out.println("\n--- CANTEEN MENU ---");

        List<MenuItem> items = menuService.getAllItems();

        if (items.isEmpty()) {
            System.out.println("No menu items available.");
            return;
        }

        for (MenuItem item : items) {

            System.out.print(
                    item.getId() + ". " +
                            item.getName() + " - Rs." +
                            item.getPrice()
            );

            if (!item.isAvailable()
                    || inventoryService.getStock(item.getId()) == 0) {

                System.out.println(" - OUT OF STOCK");

            } else {
                System.out.println();
            }
        }
    }

    private void placeOrder(long studentId) {

        browseMenu();

        List<OrderItem> items = new ArrayList<>();

        while (true) {

            long itemId = InputHelper.readLong("Enter Item ID (0 to finish): ");

            if (itemId == 0) {
                break;
            }

            if (itemId < 0) {
                System.out.println("Item ID cannot be negative.");
                continue;
            }

            MenuItem item = menuService.findItem(itemId);

            if (item == null || !item.isAvailable()) {
                System.out.println("Invalid or unavailable item.");
                continue;
            }

            int qty = InputHelper.readInt("Enter quantity: ");

            if (qty <= 0) {
                System.out.println("Quantity must be greater than zero.");
                continue;
            }

            try {

                int existingQuantity = 0;
                OrderItem existingItem = null;

                // Check whether this item was already selected
                for (OrderItem selectedItem : items) {

                    if (selectedItem.getItemId() == itemId) {
                        existingQuantity = selectedItem.getQty();
                        existingItem = selectedItem;
                        break;
                    }
                }

                // Prevent integer overflow when combining quantities
                if (qty > Integer.MAX_VALUE - existingQuantity) {
                    System.out.println("Requested quantity is too large.");
                    continue;
                }

                int totalQuantity = existingQuantity + qty;

                // Validate combined quantity
                inventoryService.validateStock(
                        itemId, totalQuantity
                );

                if (existingItem != null) {
                    existingItem.setQty(totalQuantity);
                } else {
                    items.add(new OrderItem(
                            itemId, qty, item.getPrice()
                    ));
                }

                System.out.println("Item added to order.");

            } catch (StockUnavailableException | IllegalArgumentException e) {

                System.out.println(e.getMessage());
            }
        }

        if (items.isEmpty()) {
            System.out.println("No items selected.");
            return;
        }

        // Validate complete order before confirmation
        try {

            for (OrderItem item : items) {

                inventoryService.validateStock(
                        item.getItemId(),
                        item.getQty()
                );
            }

        } catch (StockUnavailableException | IllegalArgumentException e) {

            System.out.println(e.getMessage());
            System.out.println("Order cannot be placed.");
            return;
        }

        System.out.println("\n===== ORDER SUMMARY =====");

        for (OrderItem item : items) {
            MenuItem menuItem = menuService.findItem(item.getItemId());
            String itemName = (menuItem != null) ? menuItem.getName() : ("Item #" + item.getItemId());

            System.out.println(
                    itemName
                            + " x " + item.getQty()
                            + " = Rs. "
                            + (item.getPrice() * item.getQty())
            );
        }

        double total = 0;

        for (OrderItem item : items) {
            total += item.getPrice() * item.getQty();
        }

        System.out.println("Total Amount: Rs. " + total);

        int confirm = InputHelper.readInt("Confirm order? (1 Yes / 0 No): ");

        if (confirm != 1) {
            System.out.println("Order cancelled.");
            return;
        }

        // Deduct stock and keep track of successful deductions
        List<OrderItem> deductedItems = new ArrayList<>();

        try {
            for (OrderItem item : items) {

                boolean deducted = inventoryService.deductStock(
                        item.getItemId(),
                        item.getQty()
                );

                if (!deducted) {
                    restoreDeductedStock(deductedItems);
                    System.out.println(
                            "Stock changed. Order could not be placed."
                    );
                    return;
                }

                deductedItems.add(item);
            }

            Order order = orderService.placeOrder(studentId, items);

            if (order != null) {

                System.out.println("Order placed successfully!");
                System.out.println("Token Number: #" + order.getTokenNumber());
                System.out.println("Order ID: " + order.getOrderId());
                System.out.println("Total: Rs. " + order.getTotalPrice());

            } else {

                restoreDeductedStock(deductedItems);
                System.out.println("Unable to place order.");
            }
        } catch (Exception e) {
            // Roll back deducted stock if an unexpected exception occurs
            restoreDeductedStock(deductedItems);
            System.out.println("Failed to place order: " + e.getMessage());
        }
    }

    private void restoreDeductedStock(List<OrderItem> deductedItems) {
        for (OrderItem item : deductedItems) {
            inventoryService.restoreStock(
                    item.getItemId(),
                    item.getQty()
            );
        }
    }

    private void viewHistory(long studentId) {

        List<Order> orders = orderService.getStudentOrders(studentId);

        for (Order order : orders) {

            System.out.println(
                    "Token: #" + order.getTokenNumber() +
                            " | Order ID: " + order.getOrderId() +
                            " | Status: " + order.getStatus() +
                            " | Total: Rs." + order.getTotalPrice()
            );
        }

        if (orders.isEmpty()) {
            System.out.println("No orders found.");
        }
    }

    private void cancelOrder(long studentId) {

        long orderId = InputHelper.readLong("Enter Order ID: ");

        if (orderId <= 0) {
            System.out.println("Invalid Order ID.");
            return;
        }

        Order order = orderService.findOrder(orderId);

        if (order == null || order.getStudentId() != studentId) {
            System.out.println("Order not found.");
            return;
        }

        if (orderService.cancelOrder(orderId, studentId)) {

            for (OrderItem item : order.getItems()) {

                inventoryService.restoreStock(
                        item.getItemId(),
                        item.getQty()
                );
            }

            System.out.println("Order cancelled successfully.");

        } else {
            System.out.println("Unable to cancel order.");
        }
    }

    private void trackQueue(long studentId) {

        int tokenNumber = InputHelper.readInt("Enter Token Number: ");

        if (tokenNumber <= 0) {
            System.out.println("Invalid Token Number.");
            return;
        }

        Order order = orderService.findOrderByToken(tokenNumber);

        if (order == null || order.getStudentId() != studentId) {
            System.out.println("Order not found for this token.");
            return;
        }

        int ahead = queueService.getOrdersAheadByToken(
                orderService.getAllOrders(), tokenNumber
        );

        System.out.println("Token Number: #" + order.getTokenNumber());
        System.out.println("Status: " + order.getStatus());

        if (ahead >= 0) {
            System.out.println("Orders ahead: " + ahead);
        } else {
            System.out.println("Order is not in the active queue.");
        }
    }

    private void viewStatus(long studentId) {

        long orderId = InputHelper.readLong("Enter Order ID: ");

        if (orderId <= 0) {
            System.out.println("Invalid Order ID.");
            return;
        }

        Order order = orderService.findOrder(orderId);

        if (order != null && order.getStudentId() == studentId) {
            System.out.println("Order Status: " + order.getStatus());
        } else {
            System.out.println("Order not found.");
        }
    }
}
