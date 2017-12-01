package com.project.nat.advice_nation.Preloader;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;

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


    }

    private void functionality() {

        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 5 seconds
                    sleep(3 * 1000);
                    showProgress(false);
                    if (sharedPreferences.getLong("id", 0) != 0 && isOnline(Splash.this)){
                        DashboardActivity.startScreen(Splash.this);
                        finish();
                    }
                    else{
                        Login.startScreen(Splash.this);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                        finish();
                    }

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
        ImageView logo=(ImageView)findViewById(R.id.splash_logo);
        Animation animation= android.view.animation.AnimationUtils.loadAnimation(this,R.anim.zoom_out);
        logo.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                Log.e("TA", "onAnimationEnd: " );
                showProgress(true);
                functionality();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


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
