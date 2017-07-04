package com.project.nat.advice_nation.Base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.project.nat.advice_nation.Model.Product;
import com.project.nat.advice_nation.R;

import java.util.ArrayList;

public class DetailsList extends AppCompatActivity {

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

        DetailListAdapter detailListAdapter=new DetailListAdapter(list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        ListView.setLayoutManager(mLayoutManager);
        ListView.setItemAnimator(new DefaultItemAnimator());
        ListView.setAdapter(detailListAdapter);


    }

    public static void startScreen(Context context){
        context.startActivity(new Intent(context, DetailsList.class));

    }


}
