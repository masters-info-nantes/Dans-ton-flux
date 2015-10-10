package client;

import java.rmi.RemoteException;
import java.util.Arrays;

import com.interfaces.middleware.InterfaceDisplayClient;

public class Client implements InterfaceDisplayClient {


	String userLogin;
	Object[] subjects = null;
	
	public void setUserLogin(String login){
		userLogin = login;
	}
	
	public void setSubject(Object[] sub){
		subjects = sub;
	}
	
	public Object[] getSubject(){
		return subjects;
	}
	
	public boolean isSubscribed(Object o){
		for(Object obj:subjects){
			if (obj.equals(o)) 
				return true;
		}
		return false;
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