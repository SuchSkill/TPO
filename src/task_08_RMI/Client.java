package task_08_RMI;

import task_08_RMI.add.AddRequest;
import task_08_RMI.add.AddResponse;
import task_08_RMI.echo.EchoRequest;
import task_08_RMI.echo.EchoResponse;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Client {

    public static void main(String[] args){
        try {
            Registry registry = LocateRegistry.getRegistry(2000);
            Actions  remoteObject = (Actions)registry.lookup("action");


            String message = "hello world";
            System.out.println("message to send = " + message);
            EchoRequest echoRequest = new EchoRequest(message);
            EchoResponse echo = remoteObject.echo(echoRequest);
            String remoteMessage = echo.getMessage();
            System.out.println("remoteMessage = " + remoteMessage);


            ArrayList<Integer> integers = new ArrayList<>();
            integers.add(5);
            integers.add(6);

            AddRequest addRequest = new AddRequest(integers);
            AddResponse addResponse = remoteObject.add(addRequest);
            Long result = addResponse.getResult();
            System.out.println("result of addition = " + result);


        }
        catch(Exception e){
            System.err.println(e);
        }
    }
}