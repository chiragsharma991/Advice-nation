package com.project.nat.advice_nation.Base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.muddzdev.styleabletoastlibrary.Utils;
import com.project.nat.advice_nation.Adapter.ProductReviewAdapter;
import com.project.nat.advice_nation.Adapter.SubcategoryAdapter;
import com.project.nat.advice_nation.Https.ApiResponse;
import com.project.nat.advice_nation.Https.GetApi;
import com.project.nat.advice_nation.Https.PostApi;
import com.project.nat.advice_nation.Https.PostApiPlues;
import com.project.nat.advice_nation.Model.Category;
import com.project.nat.advice_nation.Model.Subcategory;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.RecylerViewClick.RecyclerItemClickListener;
import com.project.nat.advice_nation.utils.BaseActivity;
import com.project.nat.advice_nation.utils.DialogUtils;
import com.project.nat.advice_nation.utils.NetworkUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
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
    ProductReviewAdapter adapter=null;
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
    private ImageView product_image;
    private View viewpart;
    private SharedPreferences sharedPreferences;
    private ArrayList<Category> productList;
    private TextView product_name,product_desc,product_price;
    private int position ,productId,productSubCategoryId;  // pre params
    private ArrayList<Subcategory> list;
    private String comments=null;
    private int rating=0;
    private Activity ProductReview=this;
    private String totalcoins;
    private long follow_userId=0;
    private int[] allFeatureRate;

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
        product_name =(TextView)findViewById(R.id.product_name);
        product_desc =(TextView)findViewById(R.id.product_desc);
        product_price =(TextView)findViewById(R.id.product_price);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        product_image = (ImageView) findViewById(R.id.product_image);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#24b89e"), PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.GONE);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        totalcoins = sharedPreferences.getString("totalcoins","");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        toolbar.setNavigationIcon(R.drawable.ic_left_black_24dp);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                // what do you want here
            }
        });//
        Type type=new TypeToken<List<Subcategory>>(){}.getType();
        String productList = getIntent().getStringExtra("ProductList");
        position = getIntent().getIntExtra("position",0); //previous click position
        list = gson.fromJson(productList, type);          // list of pre activity.
        Log.e(TAG, "list size: "+list.get(0).getData().size()+" "+position );
        productId= (int) list.get(0).getData().get(position).getId();
        productSubCategoryId= (int) list.get(0).getData().get(position).getProductSubCategoryId();
        setView(list,position);
        if (isOnline(context)) {
            progressBar.setVisibility(View.VISIBLE);
            callback(0,productSubCategoryId,productId);//0 is responseCode for login api
        } else {
            showSnackbar(viewpart, getResources().getString(R.string.network_notfound));
        }

    }

    public void onBuy(View view){
        totalcoins = sharedPreferences.getString("totalcoins","");
        String price="\u20B9"+""+ (int)list.get(0).getData().get(position).getPrice();
        int deduction=Integer.parseInt(totalcoins.trim())-(int)list.get(0).getData().get(position).getPrice()  ;
        Log.e(TAG, "onBuy: deduction"+deduction );
        if(deduction <0){
         new DialogUtils(context).alert(getResources().getString(R.string.enough_koins),context);
            return;
        }
        String calculateAmount="Remaining Ammount: "+totalcoins+"-"+(int)list.get(0).getData().get(position).getPrice()+"="+deduction;
        Log.e(TAG, "calculateAmount: "+calculateAmount );
                new DialogUtils(context, new DialogUtils.dialogResponse() {
            @Override
            public void positive(String data ,int rate ,int...featureRate) {
                if (isOnline(context)) {
                    callback(1,productSubCategoryId,productId);//
                } else {
                    showSnackbar(viewpart, getResources().getString(R.string.network_notfound));
                }
            }

            @Override
            public void negative() {
            }
        }).showAlertDialog("Item price: "+price,calculateAmount+"\n\nAre you sure to buy",true,context);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: " );
        if (isOnline(context)) {
            callback(5,0,0);// get total coins
        }
    }



    public void onReview(View view){
        if (isOnline(context)) {
            progressBar.setVisibility(View.VISIBLE);
            callback(6,productSubCategoryId,productId);// get feature rating
        } else {
            showSnackbar(viewpart, getResources().getString(R.string.network_notfound));
        }

    }

    /**
     *
     * @param list all list of pre activity.
     * @param position click position of previous activity.
     */
    private void setView(List<Subcategory> list, int position) {
        product_name.setText(list.get(0).getData().get(position).getProductName());
        product_desc.setText(list.get(0).getData().get(position).getDescription());
        product_price.setText("\u20B9" + (int)list.get(0).getData().get(position).getPrice());
        String image = NetworkUrl.URL_GET_IMAGE
                +list.get(0).getData().get(position).getUserId() + "/" +
                +list.get(0).getData().get(position).getProductSubCategoryId() + "/product/" +
                +list.get(0).getData().get(position).getId() + "?size=0x0&highquality=false";
        Glide
                .with(context)
                .load(image)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .crossFade()
                .into(product_image);
        setRate();
    }

    private void setRate() {
       // RatingBar ratingOverall = (RatingBar) findViewById(R.id.ratingOverall);
        RatingBar featurerating = (RatingBar) findViewById(R.id.featurerating);
      /*  LayerDrawable overall = (LayerDrawable)ratingOverall.getProgressDrawable();
        overall.getDrawable(2).setColorFilter(Color.parseColor("#24b89e"), PorterDuff.Mode.SRC_ATOP);
        overall.getDrawable(0).setColorFilter(Color.parseColor("#dfdedf"), PorterDuff.Mode.SRC_ATOP);
        overall.getDrawable(1).setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);*/
        LayerDrawable feature = (LayerDrawable)featurerating.getProgressDrawable();
        feature.getDrawable(2).setColorFilter(Color.parseColor("#24b89e"), PorterDuff.Mode.SRC_ATOP);
        feature.getDrawable(0).setColorFilter(Color.parseColor("#dfdedf"), PorterDuff.Mode.SRC_ATOP);
        feature.getDrawable(1).setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
        featurerating.setRating(list.get(0).getData().get(position).getProductRating());
    }

    public static void startScreen(Context context,String productList, int position) {

        context.startActivity(new Intent(context, ProductReview.class).putExtra("ProductList",productList)
                .putExtra("position",position));

    }


    private void callback(int responseCode, int productSubCategoryId, int productId ,int...values) {
        switch (responseCode) {
            case 0:
                long user = sharedPreferences.getLong("id", 0);
                String bearerToken = sharedPreferences.getString("bearerToken", "");
                String URL = NetworkUrl.URL_COMMENTLIST + user + "/productSubCategory/" + productSubCategoryId+"/product/"+productId+"/comments";
                String apiTag = URL;
                GetApi getApi = new GetApi(context, URL, bearerToken, apiTag, TAG, 0);
                break;
            case 1:
                user = sharedPreferences.getLong("id", 0);
                bearerToken = sharedPreferences.getString("bearerToken", "");
                URL = NetworkUrl.URL + user + "/productSubCategory/" + productSubCategoryId+"/product/"+productId+"/buy";
                apiTag =URL;
                PostApiPlues postApi = new PostApiPlues(context, URL,bearerToken, null, apiTag, TAG, 1);

                break;
            case 2:  // update comments
                user = sharedPreferences.getLong("id", 0);
                bearerToken = sharedPreferences.getString("bearerToken", "");
                try {
                    comments = URLEncoder.encode(comments,"UTF-8");
                    Log.e(TAG, "comments: "+comments);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.e(TAG, "callback: catch error"+e.getMessage() );
                }
                URL = NetworkUrl.URL + user + "/productSubCategory/" + productSubCategoryId+"/product/"+productId+"/comments?"+"comment="+comments;
                apiTag = URL;
                postApi = new PostApiPlues(context, URL,bearerToken, null, apiTag, TAG, 2);
                break;
            case 3:  // follow comments
                user = sharedPreferences.getLong("id", 0);
                bearerToken = sharedPreferences.getString("bearerToken", "");
                URL = NetworkUrl.URL + user+"/"+productId+"/"+values[0]+"/"+"follow/"+follow_userId;
                apiTag = URL;
                postApi = new PostApiPlues(context, URL,bearerToken, null, apiTag, TAG, 3);
                break;
            case 4:  // update rating
                user = sharedPreferences.getLong("id", 0);
                bearerToken = sharedPreferences.getString("bearerToken", "");
                try {
                    comments = URLEncoder.encode(comments,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //http://ec2-13-126-97-168.ap-south-1.compute.amazonaws.com:8080/AdviseNation/api/users/17041409/productSubCategory/1/product/3/features?rating1=3&rating2=2&rating3=4&rating4=5&rating5=4&rating6=3
                URL = NetworkUrl.URL + user + "/productSubCategory/" + productSubCategoryId+"/product/"+productId+"/rating?"+"rating="+rating;
                apiTag = URL;
                postApi = new PostApiPlues(context, URL,bearerToken, null, apiTag, TAG, 100);  // default response

                // call for feature rate
                URL = NetworkUrl.URL + user + "/productSubCategory/" + productSubCategoryId+"/product/"+productId+"/features?"+
                        "rating1="+allFeatureRate[0]+"&rating2="+allFeatureRate[1]+"&rating3="+allFeatureRate[2]+"&rating4="+allFeatureRate[3]+"&rating5="+allFeatureRate[4]+"&rating6="+allFeatureRate[5];
                apiTag = URL;
                postApi = new PostApiPlues(context, URL,bearerToken, null, apiTag, TAG, 100);  // default response
                break;

            case 5:  // Get info about the total ankoins
                user = sharedPreferences.getLong("id", 0);
                bearerToken = sharedPreferences.getString("bearerToken", "");
                URL = NetworkUrl.URL_GET_USER_ANKOINS + user + "/ankoin";
                apiTag = URL;
                getApi = new GetApi(context, URL, bearerToken, apiTag, TAG, 5);
                break;
            case 6:  // Get feature rating
                user = sharedPreferences.getLong("id", 0);
                bearerToken = sharedPreferences.getString("bearerToken", "");
          //http://ec2-13-126-97-168.ap-south-1.compute.amazonaws.com:8080/AdviseNation/api/users/17041409/productSubCategory/1/product/3/features
                URL = NetworkUrl.URL + user + "/productSubCategory/" + productSubCategoryId+"/product/"+productId+"/features";
                apiTag = URL;
                getApi = new GetApi(context, URL, bearerToken, apiTag, TAG, 6);
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
        Log.e(TAG, "OnSucess: id"+id+" = "+response.toString() );
        progressBar.setVisibility(View.GONE);
        switch (id) {
            case 0:
                if(adapter ==null){
                    productList = new ArrayList<Category>();
                    Category category = gson.fromJson(response.toString(), Category.class);
                    productList.add(category);
                    progressBar.setVisibility(View.GONE);
                    setview();
                }else{
                    productList.clear();
                    Category category = gson.fromJson(response.toString(), Category.class);
                    productList.add(category);
                    adapter.notifyDataSetChanged();
                }
                break;
            case 1:
                DialogUtils.showAlertDialog(context,"Thanking you !","Product buy successfully");
                onResume();
                break;
            case 2:
                customToast(getResources().getString(R.string.submit_rate),context,R.drawable.done,R.color.color_success,false);
                callback(4,productSubCategoryId,productId);// submit rating
                break;
            case 3:
                callback(0,productSubCategoryId,productId);//0 is responseCode for login api
                break;
            case 5:
                try {
                    String totalcoins=response.getString("data");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("totalcoins",totalcoins);
                    editor.apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 6:

                try {
                    String[]featurerate=new String[6];
                    String feature1=response.getJSONObject("data").getString("feature1");
                    featurerate[0]=feature1;
                    String feature2=response.getJSONObject("data").getString("feature2");
                    featurerate[1]=feature2;
                    String feature3=response.getJSONObject("data").getString("feature3");
                    featurerate[2]=feature3;
                    String feature4=response.getJSONObject("data").getString("feature4");
                    featurerate[3]=feature4;
                    String feature5=response.getJSONObject("data").getString("feature5");
                    featurerate[4]=feature5;
                    String feature6=response.getJSONObject("data").getString("feature6");
                    featurerate[5]=feature6;
                    new DialogUtils.CustomDialog(context,featurerate, new DialogUtils.dialogResponse() {

                        @Override
                        public void positive(String data , int productRate,int...featureRate) {
                            if (isOnline(context)) {
                                comments=data;
                                allFeatureRate=featureRate;
                                rating=productRate;
                                callback(2,productSubCategoryId,productId);// submit comment
                            } else {
                                progressBar.setVisibility(View.GONE);
                                showSnackbar(viewpart, getResources().getString(R.string.network_notfound));
                            }
                        }

                        @Override
                        public void negative() {
                            progressBar.setVisibility(View.GONE);
                        }
                    }).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                progressBar.setVisibility(View.GONE);
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
            case 404:
                showSnackbarError(viewpart,getResources().getString(R.string.error_404_unfollow));
                break;
            case 412:  //product buy already && insufficient amount
                DialogUtils.showAlertDialog(context,"We are sorry","Product already sold");
                break;
            case 409:  // error for multiple review
                break;
            default:
                showSnackbar(viewpart, getResources().getString(R.string.random_error));
        }

    }

    private void setview() {
            adapter = new ProductReviewAdapter(productList,ProductReview,this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
    }

    public void follow(int position){

        if (isOnline(context)) {
            int productId = productList.get(0).getData().get(position).getProductId();
            int id = (int)  productList.get(0).getData().get(position).getId();
            follow_userId = productList.get(0).getData().get(position).getUserId();
            Log.e(TAG, "follow: productId "+productId+" id "+id+ " userId "+follow_userId );
            callback(3,productSubCategoryId,productId,id);//0 is responseCode for login api

        } else {
          //  showSnackbar(viewpart, getResources().getString(R.string.network_notfound));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();

    }
}

