package Account;

import Utils.Oauth;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class oauthform {

    private Stage mainstage;
    private Scene mainviewscene;
    private Account currentuser;
    static Twitter twitter;
    static RequestToken requestToken;


    public oauthform(Stage primary, Scene mainviewscene, Account currentuser) throws TwitterException {
        this.mainstage = primary;
        this.mainviewscene = mainviewscene;
        this.currentuser= currentuser;
        twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer("KrKj0MnihSR5cUCXix2aS8aJV", "aaJY6emW1hwjmXPqrQMStjwGWGAcXpuNPvx849PUjBzijSfFVR");
        requestToken = twitter.getOAuthRequestToken();

    }

    public Scene getScene(){
        GridPane gp = createPane();
        addUI(gp);
        Scene scene = new Scene(gp, 800, 500);
        return scene;
    }

    private GridPane createPane(){
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

    private void addUI(GridPane gp){
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

        TextField twitterCode = new TextField();
        twitterCode.setPrefHeight(40);
        gp.add(twitterCode, 0, 1);

        Button submitButton = new Button("Submit");
        submitButton.setPrefWidth(100);
        submitButton.setPrefHeight(40);
        gp.add(submitButton,0,2);
        GridPane.setHalignment(submitButton, HPos.CENTER);

        submitButton.setOnAction(event -> {
            String code = twitterCode.getText();
            try {
                enterCode(code);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            this.mainstage.setScene(mainviewscene);
            //TODO: add success message
        });

    }
    private static void launchOauth() throws TwitterException{
//        Twitter twitter = TwitterFactory.getSingleton();
//        twitter.setOAuthConsumer("KrKj0MnihSR5cUCXix2aS8aJV", "aaJY6emW1hwjmXPqrQMStjwGWGAcXpuNPvx849PUjBzijSfFVR");
//        RequestToken requestToken = twitter.getOAuthRequestToken();
        //AccessToken accessToken = null;
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        System.out.println("Open the following URL and grant access to your account:");
//        System.out.println(requestToken.getAuthorizationURL());
        try {
          //  Desktop desktop = java.awt.Desktop.getDesktop();
            //URI oURL = new URI(requestToken.getAuthorizationURL());
            //desktop.browse(oURL);
            //System.out.println("Test1");
            //System.out.println(Desktop.isDesktopSupported());
            //Desktop.getDesktop().browse(new URL(requestToken.getAuthorizationURL()).toURI());
            //System.out.println("Test 2");
            if( Desktop.isDesktopSupported() )
            {
                new Thread(() -> {
                    try {
                        Desktop.getDesktop().browse( new URI( requestToken.getAuthorizationURL() ) );
                    } catch (IOException | URISyntaxException e1) {
                        e1.printStackTrace();
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enterCode(String code) throws TwitterException {
        System.out.println(code);
//        Twitter twitter = TwitterFactory.getSingleton();
//        twitter.setOAuthConsumer("KrKj0MnihSR5cUCXix2aS8aJV", "aaJY6emW1hwjmXPqrQMStjwGWGAcXpuNPvx849PUjBzijSfFVR");
//        RequestToken requestToken = twitter.getOAuthRequestToken();
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
//        System.out.println("SUCCCCCCCCCCCC");
        System.out.println(accessToken.getToken());
        System.out.println(accessToken.getTokenSecret());
        System.out.println(accessToken.getScreenName());
        //TODO: push tokens to database
        updateTokens(accessToken.getToken(), accessToken.getTokenSecret(), accessToken.getScreenName());

    }

    private void updateTokens(String token, String secret, String twitterId){

    }
}
