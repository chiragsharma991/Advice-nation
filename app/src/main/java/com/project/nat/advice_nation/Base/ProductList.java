package com.project.nat.advice_nation.Base;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.nat.advice_nation.Adapter.ProductListAdapter;
import com.project.nat.advice_nation.Https.ApiResponse;
import com.project.nat.advice_nation.Https.GetApi;
import com.project.nat.advice_nation.Model.Category;
import com.project.nat.advice_nation.Model.Product;
import com.project.nat.advice_nation.Model.Subcategory;
import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.utils.BaseActivity;
import com.project.nat.advice_nation.utils.Constants;
import com.project.nat.advice_nation.utils.NetworkUrl;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class ProductList extends BaseActivity implements View.OnClickListener,ApiResponse {

    private RecyclerView ListView;
    private RelativeLayout btnBack;
    private String TAG="ProductList";
    private Context context;
    private View viewpart;
    private SharedPreferences sharedPreferences;
    private ArrayList<Subcategory> productSubCategory;
    private Gson gson;
    private LinearLayout no_datafound,list_item_process;
    private TextView retry;
    private int productCategoryId,productSubCategoryId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_list);
        context=ProductList.this;
        initialise();
        checkstatusbar();

    }

    private void initialise() {
        gson = new Gson();
        productCategoryId =(int) getIntent().getLongExtra("productCategoryId",0);
        productSubCategoryId = (int)getIntent().getLongExtra("productId",0);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        viewpart = findViewById(android.R.id.content);
        retry=(TextView)findViewById(R.id.retry);
        no_datafound=(LinearLayout)findViewById(R.id.no_datafound);
        list_item_process=(LinearLayout)findViewById(R.id.list_item_process);
        no_datafound.setVisibility(View.GONE);
        list_item_process.setVisibility(View.GONE); //
        retry.setOnClickListener(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Product List");
        toolbar.setNavigationIcon(R.drawable.ic_left_black_24dp);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                // what do you want here
            }
        });
        ListView=(RecyclerView)findViewById(R.id.detail_listview);

        if (isOnline(context)) {
            list_item_process.setVisibility(View.VISIBLE);
            callback(0,productSubCategoryId);//0 is responseCode for login api
        } else {
            showSnackbar(viewpart, getResources().getString(R.string.network_notfound));
        }




    }

    private void callback(int responseCode, int productSubCategoryId) {
        switch (responseCode) {
            case 0:
                long user = sharedPreferences.getLong("id", 0);
                String bearerToken = sharedPreferences.getString("bearerToken", "");
                Log.e(TAG, "bearerToken: " + bearerToken);
                String URL = NetworkUrl.URL_CATEGORY + user + "/productSubCategory/" + productSubCategoryId+"/product";
                String apiTag = NetworkUrl.URL_CATEGORY + user + "/productSubCategory/" + productSubCategoryId+"/product";
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
                //Log.e(TAG, "OnSucess: "+response.toString() );
                productSubCategory = new ArrayList<Subcategory>();
                Subcategory Subcategory = gson.fromJson(response.toString(), Subcategory.class);
                productSubCategory.add(Subcategory);
                Log.e(TAG, "productSubCategory size is"+productSubCategory.get(0).getData().size() );
                list_item_process.setVisibility(View.GONE);
                if(productSubCategory.get(0).getData().size() > 0){
                    setview();
                }else{
                    no_datafound.setVisibility(View.VISIBLE);
                }
                break;

            default:
                list_item_process.setVisibility(View.GONE);
                break;
        }
    }



    @Override
    public void OnFailed(int error, int id) {
        Log.e(TAG, "OnFailed: " + error);
     //   progressBar.setVisibility(View.GONE);
        switch (error) {
            case 000:
                list_item_process.setVisibility(View.GONE);
                showSnackbar(viewpart, getResources().getString(R.string.network_poor));
                break;
            case 500:
                list_item_process.setVisibility(View.GONE);
                showSnackbar(viewpart, getResources().getString(R.string.error_500));
                break;
            default:
                list_item_process.setVisibility(View.GONE);
                showSnackbar(viewpart, getResources().getString(R.string.random_error));
        }

    }

    private void setview() {

        ProductListAdapter detailListAdapter=new ProductListAdapter(productSubCategory,context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        ListView.setLayoutManager(mLayoutManager);
        ListView.setItemAnimator(new DefaultItemAnimator());
        ListView.setAdapter(detailListAdapter);

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

    public static void startScreen(Context context,long productSubCategoryId, long productId ){
        context.startActivity(new Intent(context, ProductList.class).putExtra("productCategoryId",productSubCategoryId).putExtra("productId",productId));

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
        //   overridePendingTransition(R.anim.frist_to_second, R.anim.second_to_frist);
    }

    @Override
    public void onClick(View view)
    {
        if (view == retry){
            if (isOnline(context)) {
                list_item_process.setVisibility(View.VISIBLE);
                no_datafound.setVisibility(View.GONE);
                callback(0, productSubCategoryId);//0 is responseCode for login api
            } else {
                showSnackbar(viewpart, getResources().getString(R.string.network_notfound));
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id ==R.id.selected){
            moveActivity(new Intent(this, AddEvent.class).putExtra("productSubCategoryId",productSubCategoryId),this, false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    protected void moveActivity(Intent intent, Activity context, boolean isFinish){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation(context).toBundle());
            } else {
                startActivity(intent);
            }
            if(isFinish)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 500);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
