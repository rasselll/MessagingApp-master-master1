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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private Context context;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child("image");
   private String imageUrl;


    public MessageAdapter(List<Message> messagesList) {
        this.messagesList = messagesList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Message c = messagesList.get(viewType);
        String sender = c.getFrom();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(sender);

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.messages_layout, parent, false);
            return new MessageViewHolder(v);


    }



    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, final int index) {

        final Message c = messagesList.get(index);

        final String sender = c.getFrom();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(sender);
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                imageUrl = dataSnapshot.child("image").getValue(String.class);

              viewHolder.setUserimage(context,imageUrl);
               // viewHolder.showImage.setImageURI(Uri.parse(imageUrl));


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
