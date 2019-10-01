package adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import java.util.ArrayList;
import java.util.List;

import Config.ConstValue;
import util.CommonClass;
import util.JSONParser;
import util.JSONReader;



/**
 * Created by Rajesh on 2017-08-18.
 */

public class Quiz_result_adapter extends BaseAdapter {

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Quiz_result_adapter(Activity act, JSONArray arraylist, String subject_id) {
        this.context = act;
        common = new CommonClass(act);
        j_reader = new JSONReader(act);
        postItems = arraylist;

        if (ConnectivityChangeReceiver.isConnected(context)) {
            new loadDataTask(subject_id).execute();
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
            convertView = mInflater.inflate(R.layout.row_quiz_result, null);
        }

        lastPosition = position;

        try {
            JSONObject jObj = postItems.getJSONObject(position);

            TextView txtnum = (TextView) convertView.findViewById(R.id.tv_quiz_result_number);
            txtnum.setText((position + 1) + ")");

            TextView txtquiz = (TextView) convertView.findViewById(R.id.tv_quiz_result_quiz);
            txtquiz.setText(jObj.getString("question"));

            TextView txtyour = (TextView) convertView.findViewById(R.id.tv_quiz_result_your);

            if (jObj.getString("attempt_ans").equals("null")) {
                txtyour.setText(context.getResources().getString(R.string.not_answer));
            } else {
                txtyour.setText(jObj.getString("ans_" + jObj.getString("attempt_ans")));
            }

            TextView txtright = (TextView) convertView.findViewById(R.id.tv_quiz_result_right);
            txtright.setText(jObj.getString("ans_" + jObj.getString("r_ans")));

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return convertView;
    }

    class loadDataTask extends AsyncTask<Void, Void, String> {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

        public loadDataTask(String subject_id) {
            nameValuePairs.add(new BasicNameValuePair("student_id", common.getSession(ConstValue.COMMON_KEY)));
            nameValuePairs.add(new BasicNameValuePair("subject_id", subject_id));
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
                String json = jParser.makeHttpRequest(ConstValue.GET_QUIZ_RESULT_URL, "POST", nameValuePairs);
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
