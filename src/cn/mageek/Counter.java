package cn.mageek;

/**
 * @author Mageek Chiu
 * @date 2018-01-12:9:13
 */
import java.util.concurrent.TimeUnit;

public class Counter {

//    private static  boolean stop ;
////    private static volatile boolean stop ;
//    public static void main(String[] args) throws Exception {
//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                int i = 0;
//                while (!stop) {
//                    i++;
//                }
//            }
//        } );
//        t.start();
//
//        TimeUnit.MILLISECONDS .sleep(5);
//        stop = true;
//    }

    /****************************/

    private static boolean ready;
    private static int number;

    private static class ReaderThread extends Thread {
        @Override
        public void run() {
            int i=0;
            while (!ready) {
                i++;
//                Thread.yield();
            }
            System.out.println(number);
        }
    }

    public static void main(String[] args) throws Exception {
        new ReaderThread().start();
        TimeUnit.MILLISECONDS .sleep(9);
        number = 42;
        ready = true;
    }

}
