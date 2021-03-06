package r_server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface Server_Client_int extends Remote{
	
	public void add_bet(Integer id, ArrayList<Integer> bet )throws RemoteException;
	public void set_obj_bet(Integer id, ArrayList<String> obj_bet )throws RemoteException;
	public Boolean access()throws RemoteException;
	public void set_user(int id, int budget)throws RemoteException;
	public int get_budget(int id)throws RemoteException;
	public void update_budget(int id ,int budget)throws RemoteException;
	public HashMap<Integer, Integer> show_balance()throws RemoteException;
	public HashMap<Integer,Integer> user_join()throws RemoteException;
	public int get_turn()throws RemoteException;
	public Boolean user_exit(int id)throws RemoteException;

}
