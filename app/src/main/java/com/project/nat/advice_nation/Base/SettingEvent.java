package com.project.nat.advice_nation.Base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.project.nat.advice_nation.Https.ApiResponse;
import com.project.nat.advice_nation.Https.GetApi;
import com.project.nat.advice_nation.Https.PostApiPlues;
import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.utils.BaseActivity;
import com.project.nat.advice_nation.utils.DialogUtils;
import com.project.nat.advice_nation.utils.NetworkUrl;

import org.json.JSONException;
import org.json.JSONObject;


public class SettingEvent extends BaseActivity implements ApiResponse
{
    private EditText edt_current_password,edt_previous_password;
    private Button btn_submit;
    private String TAG=this.getClass().getSimpleName();
    private Toolbar toolbar;
    private Gson gson;
    private SharedPreferences sharedPreferences;
    private Context context;
    private View viewpart;
    private String current ,previous;
    private String totalcoins;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_event);
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
        context=this;
        gson = new Gson();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        totalcoins = sharedPreferences.getString("totalcoins","");
        edt_current_password = (EditText) findViewById(R.id.set_current_password);
        edt_previous_password = (EditText) findViewById(R.id.set_previous_password);
        toolbar= (Toolbar)findViewById(R.id.toolbar);
        viewpart = findViewById(android.R.id.content);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change password");
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

    public void onSubmit(View view){
        checkValidation();
    }



    private boolean checkValidation() {

        current = edt_current_password.getText().toString().replaceAll("\\s{2,}", " ").trim();
        previous = edt_previous_password.getText().toString().replaceAll("\\s{2,}", " ").trim();

        if (current == null || current.isEmpty() || current.equals("null")) {
            showSnackbar(viewpart, "Please enter your current password");
            return false;
        } else if ( previous == null || previous.isEmpty() || previous.equals("null")) {
            showSnackbar(viewpart, "Please enter your previous password");
            return false;
        }else{
            if (isOnline(context)) {
                progressDialogStart(context,"Loading...");
                callback(0);// change password
            } else {
                showSnackbar(viewpart, getResources().getString(R.string.network_notfound));
            }
        }

        return true;
    }

    private void callback(int responseCode) {
        switch (responseCode) {
            case 0:
                long user = sharedPreferences.getLong("id", 0);
                String bearerToken = sharedPreferences.getString("bearerToken", "");
                JSONObject jsonObject = GetLoginObject();
                Log.e(TAG, "jsonObject: "+jsonObject.toString() );
                String URL = NetworkUrl.URL + user + "/changePassword";
                String apiTag = URL;
                PostApiPlues postApi = new PostApiPlues(context, URL, bearerToken, jsonObject, apiTag, TAG, 0);
                break;
            default:
                break;


        }
    }

    @Override
    public void OnSucess(JSONObject response, int id) {

        Log.e(TAG, "OnSucess: "+response );
        switch (id) {
            case 0:
                progressDialogStop();
                customToast("Password update success",context,R.drawable.done,R.color.color_success,false);
                edt_current_password.getText().clear();
                edt_previous_password.getText().clear();
                edt_current_password.clearFocus();
                break;
            default:
                break;
        }
    }



    @Override
    public void OnFailed(int error, int id) {
        Log.e(TAG, "OnFailed: " + error);
        progressDialogStop();
        switch (error) {
            case 000:
                showSnackbar(viewpart, getResources().getString(R.string.network_poor));
                break;
            case 500:
                showSnackbar(viewpart, getResources().getString(R.string.error_500));
                break;
            case 400:
                showSnackbarError(viewpart, getResources().getString(R.string.error_400));
                break;
            default:
                showSnackbar(viewpart, getResources().getString(R.string.random_error));
        }

    }


    public JSONObject GetLoginObject() {


       /* {
            "newSecret": "test123",
                "secret": "test12"
        }*/
        JSONObject jobject = new JSONObject();
        try {
            jobject.put("newSecret",current.trim());
            jobject.put("secret",previous.trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jobject;

  }

    public static void startScreen(Context context)
    {
        context.startActivity(new Intent(context, SettingEvent.class));
    }
}
