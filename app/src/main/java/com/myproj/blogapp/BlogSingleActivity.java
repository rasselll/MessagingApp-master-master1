package com.myproj.blogapp;

import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BlogSingleActivity extends AppCompatActivity {

    private String mPost_key = null;

    private DatabaseReference mDatabase;

    private ImageView mBlogSingleImage;
    private TextView mBlogSingleTitle;
    private  TextView mBlogSingleDesc;

    private  TextView mBlogSingleContent;

    private Button mBlogSingleRemoveBtn;
    private WebView mBlogSingleContentWebView;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_single);

        mDatabase = FirebaseDatabase.getInstance().getReference("Blog");

        mAuth = FirebaseAuth.getInstance();

        mPost_key = getIntent().getExtras().getString("blog_id");

        mBlogSingleDesc = (TextView) findViewById(R.id.descriptionSingleBlog);
        mBlogSingleTitle = (TextView) findViewById(R.id.titleSingleBlog);
        mBlogSingleContent = (TextView) findViewById(R.id.contentSingleBlog);

        mBlogSingleImage = (ImageView) findViewById(R.id.imageSingleBlog);


        mBlogSingleRemoveBtn = (Button) findViewById(R.id.singleRemoveBtn);

        mBlogSingleContentWebView = (WebView) findViewById(R.id.singleContent_webView);



        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("image").getValue();
                String post_uid = (String) dataSnapshot.child("uid").getValue();
                String post_content = (String) dataSnapshot.child("content").getValue();

                mBlogSingleTitle.setText(post_title);
                mBlogSingleDesc.setText(post_desc);


                Spanned sp = Html.fromHtml(post_content);
                mBlogSingleContent.setText(sp);


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
                                "</style>"+ post_content, "text/html", "utf-8", "");

                mBlogSingleContentWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

                Picasso.with(BlogSingleActivity.this).load(post_image).into(mBlogSingleImage);

                //Toast.makeText(BlogSingleActivity.this, mAuth.getCurrentUser().getUid(), Toast.LENGTH_LONG).show();

                //Toast.makeText(BlogSingleActivity.this, post_uid, Toast.LENGTH_LONG).show();

                if(mAuth.getCurrentUser().getUid().equals(post_uid)){

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
