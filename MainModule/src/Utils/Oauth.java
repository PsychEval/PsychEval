package Utils;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class Oauth {
    public static void launchOauth() throws TwitterException, IOException {
        Twitter twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer("KrKj0MnihSR5cUCXix2aS8aJV", "aaJY6emW1hwjmXPqrQMStjwGWGAcXpuNPvx849PUjBzijSfFVR");
        RequestToken requestToken = twitter.getOAuthRequestToken();
        AccessToken accessToken = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Open the following URL and grant access to your account:");
        System.out.println(requestToken.getAuthorizationURL());
        try {
            Desktop desktop = java.awt.Desktop.getDesktop();
            URI oURL = new URI(requestToken.getAuthorizationURL());
            desktop.browse(oURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
            System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
            //String pin = br.readLine();
//            String pin = "1234";
//            try{
//                if(pin.length() > 0){
//                    accessToken = twitter.getOAuthAccessToken(requestToken, pin);
//                }else{
//                    accessToken = twitter.getOAuthAccessToken();
//                }
//            } catch (TwitterException te) {
//                if(401 == te.getStatusCode()){
//                    System.out.println("Unable to get the access token.");
//                }else{
//                    te.printStackTrace();
//                }
//            }

        //persist to the accessToken for future reference.
        storeAccessToken(twitter.verifyCredentials().getId() , accessToken);
//        Status status = twitter.updateStatus("Hi test");
//        System.out.println("Successfully updated the status to [" + status.getText() + "].");

    }

    private static void storeAccessToken(long useId, AccessToken accessToken){
        //store accessToken.getToken()
        //store accessToken.getTokenSecret()
    }
}
