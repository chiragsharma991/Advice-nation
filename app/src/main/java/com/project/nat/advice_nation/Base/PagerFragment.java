package com.project.nat.advice_nation.Base;

import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.utils.Constants;

import java.util.List;

public class PagerFragment extends Fragment {


    private ImageView Pager_image;
    private int position;
    private String TAG="PagerFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_pager_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         position= getArguments().getInt(Constants.Bundle_Pos);
         initView(view);
         setData();
    }

    private void setData() {

        Log.e(TAG, "setData: "+position );
        switch (position){
            case 0:
                Glide.with(getActivity()).load(R.mipmap.two).placeholder(R.mipmap.ic_launcher).into(Pager_image);
                break;
            case 1:
                Glide.with(getActivity()).load(R.mipmap.three).placeholder(R.mipmap.ic_launcher).into(Pager_image);
                break;
            case 2:
                Glide.with(getActivity()).load(R.mipmap.one).placeholder(R.mipmap.ic_launcher).into(Pager_image);
                break;
            case 3:
                Glide.with(getActivity()).load(R.mipmap.bg_people).placeholder(R.mipmap.ic_launcher).into(Pager_image);
                break;

        }

    }

    private void initView(View view) {
        Pager_image = (ImageView) view.findViewById(R.id.pager_image);

    }





}
