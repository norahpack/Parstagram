package com.example.parstagram;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.Date;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private Context context;
    private List<Comment> comments;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void addAll(List<Comment> commentsList) {
        comments.addAll(commentsList);
        notifyDataSetChanged();
    }

    public void clear(){
        comments.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvText;
        private ImageView ivProfile;
        private TextView tvTime;
        private TextView tvUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvText);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvUser = itemView.findViewById(R.id.tvUser);

        }

        public void bind(Comment comment) {
            //Bind the post data to the view elements
            tvText.setText(comment.getText());
            tvUser.setText(comment.getCommenter().getUsername());
            Date createdAt = comment.getCreatedAt();
            String timeAgo = Comment.calculateTimeAgo(createdAt);
            tvTime.setText(timeAgo);
            ParseUser currentUser = comment.getCommenter();
            if(currentUser.get("profilePic")!=null){
                ivProfile.setBackground(AppCompatResources.getDrawable(context, (Integer) currentUser.get("profilePic")));
            } else {
                ivProfile.setBackground(AppCompatResources.getDrawable(context, R.drawable.icon));
            }
        }
    }
}