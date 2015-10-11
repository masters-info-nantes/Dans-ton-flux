package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

import com.interfaces.middleware.InterfaceServerForum;
import com.interfaces.middleware.InterfaceSubjectDiscussion;

public class MainClient {
	
	public static void main(String[] args) throws RemoteException, NotBoundException {
		// TODO Auto-generated method stub
		
		Client client = new Client();
		String userLogin = "Dieu";		
		
		new Thread() {
            @Override
            public void run() {
                //javafx.application.Application.launch(MainWindow.class);
            	javafx.application.Application.launch(LoginWindow.class);
            }
        }.start();
        
    	/** forum = la totalité des forums/sujets **/
        InterfaceServerForum forum;
    	InterfaceSubjectDiscussion subject;
    	Registry registry;
    	
    	// TODO Doublon! regarder lequel est indispensable
		registry = LocateRegistry.getRegistry("localhost", 8080);
		forum = (InterfaceServerForum)registry.lookup("Forum");
		//forum.registrationOnForum(userLogin, "mdp");
		client.connexion();
		forum.sendSubject(userLogin, "Soiree");
		forum.sendSubject(userLogin, "Jeux");
		forum.deRegistrationOnSubject(userLogin, "Soiree");

	}

}