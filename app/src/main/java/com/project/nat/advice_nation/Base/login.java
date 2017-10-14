package com.project.nat.advice_nation.Base;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.project.nat.advice_nation.Https.ApiResponse;
import com.project.nat.advice_nation.Https.AppController;
import com.project.nat.advice_nation.Https.GetApi;
import com.project.nat.advice_nation.Https.PostApi;
import com.project.nat.advice_nation.Model.Category;
import com.project.nat.advice_nation.Model.UserDetails;
import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.utils.BaseActivity;
import com.project.nat.advice_nation.utils.NetworkUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class Login extends BaseActivity implements View.OnClickListener,ApiResponse {

    private boolean isSigninScreen = true;
    private TextView tvSignupInvoker;
    private LinearLayout llSignup;
    private TextView tvSigninInvoker,login_forgetPassword;
    private LinearLayout llSignin;
    private TextView btnSignup;
    private TextView btnSignLogin;
    private Context context;
    private String TAG = "Login";
    // Tag used to cancel the request
    private String tag_json_obj = "json_obj_req";
    private Calendar calendar;
    private int year, month, day;
    private EditText edtName;
    private EditText edtEmail;
    private EditText edtAge;
    private EditText user_name_edt;
    private EditText user_password_edt;
    private EditText edt_Passwordreg;
    private EditText edt_lastNamereg;
    private TextView txtDateofbirth;
    private SwitchCompat switchcompact;
    private ProgressBar progressBarToolbar;
    private String user_name, user_password;
    private View viewpart;
    private Gson gson;
    private SharedPreferences sharedPreferences;
    private ArrayList<UserDetails> userlist;
    private PostApi postApi;
    private GetApi getApi;
    private ArrayList<Category> categoryList;
    private TextInputLayout txtName,txtLastname,txtEmail,txtPhone,txtDate,txtPassword;
    private ProgressBar progressBarToolbarReg;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_login);
        context = Login.this;
        initialize();
        //apicall();
        Inidate();

    }

    private void callback(int id) {
        switch (id) {
            case 0:
                String URL = NetworkUrl.URL_LOGIN;
                String apiTag = NetworkUrl.URL_LOGIN;
                JSONObject jsonObject = GetLoginObject();
                Log.e(TAG, "callback: json" + jsonObject.toString());
                postApi = new PostApi(context, URL, jsonObject, apiTag, TAG ,0);  // 1 is id for call deshboard api
            break;

            case 1:
                String URLREG = NetworkUrl.URL_REGISTER;
                String apiTag_REG = NetworkUrl.URL_REGISTER;
                JSONObject jsonObject1 = signUp();
                Log.e(TAG, "callback: json" + jsonObject1.toString());
                postApi = new PostApi(context, URLREG,jsonObject1, apiTag_REG, TAG ,2);
                break;

            default:
                break;
        }
    }


    private void initialize() {
        gson = new Gson();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        edtName = (EditText) findViewById(R.id.edtName);
        edt_lastNamereg= (EditText) findViewById(R.id.edtLastName);
        edt_Passwordreg = (EditText) findViewById(R.id.edtPassword);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtAge = (EditText) findViewById(R.id.edtphone);
        user_name_edt = (EditText) findViewById(R.id.user_name_edt);
        user_password_edt = (EditText) findViewById(R.id.user_password_edt);
        progressBarToolbar = (ProgressBar) findViewById(R.id.progressBarToolbar);
        progressBarToolbarReg = (ProgressBar) findViewById(R.id.progressBarToolbarReg);
        progressBarToolbar.setVisibility(View.INVISIBLE);
        progressBarToolbarReg.setVisibility(View.INVISIBLE);

        //TextInputlayout
        txtName =(TextInputLayout) findViewById(R.id.input_layout_name);
        txtLastname=(TextInputLayout) findViewById(R.id.input_layout_Lastname);
        txtEmail =(TextInputLayout) findViewById(R.id.text_edtemail);
        txtPhone =(TextInputLayout) findViewById(R.id.text_edtphone);
        txtPassword =(TextInputLayout) findViewById(R.id.text_password);
        txtDate =(TextInputLayout) findViewById(R.id.text_Date);
        txtDateofbirth = (TextView) findViewById(R.id.txtDateofbirth);
        edtName.addTextChangedListener(new MyTextWatcher(edtName));
        edt_lastNamereg.addTextChangedListener(new MyTextWatcher(edt_lastNamereg));
        edt_Passwordreg.addTextChangedListener(new MyTextWatcher(edt_Passwordreg));
        edtEmail.addTextChangedListener(new MyTextWatcher(edtEmail));
        edtAge.addTextChangedListener(new MyTextWatcher(edtAge));
        txtDateofbirth.addTextChangedListener(new MyTextWatcher(txtDateofbirth));

        //ForgetPassword
        login_forgetPassword = (TextView) findViewById(R.id.login_forgetPassword);
        login_forgetPassword.setOnClickListener(this);









        tvSignupInvoker = (TextView) findViewById(R.id.tvSignupInvoker);
        tvSigninInvoker = (TextView) findViewById(R.id.tvSigninInvoker);

        btnSignup = (TextView) findViewById(R.id.btnSignup);
        btnSignLogin = (TextView) findViewById(R.id.login);
        btnSignLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
        txtDateofbirth.setOnClickListener(this);


        llSignup = (LinearLayout) findViewById(R.id.llSignup);
        llSignin = (LinearLayout) findViewById(R.id.llSignin);


        tvSignupInvoker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSigninScreen = false;
                showSignupForm();
            }
        });

        tvSigninInvoker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSigninScreen = true;
                showSigninForm();
            }
        });
        showSigninForm();

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void Inidate() {
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
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        txtDateofbirth.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }


    private void showSignupForm() {
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
        Animation translate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_right_to_left);
        llSignup.startAnimation(translate);

        Animation clockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_right_to_left);
        btnSignup.startAnimation(clockwise);

    }

    private void showSigninForm() {
        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
        infoLogin.widthPercent = 0.85f;
        llSignin.requestLayout();


        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
        infoSignup.widthPercent = 0.15f;
        llSignup.requestLayout();

        Animation translate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_left_to_right);
        llSignin.startAnimation(translate);

        tvSignupInvoker.setVisibility(View.VISIBLE);
        tvSigninInvoker.setVisibility(View.GONE);
        Animation clockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_left_to_right);
        btnSignLogin.startAnimation(clockwise);
    }

    public static void startScreen(Context context) {
        context.startActivity(new Intent(context, Login.class));

    }

    @Override
    public void onClick(View view) {

        viewpart = findViewById(android.R.id.content);
        if (view == btnSignLogin) {
            // name=name.replaceAll("\\s{2,}"," ").trim();
            user_name = user_name_edt.getText().toString().replaceAll("\\s{2,}", " ").trim();
            user_password = user_password_edt.getText().toString().replaceAll("\\s{2,}", " ").trim();
            Log.e(TAG, "Auth: " + user_name + " and " + user_password);

            if (user_name == null || user_name.isEmpty() || user_name.equals("null")) {
                showSnackbar(viewpart, "Please Enter Username");
            } else if (user_password == null || user_password.isEmpty() || user_password.equals("null")) {
                showSnackbar(viewpart, "Please Enter Password");
            } else {
                if (isOnline(context)) {
                    showProgress(true);
                    callback(0); //0 is id for login api
                } else {
                    showSnackbar(viewpart, getResources().getString(R.string.network_notfound));
                }
            }
        }

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

            if (!validateLastName())
            {
                return;
            }

            if (!validatepassword())
            {
                return;
            }else
            {
                if (edtAge.getText().toString().replaceAll("\\s{2,}", " ").trim().length()<10)
                {
                    showSnackbar(viewpart, getResources().getString(R.string.phonelength));
                    return;
                }else if (isOnline(context))
                {
                   showProgressReg(true);
                   callback(1);
                }else
                {
                    showSnackbar(viewpart, getResources().getString(R.string.network_notfound));
                }
            }
        }

        if (view == login_forgetPassword)
        {
            maketrasitionEffect(new Intent(this, ForgetPassword.class),this, false);

        }

        if(view.getId()==R.id.txtDateofbirth){
            showDialog(999);

        }
    }



    private JSONObject signUp() {

            JSONObject jobject = new JSONObject();
            try {
                jobject.put("active",true);
                jobject.put("anKoins",0);
                jobject.put("dateOfBirth", "2017-08-05T09:31:33.568Z");
                jobject.put("deviceOs", "ANDROID");
                jobject.put("deviceToken", "testDeviceToken");
                jobject.put("firstName", edtName.getText().toString().replaceAll("\\s{2,}", " ").trim());
                jobject.put("gender", "MALE");
                jobject.put("id",0);
                jobject.put("lastName", edt_lastNamereg.getText().toString().replaceAll("\\s{2,}", " ").trim());
                jobject.put("newSecret" ,edt_Passwordreg.getText().toString().replaceAll("\\s{2,}", " ").trim());
                jobject.put("phoneNumber", edtAge.getText().toString().replaceAll("\\s{2,}", " ").trim());
                JSONArray jsonArray = new JSONArray();
                jsonArray.put("abcd");
                jobject.put("roles",jsonArray);
                jobject.put("userName", edtEmail.getText().toString().replaceAll("\\s{2,}", " ").trim());
                jobject.put("secret", edt_Passwordreg.getText().toString().replaceAll("\\s{2,}", " ").trim());

            } catch (JSONException e) {
                e.printStackTrace();
            }

        return jobject;
    }



    public JSONObject GetLoginObject() {

        JSONObject jobject = new JSONObject();
        try {

            jobject.put("userName", user_name);
            jobject.put("secret", user_password);
            jobject.put("deviceOs", "ANDROID");
            jobject.put("deviceToken", "test");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "GetLoginObject: " + e.getMessage());
        }
        return jobject;


    }

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

    private boolean validateLastName()
    {
        if (edt_lastNamereg.getText().toString().trim().isEmpty()) {
            txtLastname.setError(getResources().getString(R.string.EnterLastName));
            requestFocus(edt_lastNamereg);
            return false;
        } else {
            txtLastname.setErrorEnabled(false);
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
        if (edtAge.getText().toString().trim().isEmpty()) {
            txtPhone.setError(getResources().getString(R.string.EnterAge));
            requestFocus(edtAge);
            return false;
        } else {
            txtPhone.setErrorEnabled(false);
        }
        return true;
    }


    private boolean validateDate()
    {
        if (txtDateofbirth.getText().toString().trim().isEmpty()) {
            txtDate.setError(getResources().getString(R.string.forgetDOb));
            requestFocus(txtDateofbirth);
            return false;
        } else {
            txtDate.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatepassword()
    {
        if (edt_Passwordreg.getText().toString().trim().isEmpty()) {
            txtPassword.setError(getResources().getString(R.string.password));
            requestFocus(edt_Passwordreg);
            return false;
        } else {
            txtPassword.setErrorEnabled(false);
        }
        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void showProgress(boolean b) {
        if (b)
        {
            com.project.nat.advice_nation.utils.AnimationUtils.animateScaleOut(progressBarToolbar);
        } else {

            com.project.nat.advice_nation.utils.AnimationUtils.animateScaleIn(progressBarToolbar);
        }
    }


    private void showProgressReg(boolean b)
    {
        if (b)
        {
            com.project.nat.advice_nation.utils.AnimationUtils.animateScaleOut(progressBarToolbarReg);
        }else
        {
            com.project.nat.advice_nation.utils.AnimationUtils.animateScaleIn(progressBarToolbarReg);
        }

    }

//9449052078 02240065442


    @Override
    public void OnFailed(int error , int id) {
        Log.e(TAG, "OnFailed:" + error);
        showProgress(false);
        showProgressReg(false);

        switch (error) {
            case 404:
                if (id==0)
                {
                    showSnackbar(viewpart, getResources().getString(R.string.error_404));
                }else
                {
                    showSnackbar(viewpart, getResources().getString(R.string.error_404_reg));
                }
                break;
            case 403:
                showSnackbar(viewpart, getResources().getString(R.string.error_403));
                break;
            case 401:
                showSnackbar(viewpart, getResources().getString(R.string.error_401));
                break;
            case 000:
                showSnackbar(viewpart, getResources().getString(R.string.network_poor));
                break;
            case 409:
                showSnackbar(viewpart, getResources().getString(R.string.error_404_reg));
                break;
            default:
                if (id==0)
                {
                    showSnackbar(viewpart, getResources().getString(R.string.error_404));
                }else
                {
                    showSnackbar(viewpart, getResources().getString(R.string.error_404_reg));
                }


        }
        //Additional cases


    }

    @Override
    public void OnSucess(JSONObject response,int id) {
        Log.e(TAG, "OnSucess: "+id+"  "+ response);

        switch (id)
        {
            case 0:
                showProgress(false);
                userlist=new ArrayList<>();
                UserDetails details = gson.fromJson(response.toString(), UserDetails.class);
                userlist.add(details);
                saveProfileData(userlist);
                customToast("Authentication success", context,R.drawable.autorenew,R.color.colorPrimarylight,true);
            //    showToast("Authentication success", context);
                DashboardActivity.startScreen(context);
                overridePendingTransition(R.anim.start, R.anim.exit);
                finish();
                break;

            case 2:
                showProgressReg(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showToast("Registration Success", context);
                    }
                }, 500);
                isSigninScreen = true;
                showSigninForm();

            default:
                break;

        }

    }

    private void saveProfileData(ArrayList<UserDetails> userlist) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", userlist.get(0).getData().getUserName());
        editor.putLong("dateOfBirth", userlist.get(0).getData().getDateOfBirth());
        editor.putLong("id", userlist.get(0).getData().getId());
        editor.putString("bearerToken", postApi.header);
        editor.putString("phoneNumber", userlist.get(0).getData().getPhoneNumber());
        editor.putString("deviceToken", userlist.get(0).getData().getDeviceToken());
        editor.putString("deviceOs", userlist.get(0).getData().getDeviceOs());
        editor.apply();
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
            switch (view.getId())
            {
                case R.id.edtName:
                    validateName();
                    break;
                case R.id.edtLastName:
                    validateLastName();
                    break;
                case R.id.edtEmail:
                    validateEmail();
                    break;
                case R.id.edtPassword:
                    validatepassword();
                    break;
                case R.id.edtphone:
                    validatePhone();
                    break;
                case R.id.txtDateofbirth:
                    validateDate();
                    break;
            }

        }
    }
}
