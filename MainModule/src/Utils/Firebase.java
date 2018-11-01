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
    static Firestore db;

    public Firebase() {
    }

    public Firebase(String type, String email, String name, String password) {
        this.type = type;
        this.email = email;
        this.name = name;
        this.password = password;
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
        db = FirestoreClient.getFirestore();

        System.out.println("Firebase successfully initialized");
    }

    // Authentication DB
    public static String getType(String email) {
        ApiFuture<QuerySnapshot> query = db.collection("Authentication").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents;
        if (querySnapshot != null) {
            documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                if (document.getString("email") == null)
                    continue;
                if (document.getString("email").equalsIgnoreCase(email))
                    return document.getString("type");
            }
        }
        return null;
    }

    public static String getName(String email) {
        ApiFuture<QuerySnapshot> query = db.collection("Authentication").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents;
        if (querySnapshot != null) {
            documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                if (document.getString("email") == null)
                    continue;
                if (document.getString("email").equalsIgnoreCase(email))
                    return document.getString("name");
            }
        }
        return null;
    }

    public static String getPassword(String email) {
        ApiFuture<QuerySnapshot> query = db.collection("Authentication").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents;
        if (querySnapshot != null) {
            documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                if (document.getString("email") == null)
                    continue;
                if (document.getString("email").equalsIgnoreCase(email))
                    return document.getString("password");
            }
        }
        return null;
    }

    public static void setPassword(String email, String password) {
        ApiFuture<QuerySnapshot> query = db.collection("Authentication").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents;
        if (querySnapshot != null) {
            documents = querySnapshot.getDocuments();
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
    }

    public static boolean createAccount(String type, String email, String name, String password) {
        DocumentReference docRef = db.collection("Authentication").document();
        Map<String, Object> data = new HashMap<>();
        if (getName(email) == null) {
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
            return true;
        }
        return false;
    }

    public static boolean login(String email, String password) {
        ApiFuture<QuerySnapshot> query = db.collection("Authentication").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents = null;
        if (querySnapshot != null) {
            documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                if (document.getString("email") == null)
                    continue;
                if ((document.getString("email")).equalsIgnoreCase(email)) {
                    if (document.getString("password") == null)
                        continue;
                    if ((document.getString("password")).equalsIgnoreCase(password))
                        return true;
                }
            }
        }
        return false;
    }

    public static boolean checkIfAdminExists() {
        ApiFuture<QuerySnapshot> query = db.collection("Authentication").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents = null;
        if (querySnapshot != null) {
            documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                if (document.getString("type") == null)
                    continue;
                if (document.getString("type").equalsIgnoreCase("Admin"))
                    return true;
            }
        }
        return false;
    }

    // counselor db - name, email, parent list (parent name, student name, flag)

    public static void setParents(String email, Map<Integer, Object> m) {
        ApiFuture<QuerySnapshot> query = db.collection("Counselor").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents;
        if (querySnapshot != null) {
            documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                // Update an existing document
                if (document.getString("Email") == null)
                    continue;
                if (document.getString("Email").equalsIgnoreCase(email)) {
                    DocumentReference docRef = db.collection("Counselor")
                            .document(document.getId());
                    Map<String, Object> map;
                    map = (Map<String, Object>) document.get("Parents");
                    List<Object> l = new ArrayList<>();
                    l.add("");
                    l.add(false);
                    l.add("");
                    l.add(false);
                    if (map.containsValue(l))
                        map.remove("EMPTY");
                    int count = map.size();
                    l.clear();
                    Iterator it = m.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        l.add(pair.getValue());
                    }
                    map.put(String.valueOf(count++), l);
                    ApiFuture<WriteResult> arrayUnion = docRef.update("Parents", map);
                    try {
                        arrayUnion.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void setParentsApproved(String email, String parentEmail) {
        ApiFuture<QuerySnapshot> query = db.collection("Counselor").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents;
        if (querySnapshot != null) {
            documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {

                // Update an existing document
                if (document.getString("Email") == null)
                    continue;
                if (document.getString("Email").equalsIgnoreCase(email)) {
                    DocumentReference docRef = db.collection("Counselor")
                            .document(document.getId());
                    Map<String, Object> map;
                    if (document.get("Parents") != null)
                        map = (Map<String, Object>) document.get("Parents");
                    else
                        continue;
                    List<Object> list = new ArrayList<>();
                    Iterator it = map.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        List<Object> l = (List<Object>) pair.getValue();
                        if (l.contains(parentEmail)) {
                            for (int j = 0; j < l.size(); j++) {
                                if (j == 1)
                                    list.add(true);
                                else
                                    list.add(l.get(j));
                            }
                            map.replace(String.valueOf(pair.getKey()), list);
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
    }

    public static String getCounselorEmail(String pEmail) {
        ApiFuture<QuerySnapshot> query = db.collection("Counselor").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents;
        if (querySnapshot != null) {
            documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                if (document.get("Parents") == null)
                    continue;
                Map<String, Object> map = (Map<String, Object>) document.get("Parents");
                Iterator it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    List<Object> l = (List<Object>) pair.getValue();
                    if (l.contains(pEmail))
                        return document.getString("Email");
                }
            }
        }
        return null;
    }

    public static ArrayList<String> getStudentNames(String email) {
        ApiFuture<QuerySnapshot> query = db.collection("Counselor").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<String> sNames = new ArrayList<>();
        List<QueryDocumentSnapshot> documents;
        if (querySnapshot != null) {
            documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                if (document.getString("Email") == null)
                    continue;
                if (document.getString("Email").equalsIgnoreCase(email)) {
                    Map<String, Object> map;
                    if (document.get("Parents") != null)
                        map = (Map<String, Object>) document.get("Parents");
                    else
                        continue;
                    Iterator it = map.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        List<Object> l = (List<Object>) pair.getValue();
                        sNames.add((String) l.get(l.size() - 2));
                    }
                }
            }
        }
        return (ArrayList<String>) sNames;
    }

    public static String getStudentName(String parentEmail) {
        ApiFuture<QuerySnapshot> query = db.collection("Counselor").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents;
        if (querySnapshot != null) {
            documents = querySnapshot.getDocuments();
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
        }
        return null;
    }

    public static void setCounselorDB(String name, String email, String pEmail, String sName) {
        DocumentReference docRef = db.collection("Counselor").document();
        Map<String, Object> data = new HashMap<>();
        data.put("Email", email);
        data.put("Name", name);

        Map<String, Object> m = new HashMap<>();
        List<Object> l = new ArrayList<>();
        Collections.addAll(l, pEmail, false, sName, false);
        int count = 0;
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
        ApiFuture<QuerySnapshot> query = db.collection("Counselor").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents;
        if (querySnapshot != null) {
            documents = querySnapshot.getDocuments();
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
                        DocumentReference docRef = db.collection("Counselor")
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
    }

    public static boolean isApproved(String counselorEmail, String parentEmail) {
        ApiFuture<QuerySnapshot> query = db.collection("Counselor").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents;
        if (querySnapshot != null) {
            documents = querySnapshot.getDocuments();
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
                    Iterator it = map.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        l = (List<Object>) pair.getValue();
                        if (l != null) {
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
        ApiFuture<QuerySnapshot> query = db.collection("Counselor").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents;
        if (querySnapshot != null) {
            documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                if (document.getString("Email") == null)
                    continue;
                if (document.getString("Email").equalsIgnoreCase(email)) {
                    Map<String, Object> map;
                    if (document.get("Parents") != null)
                        map = (Map<String, Object>) document.get("Parents");
                    else
                        continue;
                    return map;
                }
            }
        }
        return null;
    }

    public static void checkForNewParents(String email, Stage primaryStage, Scene mainViewScene, Account currentUser) {
        ApiFuture<QuerySnapshot> query = db.collection("Counselor").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents;
        if (querySnapshot != null) {
            documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                // Update an existing document
                if (document.getString("Email") == null)
                    continue;
                if (document.getString("Email").equalsIgnoreCase(email)) {
                    DocumentReference docRef = db.collection("Counselor")
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

    public static void setScoreIsBad(String pEmail) {
        String cEmail = getCounselorEmail(pEmail);
        ApiFuture<QuerySnapshot> query = db.collection("Counselor").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents;
        if (querySnapshot != null) {
            documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {

                // Update an existing document
                if (document.getString("Email") == null)
                    continue;
                if (document.getString("Email").equalsIgnoreCase(cEmail)) {
                    DocumentReference docRef = db.collection("Counselor")
                            .document(document.getId());
                    Map<String, Object> map;
                    if (document.get("Parents") != null)
                        map = (Map<String, Object>) document.get("Parents");
                    else
                        continue;
                    List<Object> list = new ArrayList<>();
                    Iterator it = map.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        List<Object> l = (List<Object>) pair.getValue();
                        if (l.contains(pEmail)) {
                            for (int j = 0; j < l.size(); j++) {
                                if (j == 3)
                                    list.add(true);
                                else
                                    list.add(l.get(j));
                            }
                            map.replace(String.valueOf(pair.getKey()), list);
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
    }

    public static boolean isAddedAlready(String cEmail) {
        ApiFuture<QuerySnapshot> query = db.collection("Counselor").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents;
        if (querySnapshot != null) {
            documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {

                // Update an existing document
                if (document.getString("Email") == null)
                    continue;
                if (document.getString("Email").equalsIgnoreCase(cEmail))
                    return true;
            }
        }
        return false;
    }

    // social media db - student name, twitter oauth key, twitter link, score, getters & setters
    public static void setSocialMediaDB(String pEmail, int riskFactor, String name, String token, String tokenSecret,
                                        String accName, String uid) {
        DocumentReference docRef = db.collection("SocialMedia").document();
        ApiFuture<QuerySnapshot> query = db.collection("SocialMedia").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents;
        if (querySnapshot != null) {
            documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot dc : documents) {
                if (dc.getString("Parent Email") == null)
                    continue;
                if (dc.getString("Parent Email").equalsIgnoreCase(pEmail)) {
                    ApiFuture<WriteResult> writeResult = db.collection("SocialMedia")
                            .document(dc.getId()).delete();
                    try {
                        System.out.println("Update time : " + writeResult.get().getUpdateTime());
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
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
        ApiFuture<QuerySnapshot> query = db.collection("SocialMedia").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents;
        if (querySnapshot != null) {
            documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                if (document.getString("Parent Email") == null)
                    continue;
                if (document.getString("Parent Email").equalsIgnoreCase(parentEmail))
                    return document.getLong("Risk Factor");
            }
        }
        return -10;
    }

    public static String getStuNameSM(String parentEmail) {
        ApiFuture<QuerySnapshot> query = db.collection("SocialMedia").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents;
        if (querySnapshot != null) {
            documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                if (document.getString("Parent Email") == null)
                    continue;
                if (document.getString("Parent Email").equalsIgnoreCase(parentEmail))
                    return document.getString("Student Name");
            }
        }
        return null;
    }

    // PostData db - Name, Posts[]
    public static void setPostDB(String name, String[] posts) {
        DocumentReference docRef = db.collection("PostData").document();
        Map<String, Object> data = new HashMap<>();
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
        ApiFuture<QuerySnapshot> query = db.collection("PostData").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents;
        if (querySnapshot != null) {
            documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {

                // Update an existing document
                if (document.getString("Name") == null)
                    continue;
                if (document.getString("Name").equalsIgnoreCase(name)) {
                    DocumentReference docRef = db.collection("PostData")
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
    }

    public static ArrayList<String> getPosts(String name) {
        ApiFuture<QuerySnapshot> query = db.collection("PostData").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents;
        if (querySnapshot != null) {
            documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                if (document.getString("Name") == null)
                    continue;
                if (document.getString("Name").equalsIgnoreCase(name))
                    if (document.get("Posts") != null)
                        return (ArrayList<String>) document.get("Posts");
            }
        }
        return null;
    }

    // Messages DB
    public static boolean newMessage(String cEmail, String pEmail, int sentBy, String message) {
        if (sentBy != 0 && sentBy != 1)
            return false;
        ApiFuture<QuerySnapshot> query = db.collection("Messages").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        DocumentReference docRef;
        int count = 0;
        if (querySnapshot != null) {
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot doc : documents) {
                if (doc.getString("Counselor Email") == null)
                    continue;
                if (doc.getString("Counselor Email").equalsIgnoreCase(cEmail)) {
                    if (doc.getString("Parent Email") == null)
                        continue;
                    if (doc.getString("Parent Email").equalsIgnoreCase(pEmail)) {
                        docRef = db.collection("Messages")
                                .document(doc.getId());
                        Map<String, Object> map;
                        if (doc.get("Message List") != null)
                            map = (Map<String, Object>) doc.get("Message List");
                        else
                            continue;
                        List<Object> list = new ArrayList<>();
                        Iterator it = map.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();
                            count = Integer.parseInt(String.valueOf(pair.getKey())) + 1;
                        }
                        Collections.addAll(list, message, sentBy);
                        map.put(String.valueOf(count++), list);
                        ApiFuture<WriteResult> arrayUnion = docRef.update("Message List", map);
                        try {
                            arrayUnion.get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                }
            }
        }
        docRef = db.collection("Messages").document();
        Map<String, Object> data = new HashMap<>();
        data.put("Counselor Email", cEmail);
        data.put("Parent Email", pEmail);

        Map<String, Object> m = new HashMap<>();
        List<Object> l = new ArrayList<>();
        Collections.addAll(l, message, sentBy);
        m.put(String.valueOf((count++)), l);
        data.put("Message List", m);

        ApiFuture<WriteResult> result = docRef.set(data);
        try {
            result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static List<String> getMessages(String cEmail, String pEmail) {
        ApiFuture<QuerySnapshot> query = db.collection("Messages").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if (querySnapshot != null) {
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot doc : documents) {
                if (doc.getString("Counselor Email") == null)
                    continue;
                if (doc.getString("Counselor Email").equalsIgnoreCase(cEmail)) {
                    if (doc.getString("Parent Email") == null)
                        continue;
                    if (doc.getString("Parent Email").equalsIgnoreCase(pEmail)) {
                        Map<String, Object> map;
                        if (doc.get("Message List") != null)
                            map = (Map<String, Object>) doc.get("Message List");
                        else
                            continue;
                        List<String> messages = new ArrayList<>();
                        Iterator it = map.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();
                            List<Object> l = (List<Object>) pair.getValue();
                            messages.add((String) l.get(0));
                        }
                        return messages;
                    }
                }
            }
        }
        return null;
    }
}
