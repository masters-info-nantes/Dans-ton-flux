package server;

import java.util.ArrayList;
import java.util.List;

import com.interfaces.middleware.InterfacesClientServer.*;

public class Client{
	
	private String name_;
	private String password_;
	private List<Subject> subjects_;
	InterfaceDisplayClient display;
	
	public String getPassword_() {
		return password_;
	}

	public void setPassword_(String password_) {
		this.password_ = password_;
	}
	
	public Client(String name, String password) {
		super();
		this.name_ = name;
		this.password_ = password;
		subjects_ = new ArrayList<Subject>();
		display = null;
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

	public void addSubject(Subject subjects) {
		this.subjects_.add(subjects);
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

	
}
