package com.myproj.blogapp;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import jp.wasabeef.richeditor.RichEditor;

public class PostActivity extends AppCompatActivity {

    private ImageButton mSelectImage;
    private EditText mPostTitle;
    private RichEditor mPostDesc;

    private TextView mPreview;


    private Button mSubmitBtn;

    private Uri mImageURi = null;

    private static final int GALLERY_REQUEST = 1;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;


    private ProgressDialog mProgress;

    private FirebaseAuth mAuth;

    private FirebaseUser mCurrentUser;

    private DatabaseReference mDatabaseUser;
    private RichEditor mEditor;
    private RichEditor mEditordesc;
    private ScrollView ScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
/*        ScrollView ScrollView = (ScrollView) findViewById(R.id.scroll);
        ScrollView.fullScroll(ScrollView.FOCUS_DOWN);*/


        final ScrollView scrollview = ((ScrollView) findViewById(R.id.scroll));
        scrollview.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        },10000);




        View lastChild = scrollview.getChildAt(scrollview.getChildCount() - 1);
        int bottom = lastChild.getBottom() + scrollview.getPaddingBottom();
        int sy = scrollview.getScrollY();
        int sh = scrollview.getHeight();
        int delta = bottom - (sy + sh);

        scrollview.smoothScrollBy(0, delta);

        scrollview.fullScroll(View.FOCUS_DOWN);
        scrollview.post(new Runnable() {
            @Override

            public void run() {

                scrollview.scrollTo(0, scrollview.getBottom());
                scrollview.fullScroll(View.FOCUS_DOWN);

            }
        });

        mEditor = (RichEditor) findViewById(R.id.editor);
        mEditordesc = (RichEditor) findViewById(R.id.descField);

        mEditordesc.setEditorHeight(18);
        mEditordesc.setEditorFontSize(18);
        mEditordesc.setPadding(5,5,20,20);

        mEditordesc.setPlaceholder("Say something nice...      ");


        mEditor.setEditorHeight(18);
        mEditor.setEditorFontSize(18);
        mEditor.setPadding(50,50,50,50);
        mEditor.setPlaceholder("Say something nice...      ");
        mEditor.loadCSS("file:///android_asset/img.css");
        mEditor.focusEditor();


        mAuth = FirebaseAuth.getInstance();

        mCurrentUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        //add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPostTitle = (EditText) findViewById(R.id.titleField);
       // mPostDesc = (EditText) findViewById(R.id.descField);
        mPostDesc = mEditordesc;
     //   mSubmitBtn = (Button) findViewById(R.id.btn);


        mProgress = new ProgressDialog(this);


        mSelectImage = (ImageButton) findViewById(R.id.imageSelect);

        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Choose image"), GALLERY_REQUEST);
            }
        });

/*
        mPreview = (TextView) findViewById(R.id.post_content);
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override public void onTextChange(String text) {
                mPreview.setText(text);
            }
        });*/




        findViewById(R.id.action_bold1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBold();
            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Choose image"), GALLERY_REQUEST);


            }
        });



    }


    private void startPosting() {
        mProgress.setMessage("Posting...");

        mProgress.getProgress();
        final String title_val = mPostTitle.getText().toString().trim();
        final String desc_val = mPostDesc.getHtml().toString().trim();
        final String content_val = mEditor.getHtml();


        if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && mImageURi != null) {
            mProgress.show();
            StorageReference filepath = mStorage.child("Blog_Image").child(mImageURi.getLastPathSegment());

            filepath.putFile(mImageURi).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {


                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    final Uri downlaodUrl = taskSnapshot.getDownloadUrl();

                    final DatabaseReference newPost = mDatabase.push();


                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            newPost.child("content").setValue(content_val);
                            newPost.child("title").setValue(title_val);
                            newPost.child("desc").setValue(desc_val);
                            newPost.child("image").setValue(downlaodUrl.toString());
                            newPost.child("uid").setValue(mCurrentUser.getUid());
                            newPost.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        finish();
                                        startActivity(new Intent(PostActivity.this, MainActivity.class));
                                    } else {
                                        //TODO error message
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {


                        }
                    });


                    mProgress.dismiss();


                }
            });
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            mImageURi = data.getData();
            mSelectImage.setImageURI(mImageURi);

            String myURI = mImageURi.toString();
            mEditor.insertImage(myURI, "image");




        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void publish(MenuItem item) {

        startPosting();
    }

}
