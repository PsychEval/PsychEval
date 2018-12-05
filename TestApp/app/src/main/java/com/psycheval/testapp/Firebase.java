package com.psycheval.testapp;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1beta1.WriteResult;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;

import static android.support.constraint.Constraints.TAG;

public class Firebase {

    static FirebaseFirestore db;

    static void init(){
        db = FirebaseFirestore.getInstance();
        Log.e("FB", "INITIALIZED!");
        db.collection("Counselor")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        List<String> data = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {

                        }
                        Log.d(TAG, "Some data: " + data);

                    }
                });
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
        void onCallback(List<ApprovedObject> isApproved);
    }

    public static void isApproved(final String cousnelorEmail, final String parentEmail, final List<String> child, final isApprovedCallback callback){
        db.collection("Counselor")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<ApprovedObject> finList = new ArrayList<>();
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
                                            if (l.get(0).equals(parentEmail) && child.contains((String) l.get(2)))
                                                finList.add(new ApprovedObject((String)l.get(2), (boolean)l.get(1)));
                                        }
                                    }
                                }
                            }
                        }else{
                            Log.e("ERROR", "Could not access database in isApproved");
                        }
                        callback.onCallback(finList);
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


    public static interface getStudentNameCallback{
        void onCallback(List<String> names);
    }


    public static void getStudentName(final String parentEmail, final getStudentNameCallback callback){
        db.collection("Counselor")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String studentName = "";
                        List<String> children = new ArrayList<>();
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
                                    children.add(l.get(2));
                                }
                            }
                        }else{
                            Log.e("ERROR", "Failed to access database from getStudentName");
                        }
                        callback.onCallback(children);
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


    public static void sendMessage(final String cEmail, final String pEmail, final String message, final sendMessageCompleteCallback callback){
        db.collection("Messages")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        DocumentReference docRef;
                        int count = 0;
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot doc : task.getResult()) {
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
                                        Collections.addAll(list, message, 1);
                                        map.put(String.valueOf(count++), list);

                                        docRef.update("Message List", map);
                                        callback.onCallback();
                                        return;
                                    }
                                }
                            }
                            docRef = db.collection("Messages").document();
                            Map<String, Object> data = new HashMap<>();
                            data.put("Counselor Email", cEmail);
                            data.put("Parent Email", pEmail);

                            Map<String, Object> m = new HashMap<>();
                            List<Object> l = new ArrayList<>();
                            Collections.addAll(l, message, 1);
                            m.put(String.valueOf((count++)), l);
                            data.put("Message List", m);

                            docRef.set(data);
                            callback.onCallback();
                            return;
                        }else{
                            Log.e("ERROR", "Failed to get database in sendMessage");
                        }
                        callback.onCallback();

                    }

                });
    }

    public static void setPassword(final String email, final String password){
        db.collection("Authentication")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                if (document.getString("email") == null)
                                    continue;
                                byte[] salt = email.getBytes();
                                byte[] pass = Passwords.hash(password, salt);
                                String p = "";
                                for (byte b:pass)
                                    p += b + " ";

                                if(document.getString("email").equalsIgnoreCase(email)){
                                    document.getReference().update("password",p);
                                    document.getReference().update("salt", salt.toString());
                                }


//                                try {B
//                                    if (document.getString("email").equalsIgnoreCase(email)) {
//                                        future = document.getReference().update("password", p);
//                                        future.get();
//                                        future = document.getReference().update("salt", salt.toString());
//                                        future.get();
//                                    }
//                                } catch (InterruptedException | ExecutionException e) {
//                                    e.printStackTrace();
//                                }
                            }
                        }else{
                            Log.e("ERROR", "Failed to access database in setPassword");
                        }
                    }
                });
    }

}































