package com.project.nat.advice_nation.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.nat.advice_nation.Base.ProductList;
import com.project.nat.advice_nation.Model.Category;
import com.project.nat.advice_nation.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wasabeef on 2015/01/03.
 */
public class SubcategoryAdapter extends RecyclerView.Adapter<SubcategoryAdapter.ViewHolder> {

  private final ArrayList<Category> data;
  private Context mContext;


  public SubcategoryAdapter(Context context, ArrayList<Category> data)
  {
    mContext = context;
    this.data = data;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(mContext).inflate(R.layout.activity_subcategory_listitem, parent, false);

    return new ViewHolder(v);
  }

  private void overridePendingTransition(int start, int exit)
  {

  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
  //  Picasso.with(mContext).load(R.drawable.bg).into(holder.image);
    Glide.with(mContext).load(R.mipmap.placeholder).into(holder.image);

    holder.text.setText(data.get(0).getData().get(position).getProductSubCategoryName());

  }

  @Override public int getItemCount() {
    return data.get(0).getData().size();
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
