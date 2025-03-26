import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

class Chef implements Runnable {
    private final BlockingQueue<Order> chefQueue;
    private final Semaphore chefSemaphore;
    private final int id;

    public Chef(BlockingQueue<Order> chefQueue, Semaphore chefSemaphore, int id) {
        this.chefQueue = chefQueue;
        this.chefSemaphore = chefSemaphore;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Order order = chefQueue.poll(2, TimeUnit.SECONDS); // Check for orders with a timeout
                if (order == null) {
                    break; // No orders left, exit loop
                }

                LoggerHelper.LOGGER.info("🔥 Chef " + id + " started preparing: " + order.getItem());
                Thread.sleep(3000); // Simulate preparation time

                order.setStatus("PREPARED"); // Update status to PREPARED
                DatabaseHelper.updateOrderStatus(order.getOrderId(), "PREPARED"); // Update DB

                LoggerHelper.LOGGER.info("✅ Chef " + id + " finished: " + order.getItem());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}


