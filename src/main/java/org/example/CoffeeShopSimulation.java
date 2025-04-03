package org.example;

public class CoffeeShopSimulation {
    private static final int NUM_BARISTAS = 3;
    private static final int NUM_CUSTOMERS = 5;

    public static void main(String[] args) {
        Thread[] baristas = new Thread[NUM_BARISTAS];
        Thread[] customers = new Thread[NUM_CUSTOMERS];

        // Start baristas
        for (int i = 0; i < NUM_BARISTAS; i++) {
            baristas[i] = new Thread(new Barista("Barista-" + (i + 1)));
            baristas[i].start();
        }

        // Start customers
        for (int i = 0; i < NUM_CUSTOMERS; i++) {
            customers[i] = new Thread(new Customer("Customer-" + (i + 1)));
            customers[i].start();
        }

        // Wait for all threads to finish
        for (Thread barista : baristas) {
            try {
                barista.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (Thread customer : customers) {
            try {
                customer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\n🚀 Coffee shop is closed. All orders completed!");
    }
}


