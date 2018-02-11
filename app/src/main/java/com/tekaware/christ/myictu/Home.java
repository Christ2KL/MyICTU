package com.tekaware.christ.myictu;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView = null;
    Toolbar toolbar = null;

    private TextView uName;


    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;

    private DatabaseReference statusRef, nameRef;
    FirebaseUser user;

    private String UserId, verifyState, username;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myhome);

        uName = findViewById(R.id.username);

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();

        assert user != null;
        UserId = user.getUid();

        getStatus();

        if (!user.isEmailVerified()){
            Intent intent = new Intent(Home.this, EmailVerificationActivity.class);
            startActivity(intent);
            finish();
        }

        //Set the fragment initially
        if (savedInstanceState == null) {
            getStatus();
            NoticeBoard fragment = new NoticeBoard();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

        }


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });**/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // final Context context = android.support.v4.app.FragmentActivity.this.getApplicationContext();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_add) {



            PostActivity fragment = new PostActivity();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Home) {
            //Set the fragment initially
            NoticeBoard fragment = new NoticeBoard();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

            setMyToolbarTitleStatic (R.string.noticeBoardTitle);


        }  else if (id == R.id.nav_support) {

        } else if (id == R.id.nav_about) {
        About fragment = new About();
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        setMyToolbarTitleStatic (R.string.aboutLabel);

        } else if (id == R.id.nav_logout) {
        LoginOut fragment = new LoginOut();
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        setMyToolbarTitleStatic (R.string.logoutLabel);

    }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //functions

    public void getStatus() {

        statusRef = mDatabase.getReference().child("Users").child(UserId).child("Status");

        ValueEventListener statusListener = new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String state = dataSnapshot.getValue(String.class);

                //Toast.makeText(getApplicationContext(), state,
                      //  Toast.LENGTH_SHORT).show();

                NoticeBoard.setStatus (state);
                // ...
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {


                Toast.makeText(getApplicationContext(), getString(R.string.status_error),
                        Toast.LENGTH_SHORT).show();
            }
        };

        statusRef.addListenerForSingleValueEvent(statusListener);
    }

    private void setMyToolbarTitleMarquee(int title) {

        if (toolbar != null) {
            setMarqueeOnMyToolbarTitle(getMyToolbarTitle ());
        } else {
            setMyToolbarTitleStatic (title);
            setMyToolbarTitleMarquee(title);

        }

    }

        private TextView getMyToolbarTitle (){
            TextView toolbarTitle = null;

            for (int i = 0; i < toolbar.getChildCount(); ++i) {
                View child = toolbar.getChildAt(i);

                // assuming that the title is the first instance of TextView
                // you can also check if the title string matches
                if (child instanceof TextView) {
                    toolbarTitle = (TextView) child;
                    break;
                }
            }

            return toolbarTitle;

        }

    private void setMarqueeOnMyToolbarTitle(TextView toolbarTitle) {

        toolbarTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        toolbarTitle.setMarqueeRepeatLimit(3);
        toolbarTitle.setFocusable(true);
        toolbarTitle.setFocusableInTouchMode(true);
        toolbarTitle.requestFocus();
        toolbarTitle.setSingleLine(true);

    }

    private void setMyToolbarTitleStatic(int title){
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

}
