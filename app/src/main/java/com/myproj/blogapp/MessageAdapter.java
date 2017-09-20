package com.myproj.blogapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by Jawwad on 19/08/2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private List<Message> messagesList;

    private DatabaseReference databaseReference;
    private DatabaseReference mDatabaseUser;
    private DatabaseReference userDatabaseRef;
    private   FirebaseAuth firebaseAuth;
    private Context context;
    private String imageUrl;
    private String getCurrent;
    private FirebaseAuth mAuth;

    private FirebaseUser mCurrentUser;
    public static int INCOMING = 1;
    public static int OUTGOING = 0;




    public MessageAdapter(List<Message> messagesList) {
        this.messagesList = messagesList;
    }

    @Override
    public int getItemViewType(int position) {

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        String last = mDatabaseUser.toString().substring(mDatabaseUser.toString().lastIndexOf('/') + 1);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("name");
        Message m = messagesList.get(position);
        if( m.getFrom().equals(last)){
            return  MessageAdapter.INCOMING;

        }else{
            return MessageAdapter.OUTGOING;
        }
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Message c = messagesList.get(viewType);
        String sender = c.getFrom();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(sender);

        View v = null;

        if( viewType == MessageAdapter.OUTGOING ) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.messages_layout, parent, false);
        }else if ( viewType == MessageAdapter.INCOMING) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.messages_layout_sent, parent, false);
        }

        return new MessageViewHolder(v);

    }




    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, final int index) {



        getItemViewType(index);
        final Message c = messagesList.get(index);

        final String sender = c.getFrom();
        getCurrent  = sender.toString();




        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(sender);

        databaseReference.addValueEventListener(new ValueEventListener() {



            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



             imageUrl = dataSnapshot.child("image").getValue(String.class);
                viewHolder.setUserimage(context,imageUrl);





                String name = dataSnapshot.child("name").getValue().toString();
                viewHolder.displayName.setText(name);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });




        viewHolder.messageText.setText(c.getMessage());
        viewHolder.time.setText(EpochtimeToDateAndTimeString(c.getTime()));


    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }


    public String EpochtimeToDateAndTimeString(long time) {
        Date date = new Date(time);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        String formatted = format.format(date);
        return formatted;
    }



}
