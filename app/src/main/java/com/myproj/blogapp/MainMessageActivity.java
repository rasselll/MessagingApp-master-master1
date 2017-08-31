package com.myproj.blogapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainMessageActivity extends AppCompatActivity {

    private TextView defaultText;
    private Firebase firebaseRootRef;
    private FirebaseAuth firebaseAuth;
    private Toolbar mainBar;
    private ViewPager pageView;
    private PageAdapter pagerAdapter;
    private TabLayout maintabLayout;
    private DatabaseReference userDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_main);
       // setMainBar((Toolbar) findViewById(R.id.main_tool_bar));
        setFirebaseAuth(FirebaseAuth.getInstance());
        if (getFirebaseAuth().getCurrentUser() != null) {
            setUserDatabaseRef(FirebaseDatabase.getInstance().getReference().child("Users").child(getFirebaseAuth().getCurrentUser().getUid()));
        }
//        setSupportActionBar(getMainBar());
        getSupportActionBar().setTitle("Contact");

        setTabs();
    }


    public void setTabs() {
        setPageView((ViewPager) findViewById(R.id.tabPage));
        setPagerAdapter(new PageAdapter(getSupportFragmentManager()));
        getPageView().setAdapter(getPagerAdapter());
        //setMaintabLayout((TabLayout) findViewById(R.id.tabs));
        //getMaintabLayout().setupWithViewPager(getPageView());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getFirebaseAuth().getCurrentUser() == null) {
            redirectToMainActivity();
        } else {
            getUserDatabaseRef().child("onlineStatus").setValue(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (getFirebaseAuth().getCurrentUser() != null) {
            getUserDatabaseRef().child("onlineStatus").setValue(false);
        }
    }

    private void redirectToMainActivity() {
        Intent startMessagingIntent = new Intent(MainMessageActivity.this, StartMessagingActivity.class);
        startActivity(startMessagingIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.global_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.logout) {
            getUserDatabaseRef().child("onlineStatus").setValue(false);
            FirebaseAuth.getInstance().signOut();
            redirectToMainActivity();
        }
        return true;
    }

    public TextView getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(TextView defaultText) {
        this.defaultText = defaultText;
    }

    public Firebase getFirebaseRootRef() {
        return firebaseRootRef;
    }

    public void setFirebaseRootRef(Firebase firebaseRootRef) {
        this.firebaseRootRef = firebaseRootRef;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public void setFirebaseAuth(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public Toolbar getMainBar() {
        return mainBar;
    }

    public void setMainBar(Toolbar mainBar) {
        this.mainBar = mainBar;
    }

    public ViewPager getPageView() {
        return pageView;
    }

    public void setPageView(ViewPager pageView) {
        this.pageView = pageView;
    }

    public PageAdapter getPagerAdapter() {
        return pagerAdapter;
    }

    public void setPagerAdapter(PageAdapter pagerAdapter) {
        this.pagerAdapter = pagerAdapter;
    }

    public TabLayout getMaintabLayout() {
        return maintabLayout;
    }

    public void setMaintabLayout(TabLayout maintabLayout) {
        this.maintabLayout = maintabLayout;
    }

    public DatabaseReference getUserDatabaseRef() {
        return userDatabaseRef;
    }

    public void setUserDatabaseRef(DatabaseReference userDatabaseRef) {
        this.userDatabaseRef = userDatabaseRef;
    }
}
