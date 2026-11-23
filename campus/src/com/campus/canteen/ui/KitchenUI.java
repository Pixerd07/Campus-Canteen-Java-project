


        package com.campus.canteen.ui;

import com.campus.canteen.model.Order;
import com.campus.canteen.model.OrderItem;
import com.campus.canteen.model.OrderStatus;
import com.campus.canteen.service.OrderService;
import com.campus.canteen.service.QueueService;

import java.util.List;
import java.util.Scanner;

public class KitchenUI {

    private OrderService orderService;
    private QueueService queueService;
    private Scanner sc;

    public KitchenUI(OrderService orderService, QueueService queueService) {
        this.orderService = orderService;
        this.queueService = queueService;
        sc = new Scanner(System.in);
    }

    public void showMenu() {
        int choice;

        do {
            System.out.println("\n===== KITCHEN PANEL =====");
            System.out.println("1. View All Orders");
            System.out.println("2. View Active Orders");
            System.out.println("3. Update Order Status");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    viewAllOrders();
                    break;

                case 2:
                    viewActiveOrders();
                    break;

                case 3:
                    updateOrderStatus();
                    break;

                case 4:
                    System.out.println("Returning to main menu...");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 4);
    }

    private void viewAllOrders() {
        List<Order> orders = orderService.getAllOrders();

        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        System.out.println("\n===== ALL ORDERS =====");

        for (Order order : orders) {
            displayOrder(order);
        }
    }

    private void viewActiveOrders() {
        List<Order> activeOrders =
                queueService.getActiveOrders(orderService.getAllOrders());

        if (activeOrders.isEmpty()) {
            System.out.println("No active orders.");
            return;
        }

        System.out.println("\n===== ACTIVE ORDERS =====");

        for (Order order : activeOrders) {
            displayOrder(order);
        }
    }

    private void updateOrderStatus() {
        System.out.print("Enter Order ID: ");
        long orderId = sc.nextLong();

        Order order = orderService.findOrder(orderId);

        if (order == null) {
            System.out.println("Order not found.");
            return;
        }

        String currentStatus = order.getStatus();
        String nextStatus = OrderStatus.next(currentStatus);

        if (nextStatus == null) {
            System.out.println("This order cannot be updated.");
            return;
        }

        System.out.println("Current Status: " + currentStatus);
        System.out.println("Next Status: " + nextStatus);

        System.out.print("Confirm update? (1 = Yes, 0 = No): ");
        int confirm = sc.nextInt();

        if (confirm == 1) {
            order.setStatus(nextStatus);
            System.out.println("Order status updated successfully!");
        } else {
            System.out.println("Status update cancelled.");
        }
    }

    private void displayOrder(Order order) {
        System.out.println("\n----------------------");
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Student ID: " + order.getStudentId());
        System.out.println("Status: " + order.getStatus());

        System.out.println("Items:");

        for (OrderItem item : order.getItems()) {
            System.out.println(
                    "Item ID: " + item.getItemId()
                            + " | Quantity: " + item.getQty()
                            + " | Price: Rs. " + item.getPrice()
            );
        }

        System.out.println("Total: Rs. " + order.getTotalPrice());
        System.out.println("----------------------");
    }
}