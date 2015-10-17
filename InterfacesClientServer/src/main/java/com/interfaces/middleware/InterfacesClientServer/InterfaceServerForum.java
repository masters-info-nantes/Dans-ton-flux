package com.interfaces.middleware.InterfacesClientServer;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface InterfaceServerForum extends Remote{
	
	/*
	 * return the subject with the title "title"
	 */
	public InterfaceSubjectDiscussion registrationOnSubject(String login, String title) throws RemoteException, SubjectDidNotExistsException, ClientDidNotExistsException, ClientAlreadyRegisteredException;
	/*
	 * the type of the elements in the array is String
	 */
	public Object[] getTitlesOfSubjects() throws RemoteException;
	/*
	 * allow to get back the subject a user follow when he wants to connect on the forum
	 * the type of the elements in the array is InterfaceSubjectDiscussion
	 * return null if the client doesn't exists.
	 */
	public Object[] connexion(String login, String password, InterfaceDisplayClient display) throws RemoteException, WrongClientException, WrongPasswordException;
	/*
	 * return false if the subject already exists or if the user doesn't exist
	 */
	public void sendSubject(String author, String title) throws RemoteException, SubjectAlreadyExistsException;
	/*
	 * return false if the client already exists
	 */
	public void registrationOnForum(String login, String password) throws RemoteException, WrongClientException, WrongPasswordException, ClientAlreadyRegisteredException;
	/*
	 * return false if the client don't follow the subject
	 */
	public void deRegistrationOnSubject(String login, String title) throws RemoteException, ClientDidNotExistsException, SubjectDidNotExistsException;
	/*
	 * return false if the client isn't the author of the subject
	 */
	public void deleteSubject(String author, String title) throws RemoteException, SubjectDidNotExistsException, DeletionPermitionDeletionException;
}
