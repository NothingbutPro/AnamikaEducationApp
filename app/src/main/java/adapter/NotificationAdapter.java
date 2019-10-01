package adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anamika.Notification_detailActivity;
import com.anamika.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
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
 * Created by Rajesh on 2017-10-04.
 */

public class NotificationAdapter extends BaseAdapter {

    private Activity context;
    private JSONArray postItems;
    CommonClass common;
    JSONReader j_reader;

    Double cLat, cLog;
    int count = 0;
    TextView txtLikes;
    private int lastPosition = -1;
    ProgressDialog dialog;

    DisplayImageOptions options;
    ImageLoaderConfiguration imgconfig;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public NotificationAdapter(Activity act, JSONArray arraylist) {
        this.context = act;
        common = new CommonClass(act);
        j_reader = new JSONReader(act);
        postItems = arraylist;

        new loadDataTask().execute();

        File cacheDir = StorageUtils.getCacheDirectory(context);
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

        imgconfig = new ImageLoaderConfiguration.Builder(context)
                .build();
        ImageLoader.getInstance().init(imgconfig);
    }

    @Override
    public int getCount() {
        return postItems.length();
    }

    @Override
    public JSONObject getItem(int position) {
        try {
            return postItems.getJSONObject(position);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.row_notification, null);
        }

        lastPosition = position;

        try {
            JSONObject jObj = postItems.getJSONObject(position);

            ImageView iv_notific = (ImageView) convertView.findViewById(R.id.iv_notific_img);
            final String image_url = jObj.getString("noti_image");
            ImageLoader.getInstance().displayImage(ConstValue.IMG_NOTIFICATION_URL + image_url, iv_notific, options, animateFirstListener);

            TextView noti_title = (TextView) convertView.findViewById(R.id.tv_notific_title);
            final String title = jObj.getString("noti_title");
            noti_title.setText(title);

            TextView noti_description = (TextView) convertView.findViewById(R.id.tv_notific_msg);
            final String desc = jObj.getString("noti_description");
            if (desc.length() > 20) {
                noti_description.setText(desc.substring(0, 20) + "...");
            } else {
                noti_description.setText(desc);
            }

            TextView date = (TextView) convertView.findViewById(R.id.tv_notific_date);
            final String getdate = jObj.getString("date");
            date.setText(getdate);

            iv_notific.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showImage(image_url);
                }
            });

            CardView cardView = (CardView) convertView.findViewById(R.id.card_view);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent detailIntent = new Intent(context, Notification_detailActivity.class);
                    detailIntent.putExtra("title", title);
                    detailIntent.putExtra("date", getdate);
                    detailIntent.putExtra("desc", desc);
                    detailIntent.putExtra("image_path", image_url);
                    context.startActivity(detailIntent);
                }
            });

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return convertView;
    }

    class loadDataTask extends AsyncTask<Void, Void, String> {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

        public loadDataTask() {
            nameValuePairs.add(new BasicNameValuePair("school_id", j_reader.getJSONkeyString("student_data", "school_id")));
        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(context, "",
                    "Loading. Please wait...", true);
            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String responseString = null;

            try {

                JSONParser jParser = new JSONParser();
                String json = jParser.makeHttpRequest(ConstValue.GET_NOTIFICATION_URL, "POST", nameValuePairs);
                Log.e("responce", json);

                JSONObject jObj = new JSONObject(json);
                if (jObj.has("responce") && !jObj.getBoolean("responce")) {
                    responseString = jObj.getString("error");
                } else {
                    if (jObj.has("data")) {
                        postItems = jObj.getJSONArray("data");
                    } else {
                        responseString = "Not Data found";
                    }
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                responseString = e.getMessage();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return responseString;

        }


        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            } else {
                notifyDataSetChanged();
            }
            dialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
        }
    }

    private void showImage(String image) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_book_img);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

        ImageView iv_image_cancle = (ImageView) dialog.findViewById(R.id.iv_dialog_cancle);
        ImageView iv_image = (ImageView) dialog.findViewById(R.id.iv_dialog_img);

        ImageLoader.getInstance().displayImage(ConstValue.IMG_NOTIFICATION_URL + image, iv_image, options, animateFirstListener);

        iv_image_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

}