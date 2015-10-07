package com.interfaces.middleware;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceDisplayClient extends Remote{
	
	/*
	 * Allow to display a new message for a subject
	 */
	public void show(String sujet, String Message) throws RemoteException;

}
