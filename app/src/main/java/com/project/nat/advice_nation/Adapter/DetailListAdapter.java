package com.project.nat.advice_nation.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.project.nat.advice_nation.Base.ReviewActivity;
import com.project.nat.advice_nation.Model.Product;
import com.project.nat.advice_nation.R;

import java.util.ArrayList;

/**
 * Created by Chari on 7/4/2017.
 */

public class DetailListAdapter extends RecyclerView.Adapter< DetailListAdapter.MyViewHolder >
{


    private final ArrayList<Product> list;
    private final Context mcontext;

    public DetailListAdapter(ArrayList<Product> list,Context context)
    {
        this.list = list;
        this.mcontext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_listdetail_row, parent, false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReviewActivity.startScreen(mcontext);
            }
        });

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {

        Product product= list.get(position);
        holder.title.setText(product.getTitle());
        holder.subtitle.setText(product.getSubtitle());
        holder.time.setText(product.getTime());
        holder.image.setImageResource(product.getImage());
        holder.ratingBar.setNumStars(3);
        LayerDrawable stars = (LayerDrawable)  holder.ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#24b89e"), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.parseColor("#dfdedf"), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.parseColor("#dfdedf"), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements RatingBar.OnRatingBarChangeListener
    {
        public TextView title, subtitle,time ;
        public ImageView image;
        public RatingBar ratingBar;

        public MyViewHolder(View view)
        {
            super(view);
            title = (TextView) view.findViewById(R.id.detail_list_title);
            subtitle = (TextView) view.findViewById(R.id.detail_list_subtitle);
            image = (ImageView) view.findViewById(R.id.detail_list_image);
            time = (TextView) view.findViewById(R.id.detail_list_time);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        }

        @Override
        public void onRatingChanged(RatingBar ratingBar, float v, boolean b)
        {
            Toast.makeText(mcontext,"This is rating:-"+String.valueOf(v),Toast.LENGTH_SHORT).show();

        }
    }



}
