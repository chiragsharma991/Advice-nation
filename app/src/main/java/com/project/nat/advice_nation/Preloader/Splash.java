package com.project.nat.advice_nation.Preloader;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.WindowManager;

import com.project.nat.advice_nation.Base.DashboardActivity;
import com.project.nat.advice_nation.Base.Login;
import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.utils.AnimationUtils;
import com.project.nat.advice_nation.utils.BaseActivity;
import com.project.nat.advice_nation.utils.CircleProgressBar;

public class Splash extends BaseActivity {

    private CircleProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    //comit from surya


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        intialize();
        fullScreen();
        functionality();


    }

    private void functionality() {

        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 5 seconds
                    sleep(3 * 1000);
                    showProgress(false);
                    if (sharedPreferences.getLong("id", 0) != 0)
                        DashboardActivity.startScreen(Splash.this);
                    else Login.startScreen(Splash.this);
                    finish();


                } catch (Exception e) {
                }
            }
        };
        // start thread
        background.start();
    }


    private void intialize() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        progressBar = (CircleProgressBar) findViewById(R.id.progressBar);
        showProgress(true);


    }

    private void showProgress(boolean b) {
        if (b) {

            AnimationUtils.animateScaleOut(progressBar);
        } else {

            AnimationUtils.animateScaleIn(progressBar);
        }
    }


    private void fullScreen() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}
