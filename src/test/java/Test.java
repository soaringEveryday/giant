import java.io.IOException;

/**
 * test cases
 * Created by cht on 2018/4/4.
 */
public class Test {

    @org.junit.Test
    public void testSmallFile() {
        test("/Users/cht/dev/java/giant/file/test.txt",
                "/Users/cht/dev/java/giant/file/out.txt");
    }

    @org.junit.Test
    public void testBigFile() {
        test("/Users/cht/dev/java/giant/file/test2.txt",
                "/Users/cht/dev/java/giant/file/out.txt");
    }

    @org.junit.Test
    public void testGiantFile() {
        test("/Users/cht/dev/java/giant/file/test3.txt",
                "/Users/cht/dev/java/giant/file/out.txt");
    }

    private void test(String sourcePath, String destPath) {

        try {
            long start = System.currentTimeMillis();
            GiantFileController controller = new GiantFileController();
            controller.setSource(sourcePath);
            controller.setDest(destPath);
            controller.init().start();

            ThreadPoolHolder.getInstance().shutdown();
            while (true) {
                if (ThreadPoolHolder.getInstance().isTerminated()) {
                    controller.destroy();
                    System.out.println("cost time " + (System.currentTimeMillis() - start) / 1000f +  "s");
                    break;
                }
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
