package com.project.nat.advice_nation.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.nat.advice_nation.Base.DashboardActivity;
import com.project.nat.advice_nation.Base.ProductList;
import com.project.nat.advice_nation.Model.Category;
import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.utils.NetworkUrl;

import java.util.List;
import java.util.Random;

/**
 * Created by Chari on 7/27/2017.
 */

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private final List<Category.CategoryList> data;
    private final long ID;
    private Random random;
    private Context mContext;
    private int color[]={R.color.dashboard_icon1,R.color.dashboard_icon2,R.color.dashboard_icon3,R.color.dashboard_icon4};
    private int colorPosition=0;


    public DashboardAdapter(Context context, long ID, List<Category.CategoryList> data) {
        mContext = context;
        this.ID = ID;
        this.data = data;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_dashboard_child, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //  overridePendingTransition(R.anim.start, R.anim.exit);
                ProductList.startScreen(mContext,0,0);

            }
        });
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        //  Picasso.with(mContext).load(R.drawable.bg).into(holder.image);
        Random rnd = new Random();
        int currentStrokeColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
       // holder.image.setBackgroundColor(currentStrokeColor);
        //  Glide.with(mContext).load(R.color.dashboard_icon2).into(holder.image);
        //http://ec2-13-126-97-168.ap-south-1.compute.amazonaws.com:8080/AdviseNation/auth/image/17041409/productCategory/1?size=0x0&highquality=false
        String imageUrl = NetworkUrl.URL_GET_IMAGE+ID+"/productCategory/"+data.get(position).getId()+"?size=0x0&highquality=false";


        Glide
                .with(mContext)
                .load(imageUrl)
                .placeholder(currentStrokeColor) // can also be a drawable
                .error(currentStrokeColor) // will be displayed if the image cannot be loaded
                .crossFade()
                .into(holder.image);
        random=new Random();
        holder.text.setText(data.get(position).getProductCategoryName());
        if(colorPosition == 4) colorPosition=0;
        holder.parent.setBackgroundResource(color[colorPosition]);
        colorPosition++;

    }

    @Override public int getItemCount() {
        return data.size();
    }



    static class ViewHolder extends RecyclerView.ViewHolder
    {

        public ImageView image;
        public TextView text;
        public RelativeLayout parent;

        public ViewHolder(View itemView)
        {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            text = (TextView) itemView.findViewById(R.id.text);
            parent = (RelativeLayout) itemView.findViewById(R.id.parent);
        }
    }
}
