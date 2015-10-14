package com.interfaces.middleware.InterfacesClientServer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface InterfaceMessage extends Remote{
	
	public Date getDate() throws RemoteException;
	public String getAuthor() throws RemoteException;
	public String getMessage() throws RemoteException;

}
