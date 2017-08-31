package com.myproj.blogapp;

import android.app.Application;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Jawwad on 18/08/2017.
 */

public class MessagingAndriodApp extends Application {

    private DatabaseReference userDatabaseRef;
    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            userDatabaseRef = FirebaseDatabase.getInstance()
                    .getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());
            userDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        userDatabaseRef.child("onlineStatus").onDisconnect().setValue(false);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

}
