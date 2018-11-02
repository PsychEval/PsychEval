package Core;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class TweetPullerTest {
    @Test
    public void getTweets() {
        Assert.assertNotEquals("Tweet Puller doesn't work",null, TweetPuller.getTweets("KrKj0MnihSR5cUCXix2aS8aJV","aaJY6emW1hwjmXPqrQMStjwGWGAcXpuNPvx849PUjBzijSfFVR","936667168149819393-79jEcQUeIjzcOBwxXLaoE6IjTqf8Dvy","7OL5wgih9i7BtjivHuCawfz57Cr7SYeIc929Npl07Kv2k"));
    }
}