package com.project.nat.advice_nation.Base;

import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.nat.advice_nation.Model.Product;
import com.project.nat.advice_nation.R;

import java.util.ArrayList;

/**
 * Created by Chari on 7/4/2017.
 */

public class DetailListAdapter extends RecyclerView.Adapter< DetailListAdapter.MyViewHolder > {


    private final ArrayList<Product> list;

    public DetailListAdapter(ArrayList<Product> list) {
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_listdetail_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Product product= list.get(position);
        holder.title.setText(product.getTitle());
        holder.subtitle.setText(product.getSubtitle());
        holder.time.setText(product.getTime());
        holder.image.setImageResource(product.getImage());
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, subtitle,time ;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.detail_list_title);
            subtitle = (TextView) view.findViewById(R.id.detail_list_subtitle);
            image = (ImageView) view.findViewById(R.id.detail_list_image);
            time = (TextView) view.findViewById(R.id.detail_list_time);
        }
    }

}
