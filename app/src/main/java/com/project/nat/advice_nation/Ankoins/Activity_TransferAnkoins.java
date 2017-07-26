package com.project.nat.advice_nation.Ankoins;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.project.nat.advice_nation.R;

/**
 * Created by Chari on 7/23/2017.
 */

public class Activity_TransferAnkoins extends AppCompatActivity
{
    private EditText edt_ankoinstotrnsfer,edt_emailtowhomsend,edt_remaing;
    private Button btn_submit;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferankoins);
        initview();
        checkstatusbar();

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

    private void initview()
    {
        edt_ankoinstotrnsfer = (EditText) findViewById(R.id.edtankointotrnsfer);
        edt_emailtowhomsend = (EditText) findViewById(R.id.edtankoinwhomtotrnsfer);
        edt_remaing = (EditText) findViewById(R.id.edtankoinRemaing);

        btn_submit =(Button) findViewById(R.id.btnconfirmankoins);

        toolbar= (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Transfer Ankoins");
        toolbar.setNavigationIcon(R.drawable.ic_left_black_24dp);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
                // what do you want here
            }
        });
    }

    public static void startScreen(Context context)
    {
        context.startActivity(new Intent(context, Activity_TransferAnkoins.class));
    }
}
