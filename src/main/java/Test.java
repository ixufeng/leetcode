import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xufeng
 * Create Date: 2020-08-28 21:39
 **/
public class Test {

    private static AtomicInteger count = new AtomicInteger(0);

    static ExecutorService service = new ThreadPoolExecutor(10, 20,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(true),
            new ThreadFactory() {
                private int index = 0;

                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("test-thread-pool-" + ++index);
                    return thread;
                }
            });

    public static void main(String[] args) {
        int index = 0;
        while (true) {
            index++;
            try {
                service.submit(() -> {
                    trySleep(1000);
                    System.out.println("do task " + count.incrementAndGet());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }


            System.out.println("任务数量：" + index);
            trySleep(10);
        }


    }


    public static void trySleep(long mi) {
        try {
            Thread.sleep(mi);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
