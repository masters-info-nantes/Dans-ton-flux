package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

import com.interfaces.middleware.InterfacesClientServer.*;

public class Message extends UnicastRemoteObject implements InterfaceMessage, Comparable<Message>{
	
	private String message_;
	private String author_;
	private Date date_;

	protected Message(String message, String author, Date date) throws RemoteException {
		super();
		this.message_ = message;
		this.author_ = author;
		this.date_ = date;
	}

	@Override
	public String getAuthor() throws RemoteException {
		// TODO Auto-generated method stub
		return author_;
	}

	@Override
	public Date getDate() throws RemoteException {
		// TODO Auto-generated method stub
		return date_;
	}

	@Override
	public String getMessage() throws RemoteException {
		// TODO Auto-generated method stub
		return message_;
	}

	@Override
	public int compareTo(Message message) {
		int comp = 0;
		try {
			comp = this.date_.compareTo(message.getDate());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return comp;
	}

}
