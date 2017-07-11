package com.project.nat.advice_nation.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.project.nat.advice_nation.Model.Product;
import com.project.nat.advice_nation.Model.ReviewItem;
import com.project.nat.advice_nation.R;

import java.util.ArrayList;

import static android.R.id.list;

/**
 * Created by Chari on 7/10/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder>
{
    private ArrayList<ReviewItem> usename;
    private Context mcontext;

    public ReviewAdapter(ArrayList<ReviewItem> strings, Context subclassActivity)
    {
        this.usename=strings;
        this.mcontext=subclassActivity;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_reviewlistitem, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        ReviewItem reviewItem= usename.get(position);
        holder.username.setText(reviewItem.getUserName());
        holder.userComment.setText(reviewItem.getUserComment());

    }

    @Override
    public int getItemCount()
    {
        return usename.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView username;
        TextView userComment;
        RatingBar ratingBar;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            username =(TextView) itemView.findViewById(R.id.UserName);
            userComment = (TextView) itemView.findViewById(R.id.Discription);
            ratingBar = (RatingBar) itemView.findViewById(R.id.subUserBar);

        }
    }
}
