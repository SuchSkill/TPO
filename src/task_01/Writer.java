package task_01;

import java.io.*;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

/**
 * Created by Eugene on 04-Mar-17.
 */
public class Writer {


    public static void main(String[] args) throws IOException, InterruptedException {
        RandomAccessFile file = new RandomAccessFile(Utils.PATH, "rw");
        FileChannel channel = file.getChannel();

        // mapping file
        MappedByteBuffer buf;
        buf = channel.map(
                FileChannel.MapMode.READ_WRITE,  // read-write mode
                0,  // starting from the beginning of the file
                8*4  // map the whole file
        );

        // getting a view of the buffer
        IntBuffer ibuf = buf.asIntBuffer();
        Random rand = new Random();

        ibuf.put(0, 0);
        ibuf.put(1, 0);
        ibuf.put(2, rand.nextInt());
        ibuf.put(3, rand.nextInt());
        while (true) {
            int isStop = ibuf.get(0);
            if (isStop == Utils.STOP) break;

            int lastAction = ibuf.get(1);
            if (lastAction == Utils.READ) {
                ibuf.put(2, rand.nextInt());
                ibuf.put(3, rand.nextInt());
                ibuf.put(1, Utils.WRITE);
                if (rand.nextInt() % 20 == 0)
                    ibuf.put(0, Utils.STOP);

            }
            Thread.sleep(Utils.SLEEP);
        }

        // reflect changes to the file
        buf.force();

        channel.close();

    }
}
