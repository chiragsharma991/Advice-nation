package com.project.nat.advice_nation.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.nat.advice_nation.Base.ProductList;
import com.project.nat.advice_nation.R;

import java.util.List;
import java.util.Random;

/**
 * Created by Chari on 7/23/2017.
 */


public class NkoinsSubcatAdapter extends RecyclerView.Adapter<NkoinsSubcatAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mDataSet;

    public NkoinsSubcatAdapter(Context context, List<String> dataSet)
    {
        mContext = context; //
        mDataSet = dataSet;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_nkoins_subcategory, parent, false);
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

    private void overridePendingTransition(int start, int exit)
    {

    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        //  Picasso.with(mContext).load(R.drawable.bg).into(holder.image);
        Random rnd = new Random();
        int currentStrokeColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.image.setBackgroundColor(currentStrokeColor);
      //  Glide.with(mContext).load(R.color.dashboard_icon2).into(holder.image);

        holder.text.setText(mDataSet.get(position));

    }

    @Override public int getItemCount() {
        return mDataSet.size();
    }



    static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
