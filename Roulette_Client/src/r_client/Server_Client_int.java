package r_client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Server_Client_int extends Remote{
	
	public void add_bet(Integer id, ArrayList<Integer> bet )throws RemoteException;
}
