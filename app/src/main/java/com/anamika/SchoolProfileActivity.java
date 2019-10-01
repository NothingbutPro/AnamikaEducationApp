package com.anamika;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Config.ConstValue;
import util.AnimateFirstDisplayListener;
import util.CommonClass;
import util.JSONParser;
import util.JSONReader;

/**
 * Created by Rajesh Dabhi on 19/6/2017.
 */

public class SchoolProfileActivity extends CommonAppCompatActivity implements View.OnClickListener {

    CommonClass common;
    JSONReader j_reader;
    JSONObject objStudData;
    DisplayImageOptions options;
    ImageLoaderConfiguration imgconfig;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private TextView txtphone1, txtphone2, txtemail, txtfacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.color_11));
        }

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(ProfileActivity.this, EnquiryActivity.class);
                startActivity(intent);
            }
        });*/

        File cacheDir = StorageUtils.getCacheDirectory(this);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.logoags)
                .showImageForEmptyUri(R.drawable.logoags)
                .showImageOnFail(R.drawable.logoags)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .imageScaleType(ImageScaleType.NONE)
                .build();

        imgconfig = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(imgconfig);
        Toast.makeText(this, ""+getIntent().getStringExtra("school_Id").toString(), Toast.LENGTH_SHORT).show();

        j_reader = new JSONReader(this);
        common = new CommonClass(this);
        new getSchoolData().execute();
    }

    public void loadCView() {
        setContentView(R.layout.activity_school_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        // collapsToolbar.setBackgroundResource(R.drawable.ic_home_logo);
//collapsToolbar.setBackgroundDrawable();
        try {
            //Bitmap bitmap = ImageLoader.getInstance().loadImageSync(ConstValue.BASE_URL+"/uploads/studentphoto/"+ objStudData.getString("student_photo"));
            //BitmapDrawable background = new BitmapDrawable(bitmap);
            //collapsToolbar.setBackgroundDrawable(background);
            ImageView top_image = (ImageView) findViewById(R.id.top_image);
           ImageLoader.getInstance().displayImage(ConstValue.BASE_URL + "/uploads/profile/" + objStudData.getString("school_logo"), top_image, options, animateFirstListener);

            //getSupportActionBar().setTitle(objStudData.getString("student_name"));
            getSupportActionBar().setTitle("");

            TextView txtaddress = (TextView) findViewById(R.id.address);
            txtaddress.setText(objStudData.getString("school_address"));

            TextView txtstate = (TextView) findViewById(R.id.state);
            txtstate.setText(objStudData.getString("school_state"));

            TextView txtcity = (TextView) findViewById(R.id.city);
            txtcity.setText(objStudData.getString("school_city"));

            TextView txtpostal = (TextView) findViewById(R.id.postal_code);
            txtpostal.setText(objStudData.getString("school_postal_code"));

            txtphone1 = (TextView) findViewById(R.id.phone_1);
            txtphone1.setText(objStudData.getString("school_phone1"));

            txtphone2 = (TextView) findViewById(R.id.phone_2);
            txtphone2.setText(objStudData.getString("school_phone2"));

            txtemail = (TextView) findViewById(R.id.email);
            txtemail.setText(objStudData.getString("school_email"));

            TextView txtfax = (TextView) findViewById(R.id.fax);
            txtfax.setText(objStudData.getString("school_fax"));

            txtfacebook = (TextView) findViewById(R.id.facebook);
            txtfacebook.setText(objStudData.getString("school_facebook"));

            TextView txtowner = (TextView) findViewById(R.id.owner);
            txtowner.setText(objStudData.getString("school_person_name"));

            txtphone1.setOnClickListener(this);
            txtphone2.setOnClickListener(this);
            txtemail.setOnClickListener(this);
            txtfacebook.setOnClickListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.phone_1) {
            showCallDialog(txtphone1.getText().toString());
        } else if (id == R.id.phone_2) {
            showCallDialog(txtphone2.getText().toString());
        } else if (id == R.id.email) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", txtemail.getText().toString(), null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } else if (id == R.id.facebook) {
            Intent viewIntent = new Intent(SchoolProfileActivity.this, ViewBookActivity.class);
            viewIntent.putExtra("book_pdf", txtfacebook.getText().toString());
            viewIntent.putExtra("title", getResources().getString(R.string.app_name));
            startActivity(viewIntent);
        }
    }

    // calling cilent phone number
    private void showCallDialog(final String user_phone) {

        AlertDialog.Builder builder = new AlertDialog.Builder(SchoolProfileActivity.this);
        builder.setTitle(getResources().getString(R.string.note_call))
                .setMessage(getResources().getString(R.string.owner_phone) + user_phone)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // call intent for call given phone number
                        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + user_phone));
                        startActivity(callIntent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
        //.show();

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        dialog.show();
    }


    public class getSchoolData extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String responseString = null;
           // Log.e("School dt" , ""+common.getSession(ConstValue.COMMON_KEY));
            Log.e("School_dt" , ""+getIntent().getStringExtra("school_Id"));
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

            //nameValuePairs.add(new BasicNameValuePair("school_id", common.getSession(ConstValue.COMMON_KEY)));
            nameValuePairs.add(new BasicNameValuePair("school_id","28"));
            String schlid = nameValuePairs.get(0).toString();
            JSONParser jsonParser = new JSONParser();

            try {
                String json_responce = jsonParser.makeHttpRequest("https://anamikaeducation.com/education_portal/index.php/api/get_school_profile", "POST", nameValuePairs);
                Log.e("responce", json_responce);
                JSONObject jObj = new JSONObject(json_responce);
                if (jObj.has("responce") && !jObj.getBoolean("responce")) {
                    responseString = jObj.getString("error");
                } else {
                    if (jObj.has("data")) {
                        objStudData = jObj.getJSONObject("data");

                    } else {
                        responseString = "User not found";
                    }
                }


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                responseString = e.getMessage();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                responseString = e.getMessage();
                e.printStackTrace();
            }

            // TODO: register the new account here.
            return responseString;
        }

        @Override
        protected void onPostExecute(final String success) {
              // final  String str = success;
            if (success == null) {

                loadCView();
            } else {
                Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
        }
    }

}
