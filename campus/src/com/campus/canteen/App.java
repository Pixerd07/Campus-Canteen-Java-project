package com.campus.canteen;

import com.campus.canteen.db.DatabaseConnection;
import com.campus.canteen.ui.InputHelper;
import com.campus.canteen.ui.MainMenu;
import java.sql.Connection;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // connection OK, nothing to print
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return;
        }

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