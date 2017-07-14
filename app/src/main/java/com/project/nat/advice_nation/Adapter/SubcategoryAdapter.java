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
import com.project.nat.advice_nation.Base.DetailsList;
import com.project.nat.advice_nation.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Wasabeef on 2015/01/03.
 */
public class SubcategoryAdapter extends RecyclerView.Adapter<SubcategoryAdapter.ViewHolder> {

  private Context mContext;
  private List<String> mDataSet;

  public SubcategoryAdapter(Context context, List<String> dataSet)
  {
    mContext = context;
    mDataSet = dataSet;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(mContext).inflate(R.layout.activity_subcategory_listitem, parent, false);
    v.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view)
      {
      //  overridePendingTransition(R.anim.start, R.anim.exit);
        DetailsList.startScreen(mContext);

      }
    });
    return new ViewHolder(v);
  }

  private void overridePendingTransition(int start, int exit)
  {

  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
  //  Picasso.with(mContext).load(R.drawable.bg).into(holder.image);
    Glide.with(mContext).load(R.mipmap.placeholder).into(holder.image);

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
