package Core;

import org.junit.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TweetProcessingTest {

    @org.junit.Test
    public void mainProcess() throws IOException {
        List<String> tweets = new ArrayList<>();
        String name;
        tweets.add("This is test 1");
        tweets.add("This is test 2");
        name = "test";
        TweetProcessing tweetProcessing = new TweetProcessing();
        int a = tweetProcessing.mainProcess(tweets, name);
        Assert.assertNotEquals(null, a);
    }

    @org.junit.Test
    public void processIBM() throws IOException {
        List<String> tweets = new ArrayList<>();
        String name;
        tweets.add("This is test 1");
        tweets.add("This is test 2");
        name = "test";
        TweetProcessing tweetProcessing = new TweetProcessing();
        int a = tweetProcessing.processIBM(tweets);
        Assert.assertNotEquals(null, a);
    }

    @org.junit.Test
    public void processMS() throws IOException {
        List<String> tweets = new ArrayList<>();
        String name;
        tweets.add("This is test 1");
        tweets.add("This is test 2");
        name = "test";

        Documents documents = new Documents ();
        int i = 1;
        for (String s : tweets) {
            documents.add("" + i++, "en", s);
        }

        TweetProcessing tweetProcessing = new TweetProcessing();
        int a = tweetProcessing.processMS(documents, 0);
        Assert.assertNotEquals(null, a);

    }

}