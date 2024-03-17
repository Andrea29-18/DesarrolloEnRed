import java.util.Random;

public class Philosopher implements Runnable {
    private final int id;
    private final Object leftFork;
    private final Object rightFork;
    private final Random random;

    public Philosopher(int id, Object leftFork, Object rightFork) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.random = new Random();
    }

    private void think() throws InterruptedException {
        System.out.println("Filósofo " + id + " está pensando.");
        Thread.sleep(random.nextInt(1000)); // Tiempo aleatorio para pensar
    }

    private void eat() throws InterruptedException {
        System.out.println("Filósofo " + id + " está comiendo.");
        Thread.sleep(random.nextInt(1000)); // Tiempo aleatorio para comer
    }

    private void takeForks() throws InterruptedException {
        synchronized (leftFork) {
            System.out.println("Filósofo " + id + " ha tomado el tenedor izquierdo.");
            synchronized (rightFork) {
                System.out.println("Filósofo " + id + " ha tomado el tenedor derecho y está listo para comer.");
            }
        }
    }

    private void releaseForks() {
        synchronized (leftFork) {
            System.out.println("Filósofo " + id + " ha liberado el tenedor izquierdo.");
            synchronized (rightFork) {
                System.out.println("Filósofo " + id + " ha liberado el tenedor derecho.");
                // Despierta a los filósofos adyacentes
                rightFork.notify();
                leftFork.notify();
            }
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                think();
                takeForks();
                eat();
                releaseForks();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
    }

    public static void main(String[] args) {
        int numPhilosophers = 5;
        Philosopher[] philosophers = new Philosopher[numPhilosophers];
        Object[] forks = new Object[numPhilosophers];

        for (int i = 0; i < numPhilosophers; i++) {
            forks[i] = new Object();
        }

        for (int i = 0; i < numPhilosophers; i++) {
            Object leftFork = forks[i];
            Object rightFork = forks[(i + 1) % numPhilosophers];

            // Se alterna la toma de tenedores para evitar deadlocks
            if (i == 0) {
                philosophers[i] = new Philosopher(i, rightFork, leftFork);
            } else {
                philosophers[i] = new Philosopher(i, leftFork, rightFork);
            }

            Thread t = new Thread(philosophers[i], "Philosopher " + (i + 1));
            t.start();
        }
    }
}
