package com.example.android.connet;



import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class RegistrationSession extends BaseActivity implements View.OnClickListener {
    private Button mSignIn ,mregister;
    private EditText memail,mpassword;
    private Spinner mgender, minstitution;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        //reference
        mSignIn= findViewById(R.id.gotoLogin);
        mregister = findViewById(R.id.register);
        memail = findViewById(R.id.email);
        mpassword= findViewById(R.id.password);
        mgender= findViewById(R.id.gender);
        minstitution= findViewById(R.id.institution);
        //setting on click listenner to button
        mSignIn.setOnClickListener(this);
        mregister.setOnClickListener(this);
         //init firebaseAuth
        mAuth= FirebaseAuth.getInstance();
        //init firebaseRef
        mRef= FirebaseDatabase.getInstance().getReference();
        //setting up spinner
    ArrayAdapter<CharSequence> gender=ArrayAdapter.createFromResource(this,R.array.gender,R.layout.support_simple_spinner_dropdown_item);
     ArrayAdapter<CharSequence>institution=ArrayAdapter.createFromResource(this,R.array.institutions,R.layout.support_simple_spinner_dropdown_item);
     //setting up layout for spinner
        gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        institution.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //setting spinner to view
        mgender.setAdapter(gender);
        minstitution.setAdapter(institution);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    private void registerAccount() {
        if (!Validation()){
            return;
        }
        showpd();
        String email= memail.getText().toString().trim();
        String password= mpassword.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    hidePd();
                startActivity(new Intent(RegistrationSession.this,MainScreen.class));
                    GetUsernameFromEmail( task.getResult().getUser());

                    Toast.makeText(RegistrationSession.this,"you have successfully registered",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistrationSession.this,"unable to sign you up right now ,please try again later",Toast.LENGTH_LONG).show();
            }
        });


    }

    private void GetUsernameFromEmail(FirebaseUser user){
        String username = GetUsername(user.getEmail());
        String  uid = user.getUid();
        String gender = mgender.getSelectedItem().toString();
        String institution= minstitution.getSelectedItem().toString();
        HashMap<String, String> map = new HashMap<>();
        map.put("Email",user.getEmail());
        map.put("username",username);
        map.put("image", "default");
        map.put("thumbImage","default");
        map.put("handle","null");
        map.put("gender", gender);
        map.put("status","hey there, I am new on campus connect");
        map.put("institution",institution);
        mRef.child("users").child(uid).setValue(map);



    }
    private String GetUsername(String email){
        if (email.contains("@")){
          return  email.split("@")[0];
        }else {
            return email;
        }
    }

    @Override
    public void onClick(View view) {
     int getId = view.getId();

     if (getId==R.id.register){
         registerAccount();
     }
     if (getId==R.id.gotoLogin){
         startActivity(new Intent(RegistrationSession.this,LoginSession.class));
     }
    }



    private boolean Validation(){
        boolean result = true;
        if (TextUtils.isEmpty(memail.getText().toString())&& TextUtils.isEmpty(mpassword.getText().toString())){
            memail.setError("REQUIRED");
            memail.requestFocus();

            mpassword.setError("REQUIRED");
            mpassword.requestFocus();
            return false;
        }
        if (mpassword.length()<6){
            mpassword.setError("MAX OF 6 DIGIT");
            mpassword.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(memail.getText().toString()).matches()){
            memail.setError("A VALID EMAIL");
            memail.requestFocus();
            return false;
        }
        return result;
    }
}
