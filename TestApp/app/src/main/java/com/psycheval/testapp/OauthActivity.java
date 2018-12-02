package com.psycheval.testapp;


import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

import static com.psycheval.testapp.Utils.requestToken;
import static com.psycheval.testapp.Utils.twitter;


public class OauthActivity extends AppCompatActivity {


    private Button twitterLoginButton;
    private Button submitTwitterButton;
    private EditText pinField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth);

        submitTwitterButton=findViewById(R.id.submitTwitterButton);
        pinField = findViewById(R.id.pinCodeField);
        twitterLoginButton = findViewById(R.id.twitterLogin);



        twitterLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL()));
                startActivity(browserIntent);
            }
        });

        submitTwitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = pinField.getText().toString();
                try {
                    enterCode(code);
                }catch (Exception e){
                    Utils.displayToast(getApplicationContext(), "Failed to authenticate");
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), MainViewScreen.class);
                startActivity(intent);
                finish();

            }
        });

    }

    void enterCode(String code){
        AccessToken accessToken = null;
        try{
            if(code.length() > 0){
                accessToken = twitter.getOAuthAccessToken(requestToken, code);
            }else{
                accessToken = twitter.getOAuthAccessToken();
            }
        } catch (TwitterException te) {
            if(401 == te.getStatusCode()){
                System.out.println("Unable to get the access token.");
                //TODO: add failure message
            }else{
                te.printStackTrace();
            }
        }
        System.out.println(accessToken.getToken());
        System.out.println(accessToken.getTokenSecret());
        System.out.println(accessToken.getScreenName());
        System.out.println(accessToken.getUserId());
        //TODO: push tokens to database
        updateTokens(accessToken.getToken(), accessToken.getTokenSecret(), accessToken.getScreenName(), Long.toString(accessToken.getUserId()));
    }

    void updateTokens(final String token, final String secret, final String twitterId, final String userID){

        Firebase.getStudentName(Account.curruser.getEmail(), new Firebase.getNameCallback() {
            @Override
            public void onCallback(String name) {
                Firebase.setSocialMediaDB(Account.curruser.getEmail(), -1, name ,token,secret,twitterId,userID );
            }
        });

        //TODO
    }
}
