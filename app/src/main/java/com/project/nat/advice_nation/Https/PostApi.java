package com.project.nat.advice_nation.Https;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.project.nat.advice_nation.utils.BaseActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chari on 7/17/2017.
 */

public class PostApi extends BaseActivity implements CheckStatus {

    private final ToAppcontroller toAppcontroller;
    private final String apiTag;
    private final JSONObject jObject;
    private final String url;
    private final String TAG;
    private final ApiSucess apiSucess;
    private final ApiFailed apiFailed;
    private Context context;
    private JsonObjectRequest jsonObjReq;


    public PostApi(Context context, String url, JSONObject jObject, String apiTag, String TAG) {

        Log.e(TAG, "PostApi: ");
        this.context = context;
        toAppcontroller = (ToAppcontroller) this.context;
        apiSucess = (ApiSucess) this.context;
        apiFailed = (ApiFailed) this.context;
        this.url = url;
        this.jObject = jObject;
        this.apiTag = apiTag;
        this.TAG = TAG;
        new NetworkTest(context,this).execute();



    }

    private void setApi() {

        Log.e(TAG, "setApi: ");

        
        Log.e(TAG, "isOnline:  true");
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        jsonObjReq = new JsonObjectRequest(Request.Method.POST, url
                , jObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        apiSucess.OnSucess(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                apiFailed.OnFailed(response.statusCode);
            }


        }
        ) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                String header = response.headers.get("authorization");
                Log.e(TAG, "parseNetworkResponse: " + header);
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        toAppcontroller.appcontroller(jsonObjReq, apiTag);
        int socketTimeout = 10000;//30000-30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);
        mRequestQueue.add(jsonObjReq);

    }

    @Override
    public void online(boolean isOnline) {
        Log.e(TAG, "online:"+isOnline );
        if(isOnline){
            setApi();
        }else{
            apiFailed.OnFailed(000);

        }
    }
}













// Test network if is connected but data is not comming



 class NetworkTest extends AsyncTask<String,String,String >
{

    private final Context context;
    private final CheckStatus checkStatus;

    public NetworkTest(Context context,CheckStatus checkStatus){
        this.context=context;
        this.checkStatus=checkStatus;
    }
    @Override
    protected String doInBackground(String... strings) {

        ConnectivityManager cm = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            try {
                URL url = new URL("http://www.google.com/");
                HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
                urlc.setRequestProperty("User-Agent", "test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1000); // mTimeout is in seconds
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                    checkStatus.online(true);
                } else {
                    checkStatus.online(false);
                }
            } catch (IOException e) {
                Log.e("warning", "Error checking internet connection", e);
                checkStatus.online(false);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }
}

interface CheckStatus{

    public void online(boolean isOnline);
}


