package adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
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

import com.anamika.R;
import com.anamika.ViewBookActivity;
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
import util.FileDownloader;
import util.JSONParser;
import util.JSONReader;

/**
 * Created by Rajesh on 2017-09-05.
 */

public class BookAdapter extends BaseAdapter {

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

    public BookAdapter(Activity act, JSONArray arraylist) {
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
            convertView = mInflater.inflate(R.layout.row_book, null);
        }

        lastPosition = position;

        try {
            JSONObject jObj = postItems.getJSONObject(position);

            ImageView iv_book = (ImageView) convertView.findViewById(R.id.iv_book_img);
            final String image_url = jObj.getString("book_thumb");
            ImageLoader.getInstance().displayImage(ConstValue.IMG_BOOK_URL + image_url, iv_book, options, animateFirstListener);

            TextView txtTitle = (TextView) convertView.findViewById(R.id.tv_book_title);
            final String title = jObj.getString("book_title");
            txtTitle.setText(title);

            TextView txtAuthor = (TextView) convertView.findViewById(R.id.tv_book_author);
            txtAuthor.setText(jObj.getString("book_author"));

            TextView txtDesc = (TextView) convertView.findViewById(R.id.tv_book_desc);
            final String desc = jObj.getString("book_description");

            TextView txtView = (TextView) convertView.findViewById(R.id.tv_book_view);
            final String pdf_url = jObj.getString("book_file");

            TextView txtDownload = (TextView) convertView.findViewById(R.id.tv_book_download);

            txtDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDescDialog(title, desc);
                }
            });

            txtView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent viewIntent = new Intent(context, ViewBookActivity.class);
                    viewIntent.putExtra("book_pdf", pdf_url);
                    viewIntent.putExtra("title", title);
                    context.startActivity(viewIntent);
                }
            });

            txtDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File SDCardRoot = Environment.getExternalStorageDirectory();
                    // create a new file, to save the downloaded file
                    File file = new File(SDCardRoot + "/Education_PDF/" + pdf_url);
                    if (file.exists()) {
                        Toast.makeText(context, "This file is already downloaded", Toast.LENGTH_SHORT).show();
                    } else {
                        new DownloadFile().execute(ConstValue.PDF_BOOK_URL + pdf_url, pdf_url);
                    }
                }
            });

            iv_book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showImage(image_url);
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
            nameValuePairs.add(new BasicNameValuePair("standard_id", j_reader.getJSONkeyString("student_data", "student_standard")));
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
                String json = jParser.makeHttpRequest(ConstValue.GET_BOOK_URL, "POST", nameValuePairs);
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

    private void showDescDialog(String book_name, String desc) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(book_name)
                .setMessage(desc)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
        });

        dialog.show();
    }

    private void showImage(String image) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_book_img);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

        ImageView iv_image_cancle = (ImageView) dialog.findViewById(R.id.iv_dialog_cancle);
        ImageView iv_image = (ImageView) dialog.findViewById(R.id.iv_dialog_img);

        ImageLoader.getInstance().displayImage(ConstValue.IMG_BOOK_URL + image, iv_image, options, animateFirstListener);

        iv_image_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


    private class DownloadFile extends AsyncTask<String, Integer, String> {

        ProgressDialog barProgressDialog;

        @Override
        protected String doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "Education_PDF");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);

            File file = new File(Environment.getExternalStorageDirectory(), fileName);
            return file.getAbsolutePath();
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            barProgressDialog = new ProgressDialog(context);
            barProgressDialog.setTitle("Downloading File ...");
            barProgressDialog.setMessage("Download in progress ...");
            barProgressDialog.show();
            barProgressDialog.setCancelable(false);
            barProgressDialog.setCanceledOnTouchOutside(false);
            super.onPreExecute();
        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
        }

        protected void setMaxProgrss(int maxval) {
            barProgressDialog.setMax(maxval);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            barProgressDialog.setMessage("Total  File size  : "
                    + " KB\n\nDownloading  " + (int) values[0]
                    + "% complete");


            super.onProgressUpdate(values);
        }

        protected void onPostExecute(String result) {
            barProgressDialog.dismiss();
            if (result != null)
                Toast.makeText(context, "PDF downloaded", Toast.LENGTH_SHORT).show();
        }

    }

}