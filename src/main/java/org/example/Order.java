package org.example;

class Order {
    private int orderId;
    private String coffeeType;

    public Order(int orderId, String coffeeType) {
        this.orderId = orderId;
        this.coffeeType = coffeeType;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getCoffeeType() {
        return coffeeType;
    }

    @Override
    public String toString() {
        return "Order #" + orderId + " - " + coffeeType;
    }
}
