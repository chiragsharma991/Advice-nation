package com.project.nat.advice_nation.Base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.nat.advice_nation.Adapter.ProductReviewAdapter;
import com.project.nat.advice_nation.Adapter.SubcategoryAdapter;
import com.project.nat.advice_nation.Https.ApiResponse;
import com.project.nat.advice_nation.Https.GetApi;
import com.project.nat.advice_nation.Model.Category;
import com.project.nat.advice_nation.Model.Subcategory;
import android.content.SharedPreferences;
import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.RecylerViewClick.RecyclerItemClickListener;
import com.project.nat.advice_nation.utils.BaseActivity;
import com.project.nat.advice_nation.utils.NetworkUrl;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.project.nat.advice_nation.R.id.collapsingToolbar;

/**
 * Created by Surya Chundawat on 7/14/2017.
 */

public class ProductReview extends BaseActivity implements ApiResponse {

    private RecyclerView recyclerView;
    ProductReviewAdapter adapter;
    private RatingBar OverallratingBar;
    private RatingBar FeatureRatingbar;
    //public MoviesAdapter moviesAdapter;
    String[] username;
    String[] TextComment;
    private String RatingValue;
    private String FeatureRating;
    private String TAG="ProductReview";
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Gson gson;
    private Context context;
    private ProgressBar progressBar;
    private View viewpart;
    private SharedPreferences sharedPreferences;
    private ArrayList<Category> productList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        context=this;
        checkstatusbar();
        Initialise();
        
    }

    private void Initialise() {
        gson=new Gson();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        viewpart = findViewById(android.R.id.content);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#24b89e"), PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.GONE);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        toolbar.setNavigationIcon(R.drawable.ic_left_black_24dp);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                // what do you want here
            }
        });//
        String productList = getIntent().getStringExtra("ProductList");
        int position = getIntent().getIntExtra("position",0);
        Type type=new TypeToken<List<Subcategory>>(){}.getType();
        List<Subcategory> list = gson.fromJson(productList, type);
        Log.e(TAG, "list size: "+list.get(0).getData().size()+" "+position );
        int productId= (int) list.get(0).getData().get(position).getId();
        int productSubCategoryId= (int) list.get(0).getData().get(position).getProductSubCategoryId();

        if (isOnline(context)) {
            progressBar.setVisibility(View.VISIBLE);
            callback(0,productSubCategoryId,productId);//0 is responseCode for login api
        } else {
            showSnackbar(viewpart, getResources().getString(R.string.network_notfound));
        }

    }

    public static void startScreen(Context context,String productList, int position) {
        context.startActivity(new Intent(context, ProductReview.class).putExtra("ProductList",productList)
                .putExtra("position",position));

    }


    private void callback(int responseCode, int productSubCategoryId, int productId) {
        switch (responseCode) {
            case 0:
                long user = sharedPreferences.getLong("id", 0);
                String bearerToken = sharedPreferences.getString("bearerToken", "");
               // Log.e(TAG, "bearerToken: " + bearerToken);
                //    //http://ec2-13-126-97-168.ap-south-1.compute.amazonaws.com:8080/AdviseNation/api/users/2563260983/productSubCategory/2/product/1/comments
                String URL = NetworkUrl.URL_COMMENTLIST + user + "/productSubCategory/" + productSubCategoryId+"/product/"+productId+"/comments";
                String apiTag = NetworkUrl.URL_COMMENTLIST + user + "/productSubCategory/" + productSubCategoryId+"/product/"+productId+"/comments";
                GetApi getApi = new GetApi(context, URL, bearerToken, apiTag, TAG, 0); //0 is for finish second api call
                break;

            default:
                break;


        }
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

    @Override
    public void OnSucess(JSONObject response, int id) {
        switch (id) {
            case 0:
                Log.e(TAG, "OnSucess: "+response.toString() );
                productList = new ArrayList<Category>();
                Category category = gson.fromJson(response.toString(), Category.class);
                productList.add(category);
                progressBar.setVisibility(View.GONE);
                setview();
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

    private void setview() {

        adapter = new ProductReviewAdapter(productList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
  
    }
}

