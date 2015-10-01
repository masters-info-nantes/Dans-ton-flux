package server;

import java.rmi.*;
import java.rmi.server.*;
import java.util.Map;
import java.util.TreeMap;

import com.interfaces.middleware.InterfaceServerForum;
import com.interfaces.middleware.InterfaceSubjectDiscussion;

public class Forum extends UnicastRemoteObject implements InterfaceServerForum{
	
	Map<String, InterfaceSubjectDiscussion> subjects;

	protected Forum() throws RemoteException {
		super();
		subjects = new TreeMap<String, InterfaceSubjectDiscussion>();
	}

	@Override
	public InterfaceSubjectDiscussion getSujet(String title) throws RemoteException {
		return subjects.get(title);
	}

	@Override
	public Object[] getTitlesOfSubjects() throws RemoteException {
		return subjects.keySet().toArray();
	}

	@Override
	public void sendSubject(String title) throws RemoteException {
		subjects.put(title, new Subject(title));		
	}
}
