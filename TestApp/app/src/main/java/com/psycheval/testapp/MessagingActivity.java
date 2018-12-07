package com.psycheval.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.FirebaseError;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import io.grpc.okhttp.internal.Util;


public class MessagingActivity extends AppCompatActivity {

    //static ArrayList<Message> messages = new ArrayList<>();

    private ListView listView;
    private EditText messagingTextField;
    private Button sendMessageButton;
    private Button viewMessageButton;
    private EditText counselorEmailField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        listView = findViewById(R.id.messageListView);
        messagingTextField = findViewById(R.id.messageTextField);
        sendMessageButton = findViewById(R.id.sendMessageButton);
        counselorEmailField = findViewById(R.id.counslelorEmailFieldMessage);
        viewMessageButton = findViewById(R.id.viewMessageButton);

        viewMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadMessages();
            }
        });

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });


//        Firebase.getCounselorEmail(Account.curruser.getEmail(), new Firebase.getCounselorEmailCallback() {
//            @Override
//            public void onCallback(String email) {
//                //Account.curruser.setCounselorEmail(email);
//                Firebase.getMessages(email, Account.curruser.getEmail(), new Firebase.getMessagesCallback() {
//                    @Override
//                    public void onCallback(List<List<Object>> list) {
//                        ArrayList<Message> messages = new ArrayList<>();
//                        if(list == null){
//                            messages.add(new Message("No messages", ""));
//                        }else{
//                            for (List<Object> e : list){
//                                if((long)e.get(1)==1){
//                                    messages.add(new Message((String)e.get(0), "Sent By Parent"));
//                                }else{
//                                    messages.add(new Message((String)e.get(0), "Sent By Counselor"));
//                                }
//                            }
//                        }
//                        MessageListAdapter adapter = new MessageListAdapter(getApplicationContext(), R.layout.adapter_view_layout, messages);
//                        listView.setAdapter(adapter);
//                    }
//                });
//            }
//        });
    }


    void sendMessage(){
        String messageToSend = messagingTextField.getText().toString();
        if(messageToSend.isEmpty()){
            return;
        }
        messagingTextField.setText("");
        Firebase.sendMessage(counselorEmailField.getText().toString(), Account.curruser.getEmail(), messageToSend, new Firebase.sendMessageCompleteCallback() {
            @Override
            public void onCallback() {
                reloadMessages();
            }
        });



//
//        finish();
//        overridePendingTransition( 0, 0);
//        startActivity(getIntent());
//        overridePendingTransition( 0, 0);
    }

    void reloadMessages(){

        if(counselorEmailField.getText().toString().isEmpty()){
            Utils.displayToast(getApplicationContext(), "Please enter a counselor email");
            return;
        }

        Firebase.getMessages(counselorEmailField.getText().toString(), Account.curruser.getEmail(), new Firebase.getMessagesCallback() {
            @Override
            public void onCallback(List<List<Object>> list) {
                ArrayList<Message> messages = new ArrayList<>();
                if(list == null){
                    messages.add(new Message("No messages", ""));
                }else{
                    for (List<Object> e : list){
                        if((long)e.get(1)==1){
                            messages.add(new Message((String)e.get(0), "Sent By Parent"));
                        }else{
                            messages.add(new Message((String)e.get(0), "Sent By Counselor"));
                        }
                    }
                }
                MessageListAdapter adapter = new MessageListAdapter(getApplicationContext(), R.layout.adapter_view_layout, messages);
                listView.setAdapter(adapter);
            }
        });
    }

}
