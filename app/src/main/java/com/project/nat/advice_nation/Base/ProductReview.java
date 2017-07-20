package com.project.nat.advice_nation.Base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RatingBar;

import com.project.nat.advice_nation.Adapter.ProductReviewAdapter;
import com.project.nat.advice_nation.R;

import java.util.ArrayList;
import java.util.Arrays;

import static com.project.nat.advice_nation.R.id.collapsingToolbar;

/**
 * Created by Surya Chundawat on 7/14/2017.
 */

public class ProductReview extends AppCompatActivity {

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        checkstatusbar();

        username = new String[]{"Surya", "Narayan", "Singh", "Chndawat"};
        TextComment = new String[]{"This product is good",
                "This product is not initially good ",
                "Nice and this is with in the budgut",
                "Not gud not bad"
        };
        Initialise();

  /*      FeatureRatingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                FeatureRating = String.valueOf(ratingBar.getRating());
                Toast.makeText(getApplicationContext(), "This is Overall rating" + FeatureRating, Toast.LENGTH_SHORT).show();
            }
        });*/
      /*  LayerDrawable featurestars = (LayerDrawable)  FeatureRatingbar.getProgressDrawable();
        featurestars.getDrawable(2).setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
        featurestars.getDrawable(0).setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
        featurestars.getDrawable(1).setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
        LayerDrawable overallstar = (LayerDrawable)  OverallratingBar.getProgressDrawable();
        overallstar.getDrawable(2).setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
        overallstar.getDrawable(0).setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
        overallstar.getDrawable(1).setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);*/


   /*     OverallratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                RatingValue = String.valueOf(ratingBar.getRating());
                Toast.makeText(getApplicationContext(), "This is Overall rating" + RatingValue, Toast.LENGTH_SHORT).show();
            }
        });
*/

    }

    private void Initialise() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_left_black_24dp);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                // what do you want here
            }
        });

     //   OverallratingBar = (RatingBar) findViewById(R.id.ratingOverall);
     //   FeatureRatingbar = (RatingBar) findViewById(R.id.featurerating);




        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        adapter = new ProductReviewAdapter(new ArrayList<>(Arrays.asList(username)), new ArrayList<>(Arrays.asList(TextComment)), this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public static void startScreen(Context context) {
        context.startActivity(new Intent(context, ProductReview.class));

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
}

