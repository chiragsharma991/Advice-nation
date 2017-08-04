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

public class PostApi extends BaseActivity   {

    private final ToAppcontroller toAppcontroller;
    private final String apiTag;
    private final JSONObject jObject;
    private final String url;
    private final String TAG;
    private final ApiResponse apiResponse;
    private final int id;
    private Context context;
    private JsonObjectRequest jsonObjReq;
    public String header;


    public PostApi(Context context, String url, JSONObject jObject, String apiTag, String TAG, int id) {

        Log.e(TAG, "PostApi: ");
        this.context = context;
        this.id = id;
        toAppcontroller = (ToAppcontroller) this.context;
        apiResponse = (ApiResponse) this.context;
        this.url = url;
        this.jObject = jObject;
        this.apiTag = apiTag;
        this.TAG = TAG;
        header="";
        setApi();



    }

    private void setApi() {

        Log.e(TAG, "setApi: "+TAG+" "+url);
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        jsonObjReq = new JsonObjectRequest(Request.Method.POST, url
                , jObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        apiResponse.OnSucess(response,id);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                try{
                    apiResponse.OnFailed(response.statusCode);
                }catch (Exception e){
                    Log.e(TAG, "onErrorResponse: "+e.getMessage());
                    apiResponse.OnFailed(000);

                }
            }


        }
        ) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                header = response.headers.get("authorization");
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


}






