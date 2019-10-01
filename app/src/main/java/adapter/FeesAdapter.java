package adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.anamika.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Config.ConstValue;
import util.CommonClass;
import util.JSONParser;
import util.JSONReader;

/**
 * Created by Rajesh on 2017-09-04.
 */

public class FeesAdapter extends BaseAdapter {

    private Activity context;
    private JSONArray postItems;
    CommonClass common;
    JSONReader j_reader;

    Double cLat, cLog;
    int count = 0;
    TextView txtLikes;
    private int lastPosition = -1;
    ProgressDialog dialog;

    public FeesAdapter(Activity act, JSONArray arraylist) {
        this.context = act;
        common = new CommonClass(act);
        j_reader = new JSONReader(act);
        postItems = arraylist;

        new FeesAdapter.loadDataTask().execute();
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
            convertView = mInflater.inflate(R.layout.row_fees, null);
        }

        lastPosition = position;

        try {
            JSONObject jObj = postItems.getJSONObject(position);

            TextView txtTitle = (TextView) convertView.findViewById(R.id.tv_fees_title);
            txtTitle.setText(jObj.getString("title"));

            TextView txtYear = (TextView) convertView.findViewById(R.id.tv_fees_year);
            txtYear.setText(jObj.getString("year"));

            TextView txtDate = (TextView) convertView.findViewById(R.id.tv_fees_date);
            txtDate.setText(jObj.getString("pay_date"));

            TextView txtAmount = (TextView) convertView.findViewById(R.id.tv_fees_amount);
            txtAmount.setText(jObj.getString("fee_amount"));

            TextView txtpayAmount = (TextView) convertView.findViewById(R.id.tv_fees_pay_amount);
            txtpayAmount.setText(jObj.getString("pay_fee_amount"));

            int total_left = Integer.parseInt(jObj.getString("fee_amount")) - Integer.parseInt(jObj.getString("pay_fee_amount"));

            TextView txtLeftAmount = (TextView) convertView.findViewById(R.id.tv_fees_left_amount);
            txtLeftAmount.setText(""+total_left);

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
                String json = jParser.makeHttpRequest(ConstValue.GET_FEES_URL, "POST", nameValuePairs);
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