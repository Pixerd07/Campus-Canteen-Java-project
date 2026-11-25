
package com.campus.canteen.ui;

import com.campus.canteen.model.MenuItem;
import com.campus.canteen.service.MenuService;
import com.campus.canteen.service.InventoryService;

import java.util.List;

public class AdminUI {

    private MenuService menuService;
    private InventoryService inventoryService;
    private long nextItemId;

    public AdminUI(MenuService menuService,
                   InventoryService inventoryService) {

        this.menuService = menuService;
        this.inventoryService = inventoryService;
        nextItemId = 6;
    }

    public void showMenu() {
        int choice = -1;

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

            choice = InputHelper.readInt("Enter choice: ");

            try {
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
            } catch (IllegalStateException e) {
                throw e;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
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
        String name = InputHelper.readNonEmptyString("Enter item name: ");

        double price = InputHelper.readDouble("Enter price: ");

        if (price <= 0) {
            System.out.println("Price must be greater than zero.");
            return;
        }

        int stock = InputHelper.readInt("Enter initial stock: ");

        if (stock < 0) {
            System.out.println("Stock cannot be negative.");
            return;
        }

        try {
            MenuItem item = new MenuItem(
                    nextItemId, name, price, true
            );

            menuService.addItem(item);
            inventoryService.setInitialStock(nextItemId, stock);

            System.out.println("Menu item added successfully!");
            System.out.println("Item ID: " + nextItemId);

            nextItemId++;
        } catch (IllegalArgumentException e) {
            System.out.println("Could not add menu item: " + e.getMessage());
        }
    }

    private void searchItem() {
        long id = InputHelper.readLong("Enter item ID: ");

        if (id <= 0) {
            System.out.println("Invalid item ID.");
            return;
        }

        MenuItem item = menuService.findItem(id);

        if (item == null) {
            System.out.println("Item not found.");
        } else {
            displayItem(item);
        }
    }

    private void changePrice() {
        long id = InputHelper.readLong("Enter item ID: ");

        if (id <= 0) {
            System.out.println("Invalid item ID.");
            return;
        }

        double price = InputHelper.readDouble("Enter new price: ");

        if (menuService.changePrice(id, price)) {
            System.out.println("Price updated successfully!");
        } else {
            System.out.println("Unable to update price.");
        }
    }

    private void changeAvailability() {
        long id = InputHelper.readLong("Enter item ID: ");

        if (id <= 0) {
            System.out.println("Invalid item ID.");
            return;
        }

        System.out.println("1. Available");
        System.out.println("2. Unavailable");

        int choice = InputHelper.readInt("Enter choice: ");

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
        long id = InputHelper.readLong("Enter item ID: ");

        if (id <= 0 || menuService.findItem(id) == null) {
            System.out.println("Item not found.");
            return;
        }

        int quantity = InputHelper.readInt("Enter stock quantity: ");

        if (quantity < 0) {
            System.out.println("Stock cannot be negative.");
            return;
        }

        inventoryService.setInitialStock(id, quantity);
        System.out.println("Stock set successfully!");
    }

    private void updateStock() {
        long id = InputHelper.readLong("Enter item ID: ");

        if (id <= 0) {
            System.out.println("Invalid item ID.");
            return;
        }

        int quantity = InputHelper.readInt("Enter new stock quantity: ");

        if (inventoryService.updateStock(id, quantity)) {
            System.out.println("Stock updated successfully!");
        } else {
            System.out.println("Unable to update stock.");
        }
    }

    private void viewStock() {
        long id = InputHelper.readLong("Enter item ID: ");

        if (id <= 0 || menuService.findItem(id) == null) {
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
