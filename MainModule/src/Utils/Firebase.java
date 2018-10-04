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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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
            if (document.getString("email") == null)
                continue;
            if (document.getString("email").equalsIgnoreCase(email))
                return document.getString("Type");
        }
        return null;
    }

    public String getName(String email) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("Authentication").get();
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            if (document.getString("email") == null)
                continue;
            if (document.getString("email").equalsIgnoreCase(email))
                return document.getString("Name");
        }
        return null;
    }

    public String getPassword(String email) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("Authentication").get();
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            if (document.getString("email") == null)
                continue;
            if (document.getString("email").equalsIgnoreCase(email))
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

            ApiFuture<WriteResult> future = null;
            // Update an existing document
            if (document.getString("email") == null)
                continue;
            if (document.getString("email").equalsIgnoreCase(email))
                future = document.getReference().update("password", password);
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

    /*public static void main(String[] args) {
//        Firebase firebase = new Firebase();
        init();
//        createAccount("akhil", "akhil", "akhil", "akhil");
        setPassword("akhil", "rish");
    }*/

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

    private void setStudents(String email, String [] studentNames) {
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

            ApiFuture<WriteResult> future = null;
            // Update an existing document
            if (document.getString("Email") == null)
                continue;
            if (document.getString("Email").equalsIgnoreCase(email))
                future = document.getReference().update("Student Names", studentNames);
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

    private void setParents(String email, String [] parentNames) {
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

            ApiFuture<WriteResult> future = null;
            // Update an existing document
            if (document.getString("Email") == null)
                continue;
            if (document.getString("Email").equalsIgnoreCase(email)) {
                future = document.getReference().update("Parents.Name", parentNames);
                future = document.getReference().update("Parents.Approved", false);
            }
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

    private void setParentsApproved(String email, String parentName) {
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

            ApiFuture<WriteResult> future = null;
            // Update an existing document
            if (document.getString("Email") == null)
                continue;
            if (document.getString("Email").equalsIgnoreCase(email)) {
                if (document.getString("Parent.Name") == null)
                    continue;
                if (document.getString("Parent.Name").equalsIgnoreCase(parentName))
                    future = document.getReference().update("Parents.Approved", true);
            }
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

    public String getCounselorName(String email) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("Counselor").get();
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            if (document.getString("Email") == null)
                continue;
            if (document.getString("Email").equalsIgnoreCase(email))
                return document.getString("Name");
        }
        return null;
    }

    public String [] getStudentNames(String email) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("Counselor").get();
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            if (document.getString("Email") == null)
                continue;
            if (document.getString("Email").equalsIgnoreCase(email))
                return (String[]) document.get("Student Names");
        }
        return null;
    }

    private void setCounselorDB(String name, String email) {
        DocumentReference docRef = FirestoreClient.getFirestore().collection("Counselor").document();
        Map<String, Object> data = new HashMap<>();
        data.put("Email", email);
        data.put("Name", name);
        data.put("Approved", false);

        ApiFuture<WriteResult> result = docRef.set(data);
        try {
            result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    // social media db - student name, twitter oauth key, twitter link, score, getters & setters
    private void setSocialMediaDB(int riskFactor, String name, String link, String oauthKey) {
        DocumentReference docRef = FirestoreClient.getFirestore().collection("SocialMedia").document();
        Map<String, Object> data = new HashMap<>();
        data.put("Risk Factor", riskFactor);
        data.put("Student Name", name);
        data.put("Twitter Link", link);

        ApiFuture<WriteResult> result = docRef.set(data);
        try {
            result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public String getRiskFactor(String name) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = FirestoreClient.getFirestore().collection("Counselor").get();
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            if (document.getString("Student Name") == null)
                continue;
            if (document.getString("Student Name").equalsIgnoreCase(name))
                return document.getString("Risk Factor");
        }
        return null;
    }
}

