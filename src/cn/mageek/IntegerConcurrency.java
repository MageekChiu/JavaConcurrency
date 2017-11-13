package cn.mageek;

public class IntegerConcurrency implements Runnable {
    private static Integer i = 0;
    private static IntegerConcurrency instance = new IntegerConcurrency();

    @Override
    public void run() {
        for (int j=0;j<1000;j++){
            // 偶尔正确
//            synchronized (i){
//                i++;
//            }
            // 一直正确
            synchronized (instance){
                i++;
            }
        }
    }

    public static  void  main(String[] args) throws InterruptedException {
        Thread thread = new Thread(instance);
        Thread thread1 = new Thread(instance);
        thread.start();thread1.start();
        thread.join();thread1.join();
        System.out.println(i);
    }

}
