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
    public static void main (String[] args) throws RemoteException, NotBoundException, InterruptedException {
        
        registry= LocateRegistry.createRegistry(1099);
        Server_main s_m = new Server_main();
        registry.rebind("SC", s_m);
        
        
        int turn = 0;
        int turn_void = 0;
        do {
        turn++;
        obj_bet_map.clear();
        bet_map.clear();
       
        System.out.println("Nuovo giro di roulotte, turno : "+turn);
        //inizio accettazione scommesse
        System.out.println("Aste aperte, si accettano le puntate");
        accesso = true;
        
        try {
            Thread.sleep(6000);
        }
        catch(InterruptedException e) {
            
        }
        while((obj_bet_map.isEmpty())||(obj_bet_map.isEmpty())) {
        	turn_void++;
        	if(turn_void<6) {
        	System.out.println("In attesa di giocatori...");
        	Thread.sleep(3000);
        	}else {System.out.println("Chiusura sever, nessun giocatore presente");System.exit(1);;}
        	}
        //fine accettazione scommesse
        accesso = false;
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

        
        for(int i = 0; i<bet_map.size();i++) {
        	
            balance = 0;
            int reward = 0;
            int lost = 0;
            int bet = 0;
     
            Boolean continua = true;

            if((obj_bet_map.containsKey(i)==false)&&(bet_map.containsKey(i)==true)) {
            	obj_bet_map.put(i, create_rnd());
            }
            
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
            if((!obj_bet_map.get(i).contains("0"))&&(obj_bet_map.get(i).contains(pd))) {
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


            balance = create_balance(reward, lost, bet);
            //System.out.println(i+" bilancio " +balance);
        
            } else System.out.println("Saltato indice : "+ i);
            /*provare a implementare funzioni tramite else
            else System.out.println("Saltato indice : "+ i);*/
            
        }
      
        
        //fine estrazione
        System.out.println("Utenti budget aggiornato : "+client_list);

        try {
        c_s = (Client_Server_int) registry.lookup("CS");
        c_s.notify_client();
        }catch(NotBoundException e) {

        	Thread.sleep(1000);
        }

        System.out.println("-----------------------------------------");
    }while((!bet_map.isEmpty())&&(!obj_bet_map.isEmpty()));
        
        System.out.println("Server chiuso");
        try {c_s.close_bet();
        
        }catch(UnmarshalException | NullPointerException e) {}
        System.exit(1);
        
    }
    
    public static int create_balance(int reward, int lost, int bet) {
        
        return reward-lost+bet;
        
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
    public static ArrayList<String> create_rnd(){
    	ArrayList<String> n = new ArrayList<String>();
    	Random rnd = new Random();
        int tmp =  rnd.nextInt(36);
        n.add(Integer.toString(tmp));
        System.err.println("Inserito : "+n.get(0));
    	return n;
    }


}
