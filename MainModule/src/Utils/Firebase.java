package Utils;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

class Profile {
    private int riskFactor;
    private String name, link, oauthKey;

    public Profile() {
    }

    public Profile(int riskFactor, String name, String link, String oauthKey) {
        this.riskFactor = riskFactor;
        this.name = name;
        this.link = link;
        this.oauthKey = oauthKey;
    }

    public int getRiskFactor() {
        return riskFactor;
    }

    public void setRiskFactor(int riskFactor) {
        this.riskFactor = riskFactor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getOauthKey() {
        return oauthKey;
    }

    public void setOauthKey(String oauthKey) {
        this.oauthKey = oauthKey;
    }
}

class Counselor {
    // counselor db - name, email, parent list (parent name, student name, flag)
    private String name, email;

    public Counselor() {
    }

    public Counselor(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

public class Firebase {

    String type;
    String email;
    String name;
    String password;

    public Firebase() {}

    public Firebase(String type, String email, String name, String password) {
        this.type = type;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public String getType(String email) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("Authentication").get();
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            if (document.getString("Email").equalsIgnoreCase(email))
                return document.getString("Type");
        }
        return null;
    }

    public String getName(String email) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("Authentication").get();
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            if (document.getString("Email").equalsIgnoreCase(email))
                return document.getString("Name");
        }
        return null;
    }

    public String getPassword(String email) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("Authentication").get();
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            if (document.getString("Email").equalsIgnoreCase(email))
                return document.getString("Password");
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

            // Update an existing document
            ApiFuture<WriteResult> future = document.getReference().update("password", password);
            WriteResult result = null;
            try {
                result = future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            System.out.println("Write result: " + result);
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

    private static boolean login(String email, String password) throws ExecutionException, InterruptedException {
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

//    private DatabaseReference login(String email, String password) {
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Authentication");
//    }

    public static void main(String[] args) {
//        Firebase firebase = new Firebase();
        init();
//        createAccount("akhil", "akhil", "akhil", "akhil");
        setPassword("akhil", "rish");
    }

    public static void init() {
        // Fetch the service account key JSON file contents
        //   System.out.println(System.getProperty("user.dir"));
        FileInputStream serviceAccount = null;
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

//        FirestoreOptions options1 =
//                FirestoreOptions.newBuilder().setTimestampsInSnapshotsEnabled(true).build();
//        Firestore firestore = options1.getService();

        FirebaseApp.initializeApp(options);

        // As an admin, the app has access to read and write all data, regardless of Security Rules
        /*final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object document = dataSnapshot.getValue();
                System.out.println(document);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });*/
        System.out.println("Firebase successfully initialized");
    }

    /*public static void createAccount(String email, String password, String dispName, String type) throws FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setEmailVerified(false)
                .setPassword(password)
                .setDisplayName(dispName)
                .setPhoneNumber(type)  // We are using this to identify client type, i.e., Admin ends with 1, Counselor ends with 2, or Parent ends with 3
                .setDisabled(false);

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        System.out.println("Successfully created new user: " + userRecord.getUid());
    }

    private void login(String email, String password) {
        try {
            String token = FirebaseAuth.getInstance().createCustomToken(getUID(email));
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);

        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }
    }

    private static void updatePassword(String uid, String password) {
        try {
            UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(uid)
                    .setPassword("newPassword")
                    .setDisabled(false);

            UserRecord userRecord = FirebaseAuth.getInstance().updateUser(request);
            System.out.println("Successfully updated user: " + userRecord.getUid());
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }
    }


    // getters for user type & display name & email & UID
    private String getUID(String email) {
        UserRecord userRecord = null;
        try {
            userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }
        assert userRecord != null;
        return userRecord.getUid();
    }

    private String getUserType(String uid) {
        UserRecord userRecord = null;
        try {
            userRecord = FirebaseAuth.getInstance().getUser(uid);
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }
        assert userRecord != null;
        String number = userRecord.getPhoneNumber();
        String lastIndex = number.substring(number.length()-1);
        if(lastIndex.equals("1")){
            return "Admin";
        }else if(lastIndex.equals("2")){
            return "Counselor";
        }else{
            return "Parent";
        }
    }

    private String getDisplayName(String uid) {
        UserRecord userRecord = null;
        try {
            userRecord = FirebaseAuth.getInstance().getUser(uid);
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }
        assert userRecord != null;
        return userRecord.getDisplayName();
    }

    private String getEmail(String uid) {
        UserRecord userRecord = null;
        try {
            userRecord = FirebaseAuth.getInstance().getUser(uid);
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }
        assert userRecord != null;
        return userRecord.getEmail();
    }*/

    // counselor db - name, email, parent list (parent name, student name, flag)
    private void setCounselorDB(String name, String email) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Counselor");
        ref.setValueAsync(new Counselor(name, email));
    }

    // social media db - student name, twitter oauth key, twitter link, score, getters & setters
    private void setSocialMediaDB(int riskFactor, String name, String link, String oauthKey) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SocialMedia");
        ref.setValueAsync(new Profile(riskFactor, name, link, oauthKey));
    }
}

