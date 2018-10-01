import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

    public void GenerateandParseCSV() throws IOException {
        String[] cmds = {
                "twitter_crawler.py",
                consumerKey,consumerSecret,accessKey,accessSecret
        };
        //TODO: edit python script to handle the args and return filename of csv
        Process p = Runtime.getRuntime().exec(cmds);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String fileName = stdInput.readLine();
        parseCSV(fileName);
    }

    private void parseCSV(String fileName){

    }







}
