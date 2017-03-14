package task_02;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 * Created by Eugene on 14-Mar-17.
 */
public class Client {
    public static void main(String[] args) throws Exception {
        try {
            // create a socket
            String serverHost = "localhost" ;   // server hostname or IP adress
            int serverPort = 9999 ;      // server port number
            Socket socket = new Socket(serverHost, serverPort);

            // get the streams
            OutputStream sockOut = socket.getOutputStream();
            InputStream sockIn = socket.getInputStream();

            // Communicate:
//            sockOut.write();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()
                    )
            );
            // send a request to the server

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

            // communication completed
            // close streams and the socket
            sockOut.close();
            sockIn.close();
            socket.close();

        } catch (UnknownHostException exc) {
            // unknown host
        } catch (SocketException exc) {
            // socket-related exceptions
        } catch (IOException exc) {
            // input-output exceptions
        }
    }
}
