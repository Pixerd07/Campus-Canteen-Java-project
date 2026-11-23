package com.campus.canteen.service;

import com.campus.canteen.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MenuService {

    private List<MenuItem> menu;

    public MenuService() {
        menu = new ArrayList<>();
    }

    // add a new menu item
    public void addItem(MenuItem item) {

        if (item == null || findItem(item.getId()) != null) {
            return;
        }

        menu.add(item);
    }

    // display all menu items
    public List<MenuItem> getAllItems() {
        return menu;
    }

    // find item using ID
    public MenuItem findItem(long id) {

        for (MenuItem item : menu) {
            if (item.getId() == id) {
                return item;
            }
        }

        return null;
    }

    // display available items
    public List<MenuItem> getAvailableItems() {

        List<MenuItem> availableItems = new ArrayList<>();

        for (MenuItem item : menu) {
            if (item.isAvailable()) {
                availableItems.add(item);
            }
        }

        return availableItems;
    }

    // update item price
    public boolean changePrice(long id, double newPrice) {

        MenuItem item = findItem(id);

        if (item == null || newPrice <= 0) {
            return false;
        }

        item.setPrice(newPrice);
        return true;
    }

    // update item availability
    public boolean setAvailability(long id, boolean available) {

        MenuItem item = findItem(id);

        if (item == null) {
            return false;
        }

        item.setAvailable(available);
        return true;
    }
}