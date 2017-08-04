package com.project.nat.advice_nation.Base;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.project.nat.advice_nation.Https.ApiResponse;
import com.project.nat.advice_nation.Https.AppController;
import com.project.nat.advice_nation.Https.GetApi;
import com.project.nat.advice_nation.Https.PostApi;
import com.project.nat.advice_nation.Https.ToAppcontroller;
import com.project.nat.advice_nation.Model.Category;
import com.project.nat.advice_nation.Model.UserDetails;
import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.utils.BaseActivity;
import com.project.nat.advice_nation.utils.NetworkUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.category;
import static android.R.attr.debuggable;

public class Login extends BaseActivity implements View.OnClickListener,ToAppcontroller,ApiResponse {

    private boolean isSigninScreen = true;
    private TextView tvSignupInvoker;
    private LinearLayout llSignup;
    private TextView tvSigninInvoker;
    private LinearLayout llSignin;
    private Button btnSignup;
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

            default:
                break;


        }


    }


    private void initialize() {
        gson = new Gson();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        edtName = (EditText) findViewById(R.id.edtName);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtAge = (EditText) findViewById(R.id.edtAge);
        user_name_edt = (EditText) findViewById(R.id.user_name_edt);
        user_password_edt = (EditText) findViewById(R.id.user_password_edt);
        progressBarToolbar = (ProgressBar) findViewById(R.id.progressBarToolbar);
        progressBarToolbar.setVisibility(View.INVISIBLE);

        txtDateofbirth = (TextView) findViewById(R.id.txtDateofbirth);


        tvSignupInvoker = (TextView) findViewById(R.id.tvSignupInvoker);
        tvSigninInvoker = (TextView) findViewById(R.id.tvSigninInvoker);

        btnSignup = (Button) findViewById(R.id.btnSignup);
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

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation clockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_right_to_left);
                if (isSigninScreen)
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

        if (view.getId() == R.id.txtDateofbirth) {
            showDialog(999);

        }


    }



    private void signUp() {


        if (edtName.getText().toString().trim().equalsIgnoreCase("")) {
            edtName.setError(getResources().getString(R.string.Entername));

        } else if (edtEmail.getText().toString().trim().equalsIgnoreCase("")) {
            edtEmail.setError(getResources().getString(R.string.Enteremails));
        } else if (edtAge.getText().toString().trim().equalsIgnoreCase("")) {
            edtAge.setError(getResources().getString(R.string.EnterAge));

        } else {

            JSONObject jobject = new JSONObject();
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


                jobject.put("Dob", txtDateofbirth.getText().toString());
                jobject.put("deviceOs", "ANDROID");
                jobject.put("deviceToken", "testDeviceToken");
                jobject.put("firstName", "abc");
                jobject.put("lastName", "def");
                jobject.put("phoneNumber", "1234567890");
                jobject.put("gender", "MALE");
                jobject.put("userName", edtEmail.getText().toString());
                jobject.put("secret", "test123");

                //  apicall(jobject);


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
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


    private void showProgress(boolean b) {
        if (b) {

            com.project.nat.advice_nation.utils.AnimationUtils.animateScaleOut(progressBarToolbar);
        } else {

            com.project.nat.advice_nation.utils.AnimationUtils.animateScaleIn(progressBarToolbar);
        }
    }


    @Override
    public void appcontroller(JsonObjectRequest jsonObjectRequest, String apiTag) {

        Log.e(TAG, "appcontroller: ");
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, apiTag);
    }

    @Override
    public void OnFailed(int error) {
        Log.e(TAG, "OnFailed:" + error);
        showProgress(false);

        switch (error) {
            case 404:
                showSnackbar(viewpart, getResources().getString(R.string.error_404));
                break;
            case 000:
                showSnackbar(viewpart, getResources().getString(R.string.network_poor));
                break;
            default:
                showSnackbar(viewpart, getResources().getString(R.string.error_404));

        }
        //Additional cases


    }

    @Override
    public void OnSucess(JSONObject response,int id) {
        Log.e(TAG, "OnSucess: "+id+"  "+ response);

        switch (id){
            case 0:
                showProgress(false);
                userlist=new ArrayList<>();
                UserDetails details = gson.fromJson(response.toString(), UserDetails.class);
                userlist.add(details);
                saveProfileData(userlist);
                showToast("Authentication success", context);
                DashboardActivity.startScreen(context);
                overridePendingTransition(R.anim.start, R.anim.exit);
                finish();
               /* Thread background = new Thread() {
                    public void run() {
                        try {
                            // Thread will sleep for 5 seconds
                            sleep(1 * 1000);
                            DashboardActivity.startScreen(context);
                            overridePendingTransition(R.anim.start, R.anim.exit);
                            finish();
                        } catch (Exception e) {
                            Log.e(TAG, "run catch error: " + e.getMessage());
                        }

                    }
                };
                background.start();*/
                break;

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
}
