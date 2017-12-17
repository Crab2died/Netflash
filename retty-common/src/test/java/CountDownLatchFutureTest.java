import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDownLatchFutureTest {

    @Test
    public void awaitTest(){

        int threadNum = 3;
        final CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 0; i < threadNum; i++){
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(3 + finalI);
                        System.out.println("线程--" + finalI + "---调用");
                        countDownLatch.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        try {
            countDownLatch.await(15L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
