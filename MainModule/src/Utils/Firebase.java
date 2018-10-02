package Utils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.*;

import java.io.FileInputStream;
import java.io.IOException;

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

    public static void init() throws IOException {
        // Fetch the service account key JSON file contents
        FileInputStream serviceAccount = new FileInputStream("/Users/akhilagrawal/IdeaProjects/PsychEval/MainModule/src/Utils/psycheval-ff91b-firebase-adminsdk-pjtsv-d414b51557.json");

        // Initialize the app with a service account, granting admin privileges
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://psycheval-ff91b.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);

        // As an admin, the app has access to read and write all data, regardless of Security Rules
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
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
        });
    }

    public static void createAccount(String email, String password, String dispName, String type) throws FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setEmailVerified(false)
                .setPassword(password)
                .setDisplayName(dispName)
                .setPhotoUrl(type)  // We are using this to identify client type, i.e., Admin, Counselor, or Parent
                .setDisabled(false);

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        System.out.println("Successfully created new user: " + userRecord.getUid());
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

    public static void main(String[] args) throws IOException {
        init();
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
        return userRecord.getPhotoUrl();
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
    }

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
