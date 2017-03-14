package task_02;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;

public class Server {

    private ServerSocketChannel ssc = null;
    private Selector selector = null;

    public Server(String host, int port ) {
        try {
            // Utworzenie kanału dla gniazda serwera
            ssc = ServerSocketChannel.open();

            // Tryb nieblokujący
            ssc.configureBlocking(false);

            // Ustalenie adresu (host+port) gniazda kanału
            ssc.socket().bind(new InetSocketAddress(host, port));

            // Utworzenie selektora
            selector = Selector.open();

            // Zarejestrowanie kanału do obsługi przez selektor
            // dla tego kanału interesuje nas tylko nawiązywanie połączeń
            // tryb - OP_ACCEPT
            ssc.register(selector,SelectionKey.OP_ACCEPT);

        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(1);
        }
        System.out.println("Server started and ready for handling requests");
        serviceConnections();
    }

    private void serviceConnections() {
        boolean serverIsRunning = true;

        while(serverIsRunning) {
            try {
                // Wywołanie blokujące
                // czeka na zajście  zdarzenia związanego z kanałami
                // zarejestrowanymi do obslugi przez selektor
                selector.select();

                // Coś się wydarzyło na kanałach
                // Zbiór kluczy opisuje zdarzenia
                Set keys = selector.selectedKeys();

                Iterator iter = keys.iterator();
                while(iter.hasNext()) {   // dla każdego klucza

                    SelectionKey key = (SelectionKey) iter.next(); // pobranie klucza
                    iter.remove();                                 // usuwamy, bo już
                    // go zaraz obsłużymy

                    if (key.isAcceptable()) { // an incoming connection (i.e. TCP handshake)

                        // getting a channel to communicate with the particular client
                        // after TCP handshake is finished
                        // accept jest nieblokujące, bo już klient się zgłosił
                        SocketChannel cc = ssc.accept();

                        // configure connection with particular client as non-blocking
                        cc.configureBlocking(false);

                        // rejestrujemy kanał komunikacji z klientem
                        // do obsługi przez selektor
                        // - typ zdarzenia - dane gotowe do czytania przez serwer
                        cc.register(selector, SelectionKey.OP_READ);
                        continue;
                    }

                    if (key.isReadable()) {  // któryś z kanałów gotowy do czytania
                        // Uzyskanie kanału na którym czekają dane do odczytania
                        SocketChannel cc = (SocketChannel) key.channel();
                        serviceRequest(cc);    // obsluga zlecenia
                        continue;
                    }
                }
            } catch(Exception exc) {
                exc.printStackTrace();
                continue;
            }
        }
    }

    private static Pattern reqPatt = Pattern.compile(" +", 3);

    private static String msg[] = { "Ok", "Invalid request", "Not found",
            "Couldn't add - entry already exists",
            "Couldn't replace non-existing entry",
    };

    // Strona kodowa do kodowania/dekodowania buforów
    private static Charset charset  = Charset.forName("ISO-8859-2");
    private static final int BSIZE = 1024;

    // byte buffer to read data from channel
    private ByteBuffer bbuf = ByteBuffer.allocate(BSIZE);

    // for storing request to handle
    private StringBuffer reqString = new StringBuffer();

    // handling single incoming request
    private void serviceRequest(SocketChannel sc) {
        if (!sc.isOpen()) {
            return;                    // if channel is closed there is nothing we could do
        }

        // reading the request
        reqString.setLength(0);
        bbuf.clear();
        try {
            readLoop:                    // reading from channel is non-blocking
            while (true) {               // we continue until we reach the EOL
                int n = sc.read(bbuf);
                if (n > 0) {
                    bbuf.flip();
                    CharBuffer cbuf = charset.decode(bbuf);
                    while(cbuf.hasRemaining()) {
                        char c = cbuf.get();
                        if (c == '\r' || c == '\n') break readLoop;
                        reqString.append(c);
                    }
                }
            }
            // processing of the incoming request and then calling
            // writeResp method responsible for writing response to the channel
            String[] req = reqPatt.split(reqString, 3);
            String cmd = req[0];
            String content = reqString.subSequence(cmd.length()+1, reqString.length()).toString();

            if (cmd.equals("echo")) {             // the end of the conversation
                writeResp(sc, content);
//                sc.close();                      // close the channel
//                sc.socket().close();             // close the underlying socket
            }
            else if (cmd.equals("add")) {
                int result = Integer.parseInt(req[1]) + Integer.parseInt(req[2]);
                writeResp(sc, result+"");
//                sc.close();                      // close the channel
//                sc.socket().close();             // close the underlying socket
            }
            else writeResp(sc, null);             // not supported request

        } catch (Exception exc) {                  // handling of raised errors
            exc.printStackTrace();                 // e.g. broken connection
            try { sc.close();
                sc.socket().close();
            } catch (Exception e) {}
        }
    }

    // for holing response message
    private StringBuffer remsg = new StringBuffer();

    private void writeResp(SocketChannel sc, String addMsg)
            throws IOException {
        remsg.setLength(0);
        if (addMsg != null) {
            remsg.append(addMsg);
            remsg.append('\n');
        }
        ByteBuffer buf = charset.encode(CharBuffer.wrap(remsg));
        sc.write(buf);
    }

    public static void main(String[] args) {
        try {
            String host = args[0];
            int port = Integer.parseInt(args[1]);

            new Server(host, port);
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(1);
        }
    }

}