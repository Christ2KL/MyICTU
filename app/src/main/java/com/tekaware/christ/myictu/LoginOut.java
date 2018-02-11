package com.tekaware.christ.myictu;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginOut extends Fragment {


    public LoginOut() {
        // Required empty public constructor
    }

    private Button sendReviewBtn;
    private EditText userReview;
    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_login_out, container, false);
        final View rootView = inflater.inflate(R.layout.fragment_login_out, container, false);

        mAuth = FirebaseAuth.getInstance();

        Button logOutBtn = rootView.findViewById(R.id.logOutBtn);
        sendReviewBtn = rootView.findViewById(R.id.sendReviewBtn);
        userReview = rootView.findViewById(R.id.userReview);

        sendReviewBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (!validateForm()) {

                    return;

                }

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Reviews").child(user.getUid());

                String review = userReview.getText().toString();

                Map<String, String> newPost = new HashMap<>();
                newPost.put("Review", review);

                current_user_db.setValue(newPost);

                sendReviewBtn.setText(R.string.thanks_msg);

            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mAuth.signOut();

                Intent intent = new Intent(rootView.getContext(), Login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return rootView;
    }

    private boolean validateForm() {

        boolean valid = true;



        String review = userReview.getText().toString();

        if (TextUtils.isEmpty(review)) {

            userReview.setError("Required.");

            valid = false;

        } else {

            userReview.setError(null);

        }


        return valid;

    }

}
