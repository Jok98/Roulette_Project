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
import java.util.Random;

public class Server_main extends UnicastRemoteObject  implements Server_Client_int {
	private static final long serialVersionUID = 1L;
	static Boolean accesso;
	static int turn;
	//possibile causa errore
	static int budget;
	static Client_Server_int c_s;
	protected Server_main() throws RemoteException {
		super();
		
	}

	static Registry registry;
	static HashMap<Integer,ArrayList<Integer>> bet_map = new HashMap<Integer,ArrayList<Integer>>();
	static HashMap<Integer,ArrayList<String>> obj_bet_map = new HashMap<Integer,ArrayList<String>>();
	static HashMap<Integer,Integer>client_list = new HashMap<Integer,Integer>();
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
		c_s = (Client_Server_int) registry.lookup("CS");
		//fine accettazione scommesse
		accesso = false;
		System.out.println("Aste chiuse, aspettare il prossimo turno");
		
		
		
		System.out.println("Valore puntate : " + bet_map);
		System.out.println("Numeri puntati : " +obj_bet_map);
		System.out.println("Utenti : "+client_list);
		
		
		//Thread.sleep(5000);
		//inizio estrazione
		Random rnd = new Random();
		int tmp = rnd.nextInt(36);
		System.out.println("Estratto il numero : "+tmp);
		String pd = (tmp%2==0)?"par":"disp";
		String estr = Integer.toString(tmp);
		for(int i = 0; i<obj_bet_map.size();i++) {
			try{
				
			
			if((obj_bet_map.get(i).contains(estr))) {
				budget = client_list.get(i);
				int bet = bet_map.get(i).indexOf(estr);
				int reward = bet*2;
				client_list.put(i, budget+(reward));
				System.out.println(i + " ha vinto : "+ (bet*2));
				
			}
			if((!obj_bet_map.get(i).contains(0))&&(obj_bet_map.get(i).contains(pd))) {
				budget = client_list.get(i);
				int bet = bet_map.get(i).get(0);
				int reward = bet*2;
				client_list.put(i, budget+(reward));
				System.out.println(i + " ha vinto : "+ (reward));
				
			}

			
			}catch(NullPointerException e) {
				//System.err.println("id : "+i+" non esistente");
			}
			
		}
		
		
		//fine estrazione
		System.out.println("Utenti budget aggiornato : "+client_list);
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


	@Override
	public void set_user(int id, int budget) throws RemoteException {
		client_list.put(id, budget);
		
	}


	@Override
	public int get_budget(int id) throws RemoteException {
		int budget = client_list.get(id);
		return budget;
	}


	@Override
	public void update_budget(int id, int budget) throws RemoteException {
		client_list.put(id, budget);
		
	}


	@Override
	public void show_balance(int id) throws RemoteException {
		
		
	}


	

}
