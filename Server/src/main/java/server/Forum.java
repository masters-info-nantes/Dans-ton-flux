package server;

import java.io.File;
import java.io.Serializable;
import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;

import com.interfaces.middleware.InterfacesClientServer.ClientAlreadyRegisteredException;
import com.interfaces.middleware.InterfacesClientServer.ClientDidNotExistsException;
import com.interfaces.middleware.InterfacesClientServer.DeletionPermitionDeletionException;
import com.interfaces.middleware.InterfacesClientServer.InterfaceDisplayClient;
import com.interfaces.middleware.InterfacesClientServer.InterfaceServerForum;
import com.interfaces.middleware.InterfacesClientServer.InterfaceSubjectDiscussion;
import com.interfaces.middleware.InterfacesClientServer.SubjectAlreadyExistsException;
import com.interfaces.middleware.InterfacesClientServer.SubjectDidNotExistsException;
import com.interfaces.middleware.InterfacesClientServer.WrongClientException;
import com.interfaces.middleware.InterfacesClientServer.WrongPasswordException;

import org.mapdb.*;

/**
 * 
 * @author Franck Boncler
 * This class is used to manage the forum, this includes clients and subjects.
 * The backup of client and subject list is manage here.
 */
public class Forum extends UnicastRemoteObject implements InterfaceServerForum, Serializable{
	

	private Map<String, Subject> subjects_;
	private Map<String, Client> clients_;
	private Map<String, String> dbTopics_;
	private Map<String, String> dbClients_;
	private DB db_;

	protected Forum() throws RemoteException {
		super();
		this.subjects_ = new TreeMap<String, Subject>();
		this.clients_ = new TreeMap<String, Client>();
		this.db_ = DBMaker.fileDB(new File("src/main/resources/storage.db")).closeOnJvmShutdown().transactionDisable().make();
	
		getBackSubjects();
		getBackClients();

	}
	
