package server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.mapdb.DB;

import com.interfaces.middleware.InterfacesClientServer.*;

public class Client{
	
	private String name_;
	private String password_;
	private List<Subject> subjects_;
	InterfaceDisplayClient display;
	private DB db;
	private Set<String> dbSubjectsTitleRegistration;
		
	public Client(String name, String password, DB db) {
		super();
		this.name_ = name;
		this.password_ = password;
		subjects_ = new ArrayList<Subject>();
		display = null;
		this.db = db;
		
		this.dbSubjectsTitleRegistration = this.db.treeSet("client.topics");
	}
	
	public String getPassword_() {
		return password_;
	}

	public void setPassword_(String password_) {
		this.password_ = password_;
	}
	
	public Client(String name, InterfaceDisplayClient display) {
		super();
		this.name_ = name;
		this.display = display;
		subjects_ = new ArrayList<Subject>();
	}
	
	public List<Subject> getSubjects() {
		return subjects_;
	}
	
	public void setSubjects(List<Subject> subjects){
		this.subjects_ = subjects;
	}

	public void addSubject(Subject subject) {
		this.subjects_.add(subject);
		try {
			this.dbSubjectsTitleRegistration.add(subject.getTitle());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return name_;
	}

	public void setName(String name) {
		this.name_ = name;
	}

	public InterfaceDisplayClient getInter() {
		return display;
	}

	public void setInter(InterfaceDisplayClient inter) {
		this.display = inter;
	}

	public void deRegistrationOn(String title) {
		this.dbSubjectsTitleRegistration.remove(title);
		this.subjects_.remove(title);
		
	} 

	
}
