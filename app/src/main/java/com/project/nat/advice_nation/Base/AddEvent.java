package com.project.nat.advice_nation.Base;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.project.nat.advice_nation.Https.GetApi;
import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.utils.BaseActivity;
import com.project.nat.advice_nation.utils.Constants;
import com.project.nat.advice_nation.utils.DialogUtils;
import com.project.nat.advice_nation.utils.NetworkUrl;


import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.project.nat.advice_nation.R.id.imageView;
import static com.project.nat.advice_nation.R.id.start;
import static com.project.nat.advice_nation.utils.Constants.REQUEST_OPEN_CAMERA;

public class AddEvent extends BaseActivity {

    private String imagePath = "";
    private int isGalleryOpen = -1;
    private String TAG = "AddEvent";
    private ImageView Upload_img;
    private LinearLayout linAdd;
    private EditText product_name, product_desc, product_feature, product_price;
    private View viewpart;
    private Uri photoUri = null;
    private SharedPreferences sharedPreferences;
    private Context context = AddEvent.this;
    private String bearerToken;
    private long user;
    private File photoFile;
    private MultipartEntity entity;
    private int productSubCategoryId;
    private TextInputLayout txt_input_product_price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupExplodeWindowAnimations(Gravity.BOTTOM);
        //  fullScreen();
        setContentView(R.layout.activity_add_event);
        getSupportActionBar().hide();
        initialise();

    }

    private void initialise() {
        productSubCategoryId = (int) getIntent().getIntExtra("productSubCategoryId", 0);
        viewpart = findViewById(android.R.id.content);
        Upload_img = (ImageView) findViewById(R.id.addevent_upload_img);
        product_name = (EditText) findViewById(R.id.product_name);
        product_desc = (EditText) findViewById(R.id.product_desc);
        product_feature = (EditText) findViewById(R.id.product_feature);
        product_price = (EditText) findViewById(R.id.product_price);
        txt_input_product_price = (TextInputLayout) findViewById(R.id.txt_input_product_price);
        linAdd = (LinearLayout) findViewById(R.id.addevent_linAdd);
        linAdd.setVisibility(View.VISIBLE);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        user = sharedPreferences.getLong("id", 0);
        bearerToken = sharedPreferences.getString("bearerToken", "");


    }

    private void fullScreen() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    public void onClickNewsPhoto(View view) {

        final Dialog dialog = new DialogUtils(this).setupCustomeDialogFromBottom(R.layout.dialog_gallery);
        ImageView imgCamera = (ImageView) dialog.findViewById(R.id.imgCamera);
        ImageView imgGallery = (ImageView) dialog.findViewById(R.id.imgGallery);
        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openCamera();
            }
        });
        imgGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openGallery();
            }
        });
        dialog.show();
    }

    private void openCamera() {
        isGalleryOpen = 1;
        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, this) &&
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, this) && checkPermission(Manifest.permission.CAMERA, this)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            try {

                //intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(intent, Constants.REQUEST_OPEN_CAMERA);


            } catch (Exception e) {
                e.printStackTrace();

            }

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Constants.REQUEST_PERMISSION_WRITE_STORAGE);
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }else {
            finish();
        }
    }

    private void openGallery() {
        isGalleryOpen = 0;

        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, this) &&
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, this) && checkPermission(Manifest.permission.CAMERA, this)) {

            try {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, Constants.REQUEST_OPEN_GALLERY);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Constants.REQUEST_PERMISSION_WRITE_STORAGE);
            }
        }

    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Advice_" + timeStamp + "_";
        File sdCard = new File(Environment.getExternalStorageDirectory() + "/AdviceNation/Images");
        if (!sdCard.exists())
            sdCard.mkdirs();
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    sdCard      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQUEST_PERMISSION_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isGalleryOpen == 0)
                    openGallery();
                else if (isGalleryOpen == 1)
                    openCamera();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_OPEN_CAMERA) {
                getCamaraImageUri(data);


            } else if (requestCode == Constants.REQUEST_OPEN_GALLERY) {
                getGalleryImageUri(data);
            }
        }
    }

    private Uri getGalleryImageUri(Intent data) {
        Uri uri = null;
        try {
            Uri imageUri = data.getData();
            String[] projection = {MediaStore.MediaColumns.DATA};
            Cursor cursor = getContentResolver().query(imageUri, projection, null, null,
                    null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            String selectedImagePath = cursor.getString(column_index);
            imagePath = selectedImagePath;
            File file = new File(imagePath);
            photoFile = file;
            uri = Uri.fromFile(new File(imagePath));
            Log.i(TAG, "Image Gallery" + imagePath);
            //bitmap = galleryCameraDialog.decodeUri(imageUri);
            displayImage(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }

    private Uri getCamaraImageUri(Intent data) {
        Uri uri = null;
        try {
            // if you use intent put extra  output then you not necessary to use output stream.
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

            photoFile = createImageFile();
            imagePath = photoFile.getAbsolutePath();
            Log.i(TAG, "getCamaraImageUri: " + imagePath);
            uri = Uri.fromFile(new File(imagePath));

            FileOutputStream fo = new FileOutputStream(photoFile);
            fo.write(bytes.toByteArray());
            fo.close();

            displayImage(uri);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }

    private void displayImage(Uri photoUri) {
        this.photoUri = photoUri;
        linAdd.setVisibility(View.GONE);
        Glide.with(this).load(photoUri).into(Upload_img);
    }


    public void onClickSubmit(View view) {


        if (photoUri == null) {
            showSnackbar(viewpart, "Please Select Image");
        } else if (TextUtils.isEmpty(product_name.getText().toString().trim())) {
            showSnackbar(viewpart, "Please Enter Product name");
        } else if (TextUtils.isEmpty(product_desc.getText().toString().trim())) {
            showSnackbar(viewpart, "Please Enter Product description");
        } else if (TextUtils.isEmpty(product_feature.getText().toString().trim())) {
            showSnackbar(viewpart, "Please Enter Product feature");
        } else if (TextUtils.isEmpty(product_price.getText().toString().trim())) {
            showSnackbar(viewpart, "Please Enter Product price");
        } else {
            progressDialogStart(this, "submitting...");
            callback();
        }


    }


    private void callback() {

        entity = new MultipartEntity();
//http://ec2-13-126-97-168.ap-south-1.compute.amazonaws.com:8080/AdviseNation/api/users/17041409/productSubCategory/1/product?productName=test&productDescription=stet&productFeatures=good&productPrice=400
        String URL = NetworkUrl.URL + user + "/productSubCategory/" + productSubCategoryId + "/product";
        Log.e(TAG, "callback: "+URL );
        MultipartRequest multipartRequest = new MultipartRequest(URL, bearerToken, null, photoFile, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                progressDialogStop();
                Log.e(TAG, "onResponse: " + response.statusCode);
                showSnackbarSuccess(viewpart,"Submission data successfully...");
                product_name.getText().clear();
                product_desc.getText().clear();
                product_feature.getText().clear();
                product_price.getText().clear();
                product_price.clearFocus();
             //   product_name.setFocusableInTouchMode(true);

                linAdd.setVisibility(View.VISIBLE);
                Glide.with(context).load(R.mipmap.ic_placeholder).into(Upload_img);
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //409-Unsupported image format with given content-type
                //400-412(image)-"Missing input parameters
                progressDialogStop();
                Log.e(TAG, "onErrorResponse: " + volleyError.networkResponse.statusCode);
                showSnackbarError(viewpart,"Submission data failed");
                //Toast.makeText(MainActivity.this, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
                ;
            }
        }) {
        };

        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(multipartRequest);
    }

    class MultipartRequest extends Request<NetworkResponse> {
        private final Response.Listener<NetworkResponse> mListener;
        private final Response.ErrorListener mErrorListener;
        private final Map<String, String> mHeaders;
        private final String bearerToken;
        ArrayList<String> img_path1;



        public MultipartRequest(String url, String bearerToken, Map<String, String> headers, File file, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {
            super(Method.POST, url, errorListener);
            this.mListener = listener;
            this.mErrorListener = errorListener;
            this.bearerToken = bearerToken;
            this.mHeaders = headers;
            buildMultipartEntity(file);

        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<String, String>();
           // headers.put("Content-Type", "text/html");
            headers.put("Authorization", "Bearer " + bearerToken);
            return headers;
        }


        private void buildMultipartEntity(File file) {
            try {
                FileBody fBody = new FileBody(file, "image/jpg");
                entity.addPart("productName", new StringBody(product_name.getText().toString().trim() + ""));
                entity.addPart("productDescription", new StringBody(product_desc.getText().toString().trim() + ""));
                entity.addPart("productFeatures", new StringBody(product_feature.getText().toString().trim() + ""));
                entity.addPart("productPrice", new StringBody(product_price.getText().toString().trim() + ""));
                entity.addPart("image", fBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        public String getBodyContentType() {
            return entity.getContentType().getValue();
        }

        @Override
        public byte[] getBody() throws AuthFailureError {

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                entity.writeTo(bos);
            } catch (IOException e) {
                VolleyLog.e("IOException writing to ByteArrayOutputStream");
            }
            return bos.toByteArray();
        }

        @Override
        protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {

            try {
                return Response.success(
                        response,
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (Exception e) {
                return Response.error(new ParseError(e));
            }
        }

        @Override
        protected void deliverResponse(NetworkResponse response) {

            Log.e("response ", " " + response.statusCode);
            if (response.statusCode == 200) {
                img_path1 = new ArrayList<String>();
            }
            mListener.onResponse(response);
        }


        @Override
        public void deliverError(VolleyError error) {

            mErrorListener.onErrorResponse(error);
        }


    }
}






