package com.psycheval.testapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainViewScreen extends AppCompatActivity {

    private FloatingActionButton fabMain;
    private FloatingActionButton fabLink;
    private FloatingActionButton fabOauth;
    private FloatingActionButton fabApproved;
    private FloatingActionButton fabMessage;
    private TextView textViewLink;
    private TextView textViewOauth;
    private TextView textViewApproved;
    private TextView textViewMessage;
    private Button editPassButton;
    private Button logoutButton;
    private TextView textViewHello;
    private boolean isFabOpen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view_screen);


        isFabOpen = false;
        fabMain  = findViewById(R.id.fabMain);
        fabLink  = findViewById(R.id.fabLink);
        fabOauth  = findViewById(R.id.fabOauth);
        fabApproved  = findViewById(R.id.fabApproved);
        fabMessage  = findViewById(R.id.fabMessage);



        textViewLink = findViewById(R.id.textViewLink);
        textViewOauth = findViewById(R.id.textViewOauth);
        textViewApproved = findViewById(R.id.textViewApproved);
        textViewMessage = findViewById(R.id.textViewMessage);

        editPassButton = findViewById(R.id.editPassButton);
        logoutButton = findViewById(R.id.logoutButton);

        textViewHello = findViewById(R.id.textViewHello);
        String helloMessage = "Hello " + Account.curruser.getName();
        textViewHello.setText(helloMessage);

        hideFabs();

        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFabOpen){
                    hideFabs();
                }else{
                    showFabs();
                }
            }
        });
        setFabOnClicks();
    }

    private void setFabOnClicks(){
        fabLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LinkActivity.class);
                startActivity(intent);


            }
        });
        fabOauth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OauthActivity.class);
                startActivity(intent);

            }
        });
        fabApproved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ApprovedActivity.class);
                startActivity(intent);

            }
        });
        fabMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MessagingActivity.class);
                startActivity(intent);

            }
        });

        editPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditPassActivity.class);
                startActivity(intent);
                finish();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getApplicationContext(), MainActivity.class);
                Account.curruser = null;
                startActivity(intent);
                finish();
            }
        });
    }

    private void hideFabs(){
        fabLink.hide();
        fabOauth.hide();
        fabApproved.hide();
        fabMessage.hide();
        textViewLink.setVisibility(View.INVISIBLE);
        textViewOauth.setVisibility(View.INVISIBLE);
        textViewApproved.setVisibility(View.INVISIBLE);
        textViewMessage.setVisibility(View.INVISIBLE);


        isFabOpen = false;
    }

    private void showFabs(){
        fabLink.show();
        fabOauth.show();
        fabApproved.show();
        fabMessage.show();
        textViewLink.setVisibility(View.VISIBLE);
        textViewOauth.setVisibility(View.VISIBLE);
        textViewApproved.setVisibility(View.VISIBLE);
        textViewMessage.setVisibility(View.VISIBLE);
        isFabOpen = true;
    }
}
