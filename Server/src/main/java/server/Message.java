package server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;

import com.interfaces.middleware.InterfacesClientServer.*;

/**
 * 
 * @author Franck
 * This class is used to manege messages, it implements comparable interface. Messages are storted with the date order, the oldest element arrives in first position.
 */
@SuppressWarnings("serial")
public class Message extends UnicastRemoteObject implements InterfaceMessage, Comparable<Message>, Serializable{
	
	private String message_;
	private String author_;
	private Calendar date_;

	protected Message(String message, String author, Calendar date) throws RemoteException {
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
	public Calendar getDate() throws RemoteException {
		// TODO Auto-generated method stub
		return date_;
	}

	@Override
	public String getMessage() throws RemoteException {
		// TODO Auto-generated method stub
		return message_;
	}

	/**
	 * Compare messages with date order
	 * @param message message is compared to this
	 */
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
