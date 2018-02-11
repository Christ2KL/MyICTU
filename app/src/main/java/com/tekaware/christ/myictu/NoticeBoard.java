package com.tekaware.christ.myictu;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import static com.tekaware.christ.myictu.R.string.status_error;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoticeBoard extends Fragment {

    private RecyclerView NoticeList;

    private FirebaseRecyclerAdapter<Notice, NoticeViewHolder> mNoticesAdapter;

    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;

    private DatabaseReference noticeRef, statusRef;

    FirebaseUser user;

    private String UserId;

    static String status = "Student";

    private Boolean PostAuth;


    public NoticeBoard() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();

        assert user != null;
        UserId = user.getUid();

        //getStatus();

        if (!Objects.equals(status, "Student")){setHasOptionsMenu(true);}

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_accueil, container, false);

        noticeRef = mDatabase.getReference().child("Notice");

        NoticeList = rootView.findViewById(R.id.noticeList);
        NoticeList.setHasFixedSize(true);
        NoticeList.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }





    static void setStatus(String value){
        status = value;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //getStatus();

        //if (Objects.equals(status, "Student")){
            inflater.inflate(R.menu.accueil, menu);
            super.onCreateOptionsMenu(menu, inflater);

       // MenuItem item = menu.findItem(R.id.action_add);

       // if (Objects.equals(status, "Student")){
        //    item.setVisible(false);
        //}
        //}
    }

    @Override
    public void onStart() {
        super.onStart();

        Query noticeQuery = noticeRef.orderByKey();

        FirebaseRecyclerOptions<Notice> noticeOptions = new FirebaseRecyclerOptions.Builder<Notice>().setQuery(noticeQuery, Notice.class).build();

        //FirebaseRecyclerAdapter <Notice, NoticeViewHolder>

        mNoticesAdapter = new FirebaseRecyclerAdapter<Notice, NoticeViewHolder>(noticeOptions){
            @Override
            protected void onBindViewHolder(@NonNull NoticeViewHolder holder, int position, @NonNull Notice model) {

                holder.setTitle(model.getTitle());
                holder.setContent(model.getContent());
                holder.setImage(getActivity().getApplicationContext() ,model.getImage());
            }

            @Override
            public NoticeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.notice_row, parent, false);

                return new NoticeViewHolder(view);

            }

           // @Override
           // protected void populateViewHolder(NoticeViewHolder viewHolder, Notice model, int position){

           // }

        };

        NoticeList.setAdapter(mNoticesAdapter);

        mNoticesAdapter.startListening();
    }

    public static class NoticeViewHolder extends RecyclerView.ViewHolder{

        View mView;

        NoticeViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        void setTitle(String title){

            TextView noticeTitle = mView.findViewById(R.id.noticeTitle);
            noticeTitle.setText(title);


        }

        public void setContent(String content){

            TextView noticeTitle = mView.findViewById(R.id.noticeContent);
            noticeTitle.setText(content);

        }

        public void setImage (Context ctx, String image){
            ImageView notice_image = mView.findViewById(R.id.noticeImage);
            Picasso.with(ctx).load(image).into(notice_image);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mNoticesAdapter.stopListening();
    }
}
