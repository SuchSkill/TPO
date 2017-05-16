package task_02;

import java.util.*;
import java.net.*;
import java.io.*;
/**
 * Created by Eugene on 15-Mar-17.
 */
public class ClientOld {
    public static void main(String[] args) throws IOException {
        // gniazdo do połączenia z serwerem
        Socket echoSocket = null;
        // strumień do zapisu do serwera
        PrintWriter out = null;
        BufferedReader in = null;
        // nazwa serwera
        String hostname = "localhost";
        if(args.length > 0) hostname=args[0];

        try {
            System.out.println("próba utworzenia gniazda");
            echoSocket = new Socket(hostname, 9999);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            System.out.println("próba utworzenia strumienia wyjściowego");
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            System.out.println("próba utworzenia strumienia wejściowego");
            // bufor do czytania z klawiatury
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            // string na dane od użytkownika
            String userInput;
            // w pętli - czytaj z klawiatury
            while ((userInput = stdIn.readLine()) != null) {
                // wyślij do serwera
                out.println(userInput);
                // odczytaj odpowiedź i wypisz
                System.out.println("response from server: " + in.readLine());
            }
            stdIn.close();
        } catch (UnknownHostException e) {
            System.err.println("Nieznany host: " + hostname + ".");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Błąd połączenia z " + hostname + "." + e);
            System.exit(1);
        }



        // zakończenie pracy - pozamykaj strumienie i gniazda
        out.close();
        in.close();
        echoSocket.close();
    }
}
