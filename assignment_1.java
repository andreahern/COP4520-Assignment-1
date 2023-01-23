public class assignment_1 {
    public static final int THREAD_COUNT = 8;
    public static final long SEARCH_SPACE = 100_000_000;
    static SharedCounter counter = new SharedCounter(3);
    static SharedCounter primeCount = new SharedCounter(1);
    static SharedCounter primeSum = new SharedCounter(2);

    public static void main(String[] args) throws Exception {
        final long startTime = System.nanoTime();
        Thread[] threads = new Thread[THREAD_COUNT];

        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(new GetPrimes(counter, primeCount, primeSum));
            threads[i].start();
        }

        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i].join();
        }

        final double seconds = (double) (System.nanoTime() - startTime) / 1_000_000_000;

        System.out.printf("Runtime:\t%.2f secs\tTotal Primes:\t%d\tPrime Sum:\t%d%n", seconds, primeCount.get(),
                primeSum.get());
    }
}

class GetPrimes implements Runnable {
    SharedCounter counter;
    SharedCounter primeCount;
    SharedCounter primeSum;

    public GetPrimes(SharedCounter counter, SharedCounter primeCount, SharedCounter primeSum) {
        this.counter = counter;
        this.primeCount = primeCount;
        this.primeSum = primeSum;
    }

    private boolean isPrime(long n) {
        if (n == 3)
            return true;
        if ((n & 1) == 0 || n % 3 == 0)
            return false;
        long sqrtN = (long) Math.sqrt(n) + 1;
        for (long i = 6L; i <= sqrtN; i += 6) {
            if (n % (i - 1) == 0 || n % (i + 1) == 0)
                return false;
        }
        return true;
    }

    public void run() {
        long num;
        while ((num = counter.getAndIncrement(2)) <= assignment_1.SEARCH_SPACE) {
            if (isPrime(num)) {
                primeCount.increment(1);
                primeSum.increment(num);
            }
        }
    }
}

class SharedCounter {
    private long count;

    public SharedCounter(long start) {
        this.count = start;
    }

    public synchronized long getAndIncrement(int incr) {
        long temp = count;
        count = temp + incr;
        return temp;
    }

    public synchronized long get() {
        return count;
    }

    public synchronized void increment(long incr) {
        count += incr;
    }
}