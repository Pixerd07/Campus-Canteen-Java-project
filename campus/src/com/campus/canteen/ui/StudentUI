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

        System.out.println("\n--- AVAILABLE MENU ---");

        for (MenuItem item : menuService.getAvailableItems()) {
            System.out.println(
                    item.getId() + ". " +
                    item.getName() + " - Rs." +
                    item.getPrice() +
                    " | Stock: " +
                    inventoryService.getStock(item.getId())
            );
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

            if (!inventoryService.hasEnoughStock(itemId, qty)) {
                System.out.println("Insufficient stock.");
                continue;
            }

            items.add(new OrderItem(itemId, qty, item.getPrice()));
        }

        if (items.isEmpty()) {
            System.out.println("No items selected.");
            return;
        }

        System.out.print("Confirm order? (1 Yes / 0 No): ");
        int confirm = sc.nextInt();

        if (confirm == 1) {

            Order order = orderService.placeOrder(studentId, items);

            if (order != null) {
                System.out.println("Order placed successfully!");
                System.out.println("Order ID: " + order.getOrderId());
                System.out.println("Total: Rs." + order.getTotalPrice());
            } else {
                System.out.println("Unable to place order.");
            }
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

        if (orderService.cancelOrder(orderId, studentId)) {
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
                orderService.getAllOrders(), orderId);

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