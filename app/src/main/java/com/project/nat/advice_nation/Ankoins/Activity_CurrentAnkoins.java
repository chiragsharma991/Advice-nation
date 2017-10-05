package com.project.nat.advice_nation.Ankoins;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.project.nat.advice_nation.Adapter.AnkoinsTranjectionAdapter;
import com.project.nat.advice_nation.Adapter.ProductListAdapter;
import com.project.nat.advice_nation.Base.ProductList;
import com.project.nat.advice_nation.Https.ApiResponse;
import com.project.nat.advice_nation.Https.GetApi;
import com.project.nat.advice_nation.Https.PostApiPlues;
import com.project.nat.advice_nation.InApp.IabBroadcastReceiver;
import com.project.nat.advice_nation.InApp.IabHelper;
import com.project.nat.advice_nation.InApp.IabResult;
import com.project.nat.advice_nation.InApp.Inventory;
import com.project.nat.advice_nation.InApp.Purchase;
import com.project.nat.advice_nation.Model.Subcategory;
import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.utils.BaseActivity;
import com.project.nat.advice_nation.utils.NetworkUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class Activity_CurrentAnkoins extends BaseActivity implements ApiResponse, IabBroadcastReceiver.IabBroadcastListener{

    private static int tagId;
    private TextView coins,buy;
    private String TAG="Activity_CurrentAnkoins";
    private Context context;
    private Gson gson;
    private SharedPreferences sharedPreferences;
    private View viewpart;
    private ProgressBar progressBar;
    static final String SKU = "200";
    static final int RC_REQUEST = 10001;
    String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhxNnoxmC9iEvJ+R2kla+ovg59yhr/XO48EwcWCYOzad6Cycn3+jO9lZMTtBvEVrGJzjxoQN4+rtbvsZBuK6ZAQtkqr+7IUTic7hbgrcd6e7igCfutyFQ64sZXRZ3NTH5FEHiovT+6d4MeeN4GwYVRam5hFGorNP0XXJqi8XemGuJNGUzlTP/FtwtQIfZtarEZhmjx6yY9kIm0yT3RyNXw4v/BLenckTDEoWdz9AzQj68MivI+gDLCZBQbZ8A9eTkNUO3am5mVu2Z+B/Ey+A3te8wcqgNwNq18a9EXtXecner05SVN97eJVtFDrocFBeTi7/BSRBHyNhwprP2NIOszwIDAQAB";
    private IabHelper mHelper;
    IabBroadcastReceiver mBroadcastReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__current_ankoins);
        checkstatusbar();
        IniliView();
        inApp();


    }

    private void inApp() {
        if(tagId == 0){
            mHelper = new IabHelper(context, base64EncodedPublicKey);
            mHelper.enableDebugLogging(true);
            Log.d(TAG, "Starting setup.");
            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                public void onIabSetupFinished(IabResult result) {
                    Log.d(TAG, "Setup finished.");
                    if (!result.isSuccess()) {
                        // Oh noes, there was a problem.
                        complain("Problem setting up in-app billing: " + result);
                        return;
                    }
                    // Have we been disposed of in the meantime? If so, quit.
                    if (mHelper == null) return;
                    // Important: Dynamically register for broadcast messages about updated purchases.
                    // We register the receiver here instead of as a <receiver> in the Manifest
                    // because we always call getPurchases() at startup, so therefore we can ignore
                    // any broadcasts sent while the app isn't running.
                    // Note: registering this listener in an Activity is a bad idea, but is done here
                    // because this is a SAMPLE. Regardless, the receiver must be registered after
                    // IabHelper is setup, but before first call to getPurchases().
                    mBroadcastReceiver = new IabBroadcastReceiver(Activity_CurrentAnkoins.this);
                    IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                    context.registerReceiver(mBroadcastReceiver, broadcastFilter);

                    // IAB is fully set up. Now, let's get an inventory of stuff we own.
                    Log.d(TAG, "Setup successful. Querying inventory.");
                    try {
                        mHelper.queryInventoryAsync(mGotInventoryListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error querying inventory. Another async operation in progress.");
                    }
                }
            });





        }

    }

    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */


            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
            Purchase gasPurchase = inventory.getPurchase(SKU);
            if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                Log.d(TAG, "We have gas. Consuming it.");
                try {
                    mHelper.consumeAsync(inventory.getPurchase(SKU), mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error consuming gas. Another async operation in progress.");
                }
                return;
            }

            // updateUi();
            //  setWaitScreen(false);
            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    private boolean verifyDeveloperPayload(Purchase purchase) {

        String payload = purchase.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }


    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
                //setWaitScreen(false);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                // setWaitScreen(false);
                return;
            }
            if (isOnline(context)) {
                callback(1);//1 is for buy Ankoins
                progressBar.setVisibility(View.VISIBLE);
            } else {
                showSnackbar(viewpart, getResources().getString(R.string.network_notfound));
            }
            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(SKU)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is gas. Starting gas consumption.");
                try {
                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error consuming gas. Another async operation in progress.");
                    //    setWaitScreen(false);
                    return;
                }
            }

        }
    };

    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.d(TAG, "Consumption successful. Provisioning.");
                // mTank = mTank == TANK_MAX ? TANK_MAX : mTank + 1;
                // saveData();
                // alert("You filled 1/4 tank. Your tank is now " + String.valueOf(mTank) + "/4 full!");
            }
            else {
                complain("Error while consuming: " + result);
            }
            // updateUi();
            // setWaitScreen(false);
            Log.d(TAG, "End consumption flow.");
        }
    };

    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
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


    private void IniliView()
    {
        context=Activity_CurrentAnkoins.this;
        gson = new Gson();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        viewpart = findViewById(android.R.id.content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView image = (ImageView) findViewById(R.id.image);
        image.setImageResource(tagId==0 ? R.mipmap.buy: R.mipmap.coins);
        coins = (TextView) findViewById(R.id.coins);
        buy = (TextView) findViewById(R.id.buy);
        buy.setVisibility(tagId==0 ? View.VISIBLE :View.GONE);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tagId==0 ? "Buy Ankoins" : "Current Ankoins");
        toolbar.setNavigationIcon(R.drawable.ic_left_black_24dp);
        toolbar.setNavigationContentDescription("Back");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                // what do you want here
            }
        });

        if (isOnline(context)) {
            callback(0);//0 is responseCode for login api
            progressBar.setVisibility(View.VISIBLE);
        } else {
            showSnackbar(viewpart, getResources().getString(R.string.network_notfound));
        }
    }

    public void onBuy(View view){

        Log.d(TAG, "Buy gas button clicked.");

        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        // setWaitScreen(true);
        Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";

        try {
            mHelper.launchPurchaseFlow(this, SKU, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            Log.e(TAG, "IabAsyncInProgressException: "+e.getMessage() );
            complain("Error launching purchase flow. Another async operation in progress.");
            //setWaitScreen(false);
        }

    }



    private void callback(int responseCode) {
        long user = sharedPreferences.getLong("id", 0);
        String bearerToken = sharedPreferences.getString("bearerToken", "");
        switch (responseCode) {
            case 0:
                String URL = NetworkUrl.URL_GET_USER_ANKOINS + user + "/ankoin";
                String apiTag = NetworkUrl.URL_GET_USER_ANKOINS + user + "/ankoin";
                GetApi getApi = new GetApi(context, URL, bearerToken, apiTag, TAG, 0); //0 is for finish second api call
                break;

            case 1:
                //http://ec2-13-126-97-168.ap-south-1.compute.amazonaws.com:8080/AdviseNation/api/users/17041409/ankoin/buy/10
                URL = NetworkUrl.URL + user + "/ankoin/buy/10";
                apiTag = URL;
                PostApiPlues postApi = new PostApiPlues(context, URL,bearerToken, null, apiTag, TAG, 1);
                break;

            default:
                break;


        }
    }


    @Override
    public void OnSucess(JSONObject response, int id) {
        progressBar.setVisibility(View.GONE);
        switch (id) {
            case 0:
                try {
                    String totalcoins=response.getString("data");
                    coins.setText("\u20B9" +" "+totalcoins);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                //callback();
                showSnackbarSuccess(viewpart, getResources().getString(R.string.purchase_success));
                customToast("Updation Coins...", context,R.drawable.autorenew,R.color.colorPrimarylight,true);
                callback(0);
                break;

            default:
                break;
        }
    }



    @Override
    public void OnFailed(int error, int id) {
        Log.e(TAG, "OnFailed: " + error);
        progressBar.setVisibility(View.GONE);
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





    /**
     *
     * @param context
     * @param tag 0 for inapp
     *            1 for current Ankoins
     */
    public static void startScreen(Context context, int tag)
    {

        tagId=tag;
        context.startActivity(new Intent(context, Activity_CurrentAnkoins.class));
    }

    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        if (mBroadcastReceiver != null) {
            context.unregisterReceiver(mBroadcastReceiver);
        }

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.disposeWhenFinished();
            mHelper = null;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    @Override
    public void receivedBroadcast() {

        // Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error querying inventory. Another async operation in progress.");
        }
    }
}
