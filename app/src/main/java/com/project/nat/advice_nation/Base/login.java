package com.project.nat.advice_nation.Base;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.project.nat.advice_nation.Https.ApiFailed;
import com.project.nat.advice_nation.Https.ApiSucess;
import com.project.nat.advice_nation.Https.AppController;
import com.project.nat.advice_nation.Https.PostApi;
import com.project.nat.advice_nation.Https.ToAppcontroller;
import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.utils.BaseActivity;
import com.project.nat.advice_nation.utils.NetworkUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class Login extends BaseActivity implements View.OnClickListener,ToAppcontroller,ApiFailed,ApiSucess
{

    private boolean isSigninScreen = true;
    private TextView tvSignupInvoker;
    private LinearLayout llSignup;
    private ImageButton imgbtnCalender;
    private TextView tvSigninInvoker;
    private LinearLayout llSignin;
    private Button btnSignup;
    private TextView btnSignLogin;
    private Context context;
    private String TAG="Login";
    // Tag used to cancel the request
    private String tag_json_obj = "json_obj_req";
    private Calendar calendar;
    private int year,month,day;
    private EditText edtName,edtEmail,edtAge,user_name,user_password;
    private TextView txtDateofbirth;
    private SwitchCompat switchcompact;
    private ProgressBar progressBarToolbar;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_login);
        context=Login.this;
        initialize();
        //apicall();
        Inidate();

    }

    private void callback ()
    {
        String URL =NetworkUrl.URL_LOGIN;
        String apiTag =NetworkUrl.URL_LOGIN;
        JSONObject jsonObject=GetLoginObject();
        Log.e(TAG, "callback: json"+jsonObject.toString() );
        PostApi postApi=new PostApi(context,URL,jsonObject,apiTag,TAG);

    }


    private void initialize()
    {
        edtName = (EditText) findViewById(R.id.edtName);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtAge = (EditText) findViewById(R.id.edtAge);
        user_name = (EditText) findViewById(R.id.user_name);
        user_password = (EditText) findViewById(R.id.user_password);
        progressBarToolbar = (ProgressBar) findViewById(R.id.progressBarToolbar);
        progressBarToolbar.setVisibility(View.INVISIBLE);

        txtDateofbirth = (TextView) findViewById(R.id.txtDateofbirth);


        tvSignupInvoker = (TextView) findViewById(R.id.tvSignupInvoker);
        tvSigninInvoker = (TextView) findViewById(R.id.tvSigninInvoker);

        btnSignup= (Button) findViewById(R.id.btnSignup);
        btnSignLogin= (TextView) findViewById(R.id.login);
        btnSignLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
        txtDateofbirth.setOnClickListener(this);


        llSignup = (LinearLayout) findViewById(R.id.llSignup);
        llSignin = (LinearLayout) findViewById(R.id.llSignin);


        tvSignupInvoker.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                isSigninScreen = false;
                showSignupForm();
            }
        });

        tvSigninInvoker.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                isSigninScreen = true;
                showSigninForm();
            }
        });
        showSigninForm();

        btnSignup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Animation clockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_right_to_left);
                if(isSigninScreen)
                    btnSignup.startAnimation(clockwise);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void Inidate()
    {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }


    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day)
    {
        txtDateofbirth.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }


    private void showSignupForm()
    {
        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
        infoLogin.widthPercent = 0.15f;
        llSignin.requestLayout();


        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
        infoSignup.widthPercent = 0.85f;
        llSignup.requestLayout();

        tvSignupInvoker.setVisibility(View.GONE);
        tvSigninInvoker.setVisibility(View.VISIBLE);
        Animation translate= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_right_to_left);
        llSignup.startAnimation(translate);

        Animation clockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_right_to_left);
        btnSignup.startAnimation(clockwise);

    }

    private void showSigninForm()
    {
        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
        infoLogin.widthPercent = 0.85f;
        llSignin.requestLayout();


        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
        infoSignup.widthPercent = 0.15f;
        llSignup.requestLayout();

        Animation translate= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_left_to_right);
        llSignin.startAnimation(translate);

        tvSignupInvoker.setVisibility(View.VISIBLE);
        tvSigninInvoker.setVisibility(View.GONE);
        Animation clockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_left_to_right);
        btnSignLogin.startAnimation(clockwise);
    }

    public static void startScreen(Context context)
    {
        context.startActivity(new Intent(context, Login.class));

    }

    @Override
    public void onClick(View view)
    {

        final View viewpart = findViewById(android.R.id.content);
        if(view == btnSignLogin)
        {
            if(user_name.getText().length()==0){
                showSnackbar(viewpart,"Please Enter Username");
            }else if(user_password.getText().length()==0){
                showSnackbar(viewpart,"Please Enter Password");
            }else {
                callback();
               /* showProgress(true);
                Thread background = new Thread() {
                    public void run() {
                        try {
                            // Thread will sleep for 5 seconds
                            sleep(3 * 1000);
                            showProgress(false);
                            String user=user_name.getText().toString();
                            user=user.replace(" ","").trim();
                            String password=user_password.getText().toString();
                            password=password.replace(" ","").trim();
                            Log.e(TAG, "Authentication: "+"user"+user+"password"+password );
                            if(user.equals("1234")&&password.equals("1234")){
                                DashboardActivity.startScreen(context);
                                overridePendingTransition(R.anim.start, R.anim.exit);
                                finish();

                            } else {
                                callback();
                            }



                        } catch (Exception e) {
                            Log.e(TAG, "run catch error: "+e.getMessage() );
                        }
                    }
                };
                // start thread
                background.start();*/
            }




        }

         if(view.getId()==R.id.txtDateofbirth){
             showDialog(999);

         }


    }



    private void signUp()
    {


        if (edtName.getText().toString().trim().equalsIgnoreCase(""))
        {
            edtName.setError(getResources().getString(R.string.Entername));

        }else if (edtEmail.getText().toString().trim().equalsIgnoreCase(""))
        {
            edtEmail.setError(getResources().getString(R.string.Enteremails));
        }else if (edtAge.getText().toString().trim().equalsIgnoreCase(""))
        {
            edtAge.setError(getResources().getString(R.string.EnterAge));

        }else
        {

            JSONObject jobject =new JSONObject();
            try {
                /*"dateOfBirth": "2016-04-10",
                        "deviceOs": "ANDROID",
                        "deviceToken": "testDeviceToken",
                        "firstName": "abc",
                        "lastName": "def",
                        "phoneNumber": "1234567890",
                        "gender":"MALE",
                        "userName": "sh@am.com",
                        "secret":"test123"*/


                jobject.put("Dob" , txtDateofbirth.getText().toString() );
                jobject.put("deviceOs","ANDROID");
                jobject.put("deviceToken","testDeviceToken");
                jobject.put("firstName", "abc");
                jobject.put("lastName", "def");
                jobject.put("phoneNumber", "1234567890");
                jobject.put("gender","MALE");
                jobject.put("userName", edtEmail.getText().toString());
                jobject.put("secret","test123");

               //  apicall(jobject);


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public JSONObject GetLoginObject() {

        JSONObject jobject =new JSONObject();
        try {

            jobject.put("userName" ,"chiragsharma@gmail.com" );
            jobject.put("secret","test123");
            jobject.put("deviceOs","testDeviceToken");
            jobject.put("deviceToken", "abc");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "GetLoginObject: "+e.getMessage() );
        }
        return jobject;


    }


    private void showProgress(boolean b) {
        if (b) {

            com.project.nat.advice_nation.utils.AnimationUtils.animateScaleOut(progressBarToolbar);
        } else {

            com.project.nat.advice_nation.utils.AnimationUtils.animateScaleIn(progressBarToolbar);
        }
    }


    @Override
    public void appcontroller(JsonObjectRequest jsonObjectRequest, String apiTag) {

        Log.e(TAG, "appcontroller: " );
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, apiTag);
    }

    @Override
    public void OnFailed(VolleyError error) {
        Log.e(TAG, "OnFailed:"+error.getMessage() );

    }

    @Override
    public void OnSucess(JSONObject response) {
        Log.e(TAG, "OnSucess: "+response );
    }
}
