import java.util.concurrent.*;


class Waiter implements Runnable {
    private final BlockingQueue<Order> orderQueue;
    private final BlockingQueue<Order> chefQueue;
    private final Semaphore chefSemaphore;
    private final Semaphore waiterSemaphore;
    private final int id;

    public Waiter(BlockingQueue<Order> orderQueue, BlockingQueue<Order> chefQueue, Semaphore chefSemaphore, Semaphore waiterSemaphore, int id) {
        this.orderQueue = orderQueue;
        this.chefQueue = chefQueue;
        this.chefSemaphore = chefSemaphore;
        this.waiterSemaphore = waiterSemaphore;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Order order = orderQueue.poll(2, TimeUnit.SECONDS);
                if (order == null) {
                    break; // Exit if no new orders
                }

                LoggerHelper.LOGGER.info("🍽️ Waiter " + id + " took order: " + order.getItem());
                Thread.sleep(1000); // Simulate taking order to chef
                chefQueue.put(order);
                LoggerHelper.LOGGER.info("👨‍🍳 Waiter " + id + " passed order to chef: " + order.getItem());
            }

            // Final check for prepared orders
            while (!chefQueue.isEmpty()) {
                Order preparedOrder = chefQueue.poll();
                if (preparedOrder != null && "PREPARED".equals(preparedOrder.getStatus())) {
                    preparedOrder.setStatus("COMPLETED");
                    DatabaseHelper.updateOrderStatus(preparedOrder.getOrderId(), "COMPLETED");
                    LoggerHelper.LOGGER.info("✅ Waiter " + id + " delivered order: " + preparedOrder.getItem());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


}
