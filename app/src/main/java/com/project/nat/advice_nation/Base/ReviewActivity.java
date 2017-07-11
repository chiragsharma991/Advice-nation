package com.project.nat.advice_nation.Base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.project.nat.advice_nation.Adapter.ReviewAdapter;
import com.project.nat.advice_nation.Model.Product;
import com.project.nat.advice_nation.Model.ReviewItem;
import com.project.nat.advice_nation.R;

import java.util.ArrayList;
import java.util.Arrays;

import static android.R.id.list;

/**
 * Created by Chari on 7/10/2017.
 */

public class ReviewActivity extends AppCompatActivity
{

    private RecyclerView recyclerView;
    private ReviewAdapter myadapter;
    String [] username;
    String [] TextComment;
    private ArrayList<ReviewItem> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        username = new String[]{"Surya", "Narayan", "Singh", "Chndawat"};
        TextComment = new String[]{"This product is good",
                "This product is not initially good ",
                "Nice and this is with in the budgut",
                "Not gud not bad"
        };

        list=new ArrayList<>();
        for (int i = 0; i <4 ; i++) {
            ReviewItem reviewItem= new ReviewItem(username[i],TextComment[i]);
            list.add(reviewItem);
        }

        Inti();


    }

    private void Inti()
    {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        myadapter = new ReviewAdapter(list,this);
        recyclerView.setAdapter(myadapter);
    }

    public static void startScreen(Context context){
        context.startActivity(new Intent(context, ReviewActivity.class));

    }
}
