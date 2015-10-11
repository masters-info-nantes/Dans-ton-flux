package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import com.interfaces.middleware.InterfaceDisplayClient;
import com.interfaces.middleware.InterfaceSubjectDiscussion;

public class Client extends UnicastRemoteObject implements InterfaceDisplayClient {


	String userLogin;
	Map<String, InterfaceSubjectDiscussion> subcribedTopics;
	
	protected Client() throws RemoteException {
		super();
		subcribedTopics = new TreeMap<String, InterfaceSubjectDiscussion>();
	}

	public String getUserLogin(){
		return userLogin;
	}
	
	public void setUserLogin(String login){
		userLogin = login;
	}
	
	public void setSubject(Object[] sub){
		for(Object o: sub){
			InterfaceSubjectDiscussion temp = (InterfaceSubjectDiscussion) o;
			try {
				subcribedTopics.put(temp.getTitle(), temp);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public InterfaceSubjectDiscussion getSubject(String title){
		return subcribedTopics.get(title);
	}
	
	public void putSubject(InterfaceSubjectDiscussion subject){
		try {
			subcribedTopics.put(subject.getTitle(), subject);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*public boolean isSubscribed(Object o){
		for(Object obj:subjects){
			if (obj.equals(o)) 
				return true;
		}
		return false;
	}*/
	@Override
	public void showMessage(String subjectTitle, String userMessage) throws RemoteException {
		// TODO Auto-generated method stub
		// ajoute le message au sujet donné
		MainWindow.notifyMessage(subjectTitle, userMessage);
		
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

	public Object[] getSubscirbeTitles() {
		// TODO Auto-generated method stub
		return subcribedTopics.keySet().toArray();
	}

	@Override
	public void showSubject(String title) throws RemoteException {
		MainWindow.notifySubject(title);
	}

}