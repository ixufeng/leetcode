import co.paralleluniverse.fibers.Fiber;

/**
 * @author xufeng
 * Create Date: 2020-08-28 21:39
 **/
public class Test {
    public static void main(String[] args) {
        new Fiber<String>() {
            @Override
            protected String run() {
                System.out.println("hello quasar");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "hello world";
            }
        }.start();

        new Fiber<String>() {
            @Override
            protected String run() {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("hello quasar1");
                return "hello world";
            }
        }.start();

        new Fiber<String>() {
            @Override
            protected String run() {
                System.out.println("hello quasar2");
                return "hello world";
            }
        }.start();

    }


}
