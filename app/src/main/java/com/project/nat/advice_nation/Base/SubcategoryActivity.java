package com.project.nat.advice_nation.Base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.project.nat.advice_nation.Adapter.MainAdapter;
import com.project.nat.advice_nation.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Chari on 7/9/2017.
 */

public class SubcategoryActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory);
        Initi();

    }

    private static String[] data = new String[]
    {
            "Apple", "Ball", "Camera", "Day", "Egg", "Foo", "Google", "Hello", "Iron", "Japan"
    };

    private void Initi()
    {
        recyclerView = (RecyclerView) findViewById(R.id.subrecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        MainAdapter adapter = new MainAdapter(this, new ArrayList<>(Arrays.asList(data)));
        recyclerView.setAdapter(adapter);
    }

    public static void startScreen(Context context)
    {
        context.startActivity(new Intent(context, SubcategoryActivity.class));
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.frist_to_second, R.anim.second_to_frist);
    }
}
