package project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private String lastAction = "Read";
    private final int[] arrayOfNumbers = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};

    private synchronized void newAction(String action) {
        try {
            for(int number : arrayOfNumbers) {
                while (lastAction.equals(action)) {
                    this.wait();
                }

                logger.info(String.valueOf(number));
                lastAction = action;
                sleep();
                this.notifyAll();
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        new Thread(() -> main.newAction("write")).start();
        new Thread(() -> main.newAction("read")).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
