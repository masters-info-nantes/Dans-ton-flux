package server;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Server {

	public static void main(String[] args) {
		Forum forum;
		try {
			forum = new Forum();
			Registry registry = LocateRegistry.createRegistry(8080);
			registry.bind("Forum", forum);
			System.out.println("Server start");
		} catch (RemoteException | AlreadyBoundException e) {
			e.printStackTrace();
		}
	}
}
