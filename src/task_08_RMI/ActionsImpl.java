package task_08_RMI;

import task_08_RMI.add.AddRequest;
import task_08_RMI.add.AddResponse;
import task_08_RMI.echo.EchoRequest;
import task_08_RMI.echo.EchoResponse;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ActionsImpl extends UnicastRemoteObject implements Actions {

    public static void main(String[] args){

        try {
            Registry reg = LocateRegistry.createRegistry(2000);
            ActionsImpl ac = new ActionsImpl();
            reg.bind("action", ac);
        }
        catch(Exception e){
            System.err.println(e);
        }
    }


    protected ActionsImpl() throws RemoteException {
    }


    @Override
    public EchoResponse echo(EchoRequest echoRequest) throws RemoteException {
        String message = echoRequest.getMessage();
        System.out.println("message get by server side = " + message);
        return new EchoResponse(message);
    }

    @Override
    public AddResponse add(AddRequest addRequest) throws RemoteException {
        Long sum = 0l;
        for (Integer integer : addRequest.getDigitsToAdd()) {
            sum +=integer;
        }
        return new AddResponse(sum);
    }
}
