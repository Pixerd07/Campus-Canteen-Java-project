
package com.campus.canteen.ui;

import com.campus.canteen.model.*;
import com.campus.canteen.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentUI {

    private Scanner sc;
    private MenuService menuService;
    private OrderService orderService;
    private QueueService queueService;
    private InventoryService inventoryService;

    public StudentUI(MenuService menuService,
                     OrderService orderService,
                     QueueService queueService,
                     InventoryService inventoryService) {

        sc = new Scanner(System.in);
        this.menuService = menuService;
        this.orderService = orderService;
        this.queueService = queueService;
        this.inventoryService = inventoryService;
    }

    public void showMenu() {

        System.out.print("Enter Student ID: ");
        long studentId = sc.nextLong();

        int choice;

        do {
            System.out.println("\n--- STUDENT MENU ---");
            System.out.println("1. Browse Menu");
            System.out.println("2. Place Order");
            System.out.println("3. View Order History");
            System.out.println("4. Cancel Order");
            System.out.println("5. Track Queue");
            System.out.println("6. View Order Status");
            System.out.println("0. Back");

            System.out.print("Enter choice: ");
            choice = sc.nextInt();

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

            System.out.print("Enter Item ID (0 to finish): ");
            long itemId = sc.nextLong();

            if (itemId == 0) {
                break;
            }

            MenuItem item = menuService.findItem(itemId);

            if (item == null || !item.isAvailable()) {
                System.out.println("Invalid or unavailable item.");
                continue;
            }

            System.out.print("Enter quantity: ");
            int qty = sc.nextInt();

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

            } catch (StockUnavailableException e) {

                System.out.println(e.getMessage());

            } catch (IllegalArgumentException e) {

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

        } catch (StockUnavailableException e) {

            System.out.println(e.getMessage());
            System.out.println("Order cannot be placed.");
            return;
        }

        System.out.println("\n===== ORDER SUMMARY =====");

        for (OrderItem item : items) {
            MenuItem menuItem = menuService.findItem(item.getItemId());

            System.out.println(
                    menuItem.getName()
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

        System.out.print("Confirm order? (1 Yes / 0 No): ");
        int confirm = sc.nextInt();

        if (confirm != 1) {
            System.out.println("Order cancelled.");
            return;
        }

        // Deduct stock and keep track of successful deductions
        List<OrderItem> deductedItems = new ArrayList<>();

        for (OrderItem item : items) {

            boolean deducted = inventoryService.deductStock(
                    item.getItemId(),
                    item.getQty()
            );

            if (!deducted) {

                // Restore any stock already deducted
                for (OrderItem deductedItem : deductedItems) {
                    inventoryService.restoreStock(
                            deductedItem.getItemId(),
                            deductedItem.getQty()
                    );
                }

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
            System.out.println("Order ID: " + order.getOrderId());
            System.out.println("Total: Rs. " + order.getTotalPrice());

        } else {

            // Restore stock if order creation fails
            for (OrderItem item : deductedItems) {
                inventoryService.restoreStock(
                        item.getItemId(),
                        item.getQty()
                );
            }

            System.out.println("Unable to place order.");
        }
    }

    private void viewHistory(long studentId) {

        List<Order> orders = orderService.getStudentOrders(studentId);

        for (Order order : orders) {

            System.out.println(
                    "Order ID: " + order.getOrderId() +
                            " | Status: " + order.getStatus() +
                            " | Total: Rs." + order.getTotalPrice()
            );
        }

        if (orders.isEmpty()) {
            System.out.println("No orders found.");
        }
    }

    private void cancelOrder(long studentId) {

        System.out.print("Enter Order ID: ");
        long orderId = sc.nextLong();

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

        System.out.print("Enter Order ID: ");
        long orderId = sc.nextLong();

        Order order = orderService.findOrder(orderId);

        if (order == null || order.getStudentId() != studentId) {
            System.out.println("Order not found.");
            return;
        }

        int ahead = queueService.getOrdersAhead(
                orderService.getAllOrders(), orderId
        );

        if (ahead >= 0) {
            System.out.println("Orders ahead: " + ahead);
        } else {
            System.out.println("Order is not in the active queue.");
        }
    }

    private void viewStatus(long studentId) {

        System.out.print("Enter Order ID: ");
        long orderId = sc.nextLong();

        Order order = orderService.findOrder(orderId);

        if (order != null && order.getStudentId() == studentId) {
            System.out.println("Order Status: " + order.getStatus());
        } else {
            System.out.println("Order not found.");
        }
    }
}
