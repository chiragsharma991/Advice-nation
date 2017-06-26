package com.project.nat.advice_nation.Base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.utils.BaseActivity;

public class login extends BaseActivity {

    private login context;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        //getSupportActionBar().hide();
        context = this;
        intialize();
        functionality();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this,DashboardActivity.class));
                finish();
            }
        });


    }

    private void intialize()
    {
        btnLogin=(Button)findViewById(R.id.login);


    }

    private void functionality() {

    }
     

    public static void startScreen(Context context){
        context.startActivity(new Intent(context, login.class));

    }



}
