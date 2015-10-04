package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.interfaces.middleware.InterfaceDisplayClient;
import com.interfaces.middleware.InterfaceMessage;
import com.interfaces.middleware.InterfaceSubjectDiscussion;

public class Subject extends UnicastRemoteObject implements InterfaceSubjectDiscussion{
	
	private String title_;
	private SortedSet<InterfaceMessage> messages_;
	private Map<String, Client> clients_;
	
	public void afficher(){
		System.out.println(clients_.get("bbb").getInter());
	}
	
	public Subject(String title_) throws RemoteException {
		super();
		this.title_ = title_;
		clients_ = new TreeMap<String, Client>();
		messages_ = new TreeSet<InterfaceMessage>();
	}

	public boolean registration(Client c) throws RemoteException {
		if(!clients_.containsKey(c.getName())){
			clients_.put(c.getName(), c);
			return true;
		}
		else{
			return false;
		}
	}
		
	public boolean deRegistration(String login) throws RemoteException {
		if(clients_.containsKey(login)){
			clients_.remove(login);
			return true;
		}
		else{
			return false;
		}
	}

	public void broadcastMessage(String message, String author) throws RemoteException{
		
		Date d = GregorianCalendar.getInstance().getTime();
		addMessage(message, author, d);
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("src/main/resources/" + this.title_ + ".txt"), true));
			writer.write(message + "|" + author + "|" + d.getTime() + "\n");
			writer.close();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		for(Map.Entry<String, Client> entry : clients_.entrySet()){
			if(!entry.getKey().equals(author)){
				try {
					if(entry.getValue().getInter() != null){
						entry.getValue().getInter().show(this.title_, message);
					}
				} catch (RemoteException e) {
					entry.getValue().setInter(null);
				}
			}
		}
	}
	
	public Object[] getMessages() throws RemoteException {
		return messages_.toArray();
	}
	
	public void addMessage(String message, String author, Date date) throws RemoteException{
		this.messages_.add(new Message(message, author, date));
	}

}
