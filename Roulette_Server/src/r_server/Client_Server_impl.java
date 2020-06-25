package r_server;

import java.rmi.RemoteException;

public class Client_Server_impl implements Client_Server_int{

	@Override
	public void notify_client() throws RemoteException {
		
		Client_thread.start_bet=true;
	}

}
