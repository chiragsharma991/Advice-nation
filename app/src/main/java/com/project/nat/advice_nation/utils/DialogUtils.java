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

import com.project.nat.advice_nation.R;

import org.json.JSONObject;

/**
 * Created by Chari on 7/7/2017.
 */

public class DialogUtils {

    private dialogResponse response;
    private Context context;
    private  static Dialog dialog;

    public DialogUtils(Context context){
        this.context=context;
    }
    public DialogUtils(Context context, dialogResponse response){
        this.context=context;
        this.response = (dialogResponse)response;

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


    public static void showAlertDialog(Context context, String title, String msg){
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

    public  void showAlertDialog(String title, String msg, boolean isNegative, final Context context) {
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(msg);
        if (isNegative)
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.e("TAG", "onClick: "+dialog+" and which"+which );
                    response.negative();

                }
            });
        builder.setPositiveButton(isNegative ? "Yes" : "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                response.positive(null);

            }
        });
        builder.show();
    }

    public static class CustomDialog extends Dialog implements View.OnClickListener{

        private final dialogResponse response;
        public Context c;
        public Dialog d;
        public TextView yes, no,alert_validation;
        private EditText comments;
        private RatingBar reating;

        public CustomDialog(Context c, dialogResponse response) {
            super(c);
            // TODO Auto-generated constructor stub
            this.c = c;
            this.response = (dialogResponse)response;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            setContentView(R.layout.activity_custom_dialog);
            yes = (TextView) findViewById(R.id.submit);
            no = (TextView) findViewById(R.id.not_now);
            alert_validation = (TextView) findViewById(R.id.alert_validation);
            comments = (EditText) findViewById(R.id.comments);
            reating = (RatingBar)findViewById(R.id.reating);
            LayerDrawable rate = (LayerDrawable)reating.getProgressDrawable();
            rate.getDrawable(2).setColorFilter(Color.parseColor("#24b89e"), PorterDuff.Mode.SRC_ATOP);
            rate.getDrawable(0).setColorFilter(Color.parseColor("#dfdedf"), PorterDuff.Mode.SRC_ATOP);
            rate.getDrawable(1).setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
            alert_validation.setVisibility(View.GONE);
            yes.setOnClickListener(this);
            no.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.submit:
                    String commentbox=comments.getText().toString().replaceAll("\\s{2,}", " ").trim();
                    if(!TextUtils.isEmpty(commentbox)){
                        response.positive(commentbox);
                        dismiss();
                    }else{
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

        void positive(String data);

        void negative();


    }


}



