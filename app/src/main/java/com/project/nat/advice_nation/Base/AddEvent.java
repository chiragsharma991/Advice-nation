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
    private Context context=AddEvent.this;
    private String bearerToken;
    private long user;
    private File photoFile;
    private MultipartEntity entity;


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
        viewpart = findViewById(android.R.id.content);
        Upload_img = (ImageView) findViewById(R.id.addevent_upload_img);
        product_name = (EditText) findViewById(R.id.product_name);
        product_desc = (EditText) findViewById(R.id.product_desc);
        product_feature = (EditText) findViewById(R.id.product_feature);
        product_price = (EditText) findViewById(R.id.product_price);
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
        Log.e(TAG, "openCamera: ");
        isGalleryOpen = 1;
        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, this) &&
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, this) && checkPermission(Manifest.permission.CAMERA, this)) {

            Log.e(TAG, "checkPermission: true");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            try {

                //intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(intent, Constants.REQUEST_OPEN_CAMERA);


            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "catch openCamera: " + e.getMessage());

            }

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.e(TAG, "checkPermission: false");

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Constants.REQUEST_PERMISSION_WRITE_STORAGE);
            }
        }

    }

    private void openGallery() {
        isGalleryOpen = 0;
        Log.e(TAG, "openGallery: ");


        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, this) &&
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, this) && checkPermission(Manifest.permission.CAMERA, this)) {

            try {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, Constants.REQUEST_OPEN_GALLERY);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "catch openGallery: " + e.getMessage());
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
            File file=new File(imagePath);
            photoFile=file;
            Log.i(TAG, "getGalleryImageUri: "+photoFile.getAbsolutePath());
            uri = Uri.fromFile(new File(imagePath));
            Log.e(TAG, "Image Gallery" + imagePath);
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
            Log.i(TAG, "getCamaraImageUri: "+imagePath );
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
           // progressDialogStart(this, "submitting...");
            callback();
        }



/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }else {
            finish();
        }*/
    }

    private void callback() {

         entity = new MultipartEntity();

        //converting image to base64 string
   /*     Bitmap bitmap = null;
        File file = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
            file = new File(String.valueOf(photoUri));
            Log.e(TAG, "bitmap: "+bitmap );
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);*/
        String URL="http://ec2-13-126-97-168.ap-south-1.compute.amazonaws.com:8080/AdviseNation/api/users/17041409/productSubCategory/3/product";
//sending image to server
       // StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            MultipartRequest multipartRequest = new MultipartRequest(URL,bearerToken, null, photoFile, new Response.Listener<NetworkResponse>() {

                @Override
                public void onResponse(NetworkResponse response) {
                    Log.e(TAG, "onResponse: "+response.toString() );
                }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, "onErrorResponse: "+volleyError.getMessage() );

                //Toast.makeText(MainActivity.this, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
                ;
            }
        }) {
       /*     @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + bearerToken);
                return headers;            }

            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("productName", product_name.getText().toString().trim());
                parameters.put("productDescription", product_desc.getText().toString().trim());
                parameters.put("productFeatures", product_feature.getText().toString().trim());
                parameters.put("productPrice", product_price.getText().toString().trim());
                parameters.put("image", imageString);
                return parameters;
            }*/
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


        public static final String KEY_PICTURE = "mypicture";
        public static final String KEY_PICTURE_NAME = "filename";

        public MultipartRequest(String url, String bearerToken, Map<String, String> headers, File file, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {
            super(Method.POST, url, errorListener);
            this.mListener = listener;
            this.mErrorListener = errorListener;
            this.bearerToken = bearerToken;
            this.mHeaders = headers;
            buildMultipartEntity(file);
          //  this.mHttpEntity = buildMultipartEntity(file);
          //  System.out.println(mHttpEntity.getContentType().getValue());
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<String, String>();
            //headers.put("Content-Type", "text/plain; charset=utf8");

           // headers.put("Content-Type", "application/json");
            headers.put("Authorization", "Bearer " + bearerToken);
            return headers;        }

/*

        private HttpEntity buildMultipartEntity(File file) {

             MultipartEntity entity = new MultipartEntity();


            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            String fileName = file.getName();
            System.out.println(fileName);
            FileBody fileBody = new FileBody(file);//
            builder.addPart("image", fileBody);
            builder.addTextBody("productName",  product_name.getText().toString().trim());
            builder.addTextBody("productDescription", product_desc.getText().toString().trim());
            builder.addTextBody("productFeatures",  product_feature.getText().toString().trim());
            builder.addTextBody("productPrice", product_price.getText().toString().trim());
            return builder.build();
        }

        @Override
        public String getBodyContentType() {
            return mHttpEntity.getContentType().getValue();
        }

        @Override
        public byte[] getBody() throws AuthFailureError {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                mHttpEntity.writeTo(bos);
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
            mListener.onResponse(response);
        }

        @Override
        public void deliverError(VolleyError error) {
            mErrorListener.onErrorResponse(error);
        }
    }




*/












    private void buildMultipartEntity(File file)
    {




        try
        {


            FileBody fBody = new FileBody(file,"image/jpg");
            entity.addPart("productName", new StringBody(product_name.getText().toString().trim() + ""));
            entity.addPart("productDescription", new StringBody(product_desc.getText().toString().trim() + ""));
            entity.addPart("productFeatures", new StringBody(product_feature.getText().toString().trim() + ""));
            entity.addPart("productPrice", new StringBody(product_price.getText().toString().trim() + ""));
            entity.addPart("image", fBody);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String getBodyContentType()
    {
        return entity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError
    {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try
        {
            entity.writeTo(bos);
            Log.e("came here "," "+entity);
        }
        catch (IOException e)
        {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response)
    {

        try {
            return Response.success(
                    response,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(NetworkResponse response)
    {

        Log.e("response "," "+response.statusCode);
        if(response.statusCode == 200){
            img_path1 = new ArrayList<String>();
        }
        mListener.onResponse(response);
    }

//        @Override
//        protected VolleyError parseNetworkError(VolleyError volleyError) {
//            return super.parseNetworkError(volleyError);
//        }

    @Override
    public void deliverError(VolleyError error) {

        mErrorListener.onErrorResponse(error);
    }




}
}






