package com.tekaware.christ.myictu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {

    private static final String TAG = "Login";

    private FirebaseAuth mAuth;

    private EditText emailLogin, passLogin;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView signUpLink = findViewById(R.id.createAccountText);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mAuth = FirebaseAuth.getInstance();

        emailLogin = findViewById(R.id.emailLogin);
        passLogin = findViewById(R.id.passLogin);
        Button loginBtn = findViewById(R.id.loginBtn);

        final Context context = this;

        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final String email =  emailLogin.getText().toString();
                final String password =  passLogin.getText().toString();

                if (!validateForm()) {

                    return;

                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");

                                    Toast.makeText(Login.this, " Successful Authentication.",
                                            Toast.LENGTH_LONG).show();

                                    //FirebaseUser user = mAuth.getCurrentUser();

                                    Intent intent = new Intent(Login.this, Home.class);
                                    //intent.putExtra("Uid",user.getUid());
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());

                                    Toast.makeText(Login.this, R.string.errorMsg,
                                            Toast.LENGTH_LONG).show();
                                }

                                // ...
                            }
                        });
            }

        });

        signUpLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SingUp.class);
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



        return valid;

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(Login.this, Home.class);
            intent.putExtra("Uid",user.getUid());
            startActivity(intent);
            finish();
        }

    }


}
