package net.healthtechhub.diabetesnewsapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private List<Post> mPostList;

    public PostAdapter(List<Post> postList) {
        mPostList = postList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Post post = mPostList.get(position);

        holder.titleTextView.setText(post.getTitle());
        holder.sectionTextView.setText(post.getSection());
        holder.authorTextView.setText(post.getAuthor());
        holder.dateTextView.setText(post.getDate());
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView, sectionTextView, authorTextView, dateTextView;

        public MyViewHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.post_title);
            sectionTextView = view.findViewById(R.id.post_section);
            authorTextView = view.findViewById(R.id.post_author);
            dateTextView = view.findViewById(R.id.post_date);
        }
    }
}
