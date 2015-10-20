package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.Permission;

public class MainClient {
	
	public static void main(String[] args) throws RemoteException, NotBoundException {
		new Thread() {
            @Override
            public void run() {
            	javafx.application.Application.launch(LoginWindow.class);
            }
        }.start();
	}

}