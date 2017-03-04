package task_01;


import java.io.*;
import java.nio.*;
import java.nio.channels.*;


/**
 * Created by Eugene on 04-Mar-17.
 */
public class Reader {
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

        while (true) {
            int isStop = ibuf.get(0);
            if (isStop == Utils.STOP) break;

            int lastAction = ibuf.get(1);
            if (lastAction == Utils.WRITE) {
                int a = ibuf.get(2);
                int b = ibuf.get(3);
                System.out.println(a+b);
                ibuf.put(1, Utils.READ);
            }
            buf.force();
            Thread.sleep(Utils.SLEEP);
        }
        channel.close();
    }
}
