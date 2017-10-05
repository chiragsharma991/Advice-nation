package com.project.nat.advice_nation.Base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.nat.advice_nation.R;

public class About extends AppCompatActivity {

    private RelativeLayout btnBack;
    private About context;
    private TextView versioncode,version_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        context = this;
        initialise();
        checkstatusbar();


        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String version = pInfo.versionName;
        int version_code = pInfo.versionCode;
        versioncode.setText("Version Name "+version);
        version_name.setText("Version Code "+version_code);

 /*       btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
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

    private void initialise() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("About");
        toolbar.setNavigationIcon(R.drawable.ic_left_black_24dp);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                // what do you want here
            }
        });
      //  btnBack = (RelativeLayout) findViewById(R.id.imageBtnBack);
        versioncode = (TextView) findViewById(R.id.versioncode);
        version_name = (TextView) findViewById(R.id.version_name);
        setUnderline();


    }

    private void setUnderline() {
        TextView email = (TextView) findViewById(R.id.email);
        TextView website = (TextView) findViewById(R.id.website);
        TextView google = (TextView) findViewById(R.id.google);
        TextView facebook = (TextView) findViewById(R.id.facebook);
        TextView linkedin = (TextView) findViewById(R.id.linkedin);
        TextView twitter = (TextView) findViewById(R.id.twitter);
        SpannableString content1 = new SpannableString("Company Email");
        content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
        email.setText(content1);
        SpannableString content2 = new SpannableString("Company Website");
        content2.setSpan(new UnderlineSpan(), 0, content2.length(), 0);
        website.setText(content2);
        SpannableString content3 = new SpannableString("Google+");
        content3.setSpan(new UnderlineSpan(), 0, content3.length(), 0);
        google.setText(content3);
        SpannableString content4 = new SpannableString("Facebook");
        content4.setSpan(new UnderlineSpan(), 0, content4.length(), 0);
        facebook.setText(content4);
        SpannableString content5 = new SpannableString("Linkedin");
        content5.setSpan(new UnderlineSpan(), 0, content5.length(), 0);
        linkedin.setText(content5);
        SpannableString content6 = new SpannableString("Twitter");
        content6.setSpan(new UnderlineSpan(), 0, content6.length(), 0);
        twitter.setText(content6);
    }

    public static void startScreen(Context context)
    {
        context.startActivity(new Intent(context, About.class));
    }
}
