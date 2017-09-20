package com.myproj.blogapp;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.ParseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class BlogSingleActivity extends AppCompatActivity {

    private String mPost_key = null;

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsrrpost;
    private DatabaseReference mDatabaseUser;
    private DatabaseReference mUser;

    private ImageView mBlogSingleImage;
    private TextView mBlogSingleTitle;
    private TextView mBlogSingleDesc;
    private TextView mBlogSingleTime;

    private TextView mBlogSingleContent;

    private Button mBlogSingleRemoveBtn;
    private WebView mBlogSingleContentWebView;
    private ImageView mUserImage;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_single);
        //getActionBar().setTitle("Hello world App");


        mDatabase = FirebaseDatabase.getInstance().getReference("Blog");
        mUser = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();

        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");

        mPost_key = getIntent().getExtras().getString("blog_id");

        mDatabaseUsrrpost = FirebaseDatabase.getInstance().getReference("Blog").child(mPost_key);
        mUser = FirebaseDatabase.getInstance().getReference().child("Users");


       mBlogSingleDesc = (TextView) findViewById(R.id.descriptionSingleBlog);
        mBlogSingleTitle = (TextView) findViewById(R.id.titleSingleBlog);
        mBlogSingleTime = (TextView) findViewById(R.id.timeSingleBlog);


        //mBlogSingleContent = (TextView) findViewById(R.id.contentSingleBlog);

       // mBlogSingleImage = (ImageView) findViewById(R.id.imageSingleBlog);
        mUserImage = (ImageView) findViewById(R.id.imageview_post_userimage);

     //   mUserImage = (ImageView) findViewById(R.id.post_image);


        mBlogSingleRemoveBtn = (Button) findViewById(R.id.singleRemoveBtn);

        mBlogSingleContentWebView = (WebView) findViewById(R.id.singleContent_webView);


        mDatabaseUsrrpost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final String userimage = (String) dataSnapshot.child("uid").getValue();
                // mUserImage.setImageURI(Uri.parse(userimage));


                mUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       String img = (String) dataSnapshot.child(userimage).child("image").getValue();
                        String username = (String) dataSnapshot.child(userimage).child("name").getValue();
                        Picasso.with(BlogSingleActivity.this).load(img).resize(200,200).transform(new CropCircleTransformation()).into(mUserImage);
                        mBlogSingleTitle.setText(username);





                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("image").getValue();
                String post_uid = (String) dataSnapshot.child("uid").getValue();
                String post_content = (String) dataSnapshot.child("content").getValue();
                String post_time = (String) dataSnapshot.child("post_time").getValue();

                getSupportActionBar().setTitle(post_title);
                mBlogSingleDesc.setText(post_title);
                mBlogSingleTime.setText(post_time);

               /* String target = "Thu Sep 28 20:29:30 JST 2000";
                DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
                try {
                    Date result =  df.parse(target);
                    mBlogSingleTime.setText(result.toString());

                } catch (ParseException e) {
                    e.printStackTrace();
                }*/

            /*
                Spanned sp = Html.fromHtml(post_content);
                mBlogSingleContent.setText(sp);*/


                //   mBlogSingleContentWebView.loadDataWithBaseURL(null, "<style>img{display: inline;height: auto;max-width: 100%;}[0]</style>"+ post_content, "text/html", "utf-8", "");

                mBlogSingleContentWebView.loadDataWithBaseURL(null,
                        "<style>html, body {\n" +
                                "width:100%;\n" +
                                "height: 100%;\n" +
                                "margin: 0px;\n" +
                                "padding: 0px;\n" +
                                "}" +
                                "img{" +
                                "max-width: 100%; " +
                                "width:auto;" +
                                " height: auto;" +
                                "}" +
                                "</style>" + post_content, "text/html", "utf-8", "");

                mBlogSingleContentWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

                //  Picasso.with(BlogSingleActivity.this).load(post_image).into(mBlogSingleImage);

                //Toast.makeText(BlogSingleActivity.this, mAuth.getCurrentUser().getUid(), Toast.LENGTH_LONG).show();

                //Toast.makeText(BlogSingleActivity.this, post_uid, Toast.LENGTH_LONG).show();


                if (mAuth.getCurrentUser().getUid().equals(post_uid)) {

                    mBlogSingleRemoveBtn.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mBlogSingleRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase.child(mPost_key).removeValue();

                Intent mainIntent = new Intent(BlogSingleActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });

    }
}
