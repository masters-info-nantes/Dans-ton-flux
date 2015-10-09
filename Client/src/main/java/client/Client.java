package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.interfaces.middleware.InterfaceDisplayClient;
import com.interfaces.middleware.InterfaceServerForum;
import com.interfaces.middleware.InterfaceSubjectDiscussion;
import com.sun.javafx.collections.MappingChange.Map;

public class Client implements InterfaceDisplayClient {

	/** forum = la totalité des forums **/
	private static InterfaceServerForum forum;
	private static InterfaceSubjectDiscussion sujet;
	private static Registry registry;
	private static Map<String, InterfaceSubjectDiscussion> subcribedTopics;
	
	private static String userLogin = "shsfhsfgh";
	
	public static void main(String[] args) throws Exception{
		new Thread() {
            @Override
            public void run() {
                javafx.application.Application.launch(MainWindow.class);
            }
        }.start();
		registry = LocateRegistry.getRegistry("localhost", 8080);
		forum = (InterfaceServerForum)registry.lookup("Forum");
		//forum.registrationOnForum(userLogin, "mdp");	
		forum.sendSubject(userLogin, "Soiree");

	}
	
	@Override
	public void show(String subjectTitle, String userMessage) throws RemoteException {
		// TODO Auto-generated method stub
		// ajoute le message au sujet donné
		
	}
	
	public void connexion() throws RemoteException {
		Object[] subjects = forum.connexion(userLogin, "mdp", this);
		for(Object o : subjects) {
			InterfaceSubjectDiscussion temp = (InterfaceSubjectDiscussion)o;
			subcribedTopics.put(temp.getTitle(),temp);
		}
	}
	
	public static void messageSend(String topicTitle, String userMessage) throws RemoteException{
		Object[] titres = forum.getTitlesOfSubjects();
		
		sujet.broadcastMessage(userMessage, userLogin);
		
	};
	
	public static void subscribeTopic(String topicTitle) throws RemoteException {
		sujet = forum.registrationOnSubject(userLogin, topicTitle);

	}

}
