package com.project.nat.advice_nation.Base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.project.nat.advice_nation.Adapter.SubcategoryAdapter;
import com.project.nat.advice_nation.Https.ApiResponse;
import com.project.nat.advice_nation.Https.AppController;
import com.project.nat.advice_nation.Https.GetApi;
import com.project.nat.advice_nation.Https.ToAppcontroller;
import com.project.nat.advice_nation.Model.Category;
import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.utils.BaseActivity;
import com.project.nat.advice_nation.utils.NetworkUrl;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by Chari on 7/9/2017.
 */

public class SubcategoryActivity extends BaseActivity implements ToAppcontroller,ApiResponse
{
    private RecyclerView recyclerView;
    private RelativeLayout btnBack;
    private SubcategoryActivity context;
    private Gson gson;
    private SharedPreferences sharedPreferences;
    private View viewpart;
    private ProgressBar progressBar;
    private ArrayList<Category> SubcategoryList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory);
        context=this;
        checkstatusbar();
        Initi();


    }



    private void checkstatusbar() {

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

    private void Initi()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sub Category");
        toolbar.setNavigationIcon(R.drawable.ic_left_black_24dp);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        gson = new Gson();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        viewpart = findViewById(android.R.id.content);
        Intent intent=getIntent();
        int selectedID=intent.getExtras().getInt("ID");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                // what do you want here
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.subrecycler);

        if (isOnline(context)) {
            progressBar.setVisibility(View.VISIBLE);
            callback(0,selectedID);//0 is responseCode for login api
        } else {
            showSnackbar(viewpart, getResources().getString(R.string.network_notfound));
        }

    }

    private void callback(int responseCode, int SelectID) {
        switch (responseCode) {
            case 0:
                long user = sharedPreferences.getLong("id",0);
                String bearerToken = sharedPreferences.getString("bearerToken","");
                Log.e(TAG, "bearerToken: "+bearerToken );
                String URL = NetworkUrl.URL_CATEGORY + user + "/productCategory/"+SelectID;
                String apiTag = NetworkUrl.URL_CATEGORY + user + "/productCategory/"+SelectID;
                GetApi getApi = new GetApi(context, URL, bearerToken, apiTag, TAG, 0); //0 is for finish second api call
                break;

            default:
                break;


        }
    }

    public static void startScreen(Context context, int selectID)
    {
        context.startActivity(new Intent(context, SubcategoryActivity.class).putExtra("ID",selectID));
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
       // overridePendingTransition(R.anim.frist_to_second, R.anim.second_to_frist);
    }

    @Override
    public void OnSucess(JSONObject response, int id) {

        switch (id){
            case 0:
                SubcategoryList=new ArrayList<Category>();
                Category category = gson.fromJson(response.toString(), Category.class);
                SubcategoryList.add(category);
                Log.e(TAG, "category list: "+SubcategoryList.get(0).getData().size());
                progressBar.setVisibility(View.GONE);
                setview();
                break;

            default:
                break;
        }
    }

    private void setview() {

        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        SubcategoryAdapter adapter = new SubcategoryAdapter(context,SubcategoryList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void OnFailed(int error,int id) {
        Log.e(TAG, "OnFailed: "+error );
        progressBar.setVisibility(View.GONE);
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
}
