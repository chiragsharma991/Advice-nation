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
import com.project.nat.advice_nation.utils.DialogUtils;
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
    private String totalcoins;

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
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        totalcoins = sharedPreferences.getString("totalcoins","");
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
            totalcoins = sharedPreferences.getString("totalcoins","");
            int deduction=Integer.parseInt(totalcoins.trim())-Integer.parseInt(transferCoins.trim())  ;
            Log.i(TAG, "onTransfer: deduction"+deduction );
            if(deduction <0){
                new DialogUtils(context).alert(getResources().getString(R.string.enough_koins),context);
                return;
            }
            String calculateAmount="Remaining Ammount: "+totalcoins+"-"+transferCoins+"="+deduction;

            new DialogUtils(context, new DialogUtils.dialogResponse() {
                @Override
                public void positive(String data ,int rate) {
                    if (isOnline(context)) {
                        progressDialogStart(context,getResources().getString(R.string.loading_data));
                        callback(0);//0 transfer api
                    } else {
                        showSnackbar(viewpart, getResources().getString(R.string.network_notfound));
                    }
                }

                @Override
                public void negative() {
                }
            }).showAlertDialog("Transfer Amount is: "+"\u20B9"+transferCoins,calculateAmount+"\n\nAre you sure to transfer",true,context);

        }


    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume: ");
        super.onResume();
        if (isOnline(context)) {
            callback(1);// get total coins
        }
    }

    private boolean checkValidation() {

        transferCoins = edt_ankoinstotrnsfer.getText().toString().replaceAll("\\s{2,}", " ").trim();
        toSend = edt_emailtowhomsend.getText().toString().replaceAll("\\s{2,}", " ").trim();
        Log.e(TAG, "checkValidation: "+android.util.Patterns.EMAIL_ADDRESS.matcher(toSend).matches() );
        Log.e(TAG, "transferCoins: " + transferCoins + " and " + toSend);

        if (transferCoins == null || transferCoins.isEmpty() || transferCoins.equals("null")) {
            showSnackbar(viewpart, "Please enter transferCoins");
            return false;
        } else if ( toSend == null || toSend.isEmpty() || toSend.equals("null") || ! android.util.Patterns.EMAIL_ADDRESS.matcher(toSend).matches()) {
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
                Log.e(TAG, "jsonObject: "+jsonObject.toString() );
                String URL = NetworkUrl.URL + user + "/ankoin/"+"transfer/"+transferCoins;
                String apiTag = URL;
                PostApiPlues postApi = new PostApiPlues(context, URL, bearerToken, jsonObject, apiTag, TAG, 0);
                break;

            case 1:
                user = sharedPreferences.getLong("id", 0);
                bearerToken = sharedPreferences.getString("bearerToken", "");
                URL = NetworkUrl.URL_GET_USER_ANKOINS + user + "/ankoin";
                apiTag = URL;
                GetApi getApi = new GetApi(context, URL, bearerToken, apiTag, TAG, 1);
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
                customToast("Transfer success",context,R.drawable.done,R.color.colorPrimaryTrans,false);
                edt_ankoinstotrnsfer.getText().clear();
                edt_emailtowhomsend.getText().clear();
                onResume();
                break;

            case 1:
                try {
                    String totalcoins=response.getString("data");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("totalcoins",totalcoins);
                    editor.apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
            case 404:
                showSnackbar(viewpart, getResources().getString(R.string.error_404_transferAnkoins));
                break;
            case 412:
                showSnackbarError(viewpart, getResources().getString(R.string.error_412));
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
