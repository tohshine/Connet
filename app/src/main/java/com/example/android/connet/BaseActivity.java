package com.example.android.connet;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;


public class BaseActivity extends AppCompatActivity {
    private ProgressDialog mprogressDialogue;

    public void hidePd(){

        if (mprogressDialogue!= null&& mprogressDialogue.isShowing()){
            mprogressDialogue.dismiss();
        }
    }

    public  void showpd(){

        if (mprogressDialogue==null){
            mprogressDialogue= new ProgressDialog(this);
            mprogressDialogue.setCanceledOnTouchOutside(false);
            mprogressDialogue.setTitle("Please wait");
            mprogressDialogue.setMessage("Getting you registered on our server...");
              mprogressDialogue.show();
        }
    }
    public  void showpdSign(){

        if (mprogressDialogue==null){
            mprogressDialogue= new ProgressDialog(this);
            mprogressDialogue.setCanceledOnTouchOutside(false);
            mprogressDialogue.setTitle("Please wait");
            mprogressDialogue.setMessage("verifying users credentials...");
            mprogressDialogue.show();
        }
    }
    public  void showpdStatus(){

        if (mprogressDialogue==null){
            mprogressDialogue= new ProgressDialog(this);
            mprogressDialogue.setCanceledOnTouchOutside(false);
            mprogressDialogue.setTitle("Saving changes");
            mprogressDialogue.setMessage("please wait while we save changes...");
            mprogressDialogue.show();
        }
    }
}
