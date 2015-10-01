package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.TreeMap;

import com.interfaces.middleware.InterfaceDisplayClient;
import com.interfaces.middleware.InterfaceSubjectDiscussion;

public class Subject extends UnicastRemoteObject implements InterfaceSubjectDiscussion{
	
	private String title_;
	Map<String, Client> clients;
	
	public Subject(String title_) throws RemoteException {
		super();
		this.title_ = title_;
		clients = new TreeMap<String, Client>();
	}

	@Override
	public void inscription(String login, InterfaceDisplayClient c) throws RemoteException {
		clients.put(login, new Client(login, c));
	}

	@Override
	public void desInscription(String login) throws RemoteException {
		clients.remove(login);
		
	}

	@Override
	public void diffuse(String Message) throws RemoteException {
		for(Map.Entry<String, Client> entry : clients.entrySet()){
			entry.getValue().getInter().show(this.title_, Message);
		}
	}

}
