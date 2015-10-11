package com.interfaces.middleware;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceDisplayClient extends Remote{
	
	/*
	 * Allow to display a new message for a subject
	 */
	public void showMessage(String sujet, String message, String author, String Date) throws RemoteException;
	
	/*
	 * ALlow to display a new subject
	 */
	public void showSubject(String subject) throws RemoteException;

}
