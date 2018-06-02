package cn.mageek;
import sun.misc.Contended;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Mageek Chiu
 * @date 2018/6/2 0002:11:10
 */
public class FalseSharing {

    private static AtomicLong time = new AtomicLong(0);

    public static void main(String... args) throws InterruptedException {
        int testNum = 50;
        for (int i = 0 ; i< testNum;i++){// 测试50次
            Thread thread = new Thread(new Job());
            thread.start();
            thread.join();
        }
        System.out.println(time.get()/1000/testNum + " us,avg");
    }

    static class Job implements Runnable{
        @Override
        public void run() {
            int number = 8;
            int iterationNumber = 20000;
            CountDownLatch countDownLatch = new CountDownLatch(number);
            Obj[] objArray = new Obj[number];
            for (int i = 0;i < number;i++) {
                objArray[i] = new Obj();
            }

            long start = System.nanoTime();
            for (int i = 0;i < number;i++){
                int ii = i;
                Thread thread = new Thread(new Runnable() {
                    int iterationNumberInner = iterationNumber;
                    @Override
                    public void run() {
                        while (iterationNumberInner-->0){
                            objArray[ii].aLong+=1L;
                        }
                        countDownLatch.countDown();
                    }
                });
                thread.start();
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long end = System.nanoTime();
            time.getAndAdd(end-start);

        }
    }

    @Contended
    private static final class Obj{
        private volatile long aLong = 8L;//8Bytes
        private volatile long a=2L,b=2L,c=2L,d=2L,e=2L,f=2L,g=2L;
    }
}

// un Contented
/*

*/
// Contented
/*

*/





