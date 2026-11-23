


        package com.campus.canteen.ui;

import com.campus.canteen.model.MenuItem;
import com.campus.canteen.service.MenuService;
import com.campus.canteen.service.InventoryService;

import java.util.List;
import java.util.Scanner;

public class AdminUI {

    private MenuService menuService;
    private InventoryService inventoryService;
    private Scanner sc;
    private long nextItemId;

    public AdminUI(MenuService menuService,
                   InventoryService inventoryService) {

        this.menuService = menuService;
        this.inventoryService = inventoryService;
        sc = new Scanner(System.in);
        nextItemId = 1;
    }

    public void showMenu() {
        int choice;

        do {
            System.out.println("\n===== ADMIN PANEL =====");
            System.out.println("1. View Menu");
            System.out.println("2. Add Menu Item");
            System.out.println("3. Search Menu Item");
            System.out.println("4. Change Item Price");
            System.out.println("5. Change Item Availability");
            System.out.println("6. Set Initial Stock");
            System.out.println("7. Update Stock");
            System.out.println("8. View Stock");
            System.out.println("9. Back");

            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    viewMenu();
                    break;

                case 2:
                    addMenuItem();
                    break;

                case 3:
                    searchItem();
                    break;

                case 4:
                    changePrice();
                    break;

                case 5:
                    changeAvailability();
                    break;

                case 6:
                    setInitialStock();
                    break;

                case 7:
                    updateStock();
                    break;

                case 8:
                    viewStock();
                    break;

                case 9:
                    System.out.println("Returning to main menu...");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 9);
    }

    private void viewMenu() {
        List<MenuItem> items = menuService.getAllItems();

        if (items.isEmpty()) {
            System.out.println("Menu is empty.");
            return;
        }

        System.out.println("\n===== MENU ITEMS =====");

        for (MenuItem item : items) {
            displayItem(item);
        }
    }

    private void addMenuItem() {
        sc.nextLine();

        System.out.print("Enter item name: ");
        String name = sc.nextLine();

        System.out.print("Enter price: ");
        double price = sc.nextDouble();

        if (name.trim().isEmpty() || price <= 0) {
            System.out.println("Invalid item details.");
            return;
        }

        MenuItem item = new MenuItem(
                nextItemId, name, price, true
        );

        menuService.addItem(item);
        nextItemId++;

        System.out.print("Enter initial stock: ");
        int stock = sc.nextInt();

        if (stock >= 0) {
            inventoryService.setInitialStock(item.getId(), stock);
            System.out.println("Menu item added successfully!");
        } else {
            System.out.println("Invalid stock quantity.");
        }
    }

    private void searchItem() {
        System.out.print("Enter item ID: ");
        long id = sc.nextLong();

        MenuItem item = menuService.findItem(id);

        if (item == null) {
            System.out.println("Item not found.");
        } else {
            displayItem(item);
        }
    }

    private void changePrice() {
        System.out.print("Enter item ID: ");
        long id = sc.nextLong();

        System.out.print("Enter new price: ");
        double price = sc.nextDouble();

        if (menuService.changePrice(id, price)) {
            System.out.println("Price updated successfully!");
        } else {
            System.out.println("Unable to update price.");
        }
    }

    private void changeAvailability() {
        System.out.print("Enter item ID: ");
        long id = sc.nextLong();

        System.out.println("1. Available");
        System.out.println("2. Unavailable");
        System.out.print("Enter choice: ");

        int choice = sc.nextInt();

        if (choice != 1 && choice != 2) {
            System.out.println("Invalid choice.");
            return;
        }

        boolean available = choice == 1;

        if (menuService.setAvailability(id, available)) {
            System.out.println("Availability updated!");
        } else {
            System.out.println("Item not found.");
        }
    }

    private void setInitialStock() {
        System.out.print("Enter item ID: ");
        long id = sc.nextLong();

        if (menuService.findItem(id) == null) {
            System.out.println("Item not found.");
            return;
        }

        System.out.print("Enter stock quantity: ");
        int quantity = sc.nextInt();

        if (quantity < 0) {
            System.out.println("Stock cannot be negative.");
            return;
        }

        inventoryService.setInitialStock(id, quantity);
        System.out.println("Stock set successfully!");
    }

    private void updateStock() {
        System.out.print("Enter item ID: ");
        long id = sc.nextLong();

        System.out.print("Enter new stock quantity: ");
        int quantity = sc.nextInt();

        if (inventoryService.updateStock(id, quantity)) {
            System.out.println("Stock updated successfully!");
        } else {
            System.out.println("Unable to update stock.");
        }
    }

    private void viewStock() {
        System.out.print("Enter item ID: ");
        long id = sc.nextLong();

        if (menuService.findItem(id) == null) {
            System.out.println("Item not found.");
            return;
        }

        System.out.println("Available stock: "
                + inventoryService.getStock(id));
    }

    private void displayItem(MenuItem item) {
        System.out.println("\n----------------------");
        System.out.println("Item ID: " + item.getId());
        System.out.println("Name: " + item.getName());
        System.out.println("Price: Rs. " + item.getPrice());
        System.out.println("Available: " + item.isAvailable());
        System.out.println("Stock: "
                + inventoryService.getStock(item.getId()));
        System.out.println("----------------------");
    }
}