package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class MainClient {
	
	public static void main(String[] args) throws RemoteException, NotBoundException {
		// TODO Auto-generated method stub		
		new Thread() {
            @Override
            public void run() {
                //javafx.application.Application.launch(MainWindow.class);
            	javafx.application.Application.launch(LoginWindow.class);
            }
        }.start();
	}

}