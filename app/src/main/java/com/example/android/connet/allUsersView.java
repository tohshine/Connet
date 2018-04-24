package com.example.android.connet;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by OWOEYE on 4/23/2018.
 */

public class allUsersView extends RecyclerView.ViewHolder{
         TextView musername;
         TextView mstatus;
         ImageView mimageView;

        public allUsersView(View itemView) {
            super(itemView);
            musername= itemView.findViewById(R.id.single_name);
            mstatus= itemView.findViewById(R.id.single_status);
            mimageView= itemView.findViewById(R.id.singleImage);
        }
        public void OnBind(Users users){
            musername.setText(users.getUsername());
            mstatus.setText(users.getStatus());
        }

}
