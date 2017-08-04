package com.project.nat.advice_nation.Https;

import android.content.Context;
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
import com.project.nat.advice_nation.Base.SubcategoryActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chari on 8/2/2017.
 */

public class GetApi {

    private final ToAppcontroller toAppcontroller;
    private final String apiTag;
    private final String url;
    private String TAG;
    private final ApiResponse apiResponse;
    private final int id;
    private final String bearerToken;
    private Context context;
    private JsonObjectRequest jsonObjReq;
    public String header;

//                getApi = new GetApi(context, URL,bearerToken,apiTag,TAG,0);

    public GetApi(Context context, String url, String bearerToken, String apiTag, String TAG, int id,ToAppcontroller toAppcontroller) {

        Log.e(TAG, "PostApi: ");
        this.context = context;
        this.id = id;
        this.toAppcontroller = (ToAppcontroller) toAppcontroller;
        apiResponse = (ApiResponse) toAppcontroller;
        this.url = url;
        this.bearerToken = bearerToken;
        this.apiTag = apiTag;
        this.TAG = TAG;
        header="";
        setApi();



    }

    public GetApi(Context context, String url, String bearerToken, String apiTag, String tag, int id) {
        Log.e(TAG, "PostApi: ");
        this.context = context;
        this.id = id;
        this.toAppcontroller = (ToAppcontroller)context ;
        apiResponse = (ApiResponse) context;
        this.url = url;
        this.bearerToken = bearerToken;
        this.apiTag = apiTag;
        header="";
        setApi();
    }


    private void setApi() {

        Log.e(TAG, "setApi: "+TAG+" "+url);
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        jsonObjReq = new JsonObjectRequest(Request.Method.GET, url
                , null,
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
                headers.put("Authorization",  "Bearer " + bearerToken);
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
