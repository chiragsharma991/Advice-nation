package com.project.nat.advice_nation.Base;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.nat.advice_nation.Adapter.SubcategoryAdapter;
import com.project.nat.advice_nation.R;

import java.util.ArrayList;
import java.util.Arrays;

import static com.project.nat.advice_nation.R.id.versioncode;

/**
 * Created by Chari on 7/9/2017.
 */

public class SubcategoryActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private RelativeLayout btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory);
        Initi();
        checkstatusbar();


    }

    private static String[] data = new String[]
    {
            "Apple", "Ball", "Camera", "Day", "Egg", "Foo", "Google", "Hello", "Iron", "Japan"
    };

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
        btnBack = (RelativeLayout) findViewById(R.id.imageBtnBack);
        recyclerView = (RecyclerView) findViewById(R.id.subrecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        SubcategoryAdapter adapter = new SubcategoryAdapter(this, new ArrayList<>(Arrays.asList(data)));
        recyclerView.setAdapter(adapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public static void startScreen(Context context)
    {
        context.startActivity(new Intent(context, SubcategoryActivity.class));
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
       // overridePendingTransition(R.anim.frist_to_second, R.anim.second_to_frist);
    }
}
