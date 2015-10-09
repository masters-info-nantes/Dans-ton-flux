package client;

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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
 
public class MainWindow extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        /*btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });*/
    	/** Liste des Sujets**/
    	/* Tout les sujets*/
    	ListView<String> topicList = new ListView<String>();
    	ObservableList<String> topics =FXCollections.observableArrayList (
    	    "1er sujet", "2eme sujet", "3eme sujet");
    	topicList.setItems(topics);
    	topicList.setPrefWidth(175);
    	topicList.setPrefHeight(350);
    	/* Abonnements */
    	ListView<String> subscribeList = new ListView<String>();
    	ObservableList<String> subscribeTopics = FXCollections.observableArrayList(
    			"mon sujet");
    	subscribeList.setItems(subscribeTopics);
    	topicList.setPrefWidth(175);
    	topicList.setPrefHeight(350);
    	/* Boite des sujets */
    	VBox topicBox = new VBox();
    	VBox.setVgrow(topicList, Priority.ALWAYS);
    	VBox.setVgrow(subscribeList, Priority.ALWAYS);
    	topicBox.getChildren().addAll(new Label("Sujets"), topicList, new Label("Abonnements"), subscribeList);

    	/** Zone des messages**/
    	TextArea fluxMessages = new TextArea("Ici le flux du forum");
    	fluxMessages.setDisable(true);
    	
    	/** Zone de tape du message de l'utilisateur**/
    	/* Champ de texte*/
    	TextArea userMessage = new TextArea();
    	/* Boutton d'envoi */
    	Button sendBtn = new Button("Envoyer");
    	sendBtn.setMaxWidth(Double.MAX_VALUE);
    	/* Box de message*/
    	HBox sendBox = new HBox();
    	HBox.setHgrow(userMessage, Priority.ALWAYS);
    	HBox.setHgrow(sendBtn, Priority.ALWAYS);
    	sendBox.getChildren().addAll(userMessage, sendBtn);
    	
    	/** Zone de Message**/
    	VBox messageBox = new VBox();
    	messageBox.getChildren().addAll(fluxMessages, sendBox);
    	
    	/** Pane principal**/
    	BorderPane rootPane =  new BorderPane();
    	rootPane.setLeft(topicBox);
    	rootPane.setCenter(messageBox);
    	
    	/** Fenetre **/
    	Scene scene = new Scene(rootPane, 600, 500);
        primaryStage.setTitle("Dans ton Flux");
    	primaryStage.setScene(scene);
        primaryStage.show();
    }
 public static void main(String[] args) {
        launch(args);
    }
}