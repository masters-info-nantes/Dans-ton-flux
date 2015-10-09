package com.interfaces.middleware;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceSubjectDiscussion extends Remote{

	/*
	 * Send the message to all the client except for the person who send it and store it
	 */
	public void broadcastMessage(String message, String author) throws RemoteException;
	/*
	 * get all the messages of the subject (type of elements: Message)
	 */
	public Object[] getMessages() throws RemoteException;
	public String getAuthor() throws RemoteException;
	public String getTitle() throws RemoteException;
}
