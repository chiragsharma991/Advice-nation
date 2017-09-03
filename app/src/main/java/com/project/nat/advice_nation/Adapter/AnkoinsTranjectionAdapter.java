package com.project.nat.advice_nation.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.nat.advice_nation.Ankoins.Activity_AnkoinsTranjection;
import com.project.nat.advice_nation.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Chari on 7/23/2017.
 */

public class AnkoinsTranjectionAdapter extends RecyclerView.Adapter<AnkoinsTranjectionAdapter.ViewHolder>
{
    private ArrayList<String> ArrayTarnjectionID;
    private ArrayList<String> ArrayDateoftrajection;
    private ArrayList<String> ArraySpentAnkoins;
    private ArrayList<String> ArrayPupose;
    private Context mcontext;


    public AnkoinsTranjectionAdapter(ArrayList<String> ArrayTarnjectionID, ArrayList<String> ArrayDateoftrajection,
                                     ArrayList<String> ArraySpentAnkoins, ArrayList<String> ArrayPupose, Activity_AnkoinsTranjection context)
    {
        this.ArrayTarnjectionID=ArrayTarnjectionID;
        this.ArrayDateoftrajection= ArrayDateoftrajection;
        this.ArraySpentAnkoins= ArraySpentAnkoins;
        this.ArrayPupose=ArrayPupose;
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
        holder.txt_TranjectionID.setText(ArrayTarnjectionID.get(position));
        holder.txt_Dateoftranjection.setText(ArrayDateoftrajection.get(position));
        holder.txt_Ankoinspent.setText(ArraySpentAnkoins.get(position));
        holder.txt_purpose.setText(ArrayPupose.get(position));

    }

    @Override
    public int getItemCount() {
        return ArrayTarnjectionID.size();
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
