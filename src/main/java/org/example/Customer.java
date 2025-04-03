package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class Customer implements Runnable {
    private String customerName;

    public Customer(String name) {
        this.customerName = name;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Order order = fetchNextOrder();
                if (order == null) {
                    if (allOrdersCompleted()) break; // Exit only if all orders are completed
                    Thread.sleep(2000); // Wait before checking again
                    continue;
                }
                updateOrderStatus(order.getOrderId());
                System.out.println("☕ " + customerName + " picked up: Order #" + order.getOrderId() + " - " + order.getCoffeeType());
                Thread.sleep(3000); // Simulate drinking coffee
            }
            System.out.println("✅ " + customerName + " has finished. No more orders left.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private Order fetchNextOrder() {
        String query = "SELECT order_id, coffee_type FROM Orders WHERE status = 'Pending' ORDER BY created_at ASC LIMIT 1 FOR UPDATE"; // Locks row
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int orderId = rs.getInt("order_id");
                String coffeeType = rs.getString("coffee_type");

                // Update order status to "In Progress" immediately
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE Orders SET status = 'In Progress' WHERE order_id = ?");
                updateStmt.setInt(1, orderId);
                updateStmt.executeUpdate();

                conn.commit(); // Commit transaction
                return new Order(orderId, coffeeType);
            }
            conn.rollback(); // Rollback if no order found
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        return null;
    }

    private void updateOrderStatus(int orderId) {
        String query = "UPDATE Orders SET status = 'Completed' WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean allOrdersCompleted() {
        String query = "SELECT COUNT(*) FROM Orders WHERE status = 'Pending'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() && rs.getInt(1) == 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



