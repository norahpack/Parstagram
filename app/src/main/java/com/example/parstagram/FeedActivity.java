package com.example.parstagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.parstagram.fragments.ComposeFragment;
import com.example.parstagram.fragments.HomeFragment;
import com.example.parstagram.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    public static final String TAG = "FeedActivity";

    public static FeedActivity self;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    protected BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        self=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.itemHome:
                        fragment = new HomeFragment();
                        break;
                    case R.id.itemPost:
                        fragment = new ComposeFragment();
                        break;
                    case R.id.itemProfile:
                        //change this
                        fragment = new ProfileFragment();
                        break;
                    default:
                        fragment = new HomeFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });

        //Checking if we came from the ChooseProfile class
        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            bottomNavigation.setSelectedItemId(R.id.itemProfile);
        } else {
            bottomNavigation.setSelectedItemId(R.id.itemHome);
        }
    }

    public void setTab(Fragment fragment, int selectedItem){
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
        bottomNavigation.setSelectedItemId(selectedItem);
    }
}