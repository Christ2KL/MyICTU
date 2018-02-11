package com.tekaware.christ.myictu;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class EmailVerificationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    FirebaseUser user;

    private String UserId;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance();

        assert user != null;
        UserId = user.getUid();

        Button verifyMailBtn = findViewById(R.id.verifyMailBtn);
        Button refreshBtn = findViewById(R.id.refreshBtn);

        verifyMailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null) {
                    user.sendEmailVerification();

                    Toast.makeText(getApplicationContext(), "Verification Link sent, check your mail box...",
                            Toast.LENGTH_LONG).show();
                }
            }
        });


        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (user.isEmailVerified()){
                    Intent intent = new Intent(EmailVerificationActivity.this, Home.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Sorry! Please verify your mail...",
                            Toast.LENGTH_LONG).show();
                }

            }
        });


    }
}
