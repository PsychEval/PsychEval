
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class twitter_crawl {
    public static void main(String[] args) {
        // gets Twitter instance with default credentials
//        Twitter twitter = new TwitterFactory().getInstance();

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("rv6od3nKApSUaEVeIehlu5Hkn")
                .setOAuthConsumerSecret("afkABogRCD1Ag7fo7paI0BWLIEXW37sZW552evyesxFjtGwoTT")
                .setOAuthAccessToken("936667168149819393-QVX20pdniONfV1MTHuGTYJpPp0lhxWh")
                .setOAuthAccessTokenSecret("VpNrfYQM1CBUMeWXeaqnlLshVjRRbZVetk6d5oToKXabZ");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        Paging pg = new Paging();

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
                tweets.addAll(twitter.getUserTimeline("realDonaldTrump",pg));
//                System.out.println("Gathered " + tweets.size() + " tweets");
                for (Status t: tweets)
                    if(t.getId() < lastID) lastID = t.getId();
            }
            catch (TwitterException te) {
                System.out.println("Couldn't connect: " + te);
            };
            pg.setMaxId(lastID-1);
        }

//        PrintWriter pw = null;
//        try {
//            pw = new PrintWriter("finderTwitter.txt");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        {
//            pw.println(arrayList.get(index));
//        }
        for (int i = 0; i < tweets.size(); i++) {
            String tw = tweets.get(i).getText();
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

}
