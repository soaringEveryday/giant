import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        String sourcePath = "/Users/cht/dev/java/GiantFile/file/test.txt";
        String destPath = "/Users/cht/dev/java/GiantFile/file/out.txt";
        long start = 0;
        try {
            start = System.currentTimeMillis();
            GiantFileController controller = new GiantFileController();
            controller.setSource(controller.getClass().getResource("/").getPath() + "/test.txt");
            controller.setDest(controller.getClass().getResource("/").getPath() + "/out.txt");
//            controller.setSource(sourcePath);
//            controller.setDest(destPath);
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
