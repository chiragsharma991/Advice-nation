package com.project.nat.advice_nation.Ankoins;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.project.nat.advice_nation.Adapter.AnkoinsTranjectionAdapter;
import com.project.nat.advice_nation.Adapter.ProductListAdapter;
import com.project.nat.advice_nation.Base.ProductList;
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

public class Activity_CurrentAnkoins extends BaseActivity implements ApiResponse{

    private TextView coins;
    private String TAG="Activity_CurrentAnkoins";
    private Context context;
    private Gson gson;
    private SharedPreferences sharedPreferences;
    private View viewpart;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__current_ankoins);
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
        context=Activity_CurrentAnkoins.this;
        gson = new Gson();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        viewpart = findViewById(android.R.id.content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        coins = (TextView) findViewById(R.id.coins);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Current Ankoins");
        toolbar.setNavigationIcon(R.drawable.ic_left_black_24dp);
        toolbar.setNavigationContentDescription("Back");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                long user = sharedPreferences.getLong("id", 0);
                String bearerToken = sharedPreferences.getString("bearerToken", "");
                String URL = NetworkUrl.URL_GET_USER_ANKOINS + user + "/ankoin";
                String apiTag = NetworkUrl.URL_GET_USER_ANKOINS + user + "/ankoin";
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
                progressBar.setVisibility(View.GONE);
                try {
                    String totalcoins=response.getString("data");
                    coins.setText("\u20B9" +" "+totalcoins);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }



    @Override
    public void OnFailed(int error, int id) {
        Log.e(TAG, "OnFailed: " + error);
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
        context.startActivity(new Intent(context, Activity_CurrentAnkoins.class));
    }
}
