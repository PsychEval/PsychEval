package Core;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class firebase {

    public static void init() {
        // Fetch the service account key JSON file contents
        FileInputStream serviceAccount;
        FirebaseOptions options = null;
        try {
            serviceAccount = new FileInputStream("WebCrawler/src/Core/psycheval-ff91b-firebase-adminsdk-pjtsv-d414b51557.json");

            // Initialize the app with a service account, granting admin privileges
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://psycheval-ff91b.firebaseio.com")
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FirebaseApp.initializeApp(options);
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
}