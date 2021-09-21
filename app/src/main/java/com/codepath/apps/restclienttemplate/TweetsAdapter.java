package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{
    //Pass in the context and list of tweets
    Context context;
    List<Tweet>tweets;
    public TweetsAdapter(Context context, List<Tweet> tweets){
        this.context= context;
        this.tweets = tweets;
    }



    @NonNull
    @Override

    //For each row, inflate the layout
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet,parent,false);


        return new ViewHolder(view);
    }

    //bind values based on the position
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Get the data at a position
        Tweet tweet =tweets.get(position);

        //Bind the tweet with the viewHolder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }
    // clean the elements of the recycler
    public void clear(){
        tweets.clear();
        notifyDataSetChanged();
    }

    //add a list of items
    public void addAll(List<Tweet> tweetList){
        tweets.addAll(tweetList);
        notifyDataSetChanged();

    }

    //Define ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);
            Glide.with(context).load(tweet.user.profileImageUrl).into(ivProfileImage);

        }
    }
}
