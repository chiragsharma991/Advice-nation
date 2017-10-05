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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.nat.advice_nation.Model.Subcategory;
import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.utils.Constants;
import com.project.nat.advice_nation.utils.NetworkUrl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PagerFragment extends Fragment {


    private ImageView Pager_image;
    private int position;
    private String TAG="PagerFragment";
    private Gson gson;
    private String listOfimage;
    private ArrayList<Subcategory> carouseImages;
    private long userId;

    public PagerFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_pager_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         position= getArguments().getInt(Constants.Bundle_Pos);
         listOfimage= getArguments().getString(Constants.KEY_IMAGE);
         userId= getArguments().getLong(Constants.KEY_USER_ID);
         initView(view);
         setData(position);
    }

    private void setData(int position) {

        //http://ec2-13-126-97-168.ap-south-1.compute.amazonaws.com:8080/AdviseNation/auth/image/17041409/carousel/3?size=0x0&highquality=false

        String image = NetworkUrl.URL_GET_IMAGE +userId+"/carousel/"+carouseImages.get(0).getData().get(position).getId()+ "?size=0x0&highquality=false";
        Log.e(TAG, "image: " + image);
        Glide
                .with(getActivity())
                .load(image)
                .placeholder(R.mipmap.ic_launcher) // can also be a drawable
                .error(R.mipmap.ic_launcher) // will be displayed if the image cannot be loaded
                .crossFade()
                .into(Pager_image);

    }

    public void initView(View view) {
        gson=new Gson();
        Pager_image = (ImageView) view.findViewById(R.id.pager_image);

        Type type=new TypeToken<List<Subcategory>>(){}.getType();
        carouseImages = gson.fromJson(listOfimage, type);

    }





}
