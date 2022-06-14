package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.Date;

public class PostDetails extends AppCompatActivity {

    TextView tvCreatedAt;
    TextView tvUsername;
    TextView tvDescription;
    ImageView ivImage;
    TextView tvLikes;
    int likes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        tvCreatedAt=findViewById(R.id.tvCreatedAt);
        tvUsername=findViewById(R.id.tvUsername);
        tvDescription=findViewById(R.id.tvDescription);
        ivImage=findViewById(R.id.ivImage);
        tvLikes=findViewById(R.id.tvLikes);

        Post post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        this.bind(post);


    }

    public void bind(Post post){
        Date createdAt = post.getCreatedAt();
        String timeAgo = Post.calculateTimeAgo(createdAt);
        //post.setLikes(1);
        likes=post.getLikes();
        post.saveInBackground();
        tvLikes.setText(String.valueOf(likes));
        tvCreatedAt.setText(timeAgo);
        tvUsername.setText(post.getUser().getUsername());
        tvDescription.setText(post.getDescription());
        ParseFile image = post.getImage();
        if (image != null){
            Glide.with(this).load(image.getUrl()).into(ivImage);
        }
    }
}