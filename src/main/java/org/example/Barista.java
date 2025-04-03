package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

class Barista implements Runnable {
    private static final int MAX_ORDERS = 5; // Stop after this many orders
    private static final AtomicInteger orderCount = new AtomicInteger(0);
    private String baristaName;
    private String[] coffeeTypes = {"Espresso", "Latte", "Cappuccino", "Americano"};
    private Random random = new Random();

    public Barista(String name) {
        this.baristaName = name;
    }

    @Override
    public void run() {
        try {
            while (orderCount.get() < MAX_ORDERS) {
                synchronized (orderCount) {
                    if (orderCount.get() >= MAX_ORDERS) break; // Double-check inside sync block
                    String coffee = coffeeTypes[random.nextInt(coffeeTypes.length)];
                    saveOrderToDatabase(coffee);
                    orderCount.incrementAndGet(); // Increment safely
                    System.out.println("👨‍🍳 " + baristaName + " prepared: " + coffee);
                }
                Thread.sleep(2000);
            }
            System.out.println("🛑 " + baristaName + " has finished making orders.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void saveOrderToDatabase(String coffeeType) {
        String query = "INSERT INTO Orders (coffee_type, status) VALUES (?, 'Pending')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, coffeeType);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



