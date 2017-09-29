package com.myproj.blogapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


/**
 * Created by Jawwad on 18/08/2017.
 */

public class UserViewHolder extends RecyclerView.ViewHolder {
    public View userView;

    public UserViewHolder(View itemView) {
        super(itemView);
        userView = itemView;
    }

    public void setDisplayName(String name) {
        TextView userNameView = (TextView) userView.findViewById(R.id.username);
        userNameView.setText(name);
    }

    public void setMessage(String message) {
        TextView userStatusView = (TextView) userView.findViewById(R.id.userstatus4);
        userStatusView.setText(message);
    }



    public void setUserimage(Context context, String imageUrl) {
        ImageView imageViewPostUserImage1 = (ImageView) userView.findViewById(R.id.imageview_post_userimage1);
        Picasso
                .with(context)
                .load(imageUrl)
                .error(R.drawable.error)
                .resize(500, 500)
                .transform(new CropCircleTransformation())
                .into(imageViewPostUserImage1);
    }



    public void setUserStatus(String status) {
 /*       TextView userStatusView = (TextView) userView.findViewById(R.id.userstatus4);
        userStatusView.setText(message);*/
    }


    public void setUid(String uid) {
    /*    TextView userStatusView = (TextView) userView.findViewById(R.id.userstatus);
        userStatusView.setText(uid);*/
    }

    public void setUserOnlineStatus(String status) {
        ImageView userOnlineView = (ImageView) userView.findViewById(R.id.imageView);
        if (status.equals("true")) {
            userOnlineView.setVisibility(View.VISIBLE);
        } else {
            userOnlineView.setVisibility(View.INVISIBLE);
        }
    }
}
