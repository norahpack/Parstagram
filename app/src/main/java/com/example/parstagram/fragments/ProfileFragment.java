package com.example.parstagram.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.parstagram.ChooseProfile;
import com.example.parstagram.EndlessRecyclerViewScrollListener;
import com.example.parstagram.GridAdapter;
import com.example.parstagram.LoginActivity;
import com.example.parstagram.Post;
import com.example.parstagram.PostsAdapter;
import com.example.parstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    private TextView tvUsername;
    private ImageButton btnProfilePic;
    private Button btnLogOut;
    private RecyclerView rvPosts;
    protected GridAdapter adapter;
    protected List<Post> personPosts;
    //private EndlessRecyclerViewScrollListener scrollListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvUsername=view.findViewById(R.id.tvUsername);
        btnProfilePic=view.findViewById(R.id.btnProfilePic);
        btnLogOut=view.findViewById(R.id.btnLogOut);
        rvPosts=view.findViewById(R.id.rvPosts);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser.get("profilePic")!=null){
            btnProfilePic.setBackground(AppCompatResources.getDrawable(getContext(), (Integer) currentUser.get("profilePic")));
        } else {
            btnProfilePic.setBackground(AppCompatResources.getDrawable(getContext(), (R.drawable.instagramtwo)));

        }
        tvUsername.setText(currentUser.getUsername());

        btnProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent i = new Intent(getContext(), ChooseProfile.class);
                startActivity(i);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();
                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);
            }
        });
        final GridLayoutManager layout = new GridLayoutManager(getContext(), 3);
        personPosts = new ArrayList<>();
        adapter = new GridAdapter(getContext(), personPosts);
        // set the adapter on the recycler view
        rvPosts.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvPosts.setLayoutManager(layout);
        // query posts from Parstagram
        queryGrid();

    }

    private void queryGrid(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        // specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, currentUser);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                // check for errors
                if (e != null) {
                    return;
                }
                // save received posts to list and notify adapter of new data
                adapter.addAll(posts);
                adapter.notifyDataSetChanged();

            }
        });
    }
}