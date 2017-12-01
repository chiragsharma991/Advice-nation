package com.project.nat.advice_nation.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.project.nat.advice_nation.Base.ProductReview;
import com.project.nat.advice_nation.Model.Product;
import com.project.nat.advice_nation.Model.Subcategory;
import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.utils.NetworkUrl;

import java.util.ArrayList;

/**
 * Created by Chari on 7/4/2017.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {


    private final ArrayList<Subcategory> list;
    private final Context mcontext;
    private Gson gson;


    public ProductListAdapter(ArrayList<Subcategory> list, Context context) {
        this.list = list;
        this.mcontext = context;
        gson = new Gson();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_listdetail_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Subcategory Subcategory = list.get(0);
        Log.e("TAG", "onBindViewHolder: position is " + position + " " +  gson.toJson(list));
        holder.title.setText(Subcategory.getData().get(position).getProductName());
        holder.subtitle.setText(Subcategory.getData().get(position).getDescription());
        holder.time.setText("\u20B9" + (int) Subcategory.getData().get(position).getPrice());
        String image = NetworkUrl.URL_GET_IMAGE
                +Subcategory.getData().get(position).getUserId() + "/" +
                +Subcategory.getData().get(position).getProductSubCategoryId() + "/product/" +
                +Subcategory.getData().get(position).getId() + "?size=0x0&highquality=false";
      //  Log.i("TAG", "onBindViewHolder: Image"+image );
        holder.ratingBar.setRating(Subcategory.getData().get(position).getProductRating());
        Glide
                .with(mcontext)
                .load(image)
                .placeholder(R.mipmap.placeholder) // can also be a drawable
                .error(R.mipmap.placeholder) // will be displayed if the image cannot be loaded
                .crossFade()
                .into(holder.image);

        LayerDrawable stars = (LayerDrawable)holder.ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#212121"), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.parseColor("#dfdedf"), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.parseColor("#212121"), PorterDuff.Mode.SRC_ATOP);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ProductList = gson.toJson(list);
                Intent intent = new Intent(mcontext, ProductReview.class);
                intent.putExtra("ProductList", ProductList);
                intent.putExtra("position", position);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) mcontext, holder.image,"product_image");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mcontext.startActivity(intent, options.toBundle());
                }else{
                    mcontext.startActivity(intent);
                }
               // ProductReview.startScreen(mcontext, ProductList, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.get(0).getData().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements RatingBar.OnRatingBarChangeListener {
        public TextView title, subtitle, time;
        public ImageView image;
        public RatingBar ratingBar;
        public CardView card;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.detail_list_title);
            subtitle = (TextView) view.findViewById(R.id.detail_list_subtitle);
            image = (ImageView) view.findViewById(R.id.detail_list_image);
            time = (TextView) view.findViewById(R.id.detail_list_time);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            card = (CardView) view.findViewById(R.id.card);


        }

        @Override
        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
            Toast.makeText(mcontext, "This is rating:-" + String.valueOf(v), Toast.LENGTH_SHORT).show();

        }
    }


}
