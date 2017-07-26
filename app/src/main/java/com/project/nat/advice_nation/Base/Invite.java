package com.project.nat.advice_nation.Base;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.project.nat.advice_nation.R;

import static com.project.nat.advice_nation.R.id.version_name;
import static com.project.nat.advice_nation.R.id.versioncode;
import static com.project.nat.advice_nation.R.id.website;

public class Invite extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        context = this;
        initialise();
        checkstatusbar();

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
        getSupportActionBar().setTitle("Invite User");
        toolbar.setNavigationIcon(R.drawable.ic_left_black_24dp);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                // what do you want here
            }
        });

        setUnderline();


    }

    private void setUnderline() {

        TextView google = (TextView) findViewById(R.id.google);
        TextView facebook = (TextView) findViewById(R.id.facebook);
        TextView whatsapp = (TextView) findViewById(R.id.whatsapp);
        TextView twitter = (TextView) findViewById(R.id.twitter);

        SpannableString content3 = new SpannableString("Google+");
        content3.setSpan(new UnderlineSpan(), 0, content3.length(), 0);
        google.setText(content3);
        SpannableString content4 = new SpannableString("Facebook");
        content4.setSpan(new UnderlineSpan(), 0, content4.length(), 0);
        facebook.setText(content4);
        SpannableString content5 = new SpannableString("WhatsApp");
        content5.setSpan(new UnderlineSpan(), 0, content5.length(), 0);
        whatsapp.setText(content5);
        SpannableString content6 = new SpannableString("Twitter");
        content6.setSpan(new UnderlineSpan(), 0, content6.length(), 0);
        twitter.setText(content6);
    }


    public static void startScreen(Context context)
    {
        context.startActivity(new Intent(context, Invite.class));
    }
}
