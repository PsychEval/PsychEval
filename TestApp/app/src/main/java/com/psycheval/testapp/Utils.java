package com.psycheval.testapp;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.util.regex.Pattern;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;
import twitter4j.TwitterFactory;


public class Utils {

    private static final Pattern hasUppercase = Pattern.compile("[A-Z]");
    private static final Pattern hasLowercase = Pattern.compile("[a-z]");
    private static final Pattern hasNumber = Pattern.compile("\\d");
    private static final Pattern hasSpecialChar = Pattern.compile("[^a-zA-Z0-9 ]");
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

    public static String validateNewPass(String pass1) {
        if (pass1 == null) {
            return "Please enter a new password!";
        }

        StringBuilder retVal = new StringBuilder();

        if (pass1.isEmpty()) {
            return "Please enter a new password!";
        }

        if (pass1.length() < 4)
            retVal.append("New password is too short. Needs to have 4 characters!\n");

        if (!hasUppercase.matcher(pass1).find())
            retVal.append("New password needs an upper case!\n");

        if (!hasLowercase.matcher(pass1).find())
            retVal.append("New password needs a lowercase!\n");

        if (!hasNumber.matcher(pass1).find())
            retVal.append("New password needs a number!\n");

        if (!hasSpecialChar.matcher(pass1).find())
            retVal.append("New password needs a special character i.e. !,@,#, etc.\n");

        return retVal.toString();
    }

}
