package client;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;

import com.interfaces.middleware.InterfacesClientServer.ClientAlreadyRegisteredException;
import com.interfaces.middleware.InterfacesClientServer.ClientDidNotExistsException;
import com.interfaces.middleware.InterfacesClientServer.DeletionPermitionDeletionException;
import com.interfaces.middleware.InterfacesClientServer.InterfaceMessage;
import com.interfaces.middleware.InterfacesClientServer.InterfaceServerForum;
import com.interfaces.middleware.InterfacesClientServer.InterfaceSubjectDiscussion;
import com.interfaces.middleware.InterfacesClientServer.SubjectAlreadyExistsException;
import com.interfaces.middleware.InterfacesClientServer.SubjectDidNotExistsException;

public class MainWindow extends Application {
    
	private static Client client;
	InterfaceServerForum forum;
	List<String> subscribeTitle;
	static String selectedNoSubscribeTopic = "";
	static String selectedSubscribeTopic = "";
	static ObservableList<String> subscribeTopics;
	static ObservableList<String> topics;
	static ObservableList<Message> messagesDisplay = FXCollections.observableArrayList();
	Button subscribeBtn;
	GridPane messagePane;
	static int i;
	private TableView<Message> tableOfMessages = new TableView<Message>();
	GridPane topicPane;
	static ListView<String> subscribeList;
	static ListView<String> topicList;
	private Stage primaryStage;
	
	public MainWindow(Client client, InterfaceServerForum forum){
		MainWindow.client = client;
		this.forum = forum;
    	subscribeTitle = new ArrayList<String>();
	}
	
	public static void notifyMessage (String title, InterfaceMessage message) throws RemoteException{
		if(subscribeList.getSelectionModel().getSelectedItem().equals(title)){
			messagesDisplay.add(new Message(message.getDate(), message.getAuthor(), message.getMessage()));
		}
	}
	
	public static void notifySubject (String title){
		topics.add(title);
	}
	
	
    @Override
    public void start(Stage primaryStage) throws RemoteException {

    	addSubscribeBtn();
    	addForumZone();
    	messageSendZone();
    	subjectsZone();
    	
    	/** Pane principal**/
    	BorderPane root =  new BorderPane();
    	root.setRight(messagePane);
    	root.setLeft(topicPane);

    	
    	/** Fenetre **/
    	final Scene scene = new Scene(root, 860, 780);
    	scene.getStylesheets().add(MainWindow.class.getResource("/MainWindow.css").toExternalForm());
    	
		//window on the middle of the screen
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		primaryStage.setX((screenBounds.getWidth() - 860) / 2); 
		primaryStage.setY((screenBounds.getHeight() - 780) / 2);
        
    	this.primaryStage = primaryStage;
        primaryStage.setTitle("Dans ton Flux");
    	primaryStage.setScene(scene);
    	primaryStage.setResizable(false);
        primaryStage.show();
        
    }
    
