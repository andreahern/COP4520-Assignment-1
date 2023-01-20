public class assignment_1 {
    public static void main(String[] args) {
        Thread t1 = new MyThread();

        t1.run();
    }
}

class MyThread extends Thread {
    int[] values = { 6, 5, 1, 7, 8 };

    public void run() {
        for (int i = 0; i < 5; i++) {
            values[i] = values[i] * 2;
        }
        System.out.println("MY Values");
        System.out.println(values[0]);
    }
}
