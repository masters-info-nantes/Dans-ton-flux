package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

import com.interfaces.middleware.InterfaceDisplayClient;
import com.interfaces.middleware.InterfaceServerForum;
import com.interfaces.middleware.InterfaceSubjectDiscussion;

public class Client implements InterfaceDisplayClient {


	String userLogin;
	Object[] subjects = null;
	
	public void setUserLogin(String login){
		userLogin = login;
	}
	
	public void setSubject(Object[] sub){
		subjects = sub;
	}
	@Override
	public void show(String subjectTitle, String userMessage) throws RemoteException {
		// TODO Auto-generated method stub
		// ajoute le message au sujet donné
		MainWindow.notify(subjectTitle, userMessage);
		
	}
	
	public void connexion() throws RemoteException {
		/*Object[] subjects = forum.connexion(userLogin, "mdp", this);
		for(Object o : subjects) {
			InterfaceSubjectDiscussion temp = (InterfaceSubjectDiscussion)o;
			subcribedTopics.put(temp.getTitle(),temp);
		}*/
	}
	
	public static void messageSend(String topicTitle, String userMessage) throws RemoteException{
		/*Object[] titres = forum.getTitlesOfSubjects();
		
		sujet.broadcastMessage(userMessage, userLogin);*/
		
	};
	
	public static void subscribeTopic(String topicTitle) throws RemoteException {
		//sujet = forum.registrationOnSubject(userLogin, topicTitle);

	}

}
