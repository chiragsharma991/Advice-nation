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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.project.nat.advice_nation.Https.ApiResponse;
import com.project.nat.advice_nation.Https.AppController;
import com.project.nat.advice_nation.Https.PostApi;
import com.project.nat.advice_nation.Https.ToAppcontroller;
import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.utils.BaseActivity;
import com.project.nat.advice_nation.utils.NetworkUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import static android.R.attr.id;
import static com.project.nat.advice_nation.R.id.edtfristname;
import static com.project.nat.advice_nation.R.id.edtpasswordnew;

/**
 * Created by Chari on 8/5/2017.
 */

public class ForgetPassword extends BaseActivity implements View.OnClickListener,ToAppcontroller,ApiResponse
{

    private TextInputLayout txtname,txtlastname,txtemail,txtpassword,txtconfirmpassword,txtphone,txtdate;
    private EditText edtname,edtlastname,edtemails,edtconfrimpassword,edtphone;
    private TextView txtdateofbirth;
    private EditText edtfristname;
    private EditText edtpasswordnew;
    private View viewpart;
    private Button btnsubmit;
    private Context context;
    private String TAG="ForgetPassword";
    private ProgressBar progressBarToolbar;
    private Calendar calendar;
    private int year,month,day;
    private PostApi postApi;
    private boolean isSuccess;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpasswordnew);
        context= this;
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
        toolbar.setTitle("Forget Password");
        toolbar.setNavigationIcon(R.drawable.ic_left_black_24dp);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);
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


        edtfristname = (EditText) findViewById(R.id.edtfristname);
        edtlastname = (EditText) findViewById(R.id.edtlastname);
        edtemails = (EditText) findViewById(R.id.edtemails);
        edtpasswordnew = (EditText) findViewById(R.id.edtpasswordnew);
        edtconfrimpassword = (EditText) findViewById(R.id.edtconfrimpassword);
        edtphone = (EditText) findViewById(R.id.edtphoneReg);

        //Textwatcher
        edtfristname.addTextChangedListener(new MyTextWatcher(edtfristname));
        edtemails.addTextChangedListener(new MyTextWatcher(edtemails));
        edtphone.addTextChangedListener(new MyTextWatcher(edtphone));
        edtpasswordnew.addTextChangedListener(new MyTextWatcher(edtpasswordnew));
        edtlastname.addTextChangedListener(new MyTextWatcher(edtlastname));
        edtconfrimpassword.addTextChangedListener(new MyTextWatcher(edtconfrimpassword));


        txtdateofbirth = (TextView) findViewById(R.id.edtdate);
        txtdateofbirth.setOnClickListener(this);

        btnsubmit =(Button) findViewById(R.id.btnforgetsubmit);
        btnsubmit.setOnClickListener(this);

        progressBarToolbar = (ProgressBar) findViewById(R.id.progressBarToolbarSignUP);

    }

    public static void startScreen(Context context)
    {
        context.startActivity(new Intent(context, ForgetPassword.class));
    }

    @Override
    public void onClick(View v)
    {
        viewpart = findViewById(android.R.id.content);
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
            {return;}
            {
                if (edtphone.getText().toString().replaceAll("\\s{2,}", " ").trim().length()<10 )
                {
                    //Toast.makeText(context, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                    showSnackbar(viewpart, getResources().getString(R.string.phonelength));
                    return;
                }else if (!(edtpasswordnew.getText().toString().replaceAll("\\s{2,}", " ").trim().equals(edtconfrimpassword.getText().toString().replaceAll("\\s{2,}", " ").trim())))
                {
                    showSnackbar(viewpart, getResources().getString(R.string.passwordNotmatch));

                }else if (isOnline(ForgetPassword.this))
                {
                    showProgress(true,progressBarToolbar);
                    callback(1);

                }else
                {
                    showSnackbar(viewpart, getResources().getString(R.string.network_notfound));
                    //Toast.makeText(context, "Check Network", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if(v.getId()==R.id.edtdate){
            showDialog(999);
        }
    }

    private void callback(int id) {
        switch (id) {
            case 1:
                String URLREG = NetworkUrl.URL_FORGET;
                String apiTag_REG = NetworkUrl.URL_FORGET;
                JSONObject jsonObject1 = GetsignUpObject();
                Log.e(TAG, "callback: json" + jsonObject1.toString());
                postApi = new PostApi(context, URLREG, jsonObject1, apiTag_REG, TAG ,2);
                break;

            default:
                break;
        }
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
            jobject.put("firstName", edtfristname.getText().toString().replaceAll("\\s{2,}", " ").trim());
            jobject.put("gender", "MALE");
            jobject.put("id","0");
            jobject.put("lastName",edtlastname.getText().toString().replaceAll("\\s{2,}", " ").trim());
            jobject.put("newSecret",edtpasswordnew.getText().toString().replaceAll("\\s{2,}", " ").trim());
            jobject.put("phoneNumber",edtphone.getText().toString().replaceAll("\\s{2,}", " ").trim());
            JSONArray jsonArray = new JSONArray();
            jsonArray.put("String");
            jobject.put("roles",jsonArray);
            jobject.put("secret","string");
            jobject.put("userName",edtemails.getText().toString().replaceAll("\\s{2,}", " ").trim());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jobject;
    }


    private boolean validateName()
    {
        if (edtfristname.getText().toString().trim().isEmpty()) {
            txtname.setError(getResources().getString(R.string.forgetname));
            requestFocus(edtfristname);
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
        String email = edtemails.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            txtemail.setError(getString(R.string.Enteremails));
            requestFocus(edtemails);
            return false;
        } else {
            txtemail.setErrorEnabled(false);
        }
        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
            txtphone.setError(getResources().getString(R.string.phonenumber));
            requestFocus(edtphone);
            return false;
        } else {
            txtphone.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword()
    {
        if (edtpasswordnew.getText().toString().trim().isEmpty()) {
            txtpassword.setError(getResources().getString(R.string.forgetPassword));
            requestFocus(edtpasswordnew);
            return false;
        } else {
            txtpassword.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateDob()
    {
        if (txtdateofbirth.getText().toString().trim().isEmpty()) {
            txtdate.setError(getResources().getString(R.string.phonenumber));
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

    @Override
    public void OnSucess(JSONObject response, int id)
    {
        showSnackbar(viewpart, response.toString());

        //Toast.makeText(context,"Error Code"+response,Toast.LENGTH_SHORT).show();
        Log.e(TAG, "OnSucess: "+id+"  "+ response);
        showProgress(false,progressBarToolbar);
        switch (id)
        {
            case 2:
                showToast("Congrats!!Password Change Successfully..", context);
                Login.startScreen(context);
        }



        //Toast.makeText(context, "Success essage"+response, Toast.LENGTH_SHORT).show();
        Log.e(TAG, "OnSuccess:"+response );
    }

    @Override
    public void OnFailed(int error,int id) {

        showSnackbar(viewpart, String.valueOf(error));

        //Toast.makeText(context,"Error Code"+error,Toast.LENGTH_SHORT).show();

        showProgress(false, progressBarToolbar);
        switch (error) {
            case 404:
                showSnackbar(viewpart, getResources().getString(R.string.error_404));
                    //Toast.makeText(context, "Message"+getResources().getString(R.string.error_404), Toast.LENGTH_SHORT).show();
                break;
            case 403:
                //Toast.makeText(context, "Message"+getResources().getString(R.string.error_403), Toast.LENGTH_SHORT).show();
                showSnackbar(viewpart, getResources().getString(R.string.error_403));
                break;
            case 401:
                //Toast.makeText(context, "Message"+getResources().getString(R.string.error_401), Toast.LENGTH_SHORT).show();
                showSnackbar(viewpart, getResources().getString(R.string.error_401));
                break;
            case 000:
                //Toast.makeText(context, "Message"+getResources().getString(R.string.network_poor), Toast.LENGTH_SHORT).show();
                showSnackbar(viewpart, getResources().getString(R.string.network_poor));
                break;
            case 409:
                //Toast.makeText(context, "Message"+getResources().getString(R.string.error_404_reg), Toast.LENGTH_SHORT).show();
                showSnackbar(viewpart, getResources().getString(R.string.error_404_reg));
                break;
            default:
                showSnackbar(viewpart, getResources().getString(R.string.error_404));
                    //Toast.makeText(context, "Message"+getResources().getString(R.string.error_404), Toast.LENGTH_SHORT).show();

        }
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
                case R.id.edtphoneReg:
                    validatePhone();
                    break;
                case R.id.edtdate:
                    validateDob();
                    break;
                case R.id.edtconfrimpassword:
                    validateConfirmPassword();
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
