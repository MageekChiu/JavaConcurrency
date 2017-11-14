package cn.mageek;

import java.util.concurrent.*;

/**
 * Java内置的future模式测试
 *
 */
public class FutureInnerTest {
    public static void main(String[] args) {

        FutureTask<String> futureTask = new FutureTask<String>(new RealDataInner("aaaaa"));
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.submit(futureTask);

        System.out.println("请求完毕");

        try {
            // 模拟其他耗时操作
            Thread.sleep(3000);
            System.out.println("其他操作完毕");
            // 会阻塞
            System.out.println(futureTask.get());

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


    }
}


class RealDataInner implements Callable<String>{

    private final String result;

    RealDataInner(String result) {
        this.result = result;
    }

    @Override
    public String call() throws Exception {
        // 模拟真实数据产生耗时
        Thread.sleep(5000);
        return result;
    }
}
