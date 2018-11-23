package Core;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.sun.org.apache.xerces.internal.utils.SecuritySupport.getResourceAsStream;

public class firebase {
    static Firestore db;

    public static void init() {
        // Fetch the service account key JSON file contents
        FileInputStream serviceAccount;
        FirebaseOptions options = null;
        try {
            InputStream stream = firebase.class.getResourceAsStream("psycheval-ff91b-firebase-adminsdk-pjtsv-d414b51557.json");
            //serviceAccount = new FileInputStream("WebCrawler/src/Core/psycheval-ff91b-firebase-adminsdk-pjtsv-d414b51557.json");
           // System.out.println(stream);
            // Initialize the app with a service account, granting admin privileges
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(stream))
                    .setDatabaseUrl("https://psycheval-ff91b.firebaseio.com")
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }


        FirebaseApp.initializeApp(options);
        db = FirestoreClient.getFirestore();
        System.out.println("Firebase successfully initialized");
    }

    // Pulling data from Social Media DB
    public static List<List<String>> getAll() {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("SocialMedia").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        List<List<String>> list = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            List<String> l = new ArrayList<>();
            String UID = String.valueOf(document.get("UID"));
            String sName = String.valueOf(document.get("Student Name"));
            String aName = String.valueOf(document.get("accountName"));
            String pEmail = String.valueOf(document.get("Parent Email"));
            String secret = String.valueOf(document.get("secret"));
            String rFactor = String.valueOf(document.get("Risk Factor"));
            String token = String.valueOf(document.get("token"));
            Collections.addAll(l, UID, sName, aName, pEmail, secret, rFactor, token);
            list.add(l);
        }
        return list;
    }

    public static void pushScore(String uid, int rFactor) {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("SocialMedia").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {

            ApiFuture<WriteResult> future;
            // Update an existing document
            if (document.getString("UID") == null)
                continue;
            if (document.getString("UID").equalsIgnoreCase(uid))
                future = document.getReference().update("Risk Factor", rFactor);
            else
                continue;
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public static void pushToQuickLookup(String tweet){
        DocumentReference docRef = db.collection("Quick Lookup").document();
        Map<String, Object> data = new HashMap<>();
        data.put("Tweet", tweet);

        ApiFuture<WriteResult> result = docRef.set(data);
        try {
            result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    //TODO:This returns null for some reason
    public static List<String> getFromQuickLookup (){
        ApiFuture<QuerySnapshot> query = db.collection("Quick Lookup").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents;
        List<String> potentialRisks = new ArrayList<>();
        if (querySnapshot != null) {
            documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                String s = document.getString("Tweets");
                System.out.println("1 " + s);
                potentialRisks.add(s);
            }
        }
        return potentialRisks;
    }
}
