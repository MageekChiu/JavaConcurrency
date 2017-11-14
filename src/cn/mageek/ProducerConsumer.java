package cn.mageek;


import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生产者消费者测试
 *
 *
 */
public class ProducerConsumer {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<PCData> queue = new LinkedBlockingDeque<>();

        Producer producer = new Producer(queue);
        Producer producer1 = new Producer(queue);
        Producer producer2 = new Producer(queue);

        Consumer consumer = new Consumer(queue);
        Consumer consumer1 = new Consumer(queue);
        Consumer consumer2 = new Consumer(queue);

        ExecutorService executorService = Executors.newCachedThreadPool();

        executorService.execute(producer);
        executorService.execute(producer1);
        executorService.execute(producer2);
        executorService.execute(consumer);
        executorService.execute(consumer1);
        executorService.execute(consumer2);

        Thread.sleep(10*1000);

        producer1.stop();producer.stop();producer2.stop();

        Thread.sleep(3000);

        consumer.stop();consumer1.stop();consumer2.stop();

        executorService.shutdown();
    }
}

class PCData{
    private final int data;

    PCData(int data) {
        this.data = data;
    }

    public int getData() {
        return data;
    }

    @Override
    public String toString() {
        return "data:"+data;
    }
}

class Producer implements  Runnable{

    private volatile boolean isRunniing = true;
    private BlockingQueue<PCData> queue ;
    private static AtomicInteger count = new AtomicInteger();
    private static final int SLEEP_TIME = 1000;

    public Producer(BlockingQueue<PCData> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        PCData data = null;
        Random random = new Random();

        System.out.println("producer:"+Thread.currentThread().getId()+" start");
        try {
            while (isRunniing){
                Thread.sleep(random.nextInt(SLEEP_TIME));
                data = new PCData(count.incrementAndGet());
                System.out.println("producer:"+Thread.currentThread().getId()+" put to Queue data start "+data);
                if (!queue.offer(data,2, TimeUnit.SECONDS)){
                    System.out.println("producer:"+Thread.currentThread().getId()+" put to Queue data failed"+data);
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public void stop(){
        isRunniing = false;
    }
}

class Consumer implements  Runnable{

    private BlockingQueue<PCData> queue ;
    private static final int SLEEP_TIME = 1000;
    private volatile boolean isRunniing = true;


    public Consumer(BlockingQueue<PCData> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        Random random = new Random();

        System.out.println("Consumer:"+Thread.currentThread().getId()+" start");
        try {
            while (isRunniing){
                // 会一直阻塞
//                PCData data = queue.take();
                // 不会一直阻塞
                PCData data = queue.poll();

                if (data != null){
                    int re = data.getData()*data.getData();
                    System.out.println("Consumer:"+Thread.currentThread().getId()+" get result "+re);
                }
                Thread.sleep(random.nextInt(SLEEP_TIME));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public void stop(){
        isRunniing = false;
    }

}
