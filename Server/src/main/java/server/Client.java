package server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.mapdb.DB;

import com.interfaces.middleware.InterfacesClientServer.*;

/**
 * 
 * @author Franck Boncler
 * This class contains all the information about a client. It save titles of subects that a follow by a client
 * 
 */
public class Client{
	
	private String name_;
	private String password_;
	private List<Subject> subjects_;
	private InterfaceDisplayClient display_;
	private DB db_;
	private Set<String> dbSubjectsTitleRegistration_;
		
	public Client(String name, String password, DB db) {
		super();
		this.name_ = name;
		this.password_ = password;
		this.subjects_ = new ArrayList<Subject>();
		this.display_ = null;
		this.db_ = db;
		
		this.dbSubjectsTitleRegistration_ = this.db_.treeSet("client." + this.name_ +".topics");
	}
	
	public Client(String name, InterfaceDisplayClient display) {
		super();
		this.name_ = name;
		this.display_ = display;
		this.subjects_ = new ArrayList<Subject>();
	}
	
	public String getPassword() {
		return this.password_;
	}

	public void setPassword(String password_) {
		this.password_ = password_;
	}
	
	public List<Subject> getSubjects() {
		return this.subjects_;
	}

	public void setSubjects(List<Subject> subjects){
		this.subjects_ = subjects;
	}

	/**
	 * Add a new subject, the link between the client and the title of the subject is saved
	 * @param subject a subject of the Forum
	 */
	public void addSubject(Subject subject) {
		this.subjects_.add(subject);
		try {
			this.dbSubjectsTitleRegistration_.add(subject.getTitle());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return this.name_;
	}

	public void setName(String name) {
		this.name_ = name;
	}

	public InterfaceDisplayClient getInter() {
		return this.display_;
	}

	public void setInter(InterfaceDisplayClient inter) {
		this.display_ = inter;
	}

	/**
	 * The client no longer follow the subject with the given title, the link between them is deleted in the storage.
	 * @param title the title of a subject
	 */
	public void deRegistrationOn(String title) {
		this.dbSubjectsTitleRegistration_.remove(title);
		this.subjects_.remove(title);
	} 
}
