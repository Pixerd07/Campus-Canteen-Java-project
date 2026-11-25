package com.campus.canteen.service;

import com.campus.canteen.db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrderDatabase {

    public void saveStudent(long studentId) throws SQLException {
        String sql = "INSERT IGNORE INTO students (student_id) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, studentId);
            stmt.executeUpdate();
        }
    }

    public int saveOrder(long studentId, double total) throws SQLException {
        String sql = "INSERT INTO orders (student_id, total) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, studentId);
            stmt.setDouble(2, total);
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }

    public void saveOrderItem(int orderId, long menuItemId, int qty, double price) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, menu_item_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setLong(2, menuItemId);
            stmt.setInt(3, qty);
            stmt.setDouble(4, price);
            stmt.executeUpdate();
        }
    }
}