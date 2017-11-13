package cn.mageek;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @description: Fork/Join Test
 * @author: Mageek Chiu
 * @date: 2017-11-13:15:15
 */
public class CountTask  extends RecursiveTask<Long>{
    private static final  int THRESHOLD = 1000;

    private long start;
    private long end;

    private CountTask(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public Long compute() {
        long sum = 0;
        boolean canCompute = (end-start)<THRESHOLD;
        if(canCompute){
            for(long i = start;i<=end;i++){
                sum +=i;
            }
        }else{
            long step = (start+end)/100;
            ArrayList<CountTask> subTasks = new ArrayList<>();
            long pos = start;
            for (int i=0;i<100;i++){
                long lastOne = pos+step;
                if (lastOne>end) {
                    lastOne = end;
                }
                CountTask subTask = new CountTask(pos, lastOne);
                pos += step+1;
                subTasks.add(subTask);
                subTask.fork();
            }
            for (CountTask t:subTasks){
                sum +=t.join();
            }
        }
        return sum;
    }

    public static void main(String[] args){
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        CountTask task = new CountTask(0,200_000L);
        ForkJoinTask<Long> result = forkJoinPool.submit(task);
        try {
            System.out.println("开始："+System.nanoTime());
            long res = result.get();
            System.out.println("结束："+System.nanoTime()+",结果："+res);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }
}
