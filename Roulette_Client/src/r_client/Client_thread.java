package r_client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Random;

public class Client_thread extends Thread {
	static Registry registry;
	static Random rnd = new Random();
	static int n_bet;
	static Integer id;
	private static ArrayList<Integer> bet_list = new ArrayList<Integer>();
	private static ArrayList<String> obj_bet_list = new ArrayList<String>();
	static int budget;
	
	public Client_thread(Integer id, int budget) {
		this.budget = budget;
		this.id = id;
		//start();
	}
	
	public void run() {
		System.out.println(id + "ha tot budget : "+budget);
		while(budget>0) {
		n_bet = (budget<=5) ? budget : rnd.nextInt(5);
		obj_bet();
		do_bet();
		
		try {
			registry = LocateRegistry.getRegistry(1077);
			Server_Client_int s_c = (Server_Client_int)	registry.lookup("SC");
			s_c.add_bet(id, bet_list);
		} catch (RemoteException | NotBoundException e) {
			
			e.printStackTrace();
		}
		
		
		
		
	}
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
