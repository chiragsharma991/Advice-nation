package com.project.nat.advice_nation.Ankoins;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.project.nat.advice_nation.Base.ProductList;
import com.project.nat.advice_nation.Https.ApiResponse;
import com.project.nat.advice_nation.Https.GetApi;
import com.project.nat.advice_nation.Https.PostApiPlues;
import com.project.nat.advice_nation.Model.Subcategory;
import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.utils.BaseActivity;
import com.project.nat.advice_nation.utils.NetworkUrl;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import static com.project.nat.advice_nation.R.id.retry;

/**
 * Created by Chari on 7/23/2017.
 */

public class Activity_TransferAnkoins extends BaseActivity implements ApiResponse
{
    private EditText edt_ankoinstotrnsfer,edt_emailtowhomsend,edt_remaing;
    private Button btn_submit;
    private String TAG="TransferAnkoins";
    private Toolbar toolbar;
    private Gson gson;
    private SharedPreferences sharedPreferences;
    private Context context;
    private View viewpart;
    private String toSend,transferCoins;

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
        context=this;
        gson = new Gson();
        StyleableToast styleableToast = new StyleableToast
                .Builder(this)
                .duration(Toast.)
                .icon(R.drawable.autorenew)
                .spinIcon()
                .text("Downloading your information")
                .textColor(Color.WHITE)
                .backgroundColor(Color.parseColor("#184c6d"))
                .build()
                ;
        styleableToast.show();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        edt_ankoinstotrnsfer = (EditText) findViewById(R.id.edtankointotrnsfer);
        edt_emailtowhomsend = (EditText) findViewById(R.id.edtankoinwhomtotrnsfer);
        edt_remaing = (EditText) findViewById(R.id.edtankoinRemaing);
        toolbar= (Toolbar)findViewById(R.id.toolbar);
        viewpart = findViewById(android.R.id.content);
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

    public void onTransfer(View view){

        if(checkValidation()){
            if (isOnline(context)) {

                callback(0);//0 is responseCode for login api
            } else {
                showSnackbar(viewpart, getResources().getString(R.string.network_notfound));
            }
        }


    }

    private boolean checkValidation() {

        transferCoins = edt_ankoinstotrnsfer.getText().toString().replaceAll("\\s{2,}", " ").trim();
        toSend = edt_emailtowhomsend.getText().toString().replaceAll("\\s{2,}", " ").trim();
        Log.e(TAG, "transferCoins: " + transferCoins + " and " + toSend);

        if (transferCoins == null || transferCoins.isEmpty() || transferCoins.equals("null")) {
            showSnackbar(viewpart, "Please enter transferCoins");
            return false;
        } else if (toSend == null || toSend.isEmpty() || toSend.equals("null") || android.util.Patterns.EMAIL_ADDRESS.matcher(toSend).matches()) {
            showSnackbar(viewpart, "Please enter right information of mail");
            return false;
        }

        return true;
    }

    private void callback(int responseCode) {
        switch (responseCode) {
            case 0:
                long user = sharedPreferences.getLong("id", 0);
                String bearerToken = sharedPreferences.getString("bearerToken", "");
                //http://ec2-13-126-97-168.ap-south-1.compute.amazonaws.com:8080/AdviseNation/api/users/17041409/ankoin/transfer/5
                JSONObject jsonObject = GetLoginObject();
                String URL = NetworkUrl.URL + user + "/ankoin/"+"transfer"+transferCoins;
                String apiTag = URL;
                PostApiPlues postApi = new PostApiPlues(context, URL, bearerToken, jsonObject, apiTag, TAG, 2);
                break;

            default:
                break;


        }
    }

    @Override
    public void OnSucess(JSONObject response, int id) {

        switch (id) {
            case 0:
                Log.e(TAG, "OnSucess: "+response );
                break;

            default:
                break;
        }
    }



    @Override
    public void OnFailed(int error, int id) {
        Log.e(TAG, "OnFailed: " + error);
        //   progressBar.setVisibility(View.GONE);
        switch (error) {
            case 000:
                showSnackbar(viewpart, getResources().getString(R.string.network_poor));
                break;
            case 500:
                showSnackbar(viewpart, getResources().getString(R.string.error_500));
                break;
            default:
                showSnackbar(viewpart, getResources().getString(R.string.random_error));
        }

    }


    public JSONObject GetLoginObject() {

        JSONObject jobject = new JSONObject();
        try {

            jobject.put("userName",toSend);


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "GetLoginObject: " + e.getMessage());
        }
        return jobject;


    }

    public static void startScreen(Context context)
    {
        context.startActivity(new Intent(context, Activity_TransferAnkoins.class));
    }
}
