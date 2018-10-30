package Utils;

import Account.Account;
import Counselor.Notifications;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.cloud.firestore.EventListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.annotations.Nullable;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class Firebase {

    String type;
    String email;
    String name;
    String password;
    static int count;

    public Firebase() {
        this.count = 0;
    }

    public Firebase(String type, String email, String name, String password) {
        this.type = type;
        this.email = email;
        this.name = name;
        this.password = password;
        this.count = 0;
    }

    public static void init() {
        // Fetch the service account key JSON file contents
        FileInputStream serviceAccount;
        FirebaseOptions options = null;
        try {
            serviceAccount = new FileInputStream("MainModule/src/Utils/psycheval-ff91b-firebase-adminsdk-pjtsv-d414b51557.json");

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

    // Authentication DB
    public static String getType(String email) {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("Authentication").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            if (document.getString("email") == null)
                continue;
            if (document.getString("email").equalsIgnoreCase(email))
                return document.getString("type");
        }
        return null;
    }

    public static String getName(String email) {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("Authentication").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            if (document.getString("email") == null)
                continue;
            if (document.getString("email").equalsIgnoreCase(email))
                return document.getString("name");
        }
        return null;
    }

    public static String getPassword(String email) {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("Authentication").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            if (document.getString("email") == null)
                continue;
            if (document.getString("email").equalsIgnoreCase(email))
                return document.getString("password");
        }
        return null;
    }

    public static void setPassword(String email, String password) {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("Authentication").get();
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

            ApiFuture<WriteResult> future = null;
            // Update an existing document
            if (document.getString("email") == null)
                continue;
            if (document.getString("email").equalsIgnoreCase(email))
                future = document.getReference().update("password", password);
            else
                continue;
            WriteResult result = null;
            try {
                result = future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createAccount(String type, String email, String name, String password) {
        DocumentReference docRef = FirestoreClient.getFirestore().collection("Authentication").document();
        Map<String, Object> data = new HashMap<>();
        data.put("type", type);
        data.put("email", email);
        data.put("name", name);
        data.put("password", password);

        ApiFuture<WriteResult> result = docRef.set(data);
        try {
            result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("Created new user with ID: " + email);
    }

    public static boolean login(String email, String password) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("Authentication").get();
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            if (document.getString("email") == null)
                return false;
            if ((document.getString("email")).equalsIgnoreCase(email)) {
                if (document.getString("password") == null)
                    return false;
                if ((document.getString("password")).equalsIgnoreCase(password))
                    return true;
            }
        }
        return false;
    }

    // counselor db - name, email, parent list (parent name, student name, flag)

    public static void setParents(String email, Map<Integer, Object> m) {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("Counselor").get();
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
            // Update an existing document
            if (document.getString("Email") == null)
                continue;
            if (document.getString("Email").equalsIgnoreCase(email)) {
                DocumentReference docRef = FirestoreClient.getFirestore().collection("Counselor")
                        .document(document.getId());
                Map<String, Object> map;
                map = (Map<String, Object>) document.get("Parents");
                List<Object> l = new ArrayList<>();
                l.add("");
                l.add(false);
                l.add("");
                if (map.containsValue(l))
                    map.remove("EMPTY");
                int size = map.size();
                l.clear();
                for (int i = 0; i < m.size(); ++i) {
                    l.add(m.get(i));
                }
                map.put(String.valueOf(size++), l);
                ApiFuture<WriteResult> arrayUnion = docRef.update("Parents", map);
                try {
                    arrayUnion.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void setParentsApproved(String email, String parentEmail) {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("Counselor").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {

            // Update an existing document
            if (document.getString("Email") == null)
                continue;
            if (document.getString("Email").equalsIgnoreCase(email)) {
                DocumentReference docRef = FirestoreClient.getFirestore().collection("Counselor")
                        .document(document.getId());
                Map<String, Object> map;
                if (document.get("Parents") != null)
                    map = (Map<String, Object>) document.get("Parents");
                else
                    continue;
                System.out.println(map);
                List<Object> list = new ArrayList<>();
                for (int i = 0; i < map.size(); ++i) {
                    List<Object> l = (List<Object>) map.get(String.valueOf(i));
                    if (l.contains(parentEmail)) {
                        for (int j = 0; j < l.size(); j++) {
                            if (j == 1)
                                list.add(true);
                            else
                                list.add(l.get(j));
                        }
                        map.replace(String.valueOf(i), list);
                    }
                }

                ApiFuture<WriteResult> arrayUnion = docRef.update("Parents", map);
                try {
                    arrayUnion.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getCounselorName(String email) {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("Counselor").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            if (document.getString("Email") == null)
                continue;
            if (document.getString("Email").equalsIgnoreCase(email))
                return document.getString("Name");
        }
        return null;
    }

    public static ArrayList<String> getStudentNames(String email) {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("Counselor").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        List<String> sNames = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            if (document.getString("Email") == null)
                continue;
            if (document.getString("Email").equalsIgnoreCase(email)) {
                DocumentReference docRef = FirestoreClient.getFirestore().collection("Counselor")
                        .document(document.getId());
                Map<String, Object> map = null;
                if (document.get("Parents") != null)
                    map = (Map<String, Object>) document.get("Parents");
                else
                    continue;
                for (int i = 0; i < map.size(); ++i) {
                    List<Object> l = (List<Object>) map.get(String.valueOf(i));
                    sNames.add((String) l.get(l.size() - 1));
                }
            }
        }
        return (ArrayList<String>) sNames;
    }

    public static String getStudentName(String parentEmail) {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("Counselor").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            Map<String, Object> map;
            if (document.get("Parents") != null)
                map = (Map<String, Object>) document.get("Parents");
            else
                continue;
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                List<String> l = (List<String>) pair.getValue();
                if (!l.contains(parentEmail))
                    continue;
                return l.get(2);
            }
        }
        return null;
    }

    public static void setCounselorDB(String name, String email, String pEmail, String sName) {
        DocumentReference docRef = FirestoreClient.getFirestore().collection("Counselor").document();
        Map<String, Object> data = new HashMap<>();
        data.put("Email", email);
        data.put("Name", name);

        Map<String, Object> m = new HashMap<>();
        List<Object> l = new ArrayList<>();
        Collections.addAll(l, pEmail, false, sName);
        if (pEmail.isEmpty() && sName.isEmpty())
            m.put("EMPTY", l);
        else
            m.put(String.valueOf((count++)), l);
        data.put("Parents", m);

        ApiFuture<WriteResult> result = docRef.set(data);
        try {
            result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void deleteParent(String cEmail, String pEmail) {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("Counselor").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            if (document.getString("Email") == null)
                continue;
            if (document.getString("Email").equalsIgnoreCase(cEmail)) {
                Map<String, Object> map;
                if (document.get("Parents") != null)
                    map = (Map<String, Object>) document.get("Parents");
                else
                    continue;
                int i = 0;
                Iterator it = map.entrySet().iterator();
                while (it.hasNext()) {
                    DocumentReference docRef = FirestoreClient.getFirestore().collection("Counselor")
                            .document(document.getId());
                    Map.Entry pair = (Map.Entry) it.next();
                    List<String> l = (List<String>) pair.getValue();
                    if (l.contains(pEmail)) {
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("Parents." + String.valueOf(i), FieldValue.delete());
                        // Update and delete the "capital" field in the document
                        ApiFuture<WriteResult> writeResult = docRef.update(updates);
                        try {
                            System.out.println("Update time : " + writeResult.get());
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    i++;
                }
            }
        }
    }

    // social media db - student name, twitter oauth key, twitter link, score, getters & setters
    public static void setSocialMediaDB(String pEmail, int riskFactor, String name, String token, String tokenSecret, String accName, String uid) {
        DocumentReference docRef = FirestoreClient.getFirestore().collection("SocialMedia").document();
        Map<String, Object> data = new HashMap<>();
        data.put("Parent Email", pEmail);
        data.put("Risk Factor", riskFactor);
        data.put("Student Name", name);
        data.put("token", token);
        data.put("secret", tokenSecret);
        data.put("accountName", accName);
        data.put("UID", uid);

        ApiFuture<WriteResult> result = docRef.set(data);
        try {
            result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static long getRiskFactor(String parentEmail) {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("SocialMedia").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            if (document.getString("Parent Email") == null)
                continue;
            if (document.getString("Parent Email").equalsIgnoreCase(parentEmail))
                return document.getLong("Risk Factor");
        }
        return -10;
    }

    public static String getStuNameSM(String parentEmail) {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("SocialMedia").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            if (document.getString("Parent Email") == null)
                continue;
            if (document.getString("Parent Email").equalsIgnoreCase(parentEmail))
                return document.getString("Student Name");
        }
        return null;
    }

    // PostData db - Name, Posts[]
    public static void setPostDB(String name, String[] posts) {
        DocumentReference docRef = FirestoreClient.getFirestore().collection("PostData").document();
        Map<String, Object> data = new HashMap<>();
        List<HashMap<String, Object>> l = new ArrayList<>();
        data.put("Name", name);
        List<Object> list = new ArrayList<>();
        for (String s : posts)
            list.add(s);
        data.put("Posts", list);
        ApiFuture<WriteResult> result = docRef.set(data);
        try {
            result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void addPosts(String name, String[] posts) {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("PostData").get();
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

            // Update an existing document
            if (document.getString("Name") == null)
                continue;
            if (document.getString("Name").equalsIgnoreCase(name)) {
                DocumentReference docRef = FirestoreClient.getFirestore().collection("PostData")
                        .document(document.getId());
                List<Object> list;
                if (document.get("Posts") != null)
                    list = (List<Object>) document.get("Posts");
                else
                    continue;
                for (int i = 0; i < posts.length; ++i) {
                    list.add(posts[i]);
                }
                ApiFuture<WriteResult> arrayUnion = docRef.update("Posts", list);
                try {
                    arrayUnion.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static ArrayList<String> getPosts(String name) {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("PostData").get();
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
            if (document.getString("Name") == null)
                continue;
            if (document.getString("Name").equalsIgnoreCase(name))
                if (document.get("Posts") != null)
                    return (ArrayList<String>) document.get("Posts");
        }
        return null;
    }

    public static boolean isApproved(String counselorEmail, String parentEmail) {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("Counselor").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            if (document.getString("Email") == null)
                continue;
            if (document.getString("Email").equalsIgnoreCase(counselorEmail)) {
                Map<String, Object> map;
                if (document.get("Parents") != null)
                    map = (Map<String, Object>) document.get("Parents");
                else
                    continue;
                List<Object> l;
                for (int i = 0; i < map.size(); i++) {
                    l = (List<Object>) map.get(String.valueOf(i));
                    if (l != null) {
                        for (int j = 0; j < l.size(); j++) {
                            if (l.get(0).equals(parentEmail))
                                return (boolean) l.get(1);
                        }
                    }
                }
            }
        }
        return false;
    }

    public static Map<String, Object> getParents(String email) {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("Counselor").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            if (document.getString("Email") == null)
                continue;
            if (document.getString("Email").equalsIgnoreCase(email)) {
                Map<String, Object> map;
                if (document.get("Parents") != null)
                    map = (Map<String, Object>) document.get("Parents");
                else
                    continue;
                System.out.println(map);
                return map;
            }
        }
        return null;
    }

    public static void checkForNewParents(String email, Stage primaryStage, Scene mainViewScene, Account currentUser) {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("Counselor").get();
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
            // Update an existing document
            if (document.getString("Email") == null)
                continue;
            if (document.getString("Email").equalsIgnoreCase(email)) {
                DocumentReference docRef = FirestoreClient.getFirestore().collection("Counselor")
                        .document(document.getId());
                docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirestoreException e) {
                        if (e != null) {
                            System.err.println("Listen failed: " + e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            Map<String, List<String>> hm = (Map<String, List<String>>) snapshot.get("Parents");
                            Iterator it = hm.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry pair = (Map.Entry) it.next();
                                List<String> l = (List<String>) pair.getValue();
                                if (l.contains(false)) {
                                    Notifications n = new Notifications(primaryStage, mainViewScene, currentUser);
                                    Platform.runLater(() -> {
                                        n.refreshApproveList();
                                        n.showAlert(Alert.AlertType.INFORMATION, primaryStage, "New Request",
                                                "You have a new parent request!");
                                    });
                                }
                            }

                        }
                    }
                });
            }
        }
    }
}
