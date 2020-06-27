package r_server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Semaphore;

 

public class Server_main extends UnicastRemoteObject  implements Server_Client_int {
    private static final long serialVersionUID = 1L;
    static Boolean accesso;
    static int balance;
    static int budget;
    static Client_Server_int c_s;
    static Semaphore sem = new Semaphore(1);
    protected Server_main() throws RemoteException {
        super();
        
    }

 

    static Registry registry;
    static HashMap<Integer,ArrayList<Integer>> bet_map = new HashMap<Integer,ArrayList<Integer>>();
    static HashMap<Integer,ArrayList<String>> obj_bet_map = new HashMap<Integer,ArrayList<String>>();
    static HashMap<Integer,Integer>client_list = new HashMap<Integer,Integer>();
    static HashMap<Integer,Integer>balance_list = new HashMap<Integer,Integer>();
    static HashMap<Integer,Integer>client_turn = new HashMap<Integer,Integer>();
    static  int turn = 0;
    public static void main (String[] args) throws RemoteException, NotBoundException, InterruptedException {
        
    	for(int m = 0; m<10;m++) {client_turn.put(m, 0);}
    	
        registry= LocateRegistry.createRegistry(1099);
        Server_main s_m = new Server_main();
        registry.rebind("SC", s_m);
        
       
       
        int turn_void = 0;
        do {
        turn++;
        
        obj_bet_map.clear();
        bet_map.clear();
        Thread.sleep(100);
        
        System.out.println("Nuovo giro di roulette, turno : "+turn);
        //inizio accettazione scommesse
        System.out.println("Aste aperte, si accettano le puntate");
       
        System.out.println("In attesa di giocatori...");
        accesso = true;
        Thread.sleep(10000);
        accesso = false;

        //fine accettazione scommesse
        c_s = (Client_Server_int) registry.lookup("CS");
        //c_s.give_access();
        System.out.println("Aste chiuse, aspettare il prossimo turno");
        System.out.println("Valore puntate : " + bet_map);
        System.out.println("Numeri puntati : " +obj_bet_map);
        System.out.println("Utenti : "+client_list);
        
        
       
        //inizio estrazione
        Random rnd = new Random();
        int tmp = rnd.nextInt(36);
        System.out.println("Estratto il numero : "+tmp);
        String pd = (tmp%2==0)?"par":"dis";
        String estr = Integer.toString(tmp);
        
        
        for(int i = 0; i<10;i++) {
        	
            balance = 0;
            int reward = 0;
            int lost = 0;
            int bet = 0;
     
            Boolean continua = true;
            /*
            if((obj_bet_map.containsKey(i)==false)&&(bet_map.containsKey(i)==true)) {
            	obj_bet_map.put(i, create_rnd());
            }*/
            
            try {Boolean x = (obj_bet_map.get(i).contains(estr));
            	continua = true;
            }catch(NullPointerException e) {
            	continua = false;
            }
            
            
            if(continua==true) {
            
            if((obj_bet_map.get(i).contains(estr))) {
                budget = client_list.get(i);
                int    ind = obj_bet_map.get(i).indexOf(estr);
                if (ind != -1) {
                    bet = bet_map.get(i).get(ind);
                    reward = bet*2;
                    client_list.put(i, budget+(reward));
                    System.out.println("giocatore "+i + " ha vinto : "+ (reward)+"|| turno "+turn);
                    
                }
                
            }
            if((obj_bet_map.get(i).contains(pd))) {
                budget = client_list.get(i);
                bet = bet_map.get(i).get(0);
                reward = bet*2;
                client_list.put(i, budget+(reward));
                System.out.println("giocatore "+i + " ha vinto : "+ (reward)+"|| turno "+turn);
                
            }
            for(int j = 0; j<bet_map.get(i).size();j++) {
                int x = bet_map.get(i).get(j);
                lost = lost + x;
                
            }
            

            //visualizza su server
            balance = create_balance(reward, lost, bet);
            //invia a client
            balance_list.put(i,create_balance(reward, lost, bet)) ;
            System.out.println(i+" bilancio " +balance);
           
            } //else {System.out.println("Saltato indice : "+ i);}
            
            
        }
        
        coun_exit();
        System.out.println(client_turn);
        System.out.println("Utenti budget aggiornato : "+client_list);
        System.out.println("-----------------------------------------");
        c_s.notify_client();
        c_s.give_access();
       
        
    }while((!bet_map.isEmpty())&&(!obj_bet_map.isEmpty()));
        
        System.out.println("Server chiuso");
        try {c_s.close_bet();
        
        }catch(UnmarshalException | NullPointerException e) {}
        System.exit(1);
        
    }
    
    public static int create_balance(int reward, int lost, int bet) {
        
        return reward-lost+bet;
        
    }
    
    public static void coun_exit() {
    	ArrayList<Integer>z = new ArrayList<Integer>();
    	z.addAll(obj_bet_map.keySet()); //01358
    	for(int k=z.size(); k<10;k++){z.add(-1);}
    	int[] not_bet = {0,1,2,3,4,5,6,7,8,9}; 	
    	for(int i =0; i<10;i++) {if(z.contains(i)) not_bet[i]=-1;
    	//System.err.println(not_bet[i]);
    	}
    	for(int j= 0; j<10;j++) {
    		if(not_bet[j]!=-1) {
    			int tmp = client_turn.get(j);
    			client_turn.put(j, tmp+1);
    		}else client_turn.put(j, 0);
    	}
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
    
    }/*
    public static ArrayList<String> create_rnd(){
    	ArrayList<String> n = new ArrayList<String>();
    	Random rnd = new Random();
        int tmp =  rnd.nextInt(36);
        n.add(Integer.toString(tmp));
        System.err.println("Inserito : "+n.get(0));
    	return n;
    }*/

	@Override
	public HashMap<Integer, Integer> show_balance() throws RemoteException {
		
		return balance_list;
	}

	@Override
	public HashMap<Integer, Integer> user_join() throws RemoteException {
		
		return client_list;
	}

	@Override
	public int get_turn() throws RemoteException {
	
		return turn;
	}

	@Override
	public Boolean user_exit(int id) throws RemoteException {
		Boolean ex = false;
    	if(client_turn.get(id) >=2) {
    		ex=true;
    	}
		return ex;
	}

}
