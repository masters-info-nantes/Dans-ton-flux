package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

import com.interfaces.middleware.InterfaceDisplayClient;
import com.interfaces.middleware.InterfaceServerForum;
import com.interfaces.middleware.InterfaceSubjectDiscussion;

public class Client implements InterfaceDisplayClient {

	@Override
	public void show(String subjectTitle, String userMessage) throws RemoteException {
		// TODO Auto-generated method stub
		// ajoute le message au sujet donné -> notifie le client
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
		
		subject.broadcastMessage(userMessage, userLogin);*/
		
	};
	
	public static void subscribeTopic(String topicTitle) throws RemoteException {
		//subject = forum.registrationOnSubject(userLogin, topicTitle);

	}

}
