package cn.mageek;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class ArrayListConcurrency {
    // 偶尔能够正常运行 得2000
//    static List<Integer> arrayList = new ArrayList<>(10);

    // 一直能够正常运行
//    static List<Integer> arrayList = new Vector<>(10);
    static List<Integer> arrayList = new CopyOnWriteArrayList<>();

    public static class AddThread implements Runnable{

        @Override
        public void run() {
            for (int i=0;i<1000;i++){
                arrayList.add(i);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new AddThread());
        Thread t2 = new Thread(new AddThread());
        t1.start();t2.start();
        t1.join();t2.join();
        System.out.println(arrayList.size());
    }

}
