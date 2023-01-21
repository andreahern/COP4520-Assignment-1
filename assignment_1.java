
public class assignment_1 {
    public static final int THREAD_COUNT = 8;
    public static final long SEARCH_SPACE = 10000000;
    static SharedCounter counter = new SharedCounter();
    static SharedCounter primeCount = new SharedCounter();

    public static void main(String[] args) throws Exception {
        final long startTime = System.nanoTime();
        Thread[] threads = new Thread[THREAD_COUNT];

        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(new GetPrimes(i, counter, primeCount));
            threads[i].start();
        }

        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i].join();
        }

        final double seconds = (double) (System.nanoTime() - startTime) / 1_000_000_000;

        System.out.printf("Runtime:\t%.2f secs\tTotal Primes:\t%d%n", seconds, primeCount.get());
    }
}

class GetPrimes implements Runnable {
    final int ID;
    SharedCounter counter;
    SharedCounter primeCount;

    public GetPrimes(int ID, SharedCounter counter, SharedCounter primeCount) {
        this.ID = ID;
        this.counter = counter;
        this.primeCount = primeCount;
    }

    private boolean isPrime(long n) {
        return false;
    }

    public void run() {
        long num;
        while ((num = counter.getAndIncrement()) <= assignment_1.SEARCH_SPACE) {
            if (isPrime(num)) {
                primeCount.increment();
            }
        }
    }
}

class SharedCounter {
    private long count;

    public SharedCounter() {
        this.count = 0;
    }

    public synchronized long getAndIncrement() {
        long temp = count;
        count = temp + 1;
        return temp;
    }

    public synchronized long get() {
        return count;
    }

    public synchronized void increment() {
        count += 1;
    }
}