package com.campus.canteen.ui;

import com.campus.canteen.service.MenuService;
import com.campus.canteen.service.OrderService;
import com.campus.canteen.service.QueueService;
import com.campus.canteen.service.InventoryService;

import java.util.Scanner;

public class MainMenu {

    private Scanner sc;

    private StudentUI studentUI;
    private KitchenUI kitchenUI;
    private AdminUI adminUI;

    public MainMenu() {
        sc = new Scanner(System.in);

        MenuService menuService = new MenuService();
        OrderService orderService = new OrderService();
        QueueService queueService = new QueueService();
        InventoryService inventoryService = new InventoryService();

        studentUI = new StudentUI(
                menuService,
                orderService,
                queueService,
                inventoryService
        );

        kitchenUI = new KitchenUI(
                orderService,
                queueService
        );

        adminUI = new AdminUI(
                menuService,
                inventoryService
        );
    }

    public void showMenu() {
        int choice;

        do {
            System.out.println("\n================================");
            System.out.println(" CAMPUS CANTEEN MANAGEMENT SYSTEM");
            System.out.println("================================");
            System.out.println("1. Student Panel");
            System.out.println("2. Kitchen Panel");
            System.out.println("3. Admin Panel");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    studentUI.showMenu();
                    break;

                case 2:
                    kitchenUI.showMenu();
                    break;

                case 3:
                    adminUI.showMenu();
                    break;

                case 4:
                    System.out.println("Thank you for using Campus Canteen System!");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 4);
    }

    public static void main(String[] args) {
        MainMenu mainMenu = new MainMenu();
        mainMenu.showMenu();
    }
}
