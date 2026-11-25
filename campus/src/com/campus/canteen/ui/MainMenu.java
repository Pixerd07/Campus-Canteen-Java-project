
package com.campus.canteen.ui;

import com.campus.canteen.model.MenuItem;
import com.campus.canteen.service.MenuService;
import com.campus.canteen.service.OrderService;
import com.campus.canteen.service.QueueService;
import com.campus.canteen.service.InventoryService;

public class MainMenu {

    private StudentUI studentUI;
    private KitchenUI kitchenUI;
    private AdminUI adminUI;

    public MainMenu() {
        MenuService menuService = new MenuService();
        OrderService orderService = new OrderService();
        QueueService queueService = new QueueService();
        InventoryService inventoryService = new InventoryService();

        // Load demo data
        loadDemoData(menuService, inventoryService);

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

    private void loadDemoData(MenuService menuService,
                              InventoryService inventoryService) {

        menuService.addItem(new MenuItem(1, "Veg Burger", 60, true));
        menuService.addItem(new MenuItem(2, "Cheese Sandwich", 80, true));
        menuService.addItem(new MenuItem(3, "Masala Maggi", 50, true));
        menuService.addItem(new MenuItem(4, "Cold Coffee", 70, true));
        menuService.addItem(new MenuItem(5, "Veg Pizza", 120, true));

        inventoryService.setInitialStock(1, 20);
        inventoryService.setInitialStock(2, 15);
        inventoryService.setInitialStock(3, 25);
        inventoryService.setInitialStock(4, 30);
        inventoryService.setInitialStock(5, 10);

        System.out.println("Demo data loaded successfully!");
    }

    public void showMenu() {
        int choice = -1;

        do {
            System.out.println("\n================================");
            System.out.println(" CAMPUS CANTEEN MANAGEMENT SYSTEM");
            System.out.println("================================");
            System.out.println("1. Student Panel");
            System.out.println("2. Kitchen Panel");
            System.out.println("3. Admin Panel");
            System.out.println("4. Exit");

            choice = InputHelper.readInt("Enter your choice: ");

            try {
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
            } catch (IllegalStateException e) {
                // Propagate closed input stream upward to exit cleanly
                throw e;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (choice != 4);
    }

    public static void main(String[] args) {
        try {
            MainMenu mainMenu = new MainMenu();
            mainMenu.showMenu();
        } catch (IllegalStateException e) {
            System.out.println("\n" + e.getMessage());
        } catch (Exception e) {
            System.out.println("\nAn unexpected error occurred: " + e.getMessage());
        } finally {
            InputHelper.close();
        }
    }
}
