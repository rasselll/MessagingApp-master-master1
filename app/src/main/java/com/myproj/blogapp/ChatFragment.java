package com.myproj.blogapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private RecyclerView userlist;
    private DatabaseReference dbRef;
    private View userView;
    private LinearLayoutManager mLayoutManager;
    private String currentUserId;
    private FirebaseAuth firebaseAuth;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userView = inflater.inflate(R.layout.fragment_chat, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        userlist = (RecyclerView) userView.findViewById(R.id.userlist);
        dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
        dbRef.keepSynced(true);
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
        FirebaseRecyclerAdapter<User, UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(
                User.class,
                R.layout.userlist_layout,
                UserViewHolder.class,
                dbRef
        ) {
            @Override
            protected void populateViewHolder(final UserViewHolder viewHolder, User model, int position) {
                final String userid = getRef(position).getKey();
                viewHolder.setDisplayName(model.getName());
                viewHolder.setUserStatus(model.getStatus());
                viewHolder.setUserimage(getContext(),model.getImage());


                dbRef.child(userid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String userName = dataSnapshot.child("name").getValue().toString();
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
                              //  chatIntent.putExtra("imageURI", imageURI);

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
