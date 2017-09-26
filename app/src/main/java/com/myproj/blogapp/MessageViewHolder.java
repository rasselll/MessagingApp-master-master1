package com.myproj.blogapp;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


/**
 * Created by Jawwad on 19/08/2017.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {
    public TextView messageText;
    public TextView displayName;
    public TextView time;
    public ImageView showImage;

    public MessageViewHolder(View view) {
        super(view);
        messageText = (TextView) view.findViewById(R.id.messagetext);
        displayName = (TextView) view.findViewById(R.id.displayname);
        time = (TextView) view.findViewById(R.id.timestamp);
        showImage = (ImageView) view.findViewById(R.id.imageview_post_userimage3);
    }


    public void setUserimage(Context context, String imageUrl) {
Picasso
        .with(context)
                .load(imageUrl)
                .error(R.drawable.error)
                .resize(140, 140)
                .transform(new CropCircleTransformation())
                .into(showImage);

    }


}

