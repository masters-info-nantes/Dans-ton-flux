package server;

import java.io.File;
import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.interfaces.middleware.InterfacesClientServer.*;

import org.mapdb.*;

public class Forum extends UnicastRemoteObject implements InterfaceServerForum{
	

	private Map<String, Subject> subjects;
	private Map<String, Client> clients;
	private Map<String, String> dbTopics;
	private Map<String, String> dbClients;

	private DB db;

	protected Forum() throws RemoteException {
		super();
		subjects = new TreeMap<String, Subject>();
		clients = new TreeMap<String, Client>();
		this.db = DBMaker.fileDB(new File("src/main/resources/storage.db")).closeOnJvmShutdown().transactionDisable().make();
				
		getBackSubjects();
		getBackClients();

	}
	
	public void getBackSubjects(){
		/*
		 * Get titles of existing subjects
		 */
		this.dbTopics = this.db.hashMap("topicList");

		if(db.exists("topicList")) {
			this.dbTopics = this.db.hashMap("topicList");
			
			/*
			 * recuperate title and authors of subjects
			 */
			for(Map.Entry<String, String> m: this.dbTopics.entrySet()){
				try {
					subjects.put(m.getKey(), new Subject(m.getKey(), m.getValue(), this.db));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}		
	}
	
	public void getBackClients(){
		
		this.dbClients = this.db.hashMap("clientList");
		for(Map.Entry<String, String> m: this.dbClients.entrySet()){
			clients.put(m.getKey(), new Client(m.getKey(), m.getValue(), this.db));
			Set<String> titles = db.treeSet("client." + m.getKey() + ".topics");
			if(db.exists("client." + m.getKey() + ".topics")){
				List<Subject> userSubjects = new ArrayList<Subject>();
				for(String s:titles){
					userSubjects.add(subjects.get(s));
				}	
				clients.get(m.getKey()).setSubjects(userSubjects);
			}
		}
	}
	
	public Object[] getTitlesOfSubjects() throws RemoteException {
		return subjects.keySet().toArray();
	}

	
	public boolean sendSubject(String author, String title) throws RemoteException {
		if(!subjects.containsKey(title)){
			this.dbTopics.put(title,  author);

			subjects.put(title, new Subject(title, author, db));
			
			for(Map.Entry<String, Client> entry : clients.entrySet()){
				if(!entry.getValue().getName().equals(author)){
					entry.getValue().getInter().showSubject(title);
				}
			}
			return true;
		}
		else{
			return false;
		}
	}
	
	public Object[] connexion(String login, String password, InterfaceDisplayClient display) throws RemoteException{

		Object[] o = null;
		if(clients.get(login) == null){
			try {
				throw new Exception("Wrong client");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(clients.get(login).getPassword_().equals(password)){
			clients.get(login).setInter(display);
			o = clients.get(login).getSubjects().toArray();
		}
		else{
			try {
				throw new Exception("Wrong password");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return o;
	}
	
	public boolean registrationOnForum(String login, String password) throws RemoteException{
			
		if(!clients.containsKey(login)){
			clients.put(login, new Client(login, password, this.db));
			dbClients.put(login, password);
		
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean deRegistrationOnSubject(String login, String title) throws RemoteException{
		if(clients.containsKey(login) && subjects.containsKey(title)){
			clients.get(login).deRegistrationOn(title);
			subjects.get(title).deRegistration(login);

			return true;
		}
		else{
			return false;
		}
	}
	
	public InterfaceSubjectDiscussion registrationOnSubject(String login, String title) throws RemoteException{
		InterfaceSubjectDiscussion i = null;
		if(clients.containsKey(login) && subjects.containsKey(title)){
			clients.get(login).addSubject(subjects.get(title));
			try {
				boolean registration = subjects.get(title).registration(clients.get(login));
				
				if(!registration){
					try {
						throw new Exception("Client is already registrate on this subject");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					clients.get(login).addSubject(subjects.get(title));
					i = subjects.get(title);

				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		else{
			try {
				throw new Exception("the subject or the client doesn't exists");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
		}
		return i;
	}

	@Override
	public boolean deleteSubject(String author, String title) throws RemoteException {
		if(subjects.containsKey(title) && subjects.get(title).getAuthor().equals(author)){
			this.subjects.get(title).deletionIsComming();
			this.subjects.remove(title);
			this.dbTopics.remove(title);
			return true;
		}
		else{
			return false;
		}
	}
}
