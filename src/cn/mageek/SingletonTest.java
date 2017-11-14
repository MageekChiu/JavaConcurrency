package cn.mageek;

/**
 * 三种单例
 * 双重检查很丑陋，且效率不高，所以不讨论
 */
public class SingletonTest {
    public static void main(String[] args) {
        SingletonOrdinary singletonOrdinary = SingletonOrdinary.getInstance();
        singletonOrdinary = SingletonOrdinary.getInstance();

        SingletonLazy singletonLazy = SingletonLazy.getInstance();
        singletonLazy = SingletonLazy.getInstance();

        SingletonHolder singletonHolder = SingletonHolder.getInstance();
        singletonHolder = SingletonHolder.getInstance();
    }
}
/**
 * 最简单的单例，正确并良好
 * 但是不太好精确控制单例的生成时间
 * 如果这个类还有其他的静态字段A，一旦引用A就会导致单例生成
 */
class SingletonOrdinary{
    private SingletonOrdinary(){
        System.out.println("created SingletonOrdinary");
    }
    private static SingletonOrdinary instance = new SingletonOrdinary();

    public static SingletonOrdinary getInstance(){
        return instance;
    }
}

/**
 * 易于理解和实现
 * 但是一直加锁，在竞争激烈的情况下对性能会产生一定影响
 */
class SingletonLazy{

    private SingletonLazy(){
        System.out.println("created SingletonLazy");
    }
    private static SingletonLazy instance = null;

    public static synchronized SingletonLazy getInstance(){
        if (instance==null){
            instance = new SingletonLazy();
        }
        return instance;
    }
}

/**
 * 结合了前面两者共同的优点
 * 没有加锁，而且延迟加载
 * 利用虚拟机的类初始化创建单例
 */
class  SingletonHolder{
    private SingletonHolder(){
        System.out.println("created SingletonHolder");
    }
    private static class Holder{
        private static SingletonHolder instance = new SingletonHolder();
    }
    public static SingletonHolder getInstance(){
        return Holder.instance;
    }
}



