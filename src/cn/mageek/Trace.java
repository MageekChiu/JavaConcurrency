package cn.mageek;

import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Trace{
    static class DivTask implements Runnable{
        int a,b;
        DivTask(int a,int b){
            this.a= a;this.b = b;
        }
        @Override
        public void run() {
            double re = a/b;
            System.out.println(re);
        }
    }

    public static  void  main(String[] args) throws InterruptedException {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(0,Integer.MAX_VALUE,0L, TimeUnit.SECONDS,new SynchronousQueue<Runnable>());
        for (int i=0;i<5;i++){
            // 没有异常提示
//            pool.submit(new DivTask(100,i));
            // 有异常提示
//            pool.execute(new DivTask(100,i));
            // 有异常提示
            Future re = pool.submit(new DivTask(100,i));
            try {
                re.get();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
