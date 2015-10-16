package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.Permission;

public class MainClient {
	
	public static void main(String[] args) throws RemoteException, NotBoundException {
		// TODO Auto-generated method stub	
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager() {
				@Override
				public void checkPermission(Permission perm) {
					return;
				}
			});
		}
		new Thread() {
            @Override
            public void run() {
                //javafx.application.Application.launch(MainWindow.class);
            	javafx.application.Application.launch(LoginWindow.class);
            }
        }.start();
	}

}