package r_server;
import java.io.IOError;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class Server_main extends UnicastRemoteObject  implements Server_Client_int {
	private static final long serialVersionUID = 1L;
	static Boolean accesso;
	static int turn;
	static Client_Server_int c_s;
	protected Server_main() throws RemoteException {
		super();
		
	}

	static Registry registry;
	static HashMap<Integer,ArrayList<Integer>> bet_map = new HashMap<Integer,ArrayList<Integer>>();
	static HashMap<Integer,ArrayList<String>> obj_bet_map = new HashMap<Integer,ArrayList<String>>();
	
	public static void main (String[] args) throws RemoteException, NotBoundException {
		
		registry= LocateRegistry.createRegistry(1099);
		Server_main s_m = new Server_main();
		registry.rebind("SC", s_m);
		
		
		
		do {
		
		System.out.println("Nuovo giro di roulotte");
		//inizio accettazione scommesse
		System.out.println("Aste aperte, si accettano le puntate");
		accesso = true;
		
		try {
			Thread.sleep(5000);
		}
		catch(InterruptedException e) {
			
		}
		//fine accettazione scommesse
		accesso = false;
		System.out.println("Aste chiuse, aspettare il prossimo turno");
		
		//inizio estrazione
		System.out.println("Valore puntate : " + bet_map);
		System.out.println("Numeri puntati : " +obj_bet_map);
		//fine estrazione
		
		//Thread.sleep(5000);
		c_s = (Client_Server_int) registry.lookup("CS");
		c_s.notify_client();
		bet_map.clear();
		obj_bet_map.clear();
		System.out.println("-----------------------------------------");
	}while(true);
		/*
		System.out.println("Server chiuso");
		try {c_s.close_bet();
		System.exit(1);
		}catch(UnmarshalException e) {}
		*/
	}
	
	
	@Override
	public synchronized void add_bet(Integer id, ArrayList<Integer> bet ) throws RemoteException {

		bet_map.put(id, bet);
		
	}

	@Override
	public void set_obj_bet(Integer id, ArrayList<String> obj_bet) throws RemoteException {
		obj_bet_map.put(id, obj_bet);
		
	}

	@Override
	public Boolean access() throws RemoteException {
		
		return accesso;
	}


	

}
