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
	public static void main (String[] args) throws RemoteException, NotBoundException, InterruptedException {
		String host = (args.length < 1) ? null : args[0];
		
		Client_main c_s = new Client_main();
		registry = LocateRegistry.getRegistry();
		registry.rebind("CS", c_s);
		Client_thread th[] = new Client_thread[3];
        for(int i=0; i<2; i++){
        	th[i] = new Client_thread(i, host, 50);
        	th[i].start();
        }
        /*
        for(int i=0; i<2; i++){
        	th[i].join();;
           
        }*/
		
	}
	
	@Override
	public void notify_client() throws RemoteException {
		synchronized(th.sem){th.sem.notifyAll();}
	}

	@Override
	public void close_bet() throws RemoteException {
		System.out.println("Server scommesse chiuso!");
		System.exit(1);
		
	}

}
