package r_server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Client_thread extends Thread  {
	
	static Random rnd = new Random();
	static int n_bet;
	static Integer id;
	private static ArrayList<Integer> bet_list = new ArrayList<Integer>();
	private static ArrayList<String> obj_bet_list = new ArrayList<String>();
	static int budget;
	static String host;
	static Boolean start_bet;
	static Semaphore sem = new Semaphore(1);
	
	public Client_thread(Integer id, int budget, String host) {
		this.host = host;
		this.budget = budget;
		this.id = id;
		//start();
	}
	
	public void run() {
		try {
			Registry registry = LocateRegistry.getRegistry(host);
			Server_Client_int s_c = (Server_Client_int)	registry.lookup("SC");
			Client_thread c_thread = new Client_thread(id, budget, host);
			
			while(true) {
				
			while(s_c.access()==false){
				
				System.out.println("Aste chiuse, aspettare");
				Thread.sleep(100);
			}
			System.out.println("Nuovo turno");
			System.out.println(id + "ha tot budget : "+budget);
			if(budget>0) {
				n_bet = (budget<=5) ? budget : rnd.nextInt(5);
				obj_bet();
				do_bet();
				s_c.set_obj_bet(id, obj_bet_list);
				s_c.add_bet(id, bet_list);
				bet_list.clear();
				obj_bet_list.clear();
				
			}else {
				break;
			}
			synchronized(sem){sem.wait();}
			
	}
	} catch (RemoteException | NotBoundException | InterruptedException e) {
			
			e.printStackTrace();
		}
	System.out.println("Giocatore "+id+"esce");
	interrupt();
	}
	
	public void obj_bet(){
		Boolean num = rnd.nextBoolean();
		System.out.println("Si scomette numeri : " + num);
		int tmp;
		String obj_bet = null;

			if(num==true) {
				
				for(int i = 0; i<n_bet;i++) {
				do {
				tmp = rnd.nextInt(36);
				obj_bet =Integer.toString(tmp) ;
				}while(obj_bet_list.contains(obj_bet));
				obj_bet_list.add(obj_bet);
				System.out.println("Si scomette su : " + obj_bet );
				}
				
				
			}else {
				n_bet=0;
				obj_bet = (rnd.nextBoolean()==true) ? "par" :"dis";
				obj_bet_list.add(obj_bet);
				System.out.println("Si scomette su : " + obj_bet );
			}

	}
	
	
	public void do_bet(){
		int bet_val;
		
		
		for(int i = 0; i<n_bet;i++) {
			do {
				bet_val = (budget==1) ? 1 : rnd.nextInt(budget);
			}while(bet_val==0);
			bet_list.add(bet_val);
			budget = budget-bet_val;
			if(budget==0) {
				System.out.print("Scommessa : "+i+" effettuata !");
				System.out.println(" valore : "+bet_val);
				System.out.println("Soldi esauriti ");
				break;
			}
			
			System.out.print("Scommessa : "+i+" effettuata !");
			System.out.println(" valore : "+bet_val);
			System.out.println("Soldi rimanenti :  "+ budget);
			
		}
		System.out.println("---------------------------------------------");
	}

	
	
	
}
