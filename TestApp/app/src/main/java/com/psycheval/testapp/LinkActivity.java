package com.psycheval.testapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class LinkActivity extends AppCompatActivity {

    private EditText counselorEmailField;
    private EditText nameField;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);

        counselorEmailField = findViewById(R.id.editTextCounselorEmail);
        nameField = findViewById(R.id.editTextName);
        submitButton = findViewById(R.id.submitButton);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String counselorEmail = counselorEmailField.getText().toString();
                String studentName = nameField.getText().toString();
                Map<Integer, Object> m;
                m = new HashMap<>();
                m.put(0, Account.curruser.getEmail());
                m.put(1, false);
                m.put(2, studentName);
                m.put(3, false);
                Firebase.setParents(counselorEmail, m);
                Intent intent  = new Intent(getApplicationContext(), MainViewScreen.class);
                Utils.displayToast(getApplicationContext(), "Success, counselor will review your request");
                startActivity(intent);
                finish();
            }
        });


    }
}
