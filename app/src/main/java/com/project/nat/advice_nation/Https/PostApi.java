package com.project.nat.advice_nation.Https;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.project.nat.advice_nation.utils.BaseActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chari on 7/17/2017.
 */

public class PostApi extends BaseActivity {

    private final ToAppcontroller toAppcontroller;
    private final String apiTag;
    private final JSONObject jObject;
    private final String url;
    private final String TAG;
  //  private final View viewpart;
    private final ApiSucess apiSucess;
    private final ApiFailed apiFailed;
    private Context context;


    public PostApi(Context context, String url, JSONObject jObject, String apiTag, String TAG){

        Log.e(TAG, "PostApi: " );
        this.context=context;
        toAppcontroller= (ToAppcontroller)this.context;
        apiSucess= (ApiSucess)this.context;
        apiFailed= (ApiFailed)this.context;
        this.url=url;
        this.jObject=jObject;
        this.apiTag=apiTag;
        this.TAG=TAG;
      //  viewpart = findViewById(android.R.id.content);

        setApi();

    }

    private void setApi() {

        Log.e(TAG, "setApi: " );
        if (isOnline(context)) {

            Log.e(TAG, "isOnline:  true" );

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,url
                    , jObject,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e(TAG, response.toString());
                            apiSucess.OnSucess(response);


                            // Toast.makeText(getApplicationContext(),"Response"+response.toString(),Toast.LENGTH_SHORT).show();
                        }


                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error: " + error.getMessage());
                    apiFailed.OnFailed(error);
                    // hide the progress dialog
                }



            }

            ) /*{ @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String,String>();
                // headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            } }*/;





            toAppcontroller.appcontroller(jsonObjReq, apiTag);



        }else{

         //   showSnackbar(viewpart, Constants.Network_notfound);

        }
    }





}
