package Core;

import java.util.ArrayList;
import java.util.List;

public class TweetPuller {

    void pullTweets(){

        List<List<String>> data =  firebase.getAll();
        //System.out.println(data);

        for (List l : data) {
            System.out.println(l);
            String UID = (String)l.get(0);
            String studentName = (String)l.get(1);
            String twitterName = (String)l.get(2);
            String parentEmail = (String)l.get(3);
            String secret = (String)l.get(4);
            String riskFactor = (String)l.get(5);
            String token = (String)l.get(6);
            System.out.println(UID + " " + studentName + " " + twitterName + " " + parentEmail + " " + secret + " " + riskFactor + " " + token);

            //pull tweets
            TweetProcessing tp = new TweetProcessing();
            //TODO tp.mainProcess(new ArrayList<String>());


        }
    }
}
