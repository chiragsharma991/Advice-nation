package com.project.nat.advice_nation.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.project.nat.advice_nation.R;

import java.util.ArrayList;

/**
 * Created by Surya Chundawat on 7/14/2017.
 */

public class ProductReviewAdapter extends RecyclerView.Adapter<ProductReviewAdapter.MyViewHolder>
{
    private ArrayList<String> usename;
    private ArrayList<String> textComment;
    private Context mcontext;

    public ProductReviewAdapter(ArrayList<String> strings, ArrayList<String> strings1, Context subclassActivity)
    {
        this.usename=strings;
        this.textComment= strings1;
        this.mcontext=subclassActivity;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_detailslistitem, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        holder.username.setText(usename.get(position));
        holder.userComment.setText(textComment.get(position));

        LayerDrawable stars = (LayerDrawable)   holder.ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#24b89e"), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.parseColor("#dfdedf"), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.parseColor("#dfdedf"), PorterDuff.Mode.SRC_ATOP);

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

