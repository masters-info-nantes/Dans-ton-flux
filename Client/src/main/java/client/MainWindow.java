package client;

import java.rmi.RemoteException;

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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
 
public class MainWindow extends Application {
    
	Boolean sub = false;
	
    @Override
    public void start(Stage primaryStage) {
   	
    	/** Boutons d'abonnement**/
    	final Button subscribeBtn = new Button("S'abonner");
    	subscribeBtn.setDisable(true);
    	subscribeBtn.setMinWidth(120.0);
    	subscribeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if(sub) {
            		System.out.println("unsubscribe");
            		sub = false;
            		subscribeBtn.setText("Se désabonner");
            	} else {
            		System.out.println("subscribe");
            		sub = true;
            		subscribeBtn.setText("S'abonner");
            	}
            }
        });
    	
    	/** Zone du forum**/
    	final TextArea fluxMessages = new TextArea("Ici le flux du forum");
    	fluxMessages.setDisable(true);
    	
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
    	final ObservableList<String> topics =FXCollections.observableArrayList (
    	    "1er sujet", "2eme sujet", "3eme sujet");
    	topicList.setItems(topics);
    	//topicList.setPrefWidth(175);
    	//topicList.setPrefHeight(350);
    	topicList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
	            System.out.println("clicked on " + topicList.getSelectionModel().getSelectedItem());
	            subscribeBtn.setDisable(false);
			}
		});
    	
    	/* Abonnements */
    	final ListView<String> subscribeList = new ListView<String>();
    	final ObservableList<String> subscribeTopics = FXCollections.observableArrayList(
    			"mon sujet");
    	subscribeList.setItems(subscribeTopics);
    	//subscribeList.setPrefWidth(175);
    	//subscribeList.setPrefHeight(350);
    	subscribeList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
	            System.out.println("clicked on " + topicList.getSelectionModel().getSelectedItem());
			}
		});
    	/* Grid des sujets */
    	final GridPane topicPane = new GridPane();
    	topicPane.add(new Label("Sujets"),0,0);
    	topicPane.add(topicList,0,1);
    	topicPane.add(new Label("Abonnements"),0,2);
    	topicPane.add(subscribeList,0,3);

    	/** Pane principal**/
    	GridPane rootPane =  new GridPane();
    	rootPane.add(topicPane,0,0);
    	rootPane.add(messagePane,1,0);
    	
    	/** Fenetre **/
    	final Scene scene = new Scene(rootPane, 1200, 500);
        primaryStage.setTitle("Dans ton Flux");
    	primaryStage.setScene(scene);
        primaryStage.show();
    }
 public static void main(String[] args) {
        launch(args);
    }
 
}