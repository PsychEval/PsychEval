
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

public class twitter_crawl {
    public static void main(String[] args) {
        // gets Twitter instance with default credentials
//        Twitter twitter = new TwitterFactory().getInstance();

//        ConfigurationBuilder cb = new ConfigurationBuilder();
//        cb.setDebugEnabled(true)
//                .setOAuthConsumerKey("rv6od3nKApSUaEVeIehlu5Hkn")
//                .setOAuthConsumerSecret("afkABogRCD1Ag7fo7paI0BWLIEXW37sZW552evyesxFjtGwoTT");
////                .setOAuthAccessToken("936667168149819393-L7yfh1uuqlptSpdCJ2mT3ghavXegfee")
////                .setOAuthAccessTokenSecret("r3ntUQdhib621FhIEggmf4j1HhMb8wM5LG3MsRDCdbTDR");
//        TwitterFactory tf = new TwitterFactory(cb.build());
//        Twitter twitter = tf.getInstance();

        Paging pg = new Paging();
        Twitter twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer("rv6od3nKApSUaEVeIehlu5Hkn", "afkABogRCD1Ag7fo7paI0BWLIEXW37sZW552evyesxFjtGwoTT");
        String name = "";
        try {
            name = getName(twitter);
        } catch (TwitterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(name);
//        try {
//            List<Status> statuses;
//            String user;
//            if (args.length == 1) {
//                user = args[0];
//                statuses = twitter.getUserTimeline(user);
//            } else {
//                user = twitter.verifyCredentials().getScreenName();
//                statuses = twitter.getUserTimeline("realDonaldTrump");
//            }
//            System.out.println("Showing @" + user + "'s user timeline.");
//            for (Status status : statuses) {
//                String statusText = status.getText();
//                if(filterRetweet(statusText)) {
//                    System.out.println("@" + status.getUser().getScreenName() + " - " + statusText);
//                }
//            }
//        } catch (TwitterException te) {
//            te.printStackTrace();
//            System.out.println("Failed to get timeline: " + te.getMessage());
//            System.exit(-1);
//        }

        int numberOfTweets = 100;
        long lastID = Long.MAX_VALUE;
        ArrayList<Status> tweets = new ArrayList<Status>();
        while (tweets.size () < numberOfTweets) {
            try {
                System.out.println("Getting Tweets");
                tweets.addAll(twitter.getUserTimeline("realDonaldTrump",pg));
                System.out.println("Gathered " + tweets.size() + " tweets");
                for (Status t: tweets)
                    if(t.getId() < lastID) lastID = t.getId();
            }
            catch (TwitterException te) {
                System.out.println("Couldn't connect: " + te);
            }
            ;
            pg.setMaxId(lastID-1);
        }

        for (int i = 0; i < tweets.size(); i++) {
            String tw = tweets.get(i).getText();
//            System.out.println(name);
            if (filterRetweet(tw)) {
                System.out.println(tw);
            }
        }
    }

    private static boolean filterRetweet(String tweet){
        if(!tweet.startsWith("RT")){
//            System.out.println("True");
            return true;
        }
        return false;
    }

    public static String getName(Twitter twitter) throws TwitterException, IOException {
        RequestToken requestToken = twitter.getOAuthRequestToken();
        AccessToken accessToken = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (null == accessToken) {
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
            String pin = br.readLine();
            try{
                if(pin.length() > 0){
                    accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                }else{
                    accessToken = twitter.getOAuthAccessToken();
                }
            } catch (TwitterException te) {
                if(401 == te.getStatusCode()){
                    System.out.println("Unable to get the access token.");
                }else{
                    te.printStackTrace();
                }
            }
        }
        //persist to the accessToken for future reference.
        storeAccessToken(twitter.verifyCredentials().getId() , accessToken);
//        Status status = twitter.updateStatus("Hi test");
//        System.out.println("Successfully updated the status to [" + status.getText() + "].");

        String uName = twitter.getScreenName();

        return uName;
    }

    private static void storeAccessToken(long useId, AccessToken accessToken){
        //store accessToken.getToken()
        //store accessToken.getTokenSecret()
    }

}
