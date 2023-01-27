import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class assignment_1 {
    public static final int THREAD_COUNT = 8;
    public static final int SEARCH_SPACE = 100_000_000;
    static SharedCounter primeCount = new SharedCounter(0);
    static SharedCounter primeSum = new SharedCounter(0);
    static ArrayList<Long> largestPrimes = new ArrayList<>(10);

    public static void main(String[] args) throws Exception {
        final long startTime = System.nanoTime();
        final int rangeMult = SEARCH_SPACE / THREAD_COUNT;
        File file = new File("primes.txt");
        PrintWriter writer = new PrintWriter(file);

        Thread[] threads = new Thread[THREAD_COUNT];

        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(
                    new GetPrimes(i + 1, rangeMult * i == 0 ? 2 : rangeMult * i, rangeMult * (i + 1), primeCount,
                            primeSum));
            threads[i].start();
        }

        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i].join();
        }

        String largest_10 = largestPrimes.toString();
        final double seconds = (double) (System.nanoTime() - startTime) / 1_000_000_000;

        writer.printf("%.2f %d %d%n", seconds, primeCount.get(),
                primeSum.get());
        writer.println(largest_10);
        writer.close();
    }

    public static synchronized void addPrime(long prime) {
        largestPrimes.add(0, prime);
    }
}

class GetPrimes implements Runnable {
    int ID;
    int low;
    int high;
    SharedCounter primeCount;
    SharedCounter primeSum;

    public GetPrimes(int ID, int low, int high, SharedCounter primeCount, SharedCounter primeSum) {
        this.ID = ID;
        this.low = low;
        this.high = high;
        this.primeCount = primeCount;
        this.primeSum = primeSum;
    }

    public static void addPrimes(ArrayList<Integer> primes, int high) {
        boolean[] sieve = new boolean[high - 1];
        Arrays.fill(sieve, true);

        for (int i = 2; i * i <= high; i++) {
            if (sieve[i - 2] == true) {
                int highSqrt = (int) Math.sqrt(high);
                for (int j = i * i; j <= highSqrt; j += i) {
                    sieve[j - 2] = false;
                }
            }
        }
        for (int i = 2; i * i <= high; i++) {
            if (sieve[i - 2] == true) {
                primes.add(i);
            }
        }
    }

    public void run() {
        ArrayList<Integer> primes = new ArrayList<Integer>();
        addPrimes(primes, high);

        boolean[] res = new boolean[high - low + 1];
        Arrays.fill(res, true);

        for (int prime : primes) {
            int firstMult = (low / prime);
            if (firstMult <= 1) {
                firstMult = prime + prime;
            } else if (low % prime != 0) {
                firstMult = (firstMult * prime) + prime;
            } else {
                firstMult = firstMult * prime;
            }
            for (int j = firstMult; j <= high; j += prime) {
                res[j - low] = false;
            }
        }

        int biggestPrimesCount = 0;
        for (int i = high; i >= low; i--) {
            if (res[i - low] == true) {
                primeCount.increment(1);
                primeSum.increment(i);
                if (ID == assignment_1.THREAD_COUNT && biggestPrimesCount++ < 10) {
                    assignment_1.addPrime(i);
                }
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