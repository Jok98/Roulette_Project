package r_client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client_main {
	
	
	public static void main (String[] args) throws RemoteException {
		
		Client_thread th= 	new Client_thread(1,50);
		th.start();
		
		
	}

}
