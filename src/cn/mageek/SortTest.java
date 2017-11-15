package cn.mageek;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @description:
 * @author: Mageek Chiu
 * @date: 2017-11-14:17:36
 */
public class SortTest {
    public static void main(String[] args) {
        // 快速生成count个数据，随机数组
        for (int count = 1000;count<100000000L;count *=8){
            List<Double> doubleList =  Stream.generate(Math::random).limit(count).collect(Collectors.toList());
            System.out.println(count+"最大值:"+Collections.max(doubleList)+",最小值:"+Collections.min(doubleList));
            long start = System.nanoTime();
            List<Double> doubleList1 = doubleList.parallelStream().sorted().collect(Collectors.toList());
            System.out.println("并行时间："+(System.nanoTime()-start)/100_000);
            start = System.nanoTime();
            doubleList.sort(Double::compareTo);
            System.out.println("串行时间："+(System.nanoTime()-start)/100_000);
            // 随机挑几个 检验一下
            System.out.println(count+"正确性校验:"+(doubleList.get(0).compareTo(doubleList1.get(0)))+(doubleList.get(336).compareTo(doubleList1.get(336)))+(doubleList.get(count-1).compareTo(doubleList1.get(count-1))));
        }

    }
}
