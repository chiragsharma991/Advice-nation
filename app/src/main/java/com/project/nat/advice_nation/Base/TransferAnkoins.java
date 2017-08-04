package com.project.nat.advice_nation.Base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.project.nat.advice_nation.R;

/**
 * Created by Surya Chundawat on 7/23/2017.
 */

public class TransferAnkoins  extends AppCompatActivity
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                // what do you want here
            }
        });
    }
}
