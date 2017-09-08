package com.project.nat.advice_nation.Ankoins;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.project.nat.advice_nation.Adapter.AnkoinsTranjectionAdapter;
import com.project.nat.advice_nation.Adapter.ProductReviewAdapter;
import com.project.nat.advice_nation.Https.ApiResponse;
import com.project.nat.advice_nation.Https.GetApi;
import com.project.nat.advice_nation.Model.Subcategory;
import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.utils.BaseActivity;
import com.project.nat.advice_nation.utils.NetworkUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static com.project.nat.advice_nation.R.id.list_item_process;
import static com.project.nat.advice_nation.R.id.no_datafound;

/**
 * Created by Chari on 7/23/2017.
 */

public class Activity_AnkoinsTranjection extends BaseActivity implements ApiResponse
{
    private RecyclerView recyclerView;
    private String[] Array_TranjectionID,Array_DateOfTranjection,Array_AnkoinsSpents,Array_Purpose;
    private AnkoinsTranjectionAdapter adapter;
    private Context context;
    private Gson gson;
    private SharedPreferences sharedPreferences;
    private View viewpart;
    private ProgressBar progressBar;
    private String TAG="AnkoinsTranjection";
    private ArrayList<Subcategory> tranjectionList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ankointranjection);
        Array_TranjectionID = new String[]{"AB456GSDHAGS","RJTH98785BD","HHER2656"};
        Array_DateOfTranjection = new String[]{"01-05-2010  12:45:50  PM","03-01-2015  11:25:10  PM","15-09-2017  09:15:02  AM"};
        Array_AnkoinsSpents = new String[]{"100","120","1500"};
        Array_Purpose = new String[]{"This is for the testing purpose tranjection from the advice nation ",
                                      "My purpose of tranjection is buys good product from spenting the ankois"
                                       ,"This is my last tesing ankoin tranjection from this seasion"};


        checkstatusbar();
        IniliView();


    }

    private void checkstatusbar()
    {

        if(Build.VERSION.SDK_INT>=21)
        {
            Window window = getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }


    private void IniliView()
    {
        context=this;
        gson = new Gson();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        viewpart = findViewById(android.R.id.content);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tranjection History");
        toolbar.setNavigationIcon(R.drawable.ic_left_black_24dp);
        toolbar.setNavigationContentDescription("Back");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                // what do you want here
                 }
        });


        if (isOnline(context)) {
            callback(0);//0 is responseCode for login api
            progressBar.setVisibility(View.VISIBLE);
        } else {
            showSnackbar(viewpart, getResources().getString(R.string.network_notfound));
        }


    }

    private void callback(int responseCode) {
        switch (responseCode) {
            case 0:
                //http://ec2-13-126-97-168.ap-south-1.compute.amazonaws.com:8080/AdviseNation/api/users/85384665/ankoin/history
                long user = sharedPreferences.getLong("id", 0);
                String bearerToken = sharedPreferences.getString("bearerToken", "");
                String URL = NetworkUrl.URL_GET_USER_ANKOINS + user + "/ankoin/history";
                String apiTag = NetworkUrl.URL_GET_USER_ANKOINS + user + "/ankoin/history";
                GetApi getApi = new GetApi(context, URL, bearerToken, apiTag, TAG, 0); //0 is for finish second api call
                break;

            default:
                break;


        }
    }


    @Override
    public void OnSucess(JSONObject response, int id) {

        switch (id) {
            case 0:
                Log.e(TAG, "OnSucess: ");
                tranjectionList = new ArrayList<Subcategory>();
                Subcategory list = gson.fromJson(response.toString(), Subcategory.class);
                tranjectionList.add(list);
                setview();
                progressBar.setVisibility(View.GONE);
                break;

            default:
                progressBar.setVisibility(View.GONE);
                break;
        }
    }

    private void setview() {

        adapter = new AnkoinsTranjectionAdapter(tranjectionList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void OnFailed(int error, int id) {
        Log.e(TAG, "OnFailed: "+error);
        progressBar.setVisibility(View.GONE);
        switch (error) {
            case 000:
                showSnackbar(viewpart, getResources().getString(R.string.network_poor));
                break;
            case 500:
                showSnackbar(viewpart, getResources().getString(R.string.error_500));
                break;
            default:
                showSnackbar(viewpart, getResources().getString(R.string.random_error));
        }

    }

    public static void startScreen(Context context)
    {
        context.startActivity(new Intent(context, Activity_AnkoinsTranjection.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
