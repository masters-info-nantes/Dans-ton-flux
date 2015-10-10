package client;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.interfaces.middleware.InterfaceServerForum;
import com.interfaces.middleware.InterfaceSubjectDiscussion;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
 
public class MainWindow extends Application {
    
	Client client;
	InterfaceServerForum forum;
	List<String> subscribeTitle;
	static String selectedNoSubscribeTopic = "";
	static String selectedSubscribeTopic = "";
	ObservableList<String> subscribeTopics;

	
	public MainWindow(Client client, InterfaceServerForum forum){
		this.client = client;
		this.forum = forum;
    	subscribeTitle = new ArrayList<String>();

	}
	public static void notify (String title, String message){
		
	}
    @Override
    public void start(Stage primaryStage) throws RemoteException {
   	
    	/** Boutons d'abonnement**/
    	final Button subscribeBtn = new Button("S'abonner");
    	subscribeBtn.setDisable(true);
    	subscribeBtn.setMinWidth(120.0);
    	subscribeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	boolean sub = false;
            	/** TODO test si inscrit, ne marche pas now **/
            	if(sub) {
            		System.out.println("unsubscribe");
            		sub = false;
            		subscribeBtn.setText("Se desabonner");
            	} else {
            		System.out.println("subscribe    "+selectedNoSubscribeTopic);
            		try {
						 InterfaceSubjectDiscussion temp = forum.registrationOnSubject(client.getUserLogin(), selectedNoSubscribeTopic);
						 client.putSubject(temp);
						 subscribeTitle.add(temp.getTitle());
						 subscribeTopics.add(temp.getTitle());
						 
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            		sub = true;
            		subscribeBtn.setText("S'abonner");
            	}
            }
        });
    	
    	/** Zone du forum**/
    	final TextArea fluxMessages = new TextArea("Ici le flux du forum");
    	fluxMessages.setDisable(true);
    	fluxMessages.setMinWidth(600);
    	fluxMessages.setMinHeight(697);
    	
    	/** Zone de tape du message de l'utilisateur**/
    	/* Champ de texte*/
    	final TextField userMessage = new TextField();
    	userMessage.setPrefWidth(500);
    
    	/* Boutton d'envoi */
    	final Button sendBtn = new Button("Envoyer");
    	sendBtn.setMinWidth(120.0);
    	sendBtn.setMaxWidth(120.0);
    	sendBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Message send");
                try {
					Client.messageSend("Soiree",userMessage.getText());
					client.toString();
					client.getSubject(selectedSubscribeTopic).broadcastMessage(selectedSubscribeTopic, userMessage.getText());
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    	/* Grid des messages */
    	final GridPane messagePane = new GridPane();
    	messagePane.add(subscribeBtn,3,0);
    	messagePane.add(fluxMessages,0,1,4,1);
    	messagePane.add(userMessage,0,2,3,1);
    	messagePane.add(sendBtn,3,2);
    	
    	/** Liste des Sujets latérale**/
    	/* Tout les sujets*/
    	final ListView<String> topicList = new ListView<String>();
    	final List<String> topicsTitle = new ArrayList<String>();
    	Object[] titles = forum.getTitlesOfSubjects();
    	for(Object o: titles){
    	    System.out.println((String)o);
    	    topicsTitle.add((String)o);
    	} 
    	final ObservableList<String> topics = FXCollections.observableArrayList(topicsTitle);
    	topicList.setItems(topics);
    	topicList.setPrefWidth(200);
    	topicList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				System.out.println(selectedNoSubscribeTopic);
				MainWindow.setNoSubscribeTopic(topicList.getSelectionModel().getSelectedItem());
	            System.out.println("clicked on " + topicList.getSelectionModel().getSelectedItem());
	            subscribeBtn.setDisable(false);
			}
		});
    	
    	/* Abonnements */
    	final ListView<String> subscribeList = new ListView<String>();
    	Object[] subscribeTitles = client.getSubscirbeTitles();
    	for(Object o: subscribeTitles){
    	    subscribeTitle.add((String)o);
    	} 
    	subscribeTopics = FXCollections.observableArrayList(subscribeTitle);
    	subscribeList.setItems(subscribeTopics);
    	subscribeList.setPrefWidth(200);
    	subscribeList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				MainWindow.setSubscribeTopic(subscribeList.getSelectionModel().getSelectedItem());
	            System.out.println("clicked on " + subscribeList.getSelectionModel().getSelectedItem());
			}
		});
    	/* Grid des sujets */
    	final GridPane topicPane = new GridPane();
    	topicPane.add(new Label("Sujets"),0,0);
    	topicPane.add(topicList,0,1);
    	topicPane.add(new Label("Abonnements"),0,2);
    	topicPane.add(subscribeList,0,3);

    	/** Pane principal**/
    	BorderPane rootPane =  new BorderPane();
    	rootPane.setRight(messagePane);
    	rootPane.setLeft(topicPane);
    	
    	/** Fenetre **/
    	final Scene scene = new Scene(rootPane, 820, 750);
        primaryStage.setTitle("Dans ton Flux");
    	primaryStage.setScene(scene);
    	primaryStage.setResizable(false);
        primaryStage.show();
    }
	protected static void setSubscribeTopic(String selectedItem) {
		selectedSubscribeTopic = selectedItem;	
	}
	
	public static void setNoSubscribeTopic(String selectedItem) {
		selectedNoSubscribeTopic = selectedItem;
	}

 
}