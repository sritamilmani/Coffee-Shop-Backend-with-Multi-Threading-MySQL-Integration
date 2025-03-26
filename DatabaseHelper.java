import java.sql.*;

class DatabaseHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/CoffeeShopDB";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    static int insertOrder(String item) {
        String query = "INSERT INTO orders (item, status) VALUES (?, 'PLACED')";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, item);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) { LoggerHelper.LOGGER.severe("Database error: " + e.getMessage()); }
        return -1;
    }

    static void updateOrderStatus(int orderId, String status) {
        String query = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
        } catch (SQLException e) { LoggerHelper.LOGGER.severe("Database error: " + e.getMessage()); }
    }
}
