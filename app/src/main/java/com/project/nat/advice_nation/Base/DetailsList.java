package com.project.nat.advice_nation.Base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.project.nat.advice_nation.Adapter.DetailListAdapter;
import com.project.nat.advice_nation.Model.Product;
import com.project.nat.advice_nation.R;

import java.util.ArrayList;

import static com.project.nat.advice_nation.R.id.ratingBar;

public class DetailsList extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView ListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_list);
        initialise();

    }

    private void initialise() {
        ListView=(RecyclerView)findViewById(R.id.detail_listview);

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

        /*//Rating onClick
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                Toast.makeText(getApplicationContext(),"Rating is:-"+String.valueOf(rating),Toast.LENGTH_SHORT).show();

            }
        });*/


    }

    public static void startScreen(Context context){
        context.startActivity(new Intent(context, DetailsList.class));

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.frist_to_second, R.anim.second_to_frist);
    }

    @Override
    public void onClick(View view)
    {


    }
}
