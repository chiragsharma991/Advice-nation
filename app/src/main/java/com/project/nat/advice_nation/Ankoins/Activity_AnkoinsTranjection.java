package com.project.nat.advice_nation.Ankoins;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.project.nat.advice_nation.Adapter.AnkoinsTranjectionAdapter;
import com.project.nat.advice_nation.Adapter.ProductReviewAdapter;
import com.project.nat.advice_nation.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Chari on 7/23/2017.
 */

public class Activity_AnkoinsTranjection extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private String[] Array_TranjectionID,Array_DateOfTranjection,Array_AnkoinsSpents,Array_Purpose;
    private AnkoinsTranjectionAdapter adapter;


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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tranjection History");
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


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        adapter = new AnkoinsTranjectionAdapter(new ArrayList<>(Arrays.asList(Array_TranjectionID)), new ArrayList<>(Arrays.asList(Array_DateOfTranjection)),
                new ArrayList<>(Arrays.asList(Array_AnkoinsSpents)),new ArrayList<>(Arrays.asList(Array_Purpose)), this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    public static void startScreen(Context context)
    {
        context.startActivity(new Intent(context, Activity_AnkoinsTranjection.class));
    }
}
