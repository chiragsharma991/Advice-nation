package com.project.nat.advice_nation.Base;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
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
import com.project.nat.advice_nation.utils.NetworkUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Calendar;

import static com.project.nat.advice_nation.R.id.btnSignup;
import static com.project.nat.advice_nation.R.id.txtDateofbirth;

/**
 * Created by Surya Chundawat on 7/31/2017.
 */

public class ForgetPassword extends AppCompatActivity implements View.OnClickListener,ToAppcontroller,ApiFailed,ApiSucess
{

    private TextInputLayout txtname,txtlastname,txtemail,txtpassword,txtconfirmpassword,txtphone,txtdate;
    private EditText edtname,edtlastname,edtemails,edtpassword,edtconfrimpassword,edtphone;
    private TextView txtdateofbirth;
    private Button btnsubmit;
    private Context context;
    private String TAG="ForgetPassword";
    private ProgressBar progressBarToolbar;
    private Calendar calendar;
    private int year,month,day;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        initialise();
        checkstatusbar();
        initview();
    }

    private void checkstatusbar() {

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

    private void initialise() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Forget Password");
        toolbar.setNavigationIcon(R.drawable.ic_left_black_24dp);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                // what do you want here
            }
        });
    }

    private void initview()
    {
        txtname = (TextInputLayout) findViewById(R.id.input_fristname);
        txtlastname = (TextInputLayout) findViewById(R.id.input_lasttname);
        txtemail = (TextInputLayout) findViewById(R.id.input_emails);
        txtpassword = (TextInputLayout) findViewById(R.id.input_enterpassword);
        txtconfirmpassword = (TextInputLayout) findViewById(R.id.input_confrimpassword);
        txtphone = (TextInputLayout) findViewById(R.id.input_phone);
        txtdate = (TextInputLayout) findViewById(R.id.input_date);


        edtname = (EditText) findViewById(R.id.edtfristname);
        edtlastname = (EditText) findViewById(R.id.edtlastname);
        edtemails = (EditText) findViewById(R.id.edtemails);
        edtpassword = (EditText) findViewById(R.id.edtpasswordnew);
        edtconfrimpassword = (EditText) findViewById(R.id.edtconfrimpassword);
        edtphone = (EditText) findViewById(R.id.edtphone);
        edtname.addTextChangedListener(new MyTextWatcher(edtname));
        edtemails.addTextChangedListener(new MyTextWatcher(edtemails));
        edtphone.addTextChangedListener(new MyTextWatcher(edtphone));
        edtpassword.addTextChangedListener(new MyTextWatcher(edtpassword));
        edtlastname.addTextChangedListener(new MyTextWatcher(edtlastname));
        edtconfrimpassword.addTextChangedListener(new MyTextWatcher(edtconfrimpassword));


        txtdateofbirth = (TextView) findViewById(R.id.edtdate);
        txtdateofbirth.setOnClickListener(this);

        btnsubmit =(Button) findViewById(R.id.btnforgetsubmit);
        btnsubmit.setOnClickListener(this);

        progressBarToolbar = (ProgressBar) findViewById(R.id.progressBarToolbarSignUP);

    }

    public static void startScreen(Context context){
        context.startActivity(new Intent(context, ForgetPassword.class));

    }

    @Override
    public void onClick(View v)
    {
        if (v==btnsubmit)
        {
            if (!validateName())
            {return;}
            if (!validateLastName())
            {return;}
            if (!validateEmail())
            {return;}
            if (!validatePassword())
            {return;}
            if (!validatePhone())
            {return;}
            if (!validateDob())
            {return;}
            if (!validateConfirmPassword())
            {return;}else
            {
                if (isOnline())
                {
                    showProgress(true,progressBarToolbar);
                    callbackSingnUp();

                }else
                {
                    Toast.makeText(context, "Check Network", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if(v.getId()==R.id.edtdate){
            showDialog(999);

        }

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void callbackSingnUp()
    {

        String URL = null;
        String apiTag = null;
        JSONObject jsonObject = null;
        jsonObject=GetsignUpObject();
        URL= NetworkUrl.URL_REGISTER;
        apiTag=NetworkUrl.URL_REGISTER;
        Log.e(TAG, "callback: json"+jsonObject.toString() );
        PostApi postApi=new PostApi(context,URL,jsonObject,apiTag,TAG);

        /*Animation clockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_right_to_left);
        if(isSigninScreen)
            btnSignup.startAnimation(clockwise);*/
    }

    private JSONObject GetsignUpObject()
    {

        JSONObject jobject =new JSONObject();
        try {
            jobject.put("active" , true);
            jobject.put("anKoins","0");
            jobject.put("dateOfBirth", "2017-08-01T18:02:28.938Z");
            jobject.put("deviceOs", "ANDROID");
            jobject.put("deviceToken", "stringvalue");
            jobject.put("firstName", edtname.getText().toString().trim());
            jobject.put("gender", "MALE");
            jobject.put("id","0");
            jobject.put("lastName",edtlastname.getText().toString().trim());
            jobject.put("newSecret",edtpassword.getText().toString());
            jobject.put("phoneNumber",edtphone.getText().toString().trim());
            JSONArray jsonArray = new JSONArray();
            jsonArray.put("String");
            jobject.put("roles",jsonArray);
            jobject.put("secret","string");
            jobject.put("userName",edtemails.getText().toString().trim());


           /* Log.e(TAG, "Authentication: "+"Name"+"abc"+"Emails"+edtEmail.getText().toString().trim()+"PhoneNumber"+edtPhone.getText().toString().trim()
                    +"dob"+txtDateofbirth.getText().toString().trim());*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jobject;
    }


    private boolean validateName()
    {
        if (edtname.getText().toString().trim().isEmpty()) {
            txtname.setError(getResources().getString(R.string.forgetname));
            requestFocus(edtname);
            return false;
        } else {
            txtname.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateLastName()
    {
        if (edtlastname.getText().toString().trim().isEmpty()) {
            txtlastname.setError(getResources().getString(R.string.forgetLasstname));
            requestFocus(edtlastname);
            return false;
        } else {
            txtlastname.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEmail()
    {
        if (edtemails.getText().toString().trim().isEmpty()) {
            txtemail.setError(getResources().getString(R.string.forgetEmails));
            requestFocus(edtemails);
            return false;
        } else {
            txtemail.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateConfirmPassword()
    {
        if (edtconfrimpassword.getText().toString().trim().isEmpty()) {
            txtconfirmpassword.setError(getResources().getString(R.string.forgetConfirmPassword));
            requestFocus(edtconfrimpassword);
            return false;
        } else {
            txtconfirmpassword.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePhone()
    {
        if (edtphone.getText().toString().trim().isEmpty()) {
            txtphone.setError(getResources().getString(R.string.forgetPhone));
            requestFocus(edtphone);
            return false;
        } else {
            txtphone.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword()
    {
        if (edtpassword.getText().toString().trim().isEmpty()) {
            txtpassword.setError(getResources().getString(R.string.forgetPassword));
            requestFocus(edtpassword);
            return false;
        } else {
            txtpassword.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateDob()
    {
        if (txtdateofbirth.getText().toString().trim().isEmpty()) {
            txtdate.setError(getResources().getString(R.string.forgetPhone));
            requestFocus(txtdateofbirth);
            return false;
        } else {
            txtdate.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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
    public void OnSucess(JSONObject response)
    {
        showProgress(false,progressBarToolbar);
        Toast.makeText(context, "Success essage"+response, Toast.LENGTH_SHORT).show();
        Log.e(TAG, "OnSuccess:"+response );

    }

    @Override
    public void OnFailed(VolleyError error) {
        showProgress(false,progressBarToolbar);
        Toast.makeText(context, "Failed Responce"+error.getMessage(), Toast.LENGTH_SHORT).show();
        Log.e(TAG, "OnFailed:"+error.getMessage() );

    }

    @Override
    public void appcontroller(JsonObjectRequest jsonObjectRequest, String apiTag)
    { Log.e(TAG, "appcontroller: " );
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, apiTag);

    }

    @Override
    public void onBackPressed()
    {
        Login.startScreen(this);
        super.onBackPressed();
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
                case R.id.edtfristname:
                    validateName();
                    break;
                case R.id.edtlastname:
                    validateLastName();
                    break;
                case R.id.edtemails:
                    validateEmail();
                    break;
                case R.id.edtpasswordnew:
                    validatePassword();
                    break;
                case R.id.edtphone:
                    validatePhone();
                    break;
                case R.id.edtdate:
                    validateDob();
                    break;
            }

        }
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
        txtdateofbirth.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

}
