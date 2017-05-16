package task_08_RMI;

import task_08_RMI.add.AddRequest;
import task_08_RMI.add.AddResponse;
import task_08_RMI.echo.EchoRequest;
import task_08_RMI.echo.EchoResponse;

import java.rmi.*;

public interface Actions extends Remote {

    EchoResponse echo(EchoRequest echoRequest) throws RemoteException;

    AddResponse add(AddRequest addRequest) throws RemoteException;
}