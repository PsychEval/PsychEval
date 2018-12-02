package com.psycheval.testapp;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;
import twitter4j.TwitterFactory;


public class Utils {

    static Twitter twitter;
    static RequestToken requestToken;

    public static void displayToast(Context context, String message){
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    public static void setTwitter(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer("KrKj0MnihSR5cUCXix2aS8aJV", "aaJY6emW1hwjmXPqrQMStjwGWGAcXpuNPvx849PUjBzijSfFVR");
        try {
            requestToken = twitter.getOAuthRequestToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
