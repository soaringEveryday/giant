import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Tread pool util
 * Created by cht on 2018/4/4.
 */
public class ThreadPoolHolder {
    private static ThreadPoolHolder sInstance;
    private static final int CORE_SIZE = Runtime.getRuntime().availableProcessors();
    private static volatile ThreadPoolExecutor mThreadPoolExecutor;

    public static ThreadPoolHolder getInstance() {
        if (sInstance == null) {
            synchronized (ThreadPoolHolder.class) {
                if (sInstance == null) {
                    sInstance = new ThreadPoolHolder();
                }
            }
        }
        return sInstance;
    }

    private ThreadPoolHolder() {
        mThreadPoolExecutor = new ThreadPoolExecutor(
                CORE_SIZE,
                CORE_SIZE * 2,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>()
        );
    }

    public void execute(Runnable task) {
        if (task != null) {
            mThreadPoolExecutor.execute(task);
        }
    }

    public void shutdown() {
        mThreadPoolExecutor.shutdown();
    }

    public boolean isTerminated(){
        return mThreadPoolExecutor.isTerminated();
    }

    public static class Job implements Runnable {
        protected long start, end;

        public Job(long start, long end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {

        }
    }
}
