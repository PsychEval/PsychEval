package com.psycheval.testapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class ApprovedActivity extends AppCompatActivity {

    private Button checkButton;
    private EditText counselorEmailField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved);

        checkButton=findViewById(R.id.checkButton);
        counselorEmailField = findViewById(R.id.approvalEmail);


        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String counselorEmail = counselorEmailField.getText().toString();
                final Intent intent = new Intent(getApplicationContext(), MainViewScreen.class);
                Firebase.getStudentName(Account.curruser.getEmail(), new Firebase.getStudentNameCallback() {
                    @Override
                    public void onCallback(List<String> names) {
                            Firebase.isApproved(counselorEmail, Account.curruser.getEmail(), names, new Firebase.isApprovedCallback() {
                                @Override
                                public void onCallback(List<ApprovedObject> isApproved) {
                                    StringBuilder approvedYetAllChildren = new StringBuilder();
                                    for (ApprovedObject o : isApproved) {
                                        if(o.getStatus()){
                                            approvedYetAllChildren.append("You are approved for child: " + o.getName() + "\n");
                                        }else{
                                            approvedYetAllChildren.append("You are not approved for child: " + o.getName() + "\n");
                                        }
                                    }
                                    Utils.displayToast(getApplicationContext(), approvedYetAllChildren.toString());
                                }
                            });

                    }
                });







//                Firebase.isApproved(cousnelorEmail, Account.curruser.getEmail(),"", new Firebase.isApprovedCallback() {
//                    @Override
//                    public void onCallback(boolean isApproved) {
//                        if(isApproved){
//                            Utils.displayToast(getApplicationContext(), "You are approved!");
//                            startActivity(intent);
//                            finish();
//                        }else{
//                            Utils.displayToast(getApplicationContext(), "You are not approved!");
//                            startActivity(intent);
//                            finish();
//                        }
//                    }
//                });
            }
        });


    }
}
