import java.util.concurrent.*;

public class CoffeeShopSimulation {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Order> orderQueue = new ArrayBlockingQueue<>(5);
        BlockingQueue<Order> chefQueue = new ArrayBlockingQueue<>(5);
        Semaphore waiterSemaphore = new Semaphore(2); // Two waiters
        Semaphore chefSemaphore = new Semaphore(2);   // Two chefs

        ExecutorService executor = Executors.newFixedThreadPool(9);

        // Create 5 customer threads
        for (int i = 0; i < 5; i++) {
            executor.submit(new Customer(orderQueue, waiterSemaphore));
        }

        // Create 2 waiter threads
        for (int i = 0; i < 2; i++) {
            executor.submit(new Waiter(orderQueue, chefQueue, chefSemaphore, waiterSemaphore, i + 1));
        }

        // Create 2 chef threads
        for (int i = 0; i < 2; i++) {
            executor.submit(new Chef(chefQueue, chefSemaphore, i + 1));
        }

        // Graceful shutdown: wait for tasks to complete
//        executor.shutdown();
//        if (!executor.awaitTermination(10, TimeUnit.SECONDS)) { // Extend wait time to 10 seconds
//            LoggerHelper.LOGGER.warning("Forcing shutdown after timeout...");
//            executor.shutdownNow();
//        }
        LoggerHelper.LOGGER.info("All orders processed. Program terminated.");

    }

}
