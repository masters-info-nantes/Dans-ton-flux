package server;

import com.interfaces.middleware.InterfaceDisplayClient;

public class Client{
	
	String name;
	InterfaceDisplayClient inter;
	
	public Client(String name, InterfaceDisplayClient inter) {
		super();
		this.name = name;
		this.inter = inter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InterfaceDisplayClient getInter() {
		return inter;
	}

	public void setInter(InterfaceDisplayClient inter) {
		this.inter = inter;
	} 

	
}
