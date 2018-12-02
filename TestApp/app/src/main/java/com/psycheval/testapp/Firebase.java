package com.psycheval.testapp;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1beta1.WriteResult;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static android.support.constraint.Constraints.TAG;

public class Firebase {

    static FirebaseFirestore db;

    static void init(){
        db = FirebaseFirestore.getInstance();
        Log.e("FB", "INITIALIZED!");
        System.out.println("estaeads");
    }

    public static interface loginCallback{
        void onCallback(boolean isTrue);
    }

    public static void login(final String email, final String password, final loginCallback callback){
        CollectionReference cr = db.collection("Authentication");

        byte[] salt = email.getBytes();
        byte[] pass = Passwords.hash(password, salt);
        String p = "";
        for(byte b:pass){
            p += b + " ";
        }
        Query q1 = cr.whereEqualTo("email",email).whereEqualTo("password",p);
        q1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                boolean isTrue = false;
                for(DocumentSnapshot ds : queryDocumentSnapshots){
                    String tempEmail, tempPass;
                    tempEmail = ds.getString("email");
                    tempPass  = ds.getString("password");
                    if(tempEmail.equalsIgnoreCase(email)){
                        //String pass = document.getString("password");
                        String [] arr = tempPass.split(" ");
                        byte[] p = new byte[arr.length];
                        for (int i = 0; i < arr.length; i++)
                            p[i] = Byte.parseByte(arr[i]);
                        byte[] salt = email.getBytes();
                        if (Passwords.isExpectedPassword(password, salt, p)){
                            isTrue = true;
                        }
                    }
                }
                callback.onCallback(isTrue);
            }
        });

    }

    public static interface isParentCallback{
        void onCallback(boolean isTrue);
    }

    public static void isParent(final String email, final isParentCallback callback){
        db.collection("Authentication")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean isTrue = false;
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot doc : task.getResult()){
                                if(doc.getString("email").equalsIgnoreCase(email)){
                                    if(doc.getString("type").equalsIgnoreCase("Parent")){
                                        isTrue = true;
                                    }
                                }
                            }
                        }else{
                            Log.e("ERROR", "Could not get database info from getType");
                        }
                        callback.onCallback(isTrue);
                    }
                });
    }

    public static interface getNameCallback{
        void onCallback(String name);
    }

    public static void getName(final String email, final getNameCallback callback){
        db.collection("Authentication")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean isTrue = false;
                        String name = "";
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot doc : task.getResult()){
                                if(doc.getString("email").equalsIgnoreCase(email)){
                                    name = doc.getString("name");
                                }
                            }
                        }else{
                            Log.e("ERROR", "Could not get database info from getType");
                        }
                        callback.onCallback(name);
                    }
                });
    }

    public static void setParents(final String email, final Map<Integer, Object> m){
        db.collection("Counselor")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc : task.getResult()){
                                if(doc.getString("Email").equalsIgnoreCase(email)){
                                    DocumentReference docref = db.collection("Counselor").document(doc.getId());
                                    Map<String, Object> map;
                                    map = (Map<String, Object>) doc.get("Parents");
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
                                    docref.update("Parents", map);
                                }
                            }
                        }else{
                            Log.e("ERROR", "Could not access database from setParents");
                        }
                    }
                });
    }


    public interface isApprovedCallback{
        void onCallback(boolean isApproved);
    }

    public static void isApproved(final String cousnelorEmail, final String parentEmail, final isApprovedCallback callback){
        db.collection("Counselor")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean isApproved = false;
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot doc : task.getResult()){
                                if(doc.getString("Email") == null){
                                    continue;
                                }
                                if(doc.getString("Email").equalsIgnoreCase(cousnelorEmail)){
                                    Map<String, Object> map;
                                    if (doc.get("Parents") != null)
                                        map = (Map<String, Object>) doc.get("Parents");
                                    else
                                        continue;
                                    List<Object> l;
                                    Iterator it = map.entrySet().iterator();
                                    while (it.hasNext()) {
                                        Map.Entry pair = (Map.Entry) it.next();
                                        l = (List<Object>) pair.getValue();
                                        if (l != null) {
                                            if (l.get(0).equals(parentEmail))
                                                isApproved = (boolean) l.get(1);
                                        }
                                    }
                                }
                            }
                        }else{
                            Log.e("ERROR", "Could not access database in isApproved");
                        }
                        callback.onCallback(isApproved);
                    }
                });
    }


    public static void setSocialMediaDB(final String pEmail, final int riskFactor, final String name, final String token, final String tokenSecret, final String accName, final String uid){
        db.collection("SocialMedia")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc : task.getResult()){
                                if (doc.getString("Parent Email")== null){
                                    continue;
                                }
                                if(doc.getString("Parent Email").equalsIgnoreCase(pEmail)){
                                    db.collection("SocialMedia").document(doc.getId()).delete();
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
                            db.collection("SocialMedia").add(data);
                        }else{
                            Log.e("ERROR", "Failed to access database in setSocialMediaDB");
                        }
                    }
                });
    }



    public static void getStudentName(final String parentEmail, final getNameCallback callback){
        db.collection("Counselor")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String studentName = "";
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc : task.getResult()){
                                Map<String, Object> map;
                                if (doc.get("Parents") != null)
                                    map = (Map<String, Object>) doc.get("Parents");
                                else
                                    continue;
                                Iterator it = map.entrySet().iterator();
                                while (it.hasNext()) {
                                    Map.Entry pair = (Map.Entry) it.next();
                                    List<String> l = (List<String>) pair.getValue();
                                    if (!l.contains(parentEmail))
                                        continue;
                                    studentName = l.get(2);
                                }
                            }
                        }else{
                            Log.e("ERROR", "Failed to access database from getStudentName");
                        }
                        callback.onCallback(studentName);
                    }
                });
    }


    public interface getCounselorEmailCallback{
        void onCallback(String email);
    }

    public static void getCounselorEmail(final String parentEmail, final  getCounselorEmailCallback callback){
        db.collection("Counselor")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String name = "";
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc : task.getResult()){
                                if(doc.get("Parents")==null){
                                    continue;
                                }
                                Map<String, Object> map = (Map<String, Object>) doc.get("Parents");
                                Iterator it = map.entrySet().iterator();
                                while (it.hasNext()) {
                                    Map.Entry pair = (Map.Entry) it.next();
                                    List<Object> l = (List<Object>) pair.getValue();
                                    if (l.contains(parentEmail))
                                        name = doc.getString("Email");
                                }

                            }
                        }else{
                            Log.e("ERROR", "Couldn't ask database in getCounselorName");
                        }
                        callback.onCallback(name);
                    }
                });
    }

    public interface getMessagesCallback{
        void onCallback(List<List<Object>> list);
    }

    public static void getMessages(final String cEmail, final String pEmail, final getMessagesCallback callback){

        db.collection("Messages")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<List<Object>> finList = null;
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc : task.getResult()){

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
                                        List<List<Object>> messages = new ArrayList<>();
                                        Iterator it = map.entrySet().iterator();
                                        while (it.hasNext()) {
                                            Map.Entry pair = (Map.Entry) it.next();
                                            List<Object> l = (List<Object>) pair.getValue();
                                            List<Object> list = new ArrayList<>();
                                            list.add(l.get(0));
                                            list.add(l.get(1));
                                            messages.add(list);
                                        }
                                        finList = messages;
                                    }
                                }
                            }
                        }else{
                            Log.e("ERROR", "Failed to access database from getMessages");
                        }
                        callback.onCallback(finList);
                    }
                });
    }

    public interface sendMessageCompleteCallback{
        void onCallback();
    }


    public static void sendMessage(String cEmail, String pEmail, String Message, final sendMessageCompleteCallback callback){
        db.collection("Messages")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                        }else{
                            Log.e("ERROR", "Failed to get database in sendMessage");
                        }
                        callback.onCallback();
                    }

                });
    }

}































