package com.example.android.connet;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginSession extends BaseActivity implements View.OnClickListener{
    private Button mGotoSignUp,mLogin;
    private EditText musername, mpassword;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //init views
        mGotoSignUp= findViewById(R.id.gotoSignUp);
        mLogin= findViewById(R.id.signIn);
        musername=findViewById(R.id.user_email);
        mpassword= findViewById(R.id.user_pass);
        //set onclick listener tp button
        mGotoSignUp.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        //init firebase
       mAuth= FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            Intent intent = new Intent(LoginSession.this,MainScreen.class);
            startActivity(intent);
            finish();
        }
    }



    private void SignInUSer() {
       if (!validate()){
           return;
       }
       showpdSign();
    String username  = musername.getText().toString().trim();
    String password=   mpassword.getText().toString().trim();
    mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            hidePd();
           if (task.isSuccessful()){
               Intent mainIntent = new Intent(LoginSession.this,MainScreen.class);
               mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
               startActivity(mainIntent);
               finish();
           }
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            hidePd();
            Toast.makeText(LoginSession.this,"unable to sign you in..check your network connection ",Toast.LENGTH_LONG).show();
        }
    });


    }
    private boolean validate(){
        boolean result = true;
        if (TextUtils.isEmpty(musername.getText().toString()) && TextUtils.isEmpty(mpassword.getText().toString())){
            musername.setError("REQUIRED");
            musername.requestFocus();
            mpassword.setError("REQUIRED");
            mpassword.requestFocus();
            return false;
        }
        return result;
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.gotoSignUp){
            startActivity(new Intent(this,RegistrationSession.class));
        }
        if (view.getId()==R.id.signIn){
            SignInUSer();
        }
    }


}