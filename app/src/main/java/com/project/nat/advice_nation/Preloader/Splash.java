package com.project.nat.advice_nation.Preloader;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.project.nat.advice_nation.Base.MainActivityN;
import com.project.nat.advice_nation.Base.login;
import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.utils.AnimationUtils;
import com.project.nat.advice_nation.utils.BaseActivity;
import com.project.nat.advice_nation.utils.CircleProgressBar;

public class Splash extends BaseActivity {

    private CircleProgressBar progressBar;

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
                    MainActivityN.startScreen(Splash.this);
                    finish();


                } catch (Exception e) {
                }
            }
        };
        // start thread
        background.start();
    }


    private void intialize() {


        progressBar = (CircleProgressBar) findViewById(R.id.progressBar);
        showProgress(true);


    }

    private void showProgress(boolean b) {
        if(b){

            AnimationUtils.animateScaleOut(progressBar);
        }else{

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
