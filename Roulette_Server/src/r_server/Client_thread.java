package r_server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Client_thread extends Thread implements Client_Server_int {
	
	private Random rnd = new Random();
	private int n_bet;
	private int id;
	private Server_Client_int s_c;
	private int budget;
	private int old_budget ;
	private static String host;
	private String tmp;
	static Semaphore sem = new Semaphore(1);
	private Semaphore semex = new Semaphore(1);
	
	public Client_thread(int id, String host, int budget) {
		this.host = host;
		this.budget = budget;
		this.id = id;
	}
	
	public synchronized void run() {
		
		try {
			Registry registry = LocateRegistry.getRegistry();
			Client_thread th = new Client_thread(id,host, budget);
			Client_Server_int c_s = (Client_Server_int) UnicastRemoteObject.exportObject(th,1077);
			registry.rebind("CS", c_s);
			registry = LocateRegistry.getRegistry(host);
			s_c = (Server_Client_int)	registry.lookup("SC");
			Client_thread c_thread = new Client_thread(id,  host, budget);
			s_c.set_user(id, budget);
			
			while(true) {

			while(s_c.access()==false){
				
				System.out.println("Aste chiuse, aspettare");
				Thread.sleep(100);
			}
			ArrayList<Integer> bet_list = new ArrayList<Integer>();
			ArrayList<String> obj_bet_list = new ArrayList<String>();
			old_budget = budget;
			budget = s_c.get_budget(id);
			
			System.out.println("Nuovo turno di : "+id);
			System.out.println(id + " ha tot budget : "+budget);
			if(budget>0) {
				n_bet = (budget<=5) ? budget : rnd.nextInt(5);
			
				synchronized(semex) {
					semex.acquire();
					obj_bet(obj_bet_list);
					s_c.set_obj_bet(id, obj_bet_list);
					do_bet(bet_list);
					s_c.add_bet(id, bet_list);
					System.out.println("Lista valori puntate di "+ id + " "+bet_list);
					System.out.println("Lista obj puntate di "+ id + " "+obj_bet_list);
					bet_list.clear();
					obj_bet_list.clear();
					budget = s_c.get_budget(id);
					semex.release();
				}
				
				
			}else {
				break;
			}
			
			
			synchronized(sem){
				int balance =budget - old_budget;
				System.err.println("Il bilancio di "+id+" è : "+balance);
				sem.wait();	}
			
			
			
			System.out.println("---------------------------------------------");
	}
	} catch (RemoteException | NotBoundException | InterruptedException e) {
			
			e.printStackTrace();
		}
	System.out.println("Giocatore "+id+" esce");
	interrupt();
	}
	
	public synchronized void obj_bet(ArrayList<String> obj_bet_list){
		Boolean num = rnd.nextBoolean();
		//System.out.println("Si scomette numeri : " + num);
		int tmp;
		String obj_bet = null;

			if(num==true) {
				
				for(int i = 0; i<n_bet;i++) {
				do {
				tmp = rnd.nextInt(36);
				obj_bet =Integer.toString(tmp) ;
				}while(obj_bet_list.contains(obj_bet));
				obj_bet_list.add(obj_bet);
				//System.out.println("Si scomette su : " + obj_bet );
				}
				
				
			}else {
				n_bet=1;
				obj_bet = (rnd.nextBoolean()==true) ? "par" :"dis";
				obj_bet_list.add(obj_bet);
				//System.out.println("Si scomette su : " + obj_bet );
			}

	}
	
	
	public synchronized void do_bet(ArrayList<Integer> bet_list) throws RemoteException{
		int bet_val;
		
		
		for(int i = 0; i<n_bet;i++) {
			do {
				bet_val = ((budget==1)||(budget==0)) ? 1 : rnd.nextInt(budget);
			}while(bet_val==0);
			
			bet_list.add(bet_val);
			synchronized(this) {
				budget = budget-bet_val;
				s_c.update_budget(id, budget);
			}
			
			if(budget<1) {/*
				System.out.print("Scommessa : "+i+" effettuata !");
				System.out.println(" valore : "+bet_val);
				System.out.println("Soldi esauriti ");
				*/
				break;
			}
			/*
			System.out.print("Scommessa : "+i+" effettuata !");
			System.out.println(" valore : "+bet_val);
			System.out.println("Soldi rimanenti :  "+ budget);
			*/
			}
		
	
	}
	@Override
	public void notify_client() throws RemoteException {
		synchronized(sem){sem.notifyAll();}
	}

	@Override
	public void close_bet() throws RemoteException {
		System.out.println("Server scommesse chiuso!");
		System.exit(1);
		
	}



	
	
	
}
