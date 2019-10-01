package adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anamika.ConnectivityChangeReceiver;
import com.anamika.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import Config.ConstValue;
import util.CommonClass;
import util.JSONParser;
import util.JSONReader;
import java.util.Calendar;

/**
 * Created by Rajesh on 2017-08-09.
 */

public class Quiz_subject_rv_adapter extends BaseAdapter {

    private Activity context;
    //private ArrayList<HashMap<String, String>> postItems;
    private JSONArray postItems;
    CommonClass common;
    JSONReader j_reader;

    Double cLat, cLog;
    int count = 0;
    TextView txtLikes;
    private int lastPosition = -1;
    ProgressDialog dialog;
    String from_time,to_time,date;

    public Quiz_subject_rv_adapter(Activity act, JSONArray arraylist) {
        this.context = act;
        common = new CommonClass(act);
        j_reader = new JSONReader(act);
        postItems = arraylist;


        if (ConnectivityChangeReceiver.isConnected(context)) {
            new loadDataTask().execute();
        }else {
            Toast.makeText(context, "no internet", Toast.LENGTH_SHORT).show();
        }

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
            convertView = mInflater.inflate(R.layout.row_quiz_subject, null);
        }

        lastPosition = position;

        try {
            JSONObject jObj = postItems.getJSONObject(position);

            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

            RelativeLayout rl = (RelativeLayout) convertView.findViewById(R.id.rl_quiz_subject);
            rl.setBackgroundColor(color);

            TextView txtsubject = (TextView) convertView.findViewById(R.id.tv_quiz_subject);
        try {
            txtsubject.setText(jObj.getString("subject_title"));
          //  from_time = jObj.getString("from_time");
           // to_time = jObj.getString("to_time");
            to_time = jObj.getString("quiz_time");
          //  date = jObj.getString("date");

            Date currentTime = Calendar.getInstance().getTime();

            SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
            Date date1 = null, date2 = null;
            try {
                date1 = format.parse("08:00 pm");
                date2 = format.parse("05:30 pm");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long mills = date1.getTime() - date2.getTime();
            Log.v("Data1", "" + date1.getTime());

            Log.v("Data2", "" + date2.getTime());
            int hours = (int) (mills / (1000 * 60 * 60));
            int mins = (int) (mills / (1000 * 60)) % 60;

            String diff = hours + ":" + mins; // updated value every1 second
            from_time = diff;
            // txtCurrentTime.setText(diff);
            if (from_time.equals(currentTime)) {
                Toast.makeText(context, "tHEY ARE EQUAL", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "nO tHEY ARE EQUAL", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

            LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.ll_quiz_subject);

            if (jObj.getString("quiz_total_right_ans").equals("null")) {
                ll.setVisibility(View.GONE);
            }else {
                ll.setVisibility(View.VISIBLE);
                TextView txttotal_right = (TextView) convertView.findViewById(R.id.tv_quiz_subject_total_right);
                txttotal_right.setText(jObj.getString("quiz_total_right_ans"));

                TextView txttotal_quiz = (TextView) convertView.findViewById(R.id.tv_quiz_subject_total);
                txttotal_quiz.setText(jObj.getString("total_qes"));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return convertView;
    }

    class loadDataTask extends AsyncTask<Void, Void, String> {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

        public loadDataTask() {
            nameValuePairs.add(new BasicNameValuePair("student_id", common.getSession(ConstValue.COMMON_KEY)));
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
                String json = jParser.makeHttpRequest(ConstValue.GET_SUBJECT_URL, "POST", nameValuePairs);
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
}
