package com.example.android.connet;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class allUsers extends BaseActivity {
     private Toolbar mtoolbar;
     private RecyclerView mrecycler;
     private DatabaseReference mDatabase;
     private LinearLayoutManager mmanger;
     private  FirebaseRecyclerAdapter<Users,allUsersView> firebaseRecyclerAdapter;
     private static final String TAG = "allUsers";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        //init
        mtoolbar= findViewById(R.id.all_users_bar);
        mrecycler= findViewById(R.id.all_users_list);
        setSupportActionBar(mtoolbar);

        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mmanger= new LinearLayoutManager(getApplicationContext());

        mrecycler.setHasFixedSize(true);
        mrecycler.setLayoutManager(mmanger);

     mDatabase=  FirebaseDatabase.getInstance().getReference().child("users");

    }


    @Override
    protected void onStart() {
        super.onStart();
           if (firebaseRecyclerAdapter!=null) {
               firebaseRecyclerAdapter.startListening();

           }


        FirebaseRecyclerOptions<Users> Options = new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(mDatabase,Users.class)
                .build();


        firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<Users, allUsersView>(Options) {

            @NonNull
            @Override
            public allUsersView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater view= LayoutInflater.from(parent.getContext());
                View v = view.inflate(R.layout.single_user,parent,false);
                return new allUsersView(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull allUsersView holder, int position,@NonNull Users model) {
                    holder.OnBind(model);


            }


        };
        mrecycler.setAdapter(firebaseRecyclerAdapter);
    }


}


