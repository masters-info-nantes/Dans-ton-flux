package client;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import com.interfaces.middleware.InterfacesClientServer.InterfaceDisplayClient;
import com.interfaces.middleware.InterfacesClientServer.InterfaceSubjectDiscussion;



public class Client extends UnicastRemoteObject implements InterfaceDisplayClient, Serializable {


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
	
	public boolean isSubscribed(String title){
		return subcribedTopics.containsKey(title);
	}
	@Override
	public void showMessage(String subjectTitle, String userMessage, String author, String date) throws RemoteException {
		// TODO Auto-generated method stub
		// ajoute le message au sujet donné
		MainWindow.notifyMessage(subjectTitle, userMessage, author, date);
		
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
	
	
	public Object[] getSubscirbeTitles() {
		// TODO Auto-generated method stub
		return subcribedTopics.keySet().toArray();
	}

	@Override
	public void showSubject(String title) throws RemoteException {
		MainWindow.notifySubject(title);
	}

	public void deRegistration(String selectedItem) {
		this.subcribedTopics.remove(selectedItem);
	}

	@Override
	public void deleteSubject(String title) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}