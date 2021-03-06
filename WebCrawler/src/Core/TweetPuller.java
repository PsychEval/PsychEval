package Core;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TweetPuller {

    static void pullTweets() {

        List<List<String>> data = firebase.getAll();

        for (List l : data) {
            String UID = (String) l.get(0);
//            String studentName = (String) l.get(1);
           String twitterName = (String) l.get(2);
//            String parentEmail = (String) l.get(3);
            String secret = (String) l.get(4);
//            String riskFactor = (String) l.get(5);
            String token = (String) l.get(6);

            //pull tweets
            ArrayList<String> tweets = getTweets("KrKj0MnihSR5cUCXix2aS8aJV", "aaJY6emW1hwjmXPqrQMStjwGWGAcXpuNPvx849PUjBzijSfFVR", token, secret);
            TweetProcessing tp = new TweetProcessing();
            try {
                int score = tp.mainProcess(tweets, twitterName);
                firebase.pushScore(UID, score);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static ArrayList<String> getTweets(String consumerKey, String consumerSecret, String accessKey, String accessSecret) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessKey)
                .setOAuthAccessTokenSecret(accessSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        ArrayList<String> tweets = new ArrayList<>();
        try {
            List<Status> statuses;
            String user;
            user = twitter.verifyCredentials().getScreenName();
            statuses = twitter.getUserTimeline();

            for (Status status : statuses) {
                String statusText = status.getText();
                if (filterRetweet(statusText)) {
                    tweets.add(statusText);
                }
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        }

        return tweets;
    }

    private static boolean filterRetweet(String tweet) {
        if (!tweet.startsWith("RT")) {
            return true;
        }
        return false;
    }

}