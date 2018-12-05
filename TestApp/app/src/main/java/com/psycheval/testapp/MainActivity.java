package com.psycheval.testapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

//import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.grpc.okhttp.internal.Util;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Context myContext = getApplicationContext();
        AssetManager asmgr = myContext.getAssets();

        //Firebase.setPassword("parent@parent.com", "Test1!");
        //Firebase.init();
        //Utils.setTwitter();

    }

    public void loginButton(View view){
        final Intent intent = new Intent(this, MainViewScreen.class);
        EditText emailField =  findViewById(R.id.emailField);
        EditText passField =  findViewById(R.id.passField);
        final String email = emailField.getText().toString();
        final String pass = passField.getText().toString();
        Firebase.login(email, pass, new Firebase.loginCallback() {
            @Override
            public void onCallback(boolean isTrue) {
                if(isTrue){
                    //displayToast("Logged in succesfully");
                    Firebase.isParent(email, new Firebase.isParentCallback() {
                        @Override
                        public void onCallback(boolean isTrue) {
                            if(isTrue){
                                Firebase.getName(email, new Firebase.getNameCallback() {
                                    @Override
                                    public void onCallback(String name) {
                                        if(!name.isEmpty()){
                                            Account.curruser = new Account(email, pass, name, Account.AccountType.PARENT);
                                            startActivity(intent);
                                            finish();
                                        }else{
                                            Utils.displayToast(getApplicationContext(), "Something went wrong. Error Code: n1");
                                        }

                                    }
                                });
                            }else{
                                Utils.displayToast(getApplicationContext(),"Only Parent Accounts Can Use This App");
                            }
                        }
                    });
                }else{
                    Utils.displayToast(getApplicationContext(),"Incorrect Username or Password");
                }
            }
        });
    }


}
