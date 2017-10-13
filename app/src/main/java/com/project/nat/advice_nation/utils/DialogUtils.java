package com.project.nat.advice_nation.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.nat.advice_nation.Base.Login;
import com.project.nat.advice_nation.R;

import org.json.JSONObject;

import static com.project.nat.advice_nation.R.id.reating;

/**
 * Created by Chari on 7/7/2017.
 */

public class DialogUtils {

    private dialogResponse response;
    private Context context;
    private static Dialog dialog;

    public DialogUtils(Context context) {
        this.context = context;
    }

    public DialogUtils(Context context, dialogResponse response) {
        this.context = context;
        this.response = (dialogResponse) response;

    }

    public Dialog setupCustomeDialogFromBottom(int layout) {
        Dialog dialog = new Dialog(context, R.style.ThemeDialog);
        dialog.getWindow().getAttributes().windowAnimations = R.style.ThemeDialog;
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(layout);
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

/*
    public Dialog showDialogForValidation(int layout)
    {
        Dialog dialog = new Dialog(context, R.style.DialogAnimation);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(layout);
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    public Dialog setupCustomeDialogFromCenter(int layout) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.ThemeDialog;
        dialog.setContentView(layout);
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        return dialog;
    }*/

   /* public void showValidateDialog(String title, String msg, String approve, String disApprove){
        final Dialog validateDialog = showDialogForValidation(R.layout.layout_dailog);
        TextView txtTitle = (TextView) validateDialog.findViewById(R.id.txtTitle);
        TextView txtMessage = (TextView) validateDialog.findViewById(R.id.txtMessage);
        TextView btnApprove = (TextView) validateDialog.findViewById(R.id.btnApprove);
        TextView btnDisApprove = (TextView) validateDialog.findViewById(R.id.btnDisApprove);
        if(title != "")
            txtTitle.setText(title);
        if(msg != "")
            txtMessage.setText(msg);
        if(approve != "")
            btnApprove.setText(approve);
        if(disApprove != "") {
            btnDisApprove.setText(disApprove);
            btnDisApprove.setVisibility(View.VISIBLE);
        }else
            btnDisApprove.setVisibility(View.GONE);
        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateDialog != null)
                    validateDialog.dismiss();
            }
        });
        btnDisApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateDialog != null)
                    validateDialog.dismiss();
            }
        });
        validateDialog.show();
    }*/


    public static void showAlertDialog(Context context, String title, String msg) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    public void alert(String message, Context context) {
        android.app.AlertDialog.Builder bld = new android.app.AlertDialog.Builder(context);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        bld.create().show();
    }

/*    public static void showProgressDialog(Context context) {
        if (dialog != null) {
            if (dialog.isShowing())
                dialog.dismiss();
            dialog = null;
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_loader);
        dialog.show();

    }
        */

    public static void stopProgressDialog() {
        if (dialog != null) {
            if (dialog.isShowing())
                dialog.dismiss();
            dialog = null;
        }
    }

    public void showAlertDialog(String title, String msg, boolean isNegative, final Context context) {
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(msg);
        if (isNegative)
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.e("TAG", "onClick: " + dialog + " and which" + which);
                    response.negative();

                }
            });
        builder.setPositiveButton(isNegative ? "Yes" : "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                response.positive(null, 0);

            }
        });
        builder.show();
    }

    public static class CustomDialog extends Dialog implements View.OnClickListener {

        private final dialogResponse response;
        private final String[] featurerate;
        public Context c;
        public Dialog d;
        public TextView yes, no, alert_validation;
        private EditText comments;
        private RatingBar rating, feature_rateOne, feature_rateTwo, feature_rateThree, feature_rateFour, feature_rateFive, feature_rateSix;

        private TextView featuretxt_rateOne, featuretxt_rateTwo, featuretxt_rateThree, featuretxt_rateFour, featuretxt_rateFive, featuretxt_rateSix;


        public CustomDialog(Context c, String[] featurerate, dialogResponse response) {
            super(c);
            // TODO Auto-generated constructor stub
            this.c = c;
            this.featurerate = featurerate;
            this.response = (dialogResponse) response;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            if (c instanceof Login) {
                Log.e("TAG", "instanceof:Login -----");
                setContentView(R.layout.activity_custom_dialog_refer);
            } else {
                Log.e("TAG", "Not instanceof:Login -----");
                setContentView(R.layout.activity_custom_dialog);
                feature_rateOne = (RatingBar) findViewById(R.id.feature_rateOne);
                feature_rateTwo = (RatingBar) findViewById(R.id.feature_rateTwo);
                feature_rateThree = (RatingBar) findViewById(R.id.feature_rateThree);
                feature_rateFour = (RatingBar) findViewById(R.id.feature_rateFour);
                feature_rateFive = (RatingBar) findViewById(R.id.feature_rateFive);
                feature_rateSix = (RatingBar) findViewById(R.id.feature_rateSix);
                rating = (RatingBar) findViewById(R.id.rating);
                featuretxt_rateOne = (TextView) findViewById(R.id.featuretxt_rateOne);
                featuretxt_rateTwo = (TextView) findViewById(R.id.featuretxt_rateTwo);
                featuretxt_rateThree = (TextView) findViewById(R.id.featuretxt_rateThree);
                featuretxt_rateFour = (TextView) findViewById(R.id.featuretxt_rateFour);
                featuretxt_rateFive = (TextView) findViewById(R.id.featuretxt_rateFive);
                featuretxt_rateSix = (TextView) findViewById(R.id.featuretxt_rateSix);
               /* LayerDrawable rate = (LayerDrawable) rating.getProgressDrawable();
                rate.getDrawable(2).setColorFilter(Color.parseColor("#24b89e"), PorterDuff.Mode.SRC_ATOP);
                rate.getDrawable(0).setColorFilter(Color.parseColor("#dfdedf"), PorterDuff.Mode.SRC_ATOP);
                rate.getDrawable(1).setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);*/
                featuretxt_rateOne.setText(featurerate[0]);
                featuretxt_rateTwo.setText(featurerate[1]);
                featuretxt_rateThree.setText(featurerate[2]);
                featuretxt_rateFour.setText(featurerate[3]);
                featuretxt_rateFive.setText(featurerate[4]);
                featuretxt_rateSix.setText(featurerate[5]);
            }
            yes = (TextView) findViewById(R.id.submit);
            no = (TextView) findViewById(R.id.not_now);
            alert_validation = (TextView) findViewById(R.id.alert_validation);
            comments = (EditText) findViewById(R.id.comments);
            alert_validation.setVisibility(View.GONE);
            yes.setOnClickListener(this);
            no.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.submit:
                    String commentbox = null;
                    int rate = 0; int rate1 = 0; int rate2 = 0; int rate3 = 0; int rate4 = 0; int rate5 = 0; int rate6 = 0;
                    if (c instanceof Login) {
                        commentbox = comments.getText().toString().replaceAll("\\s{2,}", " ").trim();
                    } else {
                        commentbox = comments.getText().toString().replaceAll("\\s{2,}", " ").trim();
                        rate = (int)  rating.getRating();
                        rate1 = (int) feature_rateOne.getRating();
                        rate2 = (int) feature_rateTwo.getRating();
                        rate3 = (int) feature_rateThree.getRating();
                        rate4 = (int) feature_rateFour.getRating();
                        rate5 = (int) feature_rateFive.getRating();
                        rate6 = (int) feature_rateSix.getRating();
                    }
                    if (!TextUtils.isEmpty(commentbox)) {
                        response.positive(commentbox, rate ,rate1,rate2,rate3,rate4,rate5,rate6);
                        dismiss();
                    } else {
                        alert_validation.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.not_now:
                    response.negative();
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    }

    public interface dialogResponse {

        void positive(String data, int rate, int... featureRate);

        void negative();


    }


}



