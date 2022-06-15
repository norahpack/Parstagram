package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.io.File;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostDetails extends AppCompatActivity {

    TextView tvCreatedAt;
    TextView tvUsername;
    TextView tvDescription;
    ImageView ivImage;
    TextView tvLikes;
    Button btnLikes;
    Drawable likeBackground;
    boolean liked;
    Button btnComment;
    EditText etComment;
    RecyclerView rvComments;
    protected List<Comment> allComments;
    private LinearLayoutManager llm;
    public static final String TAG = "PostDetails";
    protected CommentsAdapter adapter;


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
        btnComment=findViewById(R.id.btnComment);
        etComment=findViewById(R.id.etComment);
        rvComments=findViewById(R.id.rvComments);



        liked = false;
        Post post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        JSONArray postLikedBy = post.getLikedBy();
        System.out.println("here");
        System.out.println(postLikedBy);

        if(postLikedBy!=null){
            for(int i = 0; i < postLikedBy.length(); i++){
                try {
                    String userVal = postLikedBy.getString(i);
                    if(userVal.equals(ParseUser.getCurrentUser().getObjectId())){
                        liked=true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        this.bind(post);


        allComments = new ArrayList<>();
        adapter = new CommentsAdapter(PostDetails.this, allComments);
        // set the adapter on the recycler view
        rvComments.setAdapter(adapter);

        llm = new LinearLayoutManager(PostDetails.this);
        rvComments.setLayoutManager(llm);

        // query posts from Parstagram
        queryComments((ParseObject) post);

        System.out.println(post.getUser().getUsername());


        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = etComment.getText().toString();
                ParseUser currentUser = ParseUser.getCurrentUser();

                addComment(commentText,  (ParseObject) post, currentUser);
            }
        });

        btnLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Drawable newBackground;

                    if (liked){
                        newBackground = AppCompatResources.getDrawable(PostDetails.this, R.drawable.ufi_heart_active);
                    } else {
                        newBackground = AppCompatResources.getDrawable(PostDetails.this, R.drawable.ufi_heart_active);
                        liked = true;
                        post.setLikedBy(ParseUser.getCurrentUser());
                    }
                try {
                    post.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                tvLikes.setText(String.valueOf(post.getLikedBy().length()));
                btnLikes.setBackground(newBackground);
            }
        });


    }

    private void queryComments(ParseObject post) {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);

        query.include(Comment.KEY_POST_PARENT);
        query.include(Comment.KEY_COMMENTER);

        query.whereEqualTo(Comment.KEY_POST_PARENT, post);
        // limit query to latest 20 items
        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting comments", e);
                    return;
                }

                // save received posts to list and notify adapter of new data
                System.out.println(comments);
                adapter.addAll(comments);
                adapter.notifyDataSetChanged();

            }
        });

    }


    private void addComment(String commentText, ParseObject post, ParseUser currentUser) {
        Comment comment = new Comment();
        comment.setCommenter(currentUser);
        comment.setPostParent(post);
        comment.setText(commentText);
        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(PostDetails.this, "Error while saving", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Comment save was successful!");
                etComment.setText("");
                adapter.clear();
                queryComments(post);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void bind(Post post){
        Date createdAt = post.getCreatedAt();
        String timeAgo = Post.calculateTimeAgo(createdAt);
        post.saveInBackground();
        tvLikes.setText(String.valueOf(post.getLikedBy().length()));
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

    public void back(View view){
        Intent i = new Intent(PostDetails.this, FeedActivity.class);
        startActivity(i);
        finish();
    }





}