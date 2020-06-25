package r_server;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class Server_main implements Server_Client_int {
	static Registry registry;
	static HashMap<Integer,ArrayList<Integer>> bet_map = new HashMap<Integer,ArrayList<Integer>>();
	static HashMap<Integer,ArrayList<String>> obj_bet_map = new HashMap<Integer,ArrayList<String>>();
	public static void main (String[] args) throws RemoteException {
		
		registry = LocateRegistry.createRegistry(1077);
		Server_main s_m = new Server_main();
		Server_Client_int s_c = (Server_Client_int) UnicastRemoteObject.exportObject(s_m, 1077);
		registry.rebind("SC", s_c);
		
		
	}

	@Override
	public synchronized void add_bet(Integer id, ArrayList<Integer> bet ) throws RemoteException {

		bet_map.put(id, bet);
		
	}

	@Override
	public void set_obj_bet(Integer id, ArrayList<String> obj_bet) throws RemoteException {
		obj_bet_map.put(id, obj_bet);
		
	}

}
