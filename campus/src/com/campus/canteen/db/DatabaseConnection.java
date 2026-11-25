//package com.campus.canteen.db;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class DatabaseConnection {
//    private static final String URL = "jdbc:mysql://localhost:3306/campus_canteen";
//    private static final String USER = "root";
//    private static final String PASSWORD = "tishamittu1234";
//
//    private DatabaseConnection() {
//    }
//
//    public static Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(URL, USER, PASSWORD);
//    }
//}
package com.campus.canteen.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final Properties props = new Properties();

    static {
        try (FileInputStream in = new FileInputStream("config.properties")) {
            props.load(in);
        } catch (IOException e) {
            System.err.println("Could not read config.properties: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password"));
    }
}