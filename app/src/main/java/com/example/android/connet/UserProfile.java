package com.example.android.connet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;



import de.hdodenhof.circleimageview.CircleImageView;


public class UserProfile extends BaseActivity implements View.OnClickListener
{
    private TextView mgender,mhandle,minstitution,musername;
    private CircleImageView mimageView;
    private EditText mstatus;
    private Button mUpdateStatus;
    private FirebaseUser muser;
    private DatabaseReference mDatabase;
    private DatabaseReference mStatusRef;
    private static  final int PICTURE_CODE=100;
    private StorageReference mStorage;
    private ProgressBar mprogressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        //init views
        mgender= findViewById(R.id.gender);
        mhandle= findViewById(R.id.socialMedia);
        minstitution= findViewById(R.id.school);
        mimageView= findViewById(R.id.profileImage);
        mstatus = findViewById(R.id.handleUpdate);
         mUpdateStatus= findViewById(R.id.button);
         musername = findViewById(R.id.username);
         mprogressBar= findViewById(R.id.progress);
        //init
        mStorage= FirebaseStorage.getInstance().getReference();
        muser= FirebaseAuth.getInstance().getCurrentUser();
        String user_details= muser.getUid();
        mDatabase=FirebaseDatabase.getInstance().getReference("users").child(user_details);
        mDatabase.keepSynced(true);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             String user_gender=   dataSnapshot.child("gender").getValue().toString();
                String user_school=   dataSnapshot.child("institution").getValue().toString();
                String user_handle=   dataSnapshot.child("status").getValue().toString();
                String user_image=   dataSnapshot.child("image").getValue().toString();
                String user_name=   dataSnapshot.child("username").getValue().toString();
                //setting datasnapshot to view
                mgender.setText(user_gender);
                mhandle.setText(user_handle);
                minstitution.setText(user_school);
                musername.setText(user_name);
                Picasso.with(UserProfile.this).load(user_image).error(R.drawable.defaults).into(mimageView);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mimageView.setOnClickListener(this);
        mUpdateStatus.setOnClickListener(this);

    }
    private  void StatusUpdates(){
        showpdStatus();
        String updateStatus= mstatus.getText().toString().trim();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mStatusRef= FirebaseDatabase.getInstance().getReference("users").child(uid);
        mStatusRef.child("status").setValue(updateStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    hidePd();
                    mstatus.getText().clear();
                    Toast.makeText(UserProfile.this,"status Updated",Toast.LENGTH_LONG).show();
                }else {
                    hidePd();
                    Toast.makeText(UserProfile.this,"Error updating your status",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.button){
            StatusUpdates();
        }
        if (view.getId()==R.id.profileImage){
            UpdateProfilePic();
        }
    }

    private void UpdateProfilePic() {
        Intent pictureIntent = new Intent();
        pictureIntent.setType("image/*");
        pictureIntent.setAction(Intent.ACTION_PICK);
        startActivityForResult(pictureIntent,PICTURE_CODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICTURE_CODE&&resultCode==RESULT_OK && data!=null){
            Uri picture_uri= data.getData();
            CropImage.activity(picture_uri)
                    .setAspectRatio(1,1)
                    .start(UserProfile.this);


        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            mprogressBar.setVisibility(View.VISIBLE);
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                final String current_userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
               StorageReference storage= mStorage.child("profileImage").child(current_userId + ".jpg");
               storage.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                       if (task.isSuccessful()){
                           mprogressBar.setVisibility(View.GONE);
                        String currentUserPic=task.getResult().getDownloadUrl().toString();
                        mDatabase.child("image").setValue(currentUserPic).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful()){
                                   mDatabase.child("image").addValueEventListener(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(DataSnapshot dataSnapshot) {

                                           Toast.makeText(UserProfile.this,"profile updated",Toast.LENGTH_LONG).show();
                                       }

                                       @Override
                                       public void onCancelled(DatabaseError databaseError) {

                                       }
                                   });

                               }
                            }
                        });

                       }
                   }
               });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }
}
