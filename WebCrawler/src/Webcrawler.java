
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;


import java.util.ArrayList;

public class Webcrawler {
    private String consumerKey;
    private String consumerSecret;
    private String accessKey;
    private String accessSecret;

    public Webcrawler(String consumerKey, String consumerSecret, String accessKey, String accessSecret) {
        //TODO: Change these to pull from database
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.accessKey = accessKey;
        this.accessSecret = accessSecret;
    }

    //returns true if it is not a retweet
    private static boolean filterRetweet(String tweet){
        if(!tweet.startsWith("RT")){
            return true;
        }
        return false;
    }
    //TODO: get Oauth tokens as parameters
    public static ArrayList<String> getTweets(String consumerKey, String consumerSecret, String accessKey, String accessSecret){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessKey)
                .setOAuthAccessTokenSecret(accessSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        String user;

        Paging pg = new Paging();

        int numberOfTweets = 100;
        long lastID = Long.MAX_VALUE;
        ArrayList<Status> tweets = new ArrayList<Status>();
        while (tweets.size () < numberOfTweets) {
            try {
                user = twitter.verifyCredentials().getScreenName();
                tweets.addAll(twitter.getUserTimeline(user,pg));
//                System.out.println("Gathered " + tweets.size() + " tweets");
                for (Status t: tweets)
                    if(t.getId() < lastID) lastID = t.getId();
            }
            catch (TwitterException te) {
                System.out.println("Couldn't connect: " + te);
            };
            pg.setMaxId(lastID-1);
        }

        ArrayList<String> res = new ArrayList<String>();

        for (int i = 0; i < tweets.size(); i++) {
            res.add(tweets.get(i).getText());
        }

        return res;
    }

    public static void main(String[] args) {
        System.out.println();
    }

}
