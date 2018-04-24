package com.example.android.connet;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.google.firebase.auth.FirebaseAuth;

public class MainScreen extends BaseActivity{
   private FirebaseAuth mAuth;
    private Toolbar mtoolbar;
    private TabLayout mTablayout;
    private ViewPager mviewpager;
    private SectionsPagerAdapter sectionsPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        mAuth= FirebaseAuth.getInstance();
        //init viewpager /tabs
        mviewpager= findViewById(R.id.main_tabPager);
        sectionsPagerAdapter= new SectionsPagerAdapter(getSupportFragmentManager());
        mviewpager.setAdapter(sectionsPagerAdapter);

        mtoolbar= findViewById(R.id.main_screen_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("connect");

        mTablayout = findViewById(R.id.mainToolbar);
        mTablayout.setupWithViewPager(mviewpager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        getMenuInflater().inflate(R.menu.account_settings, menu);
        getMenuInflater().inflate(R.menu.all_users,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.logout){
            mAuth.signOut();
            Intent intent = new Intent(MainScreen.this,LoginSession.class);
            startActivity(intent);
            finish();
            return true;
        }
        if (item.getItemId()==R.id.settings){
            startActivity(new Intent(MainScreen.this,UserProfile.class));
        }
        if (item.getItemId()==R.id.all_users){
            startActivity(new Intent(MainScreen.this,allUsers.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
