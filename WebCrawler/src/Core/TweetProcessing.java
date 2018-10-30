package Core;
import com.google.gson.Gson;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.*;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TweetProcessing {


    public int mainProcess() throws IOException {
        //int ibm = processIBM();
        int ms = setupMS();

        //TODO: algorithm

        return  0;
    }

    private int processIBM(){

        ToneAnalyzer toneAnalyzer = new ToneAnalyzer("2017-09-21");
        toneAnalyzer.setUsernameAndPassword("6de387d8-b9c6-4014-9d8e-ef01e41396dc","vGwv3H2gphbW");
        toneAnalyzer.setEndPoint("https://gateway.watsonplatform.net/tone-analyzer/api");
        ToneOptions toneOptions = new ToneOptions.Builder().text("I Love Myself").build();
        ToneAnalysis toneAnalysis = toneAnalyzer.tone(toneOptions).execute();
        System.out.println(toneAnalysis);
        return 0;
    }


    static String accessKey = "157412d9257f4a45972080b86e844e3a";

    static String host = "https://eastus.api.cognitive.microsoft.com";

    static String path = "/text/analytics/v2.0/sentiment";


    private int setupMS() throws IOException {
        //create docuemnts
        Documents documents = new Documents ();
        documents.add ("1", "en", "I hate myself");
        documents.add ("2", "en", "I love myself");
        return processMS(documents);
    }


    private int processMS(Documents documents) throws IOException {
        String text = new Gson().toJson(documents);
        byte[] encoded_text = text.getBytes("UTF-8");

        URL url = new URL(host+path);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/json");
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", accessKey);
        connection.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.write(encoded_text, 0, encoded_text.length);
        wr.flush();
        wr.close();

        StringBuilder response = new StringBuilder ();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        String finResponse = response.toString();
        System.out.println(finResponse);
        //TODO convert response
        return 0;

    }

    private int processGoog(){

        return 0;
    }

}

class Document {
    public String id, language, text;

    public Document(String id, String language, String text){
        this.id = id;
        this.language = language;
        this.text = text;
    }
}

class Documents {
    public List<Document> documents;

    public Documents() {
        this.documents = new ArrayList<Document>();
    }
    public void add(String id, String language, String text) {
        this.documents.add (new Document (id, language, text));
    }
}
