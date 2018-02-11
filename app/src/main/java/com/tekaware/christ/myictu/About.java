package com.tekaware.christ.myictu;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class About extends Fragment {

   /* File localFile = null;
    private StorageReference mStorageRef;
    String url;

    private ImageView profile1; */

    public About() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);

        //final View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        /*profile1 = rootView.findViewById(R.id.pcimg);

        url = "https://gs://firecloudapp2-1d793.appspot.com/pp.jpg";


        mStorageRef = FirebaseStorage.getInstance().getReference("firecloudapp2-1d793");

            StorageReference pathRef = mStorageRef.child("gs://firecloudapp2-1d793.appspot.com/pp.jpg");

        pathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                url = mStorageRef.getDownloadUrl().toString();
            }
        });

        InputStream is = null;
        try {
            is = (InputStream) new URL(url).getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Drawable d = Drawable.createFromStream(is, "chrispp");

        profile1.setImageDrawable(d); */



        //return rootView;
    }

}
