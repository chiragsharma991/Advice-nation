package com.project.nat.advice_nation.Base;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import android.widget.RadioButton;
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
    private TextInputLayout txtName,txtEmail,txtPhone,txtDate;
    private LinearLayout llSignin;
    private TextView btnSignLogin,btnSignup,btnForgetPassword;
    private Context context;
    private String TAG="Login";
    // Tag used to cancel the request
    private String tag_json_obj = "json_obj_req";
    private String Gender;
    private Calendar calendar;
    private int year,month,day;
    private EditText edtName,edtEmail,edtPhone,user_name,user_password;
    private TextView txtDateofbirth;
    private SwitchCompat switchcompact;
    private ProgressBar progressBarToolbar,progressBarToolbarSignUP;


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

        String URL = null;
        String apiTag = null;
        JSONObject jsonObject = null;
        jsonObject=GetLoginObject();
        URL= NetworkUrl.URL_LOGIN;
        apiTag=NetworkUrl.URL_LOGIN;

        Log.e(TAG, "callback: json"+jsonObject.toString() );
        PostApi postApi=new PostApi(context,URL,jsonObject,apiTag,TAG);
    }

    private void callbackSingnUp ()
    {

        String URL = null;
        String apiTag = null;
        JSONObject jsonObject = null;
        jsonObject=GetsignUpObject();
            URL= NetworkUrl.URL_REGISTER;
            apiTag=NetworkUrl.URL_REGISTER;

        Log.e(TAG, "callback: json"+jsonObject.toString() );
        PostApi postApi=new PostApi(context,URL,jsonObject,apiTag,TAG);

        Animation clockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_right_to_left);
        if(isSigninScreen)
            btnSignup.startAnimation(clockwise);
    }

    private void initialize()
    {
        edtName = (EditText) findViewById(R.id.edtName);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        user_name = (EditText) findViewById(R.id.user_name);
        user_password = (EditText) findViewById(R.id.user_password);
        txtDateofbirth = (TextView) findViewById(R.id.txtDateofbirth);
        progressBarToolbar = (ProgressBar) findViewById(R.id.progressBarToolbar);
        progressBarToolbar.setVisibility(View.INVISIBLE);
        progressBarToolbarSignUP=(ProgressBar) findViewById(R.id.progressBarToolbarSignUP);
        progressBarToolbarSignUP.setVisibility(View.INVISIBLE);
        edtName.addTextChangedListener(new MyTextWatcher(edtName));
        edtEmail.addTextChangedListener(new MyTextWatcher(edtEmail));
        edtPhone.addTextChangedListener(new MyTextWatcher(edtPhone));
        txtDateofbirth.addTextChangedListener(new MyTextWatcher(txtDateofbirth));

        txtName = (TextInputLayout) findViewById(R.id.input_layout_name);
        txtEmail = (TextInputLayout) findViewById(R.id.text_edtemail);
        txtPhone = (TextInputLayout) findViewById(R.id.text_edtphone);
        txtDate = (TextInputLayout) findViewById(R.id.text_Date);



        tvSignupInvoker = (TextView) findViewById(R.id.tvSignupInvoker);
        tvSigninInvoker = (TextView) findViewById(R.id.tvSigninInvoker);

        btnSignup= (TextView) findViewById(R.id.btnSignup);
        btnSignLogin= (TextView) findViewById(R.id.login);
        btnForgetPassword=(TextView) findViewById(R.id.login_forgetPassword);
        btnSignLogin.setOnClickListener(this);
        txtDateofbirth.setOnClickListener(this);
        btnForgetPassword.setOnClickListener(this);


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
                final View viewpart = findViewById(android.R.id.content);
                if (view == btnSignup)
                {
                    if (!validateName())
                    {
                        return;
                    }

                    if (!validateEmail())
                    {
                       return;
                    }

                    if (!validatePhone())
                    {
                        return;
                    }

                    if (!validateDate())
                    {
                        return;
                    }
                    else
                    {
                        if (isOnline())
                        {
                            showProgress(true,progressBarToolbarSignUP);
                           /* Thread background = new Thread() {
                                public void run() {
                                    try {
                                        // Thread will sleep for 5 seconds
                                        sleep(3 * 1000);
                                        showProgress(false,progressBarToolbarSignUP);

                                    } catch (Exception e) {
                                        Log.e(TAG, "run catch error: "+e.getMessage() );
                                    }
                                }
                            };
                            // start thread
                            background.start();*/
                            callbackSingnUp();
                        }else
                        {
                            showSnackbar(viewpart,getResources().getString(R.string.CheckNetwork));
                        }

                       /* showProgress(true,progressBarToolbarSignUP);
                        Thread background = new Thread() {
                            public void run() {
                                try {
                                    // Thread will sleep for 5 seconds
                                    sleep(3 * 1000);
                                    showProgress(false,progressBarToolbarSignUP);
                                    callbackSingnUp();
                                } catch (Exception e) {
                                    Log.e(TAG, "run catch error: "+e.getMessage() );
                                }
                            }
                        };
                        // start thread
                        background.start();*/
                    }
                }
            }
        });
    }

    //Validation methodes

    private boolean validateName()
    {
        if (edtName.getText().toString().trim().isEmpty()) {
          txtName.setError(getResources().getString(R.string.Entername));
           requestFocus(edtName);
          return false;
         } else {
          txtName.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEmail()
    {
        String email = edtEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
        txtEmail.setError(getString(R.string.Enteremails));
        requestFocus(edtEmail);
        return false;
        } else {
         txtEmail.setErrorEnabled(false);
        }
        return true;
    }

    //Validate Emails Format
    private static boolean isValidEmail(String email) {
     return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
     }

    private boolean validatePhone()
    {
        if (edtPhone.getText().toString().trim().isEmpty()) {
            txtPhone.setError(getResources().getString(R.string.EnterAge));
            requestFocus(edtPhone);
            return false;
        } else {
            txtPhone.setErrorEnabled(false);
        }
        return true;
    }


    private boolean validateDate()
    {
        if (txtDateofbirth.getText().toString().trim().isEmpty()) {
            txtDate.setError(getResources().getString(R.string.EnterDateofbirth));
            requestFocus(txtDateofbirth);
            return false;
        } else {
            txtDate.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
     if (view.requestFocus()) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
     }
    }

    private void setNull()
    {
        edtName.setText("");
        edtPhone.setText("");
        edtEmail.setText("");
        txtDateofbirth.setText("");


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
    public void onClick(final View view)
    {

        final View viewpart = findViewById(android.R.id.content);
        if(view == btnSignLogin)
        {
            if(user_name.getText().length()==0){
                showSnackbar(viewpart,"Please Enter Username");
            }else if(user_password.getText().length()==0){
                showSnackbar(viewpart,"Please Enter Password");
            }else {

                if (isOnline())
                {
                    callback();
                }else
                {
                    showSnackbar(viewpart,getResources().getString(R.string.CheckNetwork));
                }

               /* showProgress(true,progressBarToolbar);
                Thread background = new Thread() {
                    public void run() {
                        try {
                            // Thread will sleep for 5 seconds
                            sleep(3 * 1000);
                            showProgress(false,progressBarToolbar);
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
                };*/
                // start thread
               /* background.start();*/
            }


        }
        if (view.getId()==R.id.login_forgetPassword)
        {
            ForgetPassword.startScreen(this);
            finish();
        }

         if(view.getId()==R.id.txtDateofbirth){
             showDialog(999);

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


    public JSONObject GetsignUpObject()
    {
        JSONObject jobject =new JSONObject();
        try {
            jobject.put("Dob" , txtDateofbirth.getText().toString().trim());
            jobject.put("deviceOs","ANDROID");
            jobject.put("deviceToken","testDeviceToken");
            jobject.put("firstName", "abc");
            jobject.put("lastName", "def");
            jobject.put("phoneNumber", edtPhone.getText().toString().trim());
            jobject.put("userName", edtEmail.getText().toString().trim());
            jobject.put("secret","test123");

            Log.e(TAG, "Authentication: "+"Name"+"abc"+"Emails"+edtEmail.getText().toString().trim()+"PhoneNumber"+edtPhone.getText().toString().trim()
            +"dob"+txtDateofbirth.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jobject;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    private void showProgress(boolean b,ProgressBar progressBar)
    {
        if (b) {

            com.project.nat.advice_nation.utils.AnimationUtils.animateScaleOut(progressBar);
        } else {

            com.project.nat.advice_nation.utils.AnimationUtils.animateScaleIn(progressBar);
        }
    }


    @Override
    public void appcontroller(JsonObjectRequest jsonObjectRequest, String apiTag)
    {

        Log.e(TAG, "appcontroller: " );
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, apiTag);
    }

    @Override
    public void OnFailed(VolleyError error)
    {
        showProgress(false,progressBarToolbarSignUP);
        Toast.makeText(context, "Failed Responce"+error, Toast.LENGTH_SHORT).show();
        Log.e(TAG, "OnFailed:"+error.getMessage() );

    }

    @Override
    public void OnSucess(JSONObject response)
    {
        showProgress(false,progressBarToolbarSignUP);

        Toast.makeText(context, "responce"+response, Toast.LENGTH_SHORT).show();
        Log.e(TAG, "OnSucess: "+response );
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
          this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable)
        {
         switch (view.getId()) {
           case R.id.edtName:
               validateName();
           break;
           case R.id.edtEmail:
               validateEmail();
           break;
           case R.id.edtPhone:
                validatePhone();
           break;
             case R.id.txtDateofbirth:
                validateDate();
         }

       }
     }

}
