package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.mapdb.DB;

import com.interfaces.middleware.InterfacesClientServer.*;

/**
 * 
 * @author Franck
 * This class is used to manege subjects. This classe save messages in storage file
 */
public class Subject extends UnicastRemoteObject implements InterfaceSubjectDiscussion{
	
	private String title_;
	private String author_;
	private SortedSet<InterfaceMessage> messages_;
	private Map<String, Client> clients_;
	private DB db;
	private Set<String> dbIdMessages;

	public Subject(String title_, String author_, DB db) throws RemoteException {
		super();
		this.title_ = title_;
		this.author_ = author_;
		this.clients_ = new TreeMap<String, Client>();
		this.messages_ = new TreeSet<InterfaceMessage>();
		this.db = db;

		this.dbIdMessages = this.db.treeSet("topic." + this.title_ + ".id");

		if(db.exists("topic." + this.title_ + ".id")) {
			
			for(String s : this.dbIdMessages){
				if(db.exists("topic." + this.title_ + ".messages." + s)) {
					String temp = this.db.atomicString("topic." + this.title_ + ".messages." + s).get();
					String[] info = s.split("\\|");
					Calendar cal = new GregorianCalendar();
					cal.setTimeInMillis(Long.parseLong(info[1]));
					this.messages_.add(new Message(temp, info[0], cal));
				}
			}
		} 
	}

	public String getTitle() throws RemoteException{
		return title_;
	}

	public void setTitle(String title_) {
		this.title_ = title_;
	}

	public String getAuthor() throws RemoteException{
		return author_;
	}

	public void setAuthor(String author_) {
		this.author_ = author_;
	}

	public SortedSet<InterfaceMessage> getMessages_() {
		return messages_;
	}
	
	public Object[] getMessages() throws RemoteException {
		return messages_.toArray();
	}

	public void setMessages_(SortedSet<InterfaceMessage> messages_) {
		this.messages_ = messages_;
	}

	/**
	 * 
	 * @param client client that want to follow the subject
	 * @return false if client already follow the subject
	 * @throws RemoteException
	 */
	public boolean registration(Client client) throws RemoteException {
		if(!clients_.containsKey(client.getName())){
			clients_.put(client.getName(), client);
			return true;
		}
		else{
			return false;
		}
	}
		
	/**
	 * 
	 * @param login login of the client that want to unfollow the subject
	 * @return true if the client is correctly removed from this subject
	 * @throws RemoteException
	 */
	public boolean deRegistration(String login) throws RemoteException {
		if(clients_.containsKey(login)){
			this.clients_.remove(login);
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * send a new message to all the user that follow the subject
	 * @param message message to send
	 * @param author author of the message
	 */
	public void broadcastMessage(String message, String author) throws RemoteException{
		
		Calendar cal = new GregorianCalendar();
		addMessage(message, author, cal);
		this.dbIdMessages.add(author+"|"+cal.getTimeInMillis());
		this.db.atomicStringCreate("topic." + this.title_ + ".messages."+author+"|"+cal.getTimeInMillis(), message);

		for(Map.Entry<String, Client> entry : clients_.entrySet()){
			if(!entry.getKey().equals(author)){
				try {
					if(entry.getValue().getInter() != null){
						entry.getValue().getInter().showMessage(this.title_, message, author, cal.getTimeInMillis()+"");
					}
				} catch (RemoteException e) {
					entry.getValue().setInter(null);
				}
			}
		}
	}
	
	/**
	 * add a message in the subject
	 * @param message message to add
	 * @param author author of the message
	 * @param date date of the message
	 */
	public void addMessage(String message, String author, Calendar date){
		try {
			this.messages_.add(new Message(message, author, date));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * When a subject is deleted, the subject is unfollow by all its user
	 */
	public void deletionIsComming() {
		for(Map.Entry<String, Client> m:this.clients_.entrySet()){
			m.getValue().deRegistrationOn(this.title_);
		}		
	}

}
