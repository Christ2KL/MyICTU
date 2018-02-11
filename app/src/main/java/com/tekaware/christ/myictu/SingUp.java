package com.tekaware.christ.myictu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * This file was Created by Chris on 07/01/2018.
 */

public class SingUp extends AppCompatActivity {

    private static final String TAG = "SignUp";

    private FirebaseAuth mAuth;

    private EditText emailLogin, passLogin, fullName;

    private String status;

    private RadioButton statusRadioGroup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mAuth = FirebaseAuth.getInstance();

        emailLogin = findViewById(R.id.emailLogin);
        passLogin = findViewById(R.id.passLogin);
        fullName = findViewById(R.id.fullName);
        Button signUpBtn = findViewById(R.id.signUpBtn);
        TextView signInLink = findViewById(R.id.signInText);
        statusRadioGroup = findViewById(R.id.statusAdmin);

        signUpBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final String email =  emailLogin.getText().toString();
                final String password =  passLogin.getText().toString();
                final String usernameContent =  fullName.getText().toString();

                if (!validateForm()) {

                    return;

                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SingUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");

                                    Toast.makeText(SingUp.this, R.string.sign_up_success,
                                            Toast.LENGTH_LONG).show();

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

                                    Map<String, String> newPost = new HashMap<>();
                                    newPost.put("Username", usernameContent);
                                    newPost.put("Email", email);
                                    newPost.put("Status", status);
                                    newPost.put("Verified", "no");

                                    current_user_db.setValue(newPost);
                                    user.sendEmailVerification();

                                    //FirebaseUser user = mAuth.getCurrentUser();
                                    String UserId;

                                   // UserId = user.getUid();

                                    Intent intent = new Intent(SingUp.this, Home.class);
                                    //intent.putExtra("Uid",UserId);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());

                                    Toast.makeText(SingUp.this, "Authentication failed.",
                                            Toast.LENGTH_LONG).show();
                                }

                                // ...
                            }
                        });

            }
        });


        signInLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingUp.this, com.tekaware.christ.myictu.Login.class);
                startActivity(intent);
                finish();
            }
        });



    }

    private boolean validateForm() {

        boolean valid = true;



        String email = emailLogin.getText().toString();

        if (TextUtils.isEmpty(email) || !email.contains("@ictuniversity.org")) {

            emailLogin.setError("Required.");

            valid = false;

        } else {

            emailLogin.setError(null);

        }



        String password = passLogin.getText().toString();

        if (TextUtils.isEmpty(password)) {

            passLogin.setError("Required.");

            valid = false;

        } else {

            passLogin.setError(null);

        }

        String usernameContent = fullName.getText().toString();

        if (TextUtils.isEmpty(usernameContent)) {

            fullName.setError("Required.");

            valid = false;

        } else {

            fullName.setError(null);

        }

        if (TextUtils.isEmpty(status)) {

            statusRadioGroup.setError("Required.");

            valid = false;

        } else {

            statusRadioGroup.setError(null);

        }


        return valid;

    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {

            case R.id.statusStudent:
                if (checked)
                    status = "Student";
                break;
            case R.id.statusLecturer:
                if (checked)
                    status = "Lecturer";
                break;
            case R.id.statusAdmin:
                if (checked)
                    status = "Admin";
                break;
        }

    }


}
