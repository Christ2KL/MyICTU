package com.tekaware.christ.myictu;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostActivity extends Fragment {

    private static final int GALLERY_REQUEST = 1;

    private EditText mNoticeTitle, mNoticeContent;
    private ImageButton mSelectImage;
    private Uri imageUri = null;

    private ProgressDialog mProgress;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;


    public PostActivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_post, container, false);

        mProgress = new ProgressDialog(rootView.getContext());

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Notice");

        mSelectImage = rootView.findViewById(R.id.imageSelect);
        mNoticeTitle = rootView.findViewById(R.id.titleField);
        mNoticeContent = rootView.findViewById(R.id.contentField);

        Button mSubmitBn = rootView.findViewById(R.id.submitBtn);


        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        mSubmitBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });



        return rootView;
    }

    private void startPosting() {
        final DatabaseReference newNotice = mDatabase.push();
        mProgress.setMessage("Posting Notice...");

        final String titleValue = mNoticeTitle.getText().toString().trim();
        final String contentValue = mNoticeContent.getText().toString().trim();

        if (validateForm(titleValue, contentValue)){

            mProgress.show();

            newNotice.child("title").setValue(titleValue);
            newNotice.child("content").setValue(contentValue);
            newNotice.child("image").setValue("null");

            Toast.makeText(getActivity(), getString(R.string.postSuccess),
                    Toast.LENGTH_SHORT).show();

            NoticeBoard fragment = new NoticeBoard();
            FragmentTransaction fragmentTransaction =
                    getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }

        if (imageUri != null){

            StorageReference filePath = mStorage.child("Notice_Images").child(imageUri.getLastPathSegment());

            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    if (downloadUrl != null) {
                        newNotice.child("image").setValue(downloadUrl.toString());
                    }
                }
            });

        }

        mProgress.dismiss();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            imageUri = data.getData();
            mSelectImage.setImageURI(imageUri);
        }
    }


    private boolean validateForm(String title, String content) {

        boolean valid = true;

        if (TextUtils.isEmpty(title)) {
            mNoticeTitle.setError("Required.");
            valid = false;
        } else {
            mNoticeTitle.setError(null);
        }

        if (TextUtils.isEmpty(content)) {
            mNoticeContent.setError("Required.");
            valid = false;
        } else {
            mNoticeContent.setError(null);
        }

        return valid;
    }
}