    public void addSubscribeBtn(){
    	/** Boutons d'abonnement**/
    	subscribeBtn = new Button("S'abonner");
    	subscribeBtn.setDisable(true);
    	subscribeBtn.setMinWidth(200.0);
    	subscribeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if(getClient().isSubscribed(topicList.getSelectionModel().getSelectedItem())) {
            		subscribeBtn.setText("S'abonner");
            		getClient().deRegistration(topicList.getSelectionModel().getSelectedItem());
            		MainWindow.subscribeTopics.remove(topicList.getSelectionModel().getSelectedItem());
            		subscribeTitle.remove(topicList.getSelectionModel().getSelectedItem());
            		try {
						try {
							forum.deRegistrationOnSubject(getClient().getUserLogin(), topicList.getSelectionModel().getSelectedItem());
						} catch (SubjectDidNotExistsException e) {
							e.printStackTrace();
						} catch (ClientDidNotExistsException e) {
							e.printStackTrace();
						}
					} catch (RemoteException e) {
						// TODO popup probleme de connexion => on revient à la fenêtre connexion
						e.printStackTrace();
					}
            		
            	} else {
            		System.out.println("subscribe    "+selectedNoSubscribeTopic);
            		try {
						try {
							InterfaceSubjectDiscussion temp = forum.registrationOnSubject(getClient().getUserLogin(), selectedNoSubscribeTopic);
							 getClient().putSubject(temp);
							 subscribeTitle.add(temp.getTitle());
							 subscribeTopics.add(temp.getTitle());
							 subscribeBtn.setText("Se desabonner");
						} catch (SubjectDidNotExistsException
								| ClientDidNotExistsException
								| ClientAlreadyRegisteredException e) {
							e.printStackTrace();
						}

						 
					} catch (RemoteException e) {
						// TODO popup probleme de connexion => on revient à la fenêtre connexion
						e.printStackTrace();
					}
            		catch(NullPointerException e2){
            			e2.printStackTrace();
            		}
            	}
            }
        });
	}
    
    @SuppressWarnings("unchecked")
	public void addForumZone(){
		/** Zone du forum**/
		tableOfMessages.setEditable(true);
		messagesDisplay.setAll(messagesDisplay.sorted());
		tableOfMessages.setMinWidth(600);
		tableOfMessages.setMaxWidth(600);
    	tableOfMessages.setMinHeight(700);
    	tableOfMessages.setMaxHeight(700);
     	
        TableColumn<Message, String> dateColumn = new TableColumn<Message, String>("Date");
        dateColumn.setMinWidth(150);
        dateColumn.setMaxWidth(150);
        dateColumn.setCellValueFactory(new PropertyValueFactory<Message, String>("date"));
         
        TableColumn<Message, String> authorColumn = new TableColumn<Message, String>("Auteur");
        authorColumn.setMinWidth(100);
        authorColumn.setMaxWidth(100);
        authorColumn.setCellValueFactory(new PropertyValueFactory<Message, String>("author"));
         
        TableColumn<Message, String> messageColumn = new TableColumn<Message, String>("Message");
        messageColumn.setMinWidth(tableOfMessages.getMinWidth() - 250);
        messageColumn.setCellValueFactory(new PropertyValueFactory<Message, String>("message"));
        messageColumn.setCellFactory(new Callback<TableColumn<Message, String>, TableCell<Message,String>>() {
			@Override
			public TableCell<Message, String> call(TableColumn<Message, String> arg0) {
				final TableCell<Message, String> cell = new TableCell<Message, String>() {
					private Text text;
	                @Override
	                    
	                public void updateItem(String item, boolean empty) {
	                	super.updateItem(item, empty);
	                	if (!isEmpty()) {
	                		text = new Text(item.toString());
	                        text.setWrappingWidth(tableOfMessages.getMinWidth() - 257); // Setting the wrapping width to the Text
	                        setGraphic(text);
	                	}
	                    if(text!=null && isEmpty()){
	                    	text = null;
	                        setGraphic(text);
	                    }
	                }
				};
				return cell;
			}
        });
         
        tableOfMessages.setItems(messagesDisplay);
        tableOfMessages.getColumns().addAll(dateColumn, authorColumn, messageColumn);
    	
	}
    
    public void messageSendZone(){
    	/** Zone de tape du message de l'utilisateur**/
    	/* Champ de texte*/
    	final TextField userMessage = new TextField();
    	userMessage.setPrefWidth(500);
    
    	/* Boutton d'envoi */
    	final Button sendBtn = new Button("Envoyer");
    	sendBtn.setMinWidth(80.0);
    	sendBtn.setMaxWidth(80.0);
    	sendBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Message send");
                try {
					getClient().getSubject(selectedSubscribeTopic).broadcastMessage(userMessage.getText(), getClient().getUserLogin());
					messagesDisplay.add(new Message(GregorianCalendar.getInstance(), getClient().userLogin, userMessage.getText()));
					userMessage.setText("");
				} catch (RemoteException e) {
					// TODO popup probleme de connexion => on revient à la fenêtre connexion
					e.printStackTrace();
				}
            }
        });
    	/* Grid des messages */
    	messagePane = new GridPane();
    	messagePane.add(tableOfMessages,1,1,4,1);
    	messagePane.add(userMessage,1,2,3,1);
    	messagePane.add(sendBtn,4,2);
    	messagePane.setVgap(20);
    	messagePane.setHgap(20);
	}
    
    public void subjectsZone() throws RemoteException{
		/** Liste des Sujets latérale**/
    	/* Tout les sujets*/
    	topicList = new ListView<String>();
    	final List<String> topicsTitle = new ArrayList<String>();
    	Object[] titles = forum.getTitlesOfSubjects();
    	for(Object o: titles){
    	    topicsTitle.add((String)o);
    	} 
    	topics = FXCollections.observableArrayList(topicsTitle);
    	topicList.setItems(topics);
    	topicList.setPrefWidth(200);
    	topicList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				System.out.println(selectedNoSubscribeTopic);
				if(getClient().isSubscribed(topicList.getSelectionModel().getSelectedItem())) {
            		subscribeBtn.setText("Se desabonner");
				}
				else{
					subscribeBtn.setText("S'abonner");
				}
				MainWindow.setNoSubscribeTopic(topicList.getSelectionModel().getSelectedItem());
	            System.out.println("clicked on " + topicList.getSelectionModel().getSelectedItem());
	            subscribeBtn.setDisable(false);
			}
		});
    	
    	final Button addBtn = new Button("Ajouter");
    	addBtn.setPrefWidth(95);
    	addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(primaryStage);
                dialog.setTitle("Ajout d'un nouveau sujet");
                

            	
                VBox dialogVbox = new VBox();
                TextField title = new TextField();
                title.setMaxWidth(300);
                Button add = new Button("Ajout");
                add.setOnAction(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent arg0) {
						// TODO Auto-generated method stub
						try {
							forum.sendSubject(getClient().userLogin, title.getText());
							topics.add(title.getText());
							dialog.hide();
						} catch (RemoteException e) {
							// TODO popup probleme de connexion => on revient à la fenêtre connexion
							e.printStackTrace();
						} catch (SubjectAlreadyExistsException e) {
							// TODO message pour dire que le sujet existe déjà
							e.printStackTrace();
						}
					}
                });
                
                //add blank on the top of the dialog box
                Pane  blank = new Pane();
            	dialogVbox.getChildren().addAll(blank);
            	
                dialogVbox.getChildren().addAll(title,add);
                dialogVbox.setSpacing(10);
                
                Scene dialogScene = new Scene(dialogVbox, 300, 80);
                dialogScene.getStylesheets().add(MainWindow.class.getResource("/MainWindow.css").toExternalForm());
                dialog.setScene(dialogScene);
                dialog.show();
            }
        });
    	
    	final Button deleteBtn = new Button("Supprimer");
    	deleteBtn.setPrefWidth(95);
    	deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
					forum.deleteSubject(getClient().getUserLogin(), topicList.getSelectionModel().getSelectedItem());
					getClient().deRegistration(topicList.getSelectionModel().getSelectedItem());
					subscribeTopics.remove(topicList.getSelectionModel().getSelectedItem());
					topics.remove(topicList.getSelectionModel().getSelectedItem());
				} catch (RemoteException e) {
					// TODO popup probleme de connexion => on revient à la fenêtre connexion
					e.printStackTrace();
				} catch (SubjectDidNotExistsException e) {
					e.printStackTrace();
				} catch (DeletionPermitionDeletionException e) {
					// TODO popup pour dire que le sujet ne peut pas être supprimé, les conditions sont : être l'auteur du sujet et que le denrier message envoyé date de plus d'un an
					e.printStackTrace();
				}
            }
    	});
    	
    	final HBox buttonBox = new HBox();
    	buttonBox.getChildren().addAll(addBtn,deleteBtn);
    	buttonBox.setSpacing(10);
    	
    	/* Abonnements */
    	subscribeList = new ListView<String>();
    	Object[] subscribeTitles = getClient().getSubscirbeTitles();
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
				changeDisplayMessage(subscribeList.getSelectionModel().getSelectedItem());
	            System.out.println("clicked on " + subscribeList.getSelectionModel().getSelectedItem());
			}
		});
    	/* Grid des sujets */
    	topicPane = new GridPane();
    	Pane  blank2 = new Pane();
    	topicPane.add(blank2,1,0);
    	Label sujetLab = new Label("Sujets");
    	sujetLab.setId("Lab");
    	topicPane.add(sujetLab,1,1);
    	topicPane.add(topicList,1,2);
    	topicPane.add(buttonBox,1,3);
    	topicPane.add(subscribeBtn, 1, 4);
    	Label aboLab = new Label("Abonnements");
    	aboLab.setId("Lab");
    	topicPane.add(aboLab,1,5);
    	topicPane.add(subscribeList,1,6);
    	topicPane.setVgap(10);
    	topicPane.setHgap(10);
    	Pane  blank = new Pane();
    	topicPane.add(blank,1,7);
	}

    protected static void setSubscribeTopic(String selectedItem) {
		selectedSubscribeTopic = selectedItem;	
	}
	
	public static void setNoSubscribeTopic(String selectedItem) {
		selectedNoSubscribeTopic = selectedItem;
	}

	public void changeDisplayMessage(String subjectTitle){
		messagesDisplay.clear();
		InterfaceSubjectDiscussion subject = getClient().getSubject(subjectTitle);
		try {
			for(Object o: subject.getMessages()){
				InterfaceMessage m = (InterfaceMessage)o;
				messagesDisplay.add(new Message(m.getDate(), m.getAuthor(), m.getMessage()));
			}
		} catch (RemoteException e) {
			// TODO popup probleme de connexion => on revient à la fenêtre connexion
			e.printStackTrace();
		}
	}

	public static Client getClient() {
		return client;
	}

	public static void setClient(Client client) {
		MainWindow.client = client;
	}
}