package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.*;
import java.rmi.server.*;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.interfaces.middleware.InterfaceDisplayClient;
import com.interfaces.middleware.InterfaceServerForum;
import com.interfaces.middleware.InterfaceSubjectDiscussion;

public class Forum extends UnicastRemoteObject implements InterfaceServerForum{
	
	Map<String, Subject> subjects;
	Map<String, Client> clients;

	protected Forum() throws RemoteException {
		super();
		subjects = new TreeMap<String, Subject>();
		clients = new TreeMap<String, Client>();
		getBackSubjects();
		getBackClients();

	}
	
	public void getBackSubjects(){
		/*
		 * Get titles of existing subjects
		 */
		try{
			BufferedReader fileSubjectsTitles = new BufferedReader(new FileReader("src/main/resources/titles_of_subjects.txt"));
				
			String ligne = fileSubjectsTitles.readLine();
			while (ligne != null){
				String[] info = ligne.split("\\|");
				subjects.put(info[0], new Subject(info[0], info[1]));
				ligne = fileSubjectsTitles.readLine();
			}
			fileSubjectsTitles.close();
			
			/*
			 * For all the subjects we get the messages
			 */
			for(Map.Entry<String, Subject> entry : subjects.entrySet()){
				BufferedReader subject = new BufferedReader(new FileReader("src/main/resources/" + entry.getKey() + ".txt"));
				ligne = subject.readLine();
				while (ligne != null){
					String[] info = ligne.split("\\|");
					Subject sub = subjects.get(entry.getKey());
					sub.addMessage(info[0], info[1], new Date(Long.parseLong(info[2])));
					subjects.put(entry.getKey(), sub);
					
					ligne = subject.readLine();
				}
				subject.close();
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getBackClients(){
		try {			
			/*
			 * Get the name of clients
			 */
			BufferedReader fileClientsTitles = new BufferedReader(new FileReader("src/main/resources/list_of_clients.txt"));
			String ligne = fileClientsTitles.readLine();
			while (ligne != null){
				String[] id = ligne.split("\\|");
				clients.put(id[0], new Client(id[0], id[1]));
				ligne = fileClientsTitles.readLine();
			}
			fileClientsTitles.close();
			
			/*
			 * For every client we get his subjects
			 */
			for(Map.Entry<String, Client> entry : clients.entrySet()){
				BufferedReader client = new BufferedReader(new FileReader("src/main/resources/" + entry.getKey() + ".txt"));
				ligne = client.readLine();
				while (ligne != null){
					Client c = clients.get(entry.getKey());
					c.addSubject(subjects.get(ligne));
					subjects.get(ligne).registration(c);
					ligne = client.readLine();
				}
				client.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Object[] getTitlesOfSubjects() throws RemoteException {
		return subjects.keySet().toArray();
	}

	
	public boolean sendSubject(String author, String title) throws RemoteException {
		if(!subjects.containsKey(title)){
			subjects.put(title, new Subject(title, author));
			try {
				BufferedWriter file = new BufferedWriter(new FileWriter("src/main/resources/titles_of_subjects.txt", true));
				file.write(title + "|" + author + "\n");
				file.close();
				BufferedWriter writer = new BufferedWriter(new FileWriter(new File("src/main/resources/" + title + ".txt")));
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		else{
			return false;
		}
	}
	
	public Object[] connexion(String login, String password, InterfaceDisplayClient display) throws RemoteException{

		Object[] o = null;
		if(clients.get(login).getInter() != null && clients.get(login).getPassword_().equals(password)){
			clients.get(login).setInter(display);
			o = clients.get(login).getSubjects().toArray();
		}
		else{
			try {
				throw new Exception("The Client is already connected");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return o;
	}
	
	public boolean registrationOnForum(String login, String password) throws RemoteException{
		if(!clients.containsKey(login)){
			BufferedWriter file;
			try {
				file = new BufferedWriter(new FileWriter("src/main/resources/list_of_clients.txt", true));
				file.write(login + "|" + password + "\n");
				file.close();
				BufferedWriter writer = new BufferedWriter(new FileWriter(new File("src/main/resources/" + login + ".txt"), true));
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			clients.put(login, new Client(login, password));
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean deRegistrationOnSubject(String login, String title) throws RemoteException{
		if(clients.containsKey(login) && subjects.containsKey(title)){
			clients.get(login).getSubjects().remove(title);
			subjects.get(title).deRegistration(login);
			
			for(Map.Entry<String, Subject> entry :subjects.entrySet()){
				System.out.println(entry.getKey() + "  " + entry.getValue());
			}
			return true;
		}
		else{
			return false;
		}
	}
	
	public InterfaceSubjectDiscussion registrationOnSubject(String login, String title) throws RemoteException{
		if(clients.containsKey(login) && subjects.containsKey(title)){
			clients.get(login).getSubjects().add(subjects.get(title));		
			try {
				boolean registration = subjects.get(title).registration(clients.get(login));
				
				if(!registration){
					return null;
				}
				else{
					BufferedWriter writer;
					try {
						writer = new BufferedWriter(new FileWriter(new File("src/main/resources/" + login + ".txt"), true));
						writer.write(title + "\n");
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			return subjects.get(title);
		}
		else{
			return null;
		}
	}

	@Override
	public boolean deleteSubject(String author, String title) throws RemoteException {
		if(subjects.get(title).getAuthor().equals(author)){
			subjects.remove(title);
			return true;
		}
		else{
			return false;
		}
	}

	public void broadcastSubject(String subject){
		for(Map.Entry<String, Client> entry : clients.entrySet()){
			try {
				entry.getValue().getInter().showSubject(subject);
			} catch (RemoteException e) {
				entry.getValue().setInter(null);
			}
		}
	}

}
