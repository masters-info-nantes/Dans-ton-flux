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

import com.interfaces.middleware.InterfacesClientServer.*;


public class Subject extends UnicastRemoteObject implements InterfaceSubjectDiscussion{
	
	private String title_;
	private String author_;
	private SortedSet<InterfaceMessage> messages_;
	private Map<String, Client> clients_;
	
	public Subject(String title_, String author_) throws RemoteException {
		super();
		this.title_ = title_;
		this.author_ = author_;
		this.clients_ = new TreeMap<String, Client>();
		this.messages_ = new TreeSet<InterfaceMessage>();
	}

	public String getTitle() throws RemoteException{
		return title_;
	}

	public void setTitle_(String title_) {
		this.title_ = title_;
	}

	public String getAuthor() throws RemoteException{
		return author_;
	}

	public void setAuthor_(String author_) {
		this.author_ = author_;
	}

	public SortedSet<InterfaceMessage> getMessages_() {
		return messages_;
	}

	public void setMessages_(SortedSet<InterfaceMessage> messages_) {
		this.messages_ = messages_;
	}

	public boolean registration(Client client) throws RemoteException {
		if(!clients_.containsKey(client.getName())){
			clients_.put(client.getName(), client);
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
						entry.getValue().getInter().showMessage(this.title_, message, author, d.getTime()+"");
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
	
	public void addMessage(String message, String author, Date date){
		try {
			this.messages_.add(new Message(message, author, date));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
