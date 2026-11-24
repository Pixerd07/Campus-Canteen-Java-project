package com.campus.canteen;

import com.campus.canteen.ui.InputHelper;
import com.campus.canteen.ui.MainMenu;

public class App {

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


