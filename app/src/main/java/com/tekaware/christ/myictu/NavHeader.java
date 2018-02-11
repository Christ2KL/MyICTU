package com.tekaware.christ.myictu;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
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

/**
 * This file was Created by Chris on 11/02/2018.
 */

public class NavHeader extends AppCompatActivity {

    private TextView uName;


    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;

    private DatabaseReference statusRef, nameRef;
    FirebaseUser user;

    private String UserId, username;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header_home);

        uName = findViewById(R.id.username);

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();

        assert user != null;
        UserId = user.getUid();

        nameRef = mDatabase.getReference().child("Users").child(UserId).child("Username");

        ValueEventListener nameListener = new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                username = dataSnapshot.getValue(String.class);

                uName.setText(username);

                Toast.makeText(getApplicationContext(), username,
                        Toast.LENGTH_SHORT).show();
                // ...
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                Toast.makeText(getApplicationContext(), getString(R.string.status_error),
                        Toast.LENGTH_SHORT).show();
            }
        };

        nameRef.addListenerForSingleValueEvent(nameListener);
    }


}
