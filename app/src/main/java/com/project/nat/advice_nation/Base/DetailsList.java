package com.project.nat.advice_nation.Base;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.project.nat.advice_nation.Adapter.DetailListAdapter;
import com.project.nat.advice_nation.Model.Product;
import com.project.nat.advice_nation.R;

import java.util.ArrayList;

import static com.project.nat.advice_nation.R.id.ratingBar;

public class DetailsList extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView ListView;
    private RelativeLayout btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_list);
        initialise();
        checkstatusbar();

    }

    private void initialise() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView=(RecyclerView)findViewById(R.id.detail_listview);
        btnBack = (RelativeLayout) findViewById(R.id.imageBtnBack);


        String[]time={"8:33 am","10:01 pm","03:14 pm","08:03 pm"};
        String[]title={"VR temp v1.40","Controller system","Plc-scada","Supervisory data control"};
        String[]subtitle={"Temprature controller"," Automationcontrol","Electronics","ladder programming"};
        ArrayList<Product> list=new ArrayList<>();
        int[]icon={R.mipmap.one,R.mipmap.two,R.mipmap.three,R.mipmap.four};
        for (int i = 0; i <4 ; i++) {
            Product product= new Product(time[i],title[i],subtitle[i],icon[i]);
            list.add(product);
        }

        DetailListAdapter detailListAdapter=new DetailListAdapter(list,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        ListView.setLayoutManager(mLayoutManager);
        ListView.setItemAnimator(new DefaultItemAnimator());
        ListView.setAdapter(detailListAdapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*//Rating onClick
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                Toast.makeText(getApplicationContext(),"Rating is:-"+String.valueOf(rating),Toast.LENGTH_SHORT).show();

            }
        });*/



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

    public static void startScreen(Context context){
        context.startActivity(new Intent(context, DetailsList.class));

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
     //   overridePendingTransition(R.anim.frist_to_second, R.anim.second_to_frist);
    }

    @Override
    public void onClick(View view)
    {


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
            moveActivity(new Intent(this, AddEvent.class),this, false);
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
