import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophers {

    static class Fork {
        private final Lock lock = new ReentrantLock();

        public void putDown() {
            lock.unlock();
        }
    }

    static class Philosopher extends Thread {
        private final int id;
        private final Fork leftFork;
        private final Fork rightFork;
        private int eatingCounter = 0;

        public Philosopher(int id, Fork leftFork, Fork rightFork) {
            this.id = id;
            this.leftFork = leftFork;
            this.rightFork = rightFork;
        }

        public void run() {
            try {
                while (eatingCounter < 3) {
                    think();
                    if (leftFork.lock.tryLock()) {
                        if (rightFork.lock.tryLock()) {
                            eat();
                            rightFork.putDown();
                        }
                        leftFork.putDown();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void eat() throws InterruptedException {
            System.out.println("Philosopher " + id + " is eating.");
            eatingCounter++;
            Thread.sleep(1000); // Simulate eating time
        }

        private void think() throws InterruptedException {
            System.out.println("Philosopher " + id + " is thinking.");
            Thread.sleep(1000); // Simulate thinking time
        }
    }

    private final Philosopher[] philosophers;

    public DiningPhilosophers(int numPhilosophers) {
        philosophers = new Philosopher[numPhilosophers];
        Fork[] forks = new Fork[numPhilosophers];

        for (int i = 0; i < numPhilosophers; i++) {
            forks[i] = new Fork();
        }

        for (int i = 0; i < numPhilosophers; i++) {
            Fork leftFork = forks[i];
            Fork rightFork = forks[(i + 1) % numPhilosophers];
            philosophers[i] = new Philosopher(i, leftFork, rightFork);
        }
    }

    public void startDining() {
        for (Philosopher philosopher : philosophers) {
            philosopher.start();
        }
    }

    public static void main(String[] args) {
        DiningPhilosophers diningPhilosophers = new DiningPhilosophers(5);
        diningPhilosophers.startDining();
    }
}
