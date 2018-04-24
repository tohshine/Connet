package com.example.android.connet;


import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by OWOEYE on 4/24/2018.
 */
import java.util.List;

   public  class Users {
    public String username;
    public String status;
    public String image;


public Users(){

}
    public Users(String username, String status, String image){
        this.username=username;
        this.status=status;
        this.image=image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
