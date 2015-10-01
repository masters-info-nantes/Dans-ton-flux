package server;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.*;


public class Server {

	public static void main(String[] args) {
		Forum forum;
		try {
			forum = new Forum();
			Registry registry = LocateRegistry.createRegistry(8080);
			registry.bind("Forum", forum);
			System.out.println("Start");
		} catch (RemoteException | AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
