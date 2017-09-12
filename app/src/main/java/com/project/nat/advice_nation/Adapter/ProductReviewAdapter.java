package com.project.nat.advice_nation.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.project.nat.advice_nation.Base.ProductReview;
import com.project.nat.advice_nation.Model.Category;
import com.project.nat.advice_nation.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by Surya Chundawat on 7/14/2017.
 */

public class ProductReviewAdapter extends RecyclerView.Adapter<ProductReviewAdapter.MyViewHolder>
{

    private final ArrayList<Category> productList;
    private final Activity productReview;
    private Context mcontext;

    public ProductReviewAdapter(ArrayList<Category> productList, Activity productReview, Context subclassActivity)
    {
        this.productList=productList;
        this.mcontext=subclassActivity;
        this.productReview=productReview;

    }

            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_detailslistitem, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position)
    {
        LayerDrawable stars = (LayerDrawable)holder.ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.parseColor("#dfdedf"), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.parseColor("#dfdedf"), PorterDuff.Mode.SRC_ATOP);
        holder.username.setText(productList.get(0).getData().get(position).getName());
        holder.userComment.setText(productList.get(0).getData().get(position).getComment());
        holder.follow.setText(productList.get(0).getData().get(position).isCommentFollowed()== true ? "Unfollow" : "Follow");
        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mcontext instanceof ProductReview){
                    ((ProductReview)mcontext).follow(position);

                }
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return productList.get(0).getData().size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView userComment,follow,username;
        RatingBar ratingBar;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            username =(TextView) itemView.findViewById(R.id.UserName);
            userComment = (TextView) itemView.findViewById(R.id.Discription);
            ratingBar = (RatingBar) itemView.findViewById(R.id.subUserBar);
            follow = (TextView) itemView.findViewById(R.id.follow);

        }
    }


}

