package com.myproj.blogapp;


import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private RecyclerView userlist;
    private DatabaseReference dbRef;
    private DatabaseReference dbRefs;
    private DatabaseReference users;
    private View userView;
    private LinearLayoutManager mLayoutManager;
    private String currentUserId;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUser;
    private  DatabaseReference chatIdRef;
    private DatabaseReference usersRef;
    private DatabaseReference rootRef;
    private ValueEventListener eventListener;
    private Query Q;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userView = inflater.inflate(R.layout.fragment_chat, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        userlist = (RecyclerView) userView.findViewById(R.id.userlist);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        final String last = mDatabaseUser.toString().substring(mDatabaseUser.toString().lastIndexOf('/') + 1);

        dbRef = FirebaseDatabase.getInstance().getReference().child("Chatmessages").child(last);
        users = FirebaseDatabase.getInstance().getReference().child("Users");



        rootRef = FirebaseDatabase.getInstance().getReference();
        chatIdRef = rootRef.child("Chatmessages").child(last);
/*         eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String uid = ds.getKey();
                     usersRef = rootRef.child("Users").child(uid);
                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String image = dataSnapshot.child("image").getValue(String.class);
                            String name = dataSnapshot.child("name").getValue(String.class);
                            Boolean onlineStatus = dataSnapshot.child("onlineStatus").getValue(Boolean.class);
                            String status = dataSnapshot.child("username").getValue(String.class);
                            Log.d("TAG", image + " / " + name + " / " + onlineStatus + " / " + status);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    };
                    usersRef.addListenerForSingleValueEvent(eventListener);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
     chatIdRef.addListenerForSingleValueEvent(eventListener);*/

        chatIdRef.keepSynced(true);
        userlist.setHasFixedSize(true);
        userlist.setLayoutManager(new LinearLayoutManager(getContext()));
        return userView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadUserList();
    }




    public void loadUserList() {
            mAuth = FirebaseAuth.getInstance();
            mCurrentUser = mAuth.getCurrentUser();
            mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
            final String last = mDatabaseUser.toString().substring(mDatabaseUser.toString().lastIndexOf('/') + 1);

            dbRef = FirebaseDatabase.getInstance().getReference().child("Chatmessages").child(last);
            users = FirebaseDatabase.getInstance().getReference().child("Users");

            String chatMessageId = last;    // Get or pass this key somehow
            DatabaseReference userKeyRef = rootRef.child("Chatmessages").child(chatMessageId);
            DatabaseReference userRef = rootRef.child("Users");


            FirebaseIndexRecyclerAdapter<User, UserViewHolder> firebaseRecyclerAdapter = new FirebaseIndexRecyclerAdapter<User, UserViewHolder>(
                    User.class,
                    R.layout.userlist_layout,
                    UserViewHolder.class,
                    users,
                    userRef
            )  {




            @Override

            protected void populateViewHolder(final UserViewHolder viewHolder, User model, int position) {

                mAuth = FirebaseAuth.getInstance();
                mCurrentUser = mAuth.getCurrentUser();
                mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
                final String last = mDatabaseUser.toString().substring(mDatabaseUser.toString().lastIndexOf('/') + 1);
                dbRef = FirebaseDatabase.getInstance().getReference().child("Chatmessages").child(last);
                users = FirebaseDatabase.getInstance().getReference().child("Users");



                rootRef = FirebaseDatabase.getInstance().getReference();
                chatIdRef = rootRef.child("Chatmessages").child(last);


                final String userid = getRef(position).getKey();
                viewHolder.setDisplayName(model.getName());
                viewHolder.setUserStatus(model.getStatus());
                viewHolder.setUserimage(getContext(),model.getImage());
                viewHolder.setMessage(model.getMessage());
                viewHolder.setUid(model.getUid());




                DatabaseReference d = rootRef.child("Chatmessages").child(last).child(userid);

                Query lastQuery = rootRef.child("Chatmessages").child(last).child(userid).limitToLast(1);
                lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                            String message = child.child("message").getValue().toString();

                        viewHolder.setMessage(message);

                            Log.w("myApp", message);
                         //   viewHolder.setUserStatus("dsdfds");
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });





                users.child(userid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       final String userName = dataSnapshot.child("name").getValue().toString();
                  /*      final String message  = dataSnapshot.child("name").getValue().toString();
                        viewHolder.setMessage(message);*/
               /*         final String imageURI = dataSnapshot.child("image").getValue().toString();
                        viewHolder.setUserimage(getContext(), imageURI);*/


                        if (dataSnapshot.hasChild("onlineStatus")) {
                            String userOnline = dataSnapshot.child("onlineStatus").getValue().toString();
                            viewHolder.setUserOnlineStatus(userOnline);
                        }
                        viewHolder.userView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                chatIntent.putExtra("userid", userid);
                               chatIntent.putExtra("userName", userName);
                               //chatIntent.putExtra("imageURI", imageURI);

                                startActivity(chatIntent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }
        };
        userlist.setAdapter(firebaseRecyclerAdapter);
    }
}
