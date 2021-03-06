package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.interfaces.middleware.InterfacesClientServer.ClientAlreadyRegisteredException;
import com.interfaces.middleware.InterfacesClientServer.InterfaceServerForum;
import com.interfaces.middleware.InterfacesClientServer.WrongClientException;
import com.interfaces.middleware.InterfacesClientServer.WrongPasswordException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class LoginWindow extends Application{
	
	Registry registry;
	InterfaceServerForum forum = null;
	@Override
    public void start(final Stage primaryStage) throws NotBoundException { 
		
		//window on the middle top of the screen
		 Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		 primaryStage.setX((screenBounds.getWidth() - 400) / 2); 
		 primaryStage.setY((screenBounds.getHeight() - 400) / 2);  

        primaryStage.setTitle("Dans ton flux!");
        primaryStage.show();
    	
    	GridPane grid = new GridPane();
    	grid.setAlignment(Pos.CENTER);
    	grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(25, 25, 25, 25));

    	Scene scene = new Scene(grid, 400, 200);
    	primaryStage.setScene(scene);
    	scene.getStylesheets().add
    	 (LoginWindow.class.getResource("/LoginWindow.css").toExternalForm());
    	
    	Text scenetitle = new Text("Welcome");
    	//scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
    	scenetitle.setId("welcome-text");

    	grid.add(scenetitle, 0, 0, 2, 1);

    	Label userName = new Label("User Name:");
    	grid.add(userName, 0, 1);

    	final TextField userTextField = new TextField();
    	grid.add(userTextField, 1, 1);

    	Label pw = new Label("Password:");
    	grid.add(pw, 0, 2);

    	final PasswordField pwBox = new PasswordField();
    	grid.add(pwBox, 1, 2);
    	
    	Button btnup = new Button("Sign up");
    	HBox hbBtnup = new HBox(10);
    	hbBtnup.setAlignment(Pos.BOTTOM_RIGHT);
    	hbBtnup.getChildren().add(btnup);
    	grid.add(hbBtnup, 0, 4);
    	
    	Button btn = new Button("Sign in");
    	HBox hbBtn = new HBox(10);
    	hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
    	hbBtn.getChildren().add(btn);
    	grid.add(hbBtn, 1, 4);
    	
    	final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);
        
        
        /*forum*/		
		try {
			registry = LocateRegistry.getRegistry("localhost", 9876);
			forum = (InterfaceServerForum) registry.lookup("Forum");

		} catch (RemoteException e3) {
			// TODO fenetre pour dire que le forum est inaccessible
			e3.printStackTrace();
		}

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

            	
            	Object[] sujet = null;
    			Client client = null;
				try {
					client = new Client();
				} catch (RemoteException e2) {
					e2.printStackTrace();
				}

				try {
					sujet = forum.connexion(userTextField.getText(), pwBox.getText(), client);
				} catch (RemoteException e1) {
					//TODO faire une popup pour dire qu'il y a un probleme de connexion avec le serveur
					e1.printStackTrace();
				} catch (WrongClientException | WrongPasswordException e1) {
					 actiontarget.setFill(Color.FIREBRICK);
				     actiontarget.setText("Wrong User name/Password");
				}
        		if(sujet != null){
        			
        			client.setUserLogin(userTextField.getText());
        			client.setSubject(sujet);
        			
					 actiontarget.setId("actiontarget");
	                 MainWindow stage = new MainWindow(client,forum);
	                 try {
						stage.start(primaryStage);
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
	                 
				}
            	
            }
        });
        
        btnup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	try {
            		try {
						forum.registrationOnForum(userTextField.getText(), pwBox.getText());
						actiontarget.setFill(Color.GREEN);
					    actiontarget.setText("Saved");
					} catch (WrongClientException | WrongPasswordException
							| ClientAlreadyRegisteredException e1) {
						actiontarget.setFill(Color.FIREBRICK);
					    actiontarget.setText("Error");
					}
				} catch (RemoteException e1) {
					// TODO popup pour connexion impossible
					e1.printStackTrace();
				}

            }           	
         });
    }
	
}
