package com.project.nat.advice_nation.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.project.nat.advice_nation.Adapter.DashboardAdapter;
import com.project.nat.advice_nation.Base.DashboardActivity;
import com.project.nat.advice_nation.Base.SubcategoryActivity;
import com.project.nat.advice_nation.Https.ApiResponse;
import com.project.nat.advice_nation.Https.AppController;
import com.project.nat.advice_nation.Https.GetApi;
import com.project.nat.advice_nation.Https.ToAppcontroller;
import com.project.nat.advice_nation.Model.Category;
import com.project.nat.advice_nation.Model.UserDetails;
import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.RecylerViewClick.RecyclerItemClickListener;
import com.project.nat.advice_nation.utils.BaseFragmetActivity;
import com.project.nat.advice_nation.utils.NetworkUrl;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


public class HomeFragment extends BaseFragmetActivity implements ToAppcontroller,ApiResponse {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private String TAG="HomeFragment";
    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private Context context;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private GetApi getApi;
    private ArrayList<Category> categoryList;
    private OnFragmentInteractionListener mcallback;
    private View viewpart;

    public HomeFragment() {

    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_dashboard, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v =getView();
        Initi(v);
        Log.e(TAG, "onActivityCreated: " );
        if (isOnline(context)) {
           mcallback.onFragmentInteraction(true);
            callback(0);//0 is id for login api
        } else {
            showSnackbar(viewpart, getResources().getString(R.string.network_notfound));
        }



    }

    private void setview(){

        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DashboardAdapter adapter = new DashboardAdapter(context, categoryList.get(0).getData());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position)
                    {
                       int selectID=(int)categoryList.get(0).getData().get(position).getId();
                        Log.e(TAG, "onItemClick ID: "+selectID );
                        SubcategoryActivity.startScreen(context,selectID);

                        // do whatever
                    }

                    @Override public void onLongItemClick(View view, int position)
                    {
                        // do whatever
                    }
                })
        );

    }


    private void Initi(View view) {
        gson = new Gson();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        viewpart = view.findViewById(android.R.id.content);
        recyclerView = (RecyclerView)view.findViewById(R.id.subrecycler);


    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        mcallback=(OnFragmentInteractionListener)context;



    }



    private void callback(int id) {
        switch (id) {
            case 0:
                long ID = sharedPreferences.getLong("id",0);
                String bearerToken = sharedPreferences.getString("bearerToken","");
                Log.e(TAG, "bearerToken: "+bearerToken );
                String URL = NetworkUrl.URL_CATEGORY + ID + "/productCategory";
                String apiTag = NetworkUrl.URL_CATEGORY + ID + "/productCategory";
                getApi = new GetApi(context, URL,bearerToken,apiTag,TAG,0,this); //0 is for finish second api call
                break;

            default:
                break;


        }


    }

    @Override
    public void OnSucess(JSONObject response, int id) {
        Log.e(TAG, "OnSucess: home fragment"+id+" "+response );

        switch (id){
            case 0:
                categoryList=new ArrayList<Category>();
                Category category = gson.fromJson(response.toString(), Category.class);
                categoryList.add(category);
                Log.e(TAG, "category list: "+categoryList.get(0).getData().size());
                setview();
                mcallback.onFragmentInteraction(false);
                break;

            default:
                break;
        }
    }

    @Override
    public void OnFailed(int error,int id) {
        Log.e(TAG, "OnFailed: "+error );
        mcallback.onFragmentInteraction(false);
        switch (error) {
            case 000:
                showSnackbar(viewpart, getResources().getString(R.string.network_poor));
                break;
            default:
                showSnackbar(viewpart, getResources().getString(R.string.random_error));

        }

    }

    @Override
    public void appcontroller(JsonObjectRequest jsonObjectRequest, String apiTag) {
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, apiTag);

    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(boolean loader);
    }
}
