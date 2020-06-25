package r_server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;

public class Client_main extends UnicastRemoteObject implements Client_Server_int {
	static Client_thread th;
 protected Client_main() throws RemoteException {
		super();

	}

static Registry registry;
	public static void main (String[] args) throws RemoteException, NotBoundException {
		String host = (args.length < 1) ? null : args[0];
		Client_thread th= 	new Client_thread(1,50,host);
		Client_main c_s = new Client_main();
		registry = LocateRegistry.getRegistry();
		registry.rebind("CS", c_s);
		

		th.start();
		
		
	}
	
	@Override
	public void notify_client() throws RemoteException {
		synchronized(th.sem){th.sem.notifyAll();}
	}

}
