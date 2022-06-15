package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    Button btnLikes;
    int likes;
    boolean liked = false;
    Drawable likeBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        tvCreatedAt=findViewById(R.id.tvCreatedAt);
        tvUsername=findViewById(R.id.tvUsername);
        tvDescription=findViewById(R.id.tvDescription);
        ivImage=findViewById(R.id.ivImage);
        tvLikes=findViewById(R.id.tvLikes);
        btnLikes=findViewById(R.id.btnLikes);


        Post post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        this.bind(post);

        btnLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Drawable newBackground;
                    int change_likes;

                    if (liked){
                        newBackground = AppCompatResources.getDrawable(PostDetails.this, R.drawable.ufi_heart);
                        liked = false;
                        change_likes=-1;
                    } else {
                        newBackground = AppCompatResources.getDrawable(PostDetails.this, R.drawable.ufi_heart_active);
                        liked = true;
                        change_likes=1;
                    }
                    post.setLikes(post.getLikes()+change_likes);
                    tvLikes.setText(String.valueOf(post.getLikes()));
                try {
                    post.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                btnLikes.setBackground(newBackground);
            }
        });


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

        if(liked) {
            likeBackground = AppCompatResources.getDrawable(PostDetails.this, R.drawable.ufi_heart_active);
        } else {
            likeBackground = AppCompatResources.getDrawable(PostDetails.this, R.drawable.ufi_heart);

        }
        btnLikes.setBackground(likeBackground);
    }



}