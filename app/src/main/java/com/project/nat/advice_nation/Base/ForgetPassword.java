package com.project.nat.advice_nation.Base;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.project.nat.advice_nation.Https.ApiResponse;
import com.project.nat.advice_nation.Https.AppController;
import com.project.nat.advice_nation.Https.PostApi;
import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.utils.BaseActivity;
import com.project.nat.advice_nation.utils.NetworkUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Chari on 8/5/2017.
 */

public class ForgetPassword extends BaseActivity implements ApiResponse
{


    private EditText email;
    private View viewpart;
    private Context context;
    private String TAG="ForgetPassword";
    private ProgressBar progressBarToolbar;
    private PostApi postApi;
    private boolean isSuccess;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setupSlideWindowAnimations(Gravity.BOTTOM,Gravity.BOTTOM);
        setContentView(R.layout.activity_forgetpasswordnew);
        context= this;
        checkstatusbar();
        initialise();
        initview();
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
        viewpart = findViewById(android.R.id.content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Forget Password");
        toolbar.setNavigationIcon(R.drawable.ic_left_black_24dp);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

                // what do you want here
            }
        });
    }

    private void initview()
    {
        email = (EditText) findViewById(R.id.email);
    }

    public static void startScreen(Context context)
    {
        context.startActivity(new Intent(context, ForgetPassword.class));
    }

    public void onSubmit(View view){
        if(TextUtils.isEmpty(email.getText().toString())){
            showSnackbar(viewpart, getResources().getString(R.string.correct_email));
        }else{
            callback(0);
        }
    }



    private void callback(int id) {
        switch (id) {
            case 0:
                String URLREG = NetworkUrl.URL_FORGET;
                String apiTag_REG = NetworkUrl.URL_FORGET;
                JSONObject jsonObject1 = GetsignUpObject();
                Log.e(TAG, "callback: json" + jsonObject1.toString());
                postApi = new PostApi(context, URLREG, jsonObject1, apiTag_REG, TAG ,0);
                break;

            default:
                break;
        }
    }


    private JSONObject GetsignUpObject()
    {

        JSONObject jobject =new JSONObject();
        try {
            jobject.put("userName",email.getText().toString().replaceAll("\\s{2,}", " ").trim());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jobject;
    }



    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            //finishAfterTransition();
            finish();
        }else
            {
            finish();
        }
    }

    @Override
    public void OnSucess(JSONObject response, int id)
    {
       // showSnackbar(viewpart, response.toString());
        Log.e(TAG, "OnSucess: "+id+"  "+ response);
        switch (id)
        {
            case 0:
                showToast("Congrats!\nYour password has been sent in your email id", context);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                }, 500);
        }

    }

    @Override
    public void OnFailed(int error,int id) {

      //  showSnackbar(viewpart, String.valueOf(error));
        Log.e(TAG, "OnFailed: " );
        switch (error) {
            case 404:
                showSnackbar(viewpart, getResources().getString(R.string.error_404));
                break;
            case 403:
                showSnackbar(viewpart, getResources().getString(R.string.error_403));
                break;
            case 401:
                showSnackbar(viewpart, getResources().getString(R.string.error_401));
                break;
            case 000:
                showSnackbar(viewpart, getResources().getString(R.string.network_poor));
                break;
            case 409:
                showSnackbar(viewpart, getResources().getString(R.string.error_404_reg));
                break;
            default:
                showSnackbar(viewpart, getResources().getString(R.string.error_404));

        }
    }



}
