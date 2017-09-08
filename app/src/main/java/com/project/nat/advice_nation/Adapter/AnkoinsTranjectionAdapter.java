package com.project.nat.advice_nation.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.nat.advice_nation.Ankoins.Activity_AnkoinsTranjection;
import com.project.nat.advice_nation.Model.Subcategory;
import com.project.nat.advice_nation.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Chari on 7/23/2017.
 */

public class AnkoinsTranjectionAdapter extends RecyclerView.Adapter<AnkoinsTranjectionAdapter.ViewHolder>
{
    private final ArrayList<Subcategory> tranjectionList;
    private Context mcontext;


    public AnkoinsTranjectionAdapter(ArrayList<Subcategory> tranjectionList, Activity_AnkoinsTranjection context)
    {
        this.tranjectionList=tranjectionList;
        this.mcontext=context;

    }

    @Override
    public AnkoinsTranjectionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_activity_ankointranjection, parent, false);
        return new AnkoinsTranjectionAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AnkoinsTranjectionAdapter.ViewHolder holder, int position) {

        holder.txt_TranjectionID.setText("0");
        holder.txt_Dateoftranjection.setText(""+(int)tranjectionList.get(0).getData().get(position).getDate());
        holder.txt_Ankoinspent.setText(""+(int)tranjectionList.get(0).getData().get(position).getAnKoin());
        holder.txt_purpose.setText(tranjectionList.get(0).getData().get(position).getMessage());

    }

    @Override
    public int getItemCount() {
        return tranjectionList.get(0).getData().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_TranjectionID;
        TextView txt_Dateoftranjection;
        TextView txt_Ankoinspent;
        TextView txt_purpose;

        public ViewHolder(View itemView)
        {
            super(itemView);
            txt_TranjectionID =(TextView) itemView.findViewById(R.id.txt_tranjectIDdetails);
            txt_Dateoftranjection= (TextView) itemView.findViewById(R.id.txt_tranjectDateDetails);
            txt_Ankoinspent= (TextView) itemView.findViewById(R.id.txt_SeptAnkoinsDetails);
            txt_purpose= (TextView) itemView.findViewById(R.id.txt_purposeDiscription);
        }
    }
}
