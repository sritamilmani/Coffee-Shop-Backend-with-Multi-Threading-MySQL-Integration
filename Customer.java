import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

class Customer implements Runnable {
    private final BlockingQueue<Order> orderQueue;
    private final Semaphore waiterSemaphore;
    private final String[] menu = {"Espresso", "Cappuccino", "Latte", "Mocha"};

    public Customer(BlockingQueue<Order> orderQueue, Semaphore waiterSemaphore) {
        this.orderQueue = orderQueue;
        this.waiterSemaphore = waiterSemaphore;
    }

    @Override
    public void run() {
        try {
            String item = menu[(int) (Math.random() * menu.length)];
            int orderId = DatabaseHelper.insertOrder(item);
            if (orderId == -1) return;
            Order order = new Order(orderId, item);
            LoggerHelper.LOGGER.info("🧑‍💼 Customer placed: " + order.getItem());
            orderQueue.put(order);

            waiterSemaphore.acquire(); // Wait for waiter to be available
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}