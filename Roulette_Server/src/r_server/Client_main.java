package r_server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;

public class Client_main  {
	static Client_thread th;
 protected Client_main() throws RemoteException {
		super();

	}

static Registry registry;

	public static void main (String[] args) throws RemoteException, NotBoundException, InterruptedException {
		String host = (args.length < 1) ? null : args[0];
		
		/*Client_main c_s = new Client_main();
		registry = LocateRegistry.getRegistry();
		registry.rebind("CS", c_s);*/
		Client_thread th[] = new Client_thread[5];
        for(int i=0; i<5; i++){
        	th[i] = new Client_thread(i, host, 50);
        	th[i].start();
        }
		
	}
	
	

}
