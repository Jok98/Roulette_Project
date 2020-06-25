package r_server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client_Server_int extends Remote {

	public void notify_client() throws RemoteException;
	
}
