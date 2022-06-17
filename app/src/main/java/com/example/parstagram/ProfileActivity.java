package com.example.parstagram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.parstagram.fragments.ProfileFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUsername;
    private ImageButton btnProfilePic;
    private RecyclerView rvPosts;
    protected GridAdapter adapter;
    protected List<Post> personPosts;
    private Post lastPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tvUsername=findViewById(R.id.tvUsername);
        btnProfilePic=findViewById(R.id.btnProfilePic);
        rvPosts=findViewById(R.id.rvPosts);
        ParseUser user = (ParseUser) Parcels.unwrap(getIntent().getParcelableExtra(ParseUser.class.getSimpleName()));
        lastPost = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        this.bind(user);
    }

    private void bind(ParseUser user) {
        tvUsername.setText(user.getUsername());
        if(user.get("profilePic")!=null){
            btnProfilePic.setBackground(AppCompatResources.getDrawable(ProfileActivity.this, (Integer) user.get("profilePic")));
        } else {
            btnProfilePic.setBackground(AppCompatResources.getDrawable(ProfileActivity.this, R.drawable.icon));
        }

        final GridLayoutManager layout = new GridLayoutManager(ProfileActivity.this, 3);
        personPosts = new ArrayList<>();
        adapter = new GridAdapter(ProfileActivity.this, personPosts);
        // set the adapter on the recycler view
        rvPosts.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvPosts.setLayoutManager(layout);
        // query posts from Parstagram
        queryGrid(user);
    }

    private void queryGrid(ParseUser user){
        // specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, user);
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

    public void back(View view){

        Intent i = new Intent(ProfileActivity.this, PostDetails.class);
        i.putExtra(Post.class.getSimpleName(), Parcels.wrap(lastPost));
        startActivity(i);
        finish();
    }

}