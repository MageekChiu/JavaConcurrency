package cn.mageek;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HashMapConcurrency {

    // 偶尔能够正常运行
//    static Map<String,String> map = new HashMap<>();

    // 一直能够正常运行 性能依次递增
    static Map<String,String> map = new Hashtable<>();
//    static Map<String,String> map = Collections.synchronizedMap(new HashMap<>());
//    static Map<String,String> map = new ConcurrentHashMap<>();

    public static class AddThread implements Runnable{

        int start = 0;
        AddThread(int start){
            this.start = start;
        }
        @Override
        public void run() {
            for (int i=start;i<1000;i+=2){
                map.put(Integer.toString(i),Integer.toBinaryString(i));
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new AddThread(0));
        Thread t2 = new Thread(new AddThread(1));
        t1.start();t2.start();
        t1.join();t2.join();
        System.out.println(map.size());
    }

}
