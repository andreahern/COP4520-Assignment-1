
public class assignment_1 {
    static final int THREAD_COUNT = 8;
    static final long SEARCH_SPACE = 10000000000L;

    public static void main(String[] args) throws Exception {
        Thread[] threads = new Thread[THREAD_COUNT];

        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(new GetPrimes(i));
            threads[i].start();
        }

        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i].join();
        }
    }
}

class GetPrimes implements Runnable {
    final int ID;

    public GetPrimes(int ID) {
        this.ID = ID;
    }

    public void run() {
        for (long i = 0; i < assignment_1.SEARCH_SPACE; i++) {
            if (i % assignment_1.THREAD_COUNT == ID) {
                // Do STUFF
            }
        }
    }
}