package server;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.Permission;


public class Server {

	public static void main(String[] args) {
		Forum forum;
		
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager() {
				@Override
				public void checkPermission(Permission perm) {
					return;
				}
			});
		}
		
		try {
			forum = new Forum();
			Registry registry = LocateRegistry.createRegistry(8080);
			registry.bind("Forum", forum);
			System.out.println("Server start");
			while(true){
				
			}
		} catch (RemoteException | AlreadyBoundException e) {
			e.printStackTrace();
		}
	}
}