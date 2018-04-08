import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * main process
 *
 * Created by cht on 2018/4/4.
 */
public class GiantFileController {

    private static final long BLOCK_SIZE = 10240;

    @Setter
    private String source;
    @Setter
    private String dest;

    private RandomAccessFile fileOut;
    private FileChannel fcIn;
    private FileChannel fcOut;

    private long totalSize = 0;
    private int threadCount = 0;

    public GiantFileController init() throws IOException{
        RandomAccessFile fileIn = new RandomAccessFile(this.source, "r");
        fileOut = new RandomAccessFile(this.dest, "rw");
        fcIn = fileIn.getChannel();
        fcOut = fileOut.getChannel();

        totalSize = fcIn.size();
        threadCount = (int) (totalSize / BLOCK_SIZE);
        fileOut.setLength(totalSize);
        return this;
    }

    public void start() throws IOException {
        System.out.println("started ===> ");
        for (int i = 0 ; i < threadCount ; i++) {
            ThreadPoolHolder.Job job = new ThreadPoolHolder.Job(i * BLOCK_SIZE, (i + 1) * BLOCK_SIZE) {

                @Override
                public void run() {
                    super.run();
                    try {
                        MappedByteBuffer mappedByteBufferIn = fcIn.map(FileChannel.MapMode.READ_ONLY, start, BLOCK_SIZE);
                        byte[] reverse = new byte[(int) BLOCK_SIZE];
                        int j = 0;
                        for (long i = BLOCK_SIZE - 1; i >= 0; i--) {
                            reverse[j++] = mappedByteBufferIn.get((int) i);
                        }
                        fcOut.write(ByteBuffer.wrap(reverse), totalSize - end);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            ThreadPoolHolder.getInstance().execute(job);
        }

        // 不能整除部分
        long left = totalSize % BLOCK_SIZE;
        if (left > 0) {
            MappedByteBuffer mappedByteBufferIn = fcIn.map(FileChannel.MapMode.READ_ONLY, threadCount * BLOCK_SIZE, left);
            byte[] reverse = new byte[(int) left];
            int j = 0;
            for (long i = left - 1; i >= 0; i--) {
                reverse[j++] = mappedByteBufferIn.get((int) i);
            }
            fcOut.write(ByteBuffer.wrap(reverse), 0);
        }

    }

    public void destroy() throws IOException {
        System.out.println("finished <===");
        fileOut.close();
        fcIn.close();
        fcOut.close();
    }

}
