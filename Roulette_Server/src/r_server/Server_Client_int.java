package r_server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Server_Client_int extends Remote{
	
	public void add_bet(Integer id, ArrayList<Integer> bet )throws RemoteException;
	public void set_obj_bet(Integer id, ArrayList<String> obj_bet )throws RemoteException;
	public Boolean access()throws RemoteException;
}
