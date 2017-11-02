package com.project.nat.advice_nation.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.nat.advice_nation.Base.ProductList;
import com.project.nat.advice_nation.Model.Category;
import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.utils.NetworkUrl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wasabeef on 2015/01/03.
 */
public class SubcategoryAdapter extends RecyclerView.Adapter<SubcategoryAdapter.ViewHolder> {

  private final ArrayList<Category> data;
  private Context mContext;
  private long user;


  public SubcategoryAdapter(Context context, long user, ArrayList<Category> data)
  {
    mContext = context;
    this.user = user;
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
  //  Glide.with(mContext).load(R.mipmap.placeholder).into(holder.image);
    // http://ec2-13-126-97-168.ap-south-1.compute.amazonaws.com:8080/AdviseNation/auth/image/17041409/1/productSubCategory/1?size=0x0&highquality=false

    String imageUrl = NetworkUrl.URL_GET_IMAGE+user+"/"+data.get(0).getData().get(position).getProductCategoryId()+"/productSubCategory/"+
            data.get(0).getData().get(position).getId()+"?size=0x0&highquality=false";
    Log.e("TAG", "imageUrl: "+imageUrl );
    Glide
            .with(mContext)
            .load(imageUrl)
            .placeholder(R.mipmap.placeholder) // can also be a drawable
            .error(R.mipmap.placeholder) // will be displayed if the image cannot be loaded
            .crossFade()
            .into(holder.image);

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
