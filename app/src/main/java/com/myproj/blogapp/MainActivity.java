package com.myproj.blogapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.richeditor.RichEditor;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mBlogList;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    //query for single user posts
    private DatabaseReference mDatabaseCurrentUser;
    private Query mQueryCurrentUser;
    private LinearLayoutManager mLayoutManager;
    private DatabaseReference mDatabaseLike;
    private boolean mProcessLike = false;
    private RichEditor mEditor;

    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEditor = (RichEditor) findViewById(R.id.editor);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {

                    Intent loginIntent = new Intent(MainActivity.this, mainact.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);


                }
            }
        };


        if (mAuth.getCurrentUser() == null) {
            checkUserExist();
        }
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");

        //query for post of single user
   /*     String currentUserId = mAuth.getCurrentUser().getUid();
        mDatabaseCurrentUser = FirebaseDatabase.getInstance().getReference().child("Blog");
        mQueryCurrentUser = mDatabaseCurrentUser.orderByChild("uid").equalTo(currentUserId);*/

        mDatabaseUsers.keepSynced(true);
        mDatabaseLike.keepSynced(true);
        mDatabase.keepSynced(true);


        mBlogList = (RecyclerView) findViewById(R.id.blog_list);
        //mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));
        //checkUserExist();

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PostActivity.class));
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();


        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(

                Blog.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                //query for 1 user
                // mQueryCurrentUser
                mDatabase

        ) {

            @Override
            protected void populateViewHolder(final BlogViewHolder viewHolder, final Blog model, int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.setUsername(model.getUsername());
                viewHolder.setContent(model.getContent());
                viewHolder.setLikeBtn(post_key);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(MainActivity.this, post_key, Toast.LENGTH_LONG).show();

                        Intent singleBlogintent = new Intent(MainActivity.this, BlogSingleActivity.class);
                        singleBlogintent.putExtra("blog_id", post_key);
                        startActivity(singleBlogintent);
                    }
                });


                mDatabaseUsers.child(model.getUid()).child("image").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                            String imageUrl = dataSnapshot.getValue().toString();
                            viewHolder.setUserimage(getApplicationContext(), imageUrl);
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                viewHolder.mLikeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mProcessLike = true;


                        mDatabaseLike.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                                if (mProcessLike) {
                                    if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {

                                        mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();

                                        mProcessLike = false;
                                    } else {
                                        mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("Random Value");

                                        mProcessLike = false;
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                });

            }


        };


        //Decending order firebase
        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        // And now set it to the RecyclerView
        mBlogList.setLayoutManager(mLayoutManager);
        mBlogList.setAdapter(firebaseRecyclerAdapter);

    }

    private void checkUserExist() {


        if (mAuth.getCurrentUser() != null) {
            final String user_id = mAuth.getCurrentUser().getUid();


            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild(user_id)) {

                        Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);

                        /*Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);*/
                    }/*else {
                        Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);

                    }*/
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });


        }
    }


    public static class BlogViewHolder extends RecyclerView.ViewHolder {

        View mView;

        ImageButton mLikeBtn;

        DatabaseReference mDatabaseLike;
        FirebaseAuth mAuth;

        public BlogViewHolder(View itemView) {

            super(itemView);
            mView = itemView;


            //like
            mLikeBtn = (ImageButton) mView.findViewById(R.id.like_btn);
            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
            mAuth = FirebaseAuth.getInstance();

            mDatabaseLike.keepSynced(true);


        }

        public void setLikeBtn(final String post_key) {

    /*        mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                    if(dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())){
                        mLikeBtn.setImageResource(R.mipmap.thumbgreen);
                    }else{

                        mLikeBtn.setImageResource(R.mipmap.thumbblack);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });*/
        }

        public void setTitle(String title) {

            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);


        }

        public void setContent(String content) {

            TextView post_content = (TextView) mView.findViewById(R.id.post_content);
          //  WebView webview = (WebView)  mView.findViewById(R.id.post_contentWeb);
            WebView singleImg = (WebView) mView.findViewById(R.id.post_singleImage);


            Spanned sp = Html.fromHtml(content);
            post_content.setText(sp);

            String v ="";
            Pattern pattern = Pattern.compile("(<img .*?>)");
            Matcher matcher = pattern.matcher(content);
            if (matcher.find()) {
                v = matcher.group(1);
            }


          //  webview.loadDataWithBaseURL(null, "<style>img{display: inline;height: auto;max-width: 100%;}</style>"+ content, "text/html", "utf-8", "");
           // webview.getSettings().setLoadsImagesAutomatically(false);
            singleImg.loadDataWithBaseURL(null,
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
                      "</style>"+ v, "text/html", "utf-8", "");

            singleImg.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        }


        public void setDesc(String desc) {
            TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }

        public void setImage(Context ctx, String image) {
       /*     ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);

            Picasso.with(ctx).load(image).into(post_image);*/



        }

        public void setUsername(String username) {
            TextView post_username = (TextView) mView.findViewById(R.id.post_username);
            post_username.setText(username);
        }

        public void setUserimage(Context applicationContext, String imageUrl) {
            ImageView imageViewPostUserImage = (ImageView) mView.findViewById(R.id.imageview_post_userimage);
            Picasso
                    .with(applicationContext)
                    .load(imageUrl)
                    .error(R.drawable.error)
                    .resize(120, 120)
                    .transform(new CropCircleTransformation())
                    .into(imageViewPostUserImage);

        }

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_manu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(MainActivity.this, MainMessageActivity.class));
        }
        if (item.getItemId() == R.id.action_logout) {
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        mAuth.signOut();
    }


}


