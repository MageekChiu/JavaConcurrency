package cn.mageek;

/**
 * future模式测试
 *
 */
public class FutureTest {
    public static void main(String[] args) {
        Client client = new Client();
        Data data = client.dataRequest("dsfdsfdsfdsfdsfds");
        System.out.println("请求完毕");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("过了一会，此时数据："+data.getResult());
    }

}

interface Data{
    String getResult();
}

class FutureData implements Data{

    protected RealData realData = null;
    protected boolean isReady = false;

    public RealData getRealData() {
        return realData;
    }

    public synchronized void setRealData(RealData realData) {
        if (isReady){
            return;
        }
        this.realData = realData;
        isReady = true;
        notifyAll();
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }


    @Override
    public synchronized String getResult() {
        while (!isReady){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return realData.getResult();
    }
}

class RealData implements  Data{

    protected final String result;

    RealData(String result) {
        this.result = result;
        // 模拟真实数据产生的耗时
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getResult() {
        return result;
    }
}

class Client {
    public Data dataRequest(final String query){
        final FutureData futureData = new FutureData();
        new Thread(() -> {
            RealData realData = new RealData(query);
            futureData.setRealData(realData);
        }).start();
        return futureData;
    }
}