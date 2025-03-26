class Order {
    private final int orderId;
    private final String item;
    private String status; // Add status field

    public Order(int orderId, String item) {
        this.orderId = orderId;
        this.item = item;
        this.status = "PLACED"; // Default status
    }

    // Getters
    public int getOrderId() { return orderId; }
    public String getItem() { return item; }
    public String getStatus() { return status; }

    // Setter to update status
    public void setStatus(String status) {
        this.status = status;
    }
}
