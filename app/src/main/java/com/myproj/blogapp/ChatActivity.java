package com.myproj.blogapp;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private final List<Message> messagesList = new ArrayList<>();
    private Toolbar toolbar;
    private DatabaseReference databaseReference, dbRef;
    private FirebaseAuth firebaseAuth;
    private String userId;
    private String currentChatUser;
    private ImageButton sendButton;
    private EditText messageTextBox;
    private RecyclerView messagesRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private ImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
/*        toolbar = (Toolbar) findViewById(R.id.chat_app_bar);
        setSupportActionBar(toolbar);*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getIntent().getStringExtra("userName"));

        currentChatUser = getIntent().getStringExtra("userid");
        dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
        final String imageURI = dbRef.child("image").toString();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        sendButton = (ImageButton) findViewById(R.id.send_button);
        messageTextBox = (EditText) findViewById(R.id.message_text);
        //  -----------------------------------------------------------------------

        messageAdapter = new MessageAdapter(messagesList);
        messagesRecyclerView = (RecyclerView) findViewById(R.id.messagesList);
        linearLayoutManager = new LinearLayoutManager(this);
        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(linearLayoutManager);
        messagesRecyclerView.setAdapter(messageAdapter);
        loadMessages();
          sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String message = messageTextBox.getText().toString();
        if (!TextUtils.isEmpty(message)) {

            String current_user_ref = "Chatmessages/" + currentChatUser + "/" + userId;
            String chat_user_ref = "Chatmessages/" + userId + "/" + currentChatUser;
            String chat_user_image = "Chatmessages/" + userId + "/" + currentChatUser;

            DatabaseReference user_message_push = databaseReference.child("Chatmessages").child(currentChatUser).child(userId).push();

            String push_id = user_message_push.getKey();
            Map messageMap = new HashMap();
            messageMap.put("message", message);
            messageMap.put("time", ServerValue.TIMESTAMP);
            messageMap.put("from", userId);
            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
            messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);
            messageTextBox.setText("");
            databaseReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    // We can show Error Messages Here If we want
                }
            });
        }
    }

    private void loadMessages() {
        databaseReference.child("Chatmessages").child(userId).child(currentChatUser).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                messagesList.add(message);
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}