	/**
	 * Get back the title and the author of all the subjects in the storage
	 */
	public void getBackSubjects(){
		/*
		 * Get titles of existing subjects
		 */
		this.dbTopics_ = this.db_.hashMap("topicList");

		if(db_.exists("topicList")) {
			this.dbTopics_ = this.db_.hashMap("topicList");
			for(Map.Entry<String, String> m: this.dbTopics_.entrySet()){
				try {
					subjects_.put(m.getKey(), new Subject(m.getKey(), m.getValue(), this.db_));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}		
	}
	
	/**
	 * Get back the login, the password and the titles of followed subjects of all the clients in the storage
	 */
	public void getBackClients(){
		
		this.dbClients_ = this.db_.hashMap("clientList");
		for(Map.Entry<String, String> m: this.dbClients_.entrySet()){
			clients_.put(m.getKey(), new Client(m.getKey(), m.getValue(), this.db_));
			Set<String> titles = db_.treeSet("client." + m.getKey() + ".topics");
			System.out.println(m.getKey());
			if(db_.exists("client." + m.getKey() + ".topics")){
				List<Subject> userSubjects = new ArrayList<Subject>();
				for(String s:titles){
					userSubjects.add(subjects_.get(s));
				}	
				clients_.get(m.getKey()).setSubjects(userSubjects);
			}
		}
	}
	
	public Object[] getTitlesOfSubjects() throws RemoteException {
		return subjects_.keySet().toArray();
	}

	/**
	 * Create a new subject, save it and show it to all the clients
	 * @param author login of the client that want to create the subject
	 * @param title title of the subject the client wants to create
	 * @throws SubjectAlreadyExistsException if the subject already exists
	 */
	public void sendSubject(String author, String title) throws SubjectAlreadyExistsException {
		if(!subjects_.containsKey(title)){
			this.dbTopics_.put(title,  author);

			try {
				subjects_.put(title, new Subject(title, author, db_));
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			
			for(Map.Entry<String, Client> entry : clients_.entrySet()){
				if(!entry.getValue().getName().equals(author)){
					if(entry.getValue().getInter() != null){
						try {
							entry.getValue().getInter().showSubject(title);
						} catch (RemoteException e) {
							entry.getValue().setInter(null);
						}
					}
				}
			}
		}
		else{
			throw new SubjectAlreadyExistsException();
		}
	}
	
	/**
	 * Allow to a client to get the subjects he follow
	 * @param display object that allow to display new messages or new subjects
	 * @param login login of a client
	 * @param password password of a client
	 * @return all the subject the client follow
	 * @exception WrongClientException if the client doesn't exists
	 * @exception WrongPasswordException if the password is wrong
	 */
	public Object[] connexion(String login, String password, InterfaceDisplayClient display) throws RemoteException, WrongClientException, WrongPasswordException{
		Object[] subjectsFollow = null;
		if(clients_.get(login) == null){
			throw new WrongClientException();
		}
		else if(clients_.get(login).getPassword().equals(password)){
			clients_.get(login).setInter(display);
			subjectsFollow = clients_.get(login).getSubjects().toArray();
		}
		else{
			throw new WrongPasswordException();
		}
		return subjectsFollow;
	}
	
	/**
	 * records a new client on the forum, the login and password are saved in the storage
	 * @param login the login has to not be already used, its length has to be superior than 0
	 * @param password the password length has to be superior than 0
	 * @return return true the login and password have a length superior than 0 and the login doesn't exists
	 * @throws WrongClientException if the login length is not superior than 0
	 * @throws WrongPasswordException if the password length is not superior than 0
	 * @throws ClientAlreadyRegisteredException if a client with the login already exists 
	 */
	public void registrationOnForum(String login, String password) throws RemoteException, WrongClientException, WrongPasswordException, ClientAlreadyRegisteredException{
			
		if(!clients_.containsKey(login) && login.length() > 0 && password.length() > 0){
			clients_.put(login, new Client(login, password, this.db_));
			dbClients_.put(login, password);
		}
		else if(clients_.containsKey(login)){
			throw new ClientAlreadyRegisteredException();
		}
		else if(!(login.length() > 0)){
			throw new WrongClientException();
		}
		else{
			throw new WrongPasswordException();
		}
	}
	
	/**
	 * unsubscribe a client on a subject, the client has to follow the subject to be correctly unsubscribed.
	 * @param login login of a client
	 * @param title title of a subject
	 * @throws ClientDidNotExistsException if the given client did not exists
	 * @throws SubjectDidNotExistsException if the given title is not a title of an existing subject
	 */
	public void deRegistrationOnSubject(String login, String title) throws RemoteException, ClientDidNotExistsException, SubjectDidNotExistsException{
		if(clients_.containsKey(login) && subjects_.containsKey(title)){
			clients_.get(login).deRegistrationOn(title);
			subjects_.get(title).deRegistration(login);
		}
		else if(!clients_.containsKey(login)){
			throw new ClientDidNotExistsException();
		}
		else{
			throw new SubjectDidNotExistsException();
		}
	}
	
	/**
	 * Allow to subscribe a client on a subject
	 * @param login login of a client
	 * @param title title of a subject
	 * @exception ClientAlreadyRegisteredException if the client already follow the subject
	 * @exception ClientDidNotExistsException if the client did not exists
	 * @exception SubjectDidNotExistsException if the subject did not exists
	 * @return the subject that the client wants to follow
	 */
	public InterfaceSubjectDiscussion registrationOnSubject(String login, String title) throws RemoteException, SubjectDidNotExistsException, ClientDidNotExistsException, ClientAlreadyRegisteredException{
		InterfaceSubjectDiscussion subject = null;
		if(clients_.containsKey(login) && subjects_.containsKey(title)){
			clients_.get(login).addSubject(subjects_.get(title));
			try {
				boolean registration = subjects_.get(title).registration(clients_.get(login));
				
				if(!registration){
					throw new ClientAlreadyRegisteredException();
				}
				else{
					clients_.get(login).addSubject(subjects_.get(title));
					subject = subjects_.get(title);
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		else{
			if(!clients_.containsKey(login)){
				throw new ClientDidNotExistsException();
			}
			else{
				throw new SubjectDidNotExistsException();
			}
		}
		return subject;
	}

	/**
	 * @param author login of the client
	 * @param title tilte of a subject
	 * @throws SubjectDidNotExistsException 
	 * @throws DeletionPermitionDeletionException 
	 * 
	 */
	public void deleteSubject(String author, String title) throws RemoteException, SubjectDidNotExistsException, DeletionPermitionDeletionException {
		if(subjects_.containsKey(title) && subjects_.get(title).getAuthor().equals(author)){
			try{
				Calendar dateLastMessage = subjects_.get(title).getMessages_().last().getDate();
				dateLastMessage.set(Calendar.YEAR, dateLastMessage.YEAR + 1);
				Calendar cal = new GregorianCalendar();
				if(dateLastMessage.getTimeInMillis() > cal.getTimeInMillis()){
					this.subjects_.get(title).deletionIsComming();
					this.subjects_.remove(title);
					this.dbTopics_.remove(title);
				}
				else{
					throw new DeletionPermitionDeletionException();
				}
			}
			catch(NoSuchElementException e1){
				this.subjects_.get(title).deletionIsComming();
				this.subjects_.remove(title);
				this.dbTopics_.remove(title);
			}
			
		}
		else if(!subjects_.containsKey(title)){
			throw new SubjectDidNotExistsException();
		}
	}
}
