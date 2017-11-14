package cn.mageek;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * @description: Fork/Join Test
 * @author: Mageek Chiu
 * @date: 2017-11-13:15:15
 */
public class CountTask  extends RecursiveTask<Long>{
    private static final int THRESHOLD = 10000;

    private long start;
    private long end;

    private CountTask(long start, long end) {
        this.start = start;
        this.end = end;
    }


    @Override
    protected Long compute() {
        long sum = 0;
        boolean canCompute = (end - start) < THRESHOLD;
        if (canCompute) {
//            for (long i = start; i <= end; i++) {
//                sum += i;
//            }
            // rangeClosed包含最后的结束节点，range不包含。
            sum = LongStream.rangeClosed(start,end).sum();
        } else {
            long step = (start + end) / 100;

            ArrayList<CountTask> subTasks = new ArrayList<>();
            long pos = start;

            for (int i = 0; i < 100; i++) {
                long lastOne = pos + step;
                if (lastOne > end) {
                    lastOne = end;
                }
                CountTask subTask = new CountTask(pos, lastOne);
                pos += step + 1;
                subTasks.add(subTask);
                subTask.fork();
            }

            for (CountTask t : subTasks) {
                sum += t.join();
            }
        }


        return sum;
    }


    public static void main(String[] args) {
        Long end = 200000L;
        long start = System.nanoTime();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        CountTask task = new CountTask(0, end);
        ForkJoinTask<Long> result = forkJoinPool.submit(task);
        long res;
        try {
            res = result.get();
            System.out.println("sum=" + res);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println((System.nanoTime()-start)/10_000);
//        作为比较 发现数据量小了多线程不能体现优势
        res = 0;
        start = System.nanoTime();
        for (int i = 0;i<=end;i++){
            res +=i;
        }
        System.out.println("res=" + res);
        System.out.println((System.nanoTime()-start)/10_000);
    }
}

