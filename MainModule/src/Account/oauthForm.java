package Account;

import Utils.Firebase;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

//import javax.xml.soap.Text;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static Account.CreateAccountView.requestToken;
import static Account.CreateAccountView.twitter;

public class oauthForm {

    private Stage mainStage;
    private Scene mainViewScene;
    private Account currentUser;
    //static Twitter twitter;
    private TextField nameField;
    //static RequestToken requestToken;

    public oauthForm(Stage primary, Scene mainViewScene, Account currentUser) throws TwitterException {
        this.mainStage = primary;
        this.mainViewScene = mainViewScene;
        this.currentUser= currentUser;
    }

    public oauthForm(){

    }

    public Scene getScene(){
        GridPane gp = createPane();
        addUI(gp);
        Scene scene = new Scene(gp, 800, 500);
        return scene;
    }

    public GridPane createPane(){
        GridPane gp = new GridPane();

        gp.setAlignment(Pos.CENTER);

        // Set a padding of 20px on each side
        gp.setPadding(new Insets(40, 40, 40, 40));

        // Set the horizontal gap between columns
        gp.setHgap(10);

        // Set the vertical gap between rows
        gp.setVgap(10);

        return gp;
    }

    public void addUI(GridPane gp){
        Button showOauth = new Button("Link Twitter");
        showOauth.setPrefWidth(100);
        showOauth.setPrefHeight(40);
        gp.add(showOauth,0,0);
        GridPane.setHalignment(showOauth, HPos.CENTER);


        showOauth.setOnAction(event -> {
            try {
                launchOauth();
            } catch (TwitterException e) {
                e.printStackTrace();
            }

        });


        Label codeLabel = new Label("Enter Twitter Code");
        gp.add(codeLabel, 0, 1);

        TextField twitterCode = new TextField();
        twitterCode.setPrefHeight(40);
        gp.add(twitterCode, 1, 1);


        Label nameLabel = new Label("Enter Child name");
        gp.add(nameLabel, 0, 2);
        nameField = new TextField();
        nameField.setPrefHeight(40);
        gp.add(nameField,1,2);



        Button submitButton = new Button("Submit");
        submitButton.setPrefWidth(100);
        submitButton.setPrefHeight(40);
        gp.add(submitButton,0,3);
        GridPane.setHalignment(submitButton, HPos.CENTER);

        submitButton.setOnAction(event -> {
            String code = twitterCode.getText();
            try {
                enterCode(code);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            this.mainStage.setScene(mainViewScene);
            //TODO: add success message
        });

        //Add back button
        Button goBack = new Button("Back");
        goBack.setPrefHeight(40);
        goBack.setPrefWidth(100);
        gp.add(goBack, 0, 4);
        goBack.setOnAction(e -> mainStage.setScene(mainViewScene));
        GridPane.setHalignment(goBack, HPos.CENTER);


    }
    public  void launchOauth() throws TwitterException{
        try {

            if( Desktop.isDesktopSupported() )
            {
                new Thread(() -> {
                    try {
                        Desktop.getDesktop().browse( new URI(requestToken.getAuthorizationURL() ) );
                    } catch (IOException | URISyntaxException e1) {
                        e1.printStackTrace();
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    public void enterCode(String code) throws TwitterException {

        if(nameField.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, mainStage, "Error", "Please Enter Child Name");
            return;
        }



        AccessToken accessToken = null;
        try{
                if(code.length() > 0){
                    accessToken = twitter.getOAuthAccessToken(requestToken, code);
                }else{
                    accessToken = twitter.getOAuthAccessToken();
                }
            } catch (TwitterException te) {
                if(401 == te.getStatusCode()){
                    System.out.println("Unable to get the access token.");
                    //TODO: add failure message
                }else{
                    te.printStackTrace();
                }
            }
        System.out.println(accessToken.getToken());
        System.out.println(accessToken.getTokenSecret());
        System.out.println(accessToken.getScreenName());
        System.out.println(accessToken.getUserId());
        //TODO: push tokens to database
        updateTokens(accessToken.getToken(), accessToken.getTokenSecret(), accessToken.getScreenName(), Long.toString(accessToken.getUserId()));

    }

    public void updateTokens(String token, String secret, String twitterId, String userID){
        //TODO: make sure they have linked with counselor
        //TODO: pull student name from database
        Firebase.setSocialMediaDB(currentUser.getEmail(), -1, nameField.getText() , token, secret, twitterId, userID);
    }
}
