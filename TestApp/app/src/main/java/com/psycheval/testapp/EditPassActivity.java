package com.psycheval.testapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditPassActivity extends AppCompatActivity {

    private Button submitButton;
    private EditText oldPassField;
    private EditText newPassField;
    private EditText confirmNewPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pass);


        oldPassField = findViewById(R.id.oldPasswordField);
        newPassField = findViewById(R.id.newPasswordField);
        submitButton = findViewById(R.id.buttonEditPass);
        confirmNewPass = findViewById(R.id.confirmPassField);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePass();
            }
        });


    }

    void changePass(){
        String newPassword = newPassField.getText().toString();
        //String confirmPassword = confirmNewPass.getText().toString();
        //String oldPassword = oldPassField.getText().toString();

        String message = Utils.validateNewPass(newPassword);
        if (!message.isEmpty()) {
            //there is an error
            Utils.displayToast(getApplicationContext(), message);
            return;
        }
        message = checkInput(oldPassField, newPassField, confirmNewPass);
        if(!message.isEmpty()){
            Utils.displayToast(getApplicationContext(), message);
            return;
        }
        Firebase.setPassword(Account.curruser.getEmail(), newPassword);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Utils.displayToast(getApplicationContext(), "Password successfully changed! Please log in again.");
        finish();
    }

    public String checkInput(EditText oldPassField, EditText newPassField, EditText newPass2Field) {
        String oldPass = oldPassField.getText().toString();
        //oldPassField.setText("");
        String newPass = newPassField.getText().toString();
        //newPassField.setText("");
        String newPass2 = newPass2Field.getText().toString();
        //newPass2Field.setText("");
        // check if old pass the same as account's old pass
        if(!oldPass.equals(Account.curruser.getPassword())){
            return "Incorrect current password";
        }
        if(!newPass.equals(newPass2)){
            return "New passwords do not match";
        }
        return "";
    }
}
