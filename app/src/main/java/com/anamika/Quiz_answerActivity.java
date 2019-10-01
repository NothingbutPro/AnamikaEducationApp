package com.anamika;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Config.ConstValue;
import util.CommonClass;
import util.DatabaseHandler;
import util.JSONParser;
import util.JSONReader;

public class Quiz_answerActivity extends CommonAppCompatActivity implements View.OnClickListener {

    private TextView timerTextView, tv_total_qus, tv_prev, tv_next, tv_qus, tv_current_ans;
    private TextView tv_ans1, tv_ans2, tv_ans3, tv_ans4, tv_ans5, tv_ans6;
    private RelativeLayout rl_ans1, rl_ans2, rl_ans3, rl_ans4, rl_ans5, rl_ans6;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    long countdownPeriod = 100000;

    private CommonClass common;
    private JSONReader j_reader;
    JSONObject objStudData;
    private JSONArray jsonArray;

    private int total_qus = 0;
    private int current_qus = 1;

    private String current_right_ans = "";
    private String selected_ans = "";
    private String qus_id = "";
    private String from_time = "";
    private String to_time = "";
    private String date = "";

    private boolean is_dialog_show = false;

    private String title, time, id;

    private DatabaseHandler db;

    private AlertDialog.Builder myAlertDialog;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_answer);

        db = new DatabaseHandler(this);

        j_reader = new JSONReader(this);
        common = new CommonClass(this);

        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        time = getIntent().getStringExtra("time");
        String total_qus = getIntent().getStringExtra("total_qus");

        getSupportActionBar().setTitle(title);

        myAlertDialog = new AlertDialog.Builder(this);

        timerTextView = (TextView) findViewById(R.id.textView7);
        tv_total_qus = (TextView) findViewById(R.id.tv_quiz_total_qus);
        tv_current_ans = (TextView) findViewById(R.id.tv_quiz_current_ans);
        tv_prev = (TextView) findViewById(R.id.tv_quiz_prev);
        tv_next = (TextView) findViewById(R.id.tv_quiz_next);
        tv_qus = (TextView) findViewById(R.id.tv_quiz_subject);

        tv_ans1 = (TextView) findViewById(R.id.tv_quiz_ans1);
        tv_ans2 = (TextView) findViewById(R.id.tv_quiz_ans2);
        tv_ans3 = (TextView) findViewById(R.id.tv_quiz_ans3);
        tv_ans4 = (TextView) findViewById(R.id.tv_quiz_ans4);
        tv_ans5 = (TextView) findViewById(R.id.tv_quiz_ans5);
        tv_ans6 = (TextView) findViewById(R.id.tv_quiz_ans6);

        rl_ans1 = (RelativeLayout) findViewById(R.id.rl_ans1);
        rl_ans2 = (RelativeLayout) findViewById(R.id.rl_ans2);
        rl_ans3 = (RelativeLayout) findViewById(R.id.rl_ans3);
        rl_ans4 = (RelativeLayout) findViewById(R.id.rl_ans4);
        rl_ans5 = (RelativeLayout) findViewById(R.id.rl_ans5);
        rl_ans6 = (RelativeLayout) findViewById(R.id.rl_ans6);
        String[] separated = time.split(":");
        if(separated[1].toString().contains("pm"))
        {
            separated[1] =  separated[1].replace("pm" , "");
            separated[1] =  separated[1].substring(0,separated[1].length()-1);
        }

      //  separated[1] = String.valueOf(separated[1].split(""));
        long min = Integer.parseInt(separated[0]);
        long sec = Integer.parseInt(separated[1]);

        long t = (min * 60L) + sec;

        long result = TimeUnit.SECONDS.toMillis(t);

        countdownPeriod = result;

        new getQuestionData(id).execute();

        tv_next.setOnClickListener(this);
        tv_prev.setOnClickListener(this);

        tv_ans1.setOnClickListener(this);
        tv_ans2.setOnClickListener(this);
        tv_ans3.setOnClickListener(this);
        tv_ans4.setOnClickListener(this);
        tv_ans5.setOnClickListener(this);
        tv_ans6.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        HashMap<String, String> map = new HashMap<String, String>();

        tv_ans1.setBackgroundResource(R.drawable.xml_left_rounded);
        tv_ans2.setBackgroundResource(R.drawable.xml_left_rounded);
        tv_ans3.setBackgroundResource(R.drawable.xml_left_rounded);
        tv_ans4.setBackgroundResource(R.drawable.xml_left_rounded);
        tv_ans5.setBackgroundResource(R.drawable.xml_left_rounded);
        tv_ans6.setBackgroundResource(R.drawable.xml_left_rounded);

        if (id == R.id.tv_quiz_prev) {

            if (current_qus > 1) {
                current_qus--;

                tv_current_ans.setText("" + current_qus);
                loadQuestion(current_qus - 1);
            }

        } else if (id == R.id.tv_quiz_next) {

            if (!selected_ans.isEmpty()) {
                if (current_qus < total_qus) {

                    current_qus++;

                    tv_current_ans.setText("" + current_qus);
                    loadQuestion(current_qus - 1);

                    new GetCommentAsynctask(current_qus, sharedPreferences.getString("selected_ans",""),id).execute();

                } else {
                    updateAnswerUI(qus_id);

                    showResult("");
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.select_answer), Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.tv_quiz_ans1) {
            selected_ans = "1";
            tv_ans1.setBackgroundResource(R.drawable.xml_left_rounded_selected);
            tv_ans1.setBackgroundColor(Color.GREEN);
            sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            map.put("ques_id", qus_id);
            map.put("r_ans", current_right_ans);
            map.put("user_ans", selected_ans);
            selected_ans = sharedPreferences.getString("selected_ans",selected_ans);
            db.setAns(map);

        } else if (id == R.id.tv_quiz_ans2) {
            selected_ans = "2";
            tv_ans2.setBackgroundResource(R.drawable.xml_left_rounded_selected);
            tv_ans2.setBackgroundColor(Color.GREEN);
            sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            map.put("ques_id", qus_id);
            map.put("r_ans", current_right_ans);
            map.put("user_ans", selected_ans);
            selected_ans = sharedPreferences.getString("selected_ans",selected_ans);
            db.setAns(map);

        } else if (id == R.id.tv_quiz_ans3) {
            selected_ans = "3";
            tv_ans3.setBackgroundResource(R.drawable.xml_left_rounded_selected);
            tv_ans3.setBackgroundColor(Color.GREEN);
            sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            map.put("ques_id", qus_id);
            map.put("r_ans", current_right_ans);
            map.put("user_ans", selected_ans);
            selected_ans = sharedPreferences.getString("selected_ans",selected_ans);
            db.setAns(map);

        } else if (id == R.id.tv_quiz_ans4) {
            selected_ans = "4";
            tv_ans4.setBackgroundResource(R.drawable.xml_left_rounded_selected);
            tv_ans4.setBackgroundColor(Color.GREEN);
            sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            map.put("ques_id", qus_id);
            map.put("r_ans", current_right_ans);
            map.put("user_ans", selected_ans);
            selected_ans = sharedPreferences.getString("selected_ans",selected_ans);
            db.setAns(map);

        } else if (id == R.id.tv_quiz_ans5) {
            selected_ans = "5";
            tv_ans5.setBackgroundResource(R.drawable.xml_left_rounded_selected);
            tv_ans5.setBackgroundColor(Color.GREEN);
            sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            map.put("ques_id", qus_id);
            map.put("r_ans", current_right_ans);
            map.put("user_ans", selected_ans);
            selected_ans = sharedPreferences.getString("selected_ans",selected_ans);
            db.setAns(map);

        } else if (id == R.id.tv_quiz_ans6) {
            selected_ans = "6";
            tv_ans6.setBackgroundResource(R.drawable.xml_left_rounded_selected);
            tv_ans6.setBackgroundColor(Color.GREEN);
            sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            map.put("ques_id", qus_id);
            map.put("r_ans", current_right_ans);
            map.put("user_ans", selected_ans);
            selected_ans = sharedPreferences.getString("selected_ans",selected_ans);
            db.setAns(map);
        }

    }

    private void startCounter() {
        timer = new CountDownTimer(countdownPeriod + 1000, 1) {

            @Override
            public void onTick(long millisUntilFinished) {
                //timerTextView.setText("Timer: " + millisUntilFinished / 1000);

                timerTextView.setText("" + String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                countdownPeriod = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                //submitData();

                if (!is_dialog_show) {
                    showResult(getResources().getString(R.string.oops_time_up));
                }

            }
        };
        timer.start();
    }

    private class getQuestionData extends AsyncTask<Void, Void, String> {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

        getQuestionData(String subject_id) {
            nameValuePairs.add(new BasicNameValuePair("subject_id", subject_id));
            nameValuePairs.add(new BasicNameValuePair("standard_id", j_reader.getJSONkeyString("student_data", "student_standard")));
            nameValuePairs.add(new BasicNameValuePair("school_id", j_reader.getJSONkeyString("student_data", "school_id")));
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String responseString = null;

            JSONParser jsonParser = new JSONParser();

            try {
                String json_responce = jsonParser.makeHttpRequest(ConstValue.GET_QUESTION_URL, "POST", nameValuePairs);
                Log.e("responce", json_responce);
                JSONObject jObj = new JSONObject(json_responce);
                if (jObj.has("responce") && !jObj.getBoolean("responce")) {
                    responseString = jObj.getString("error");
                } else {
                    if (jObj.has("data")) {
                        //objStudData = jObj.getJSONObject("data");
                        jsonArray = jObj.getJSONArray("data");
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

            if (success == null) {
                //loadCView();

                total_qus = jsonArray.length();
                tv_total_qus.setText("" + total_qus);
                tv_current_ans.setText("1");

                startCounter();
                loadQuestion(0);
            } else {
                Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
        }
    }

    private void loadQuestion(int position) {

        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);

            tv_qus.setText(jsonObject.getString("question"));
            current_right_ans = jsonObject.getString("r_ans");
            qus_id = jsonObject.getString("ques_id");


            rl_ans1.setVisibility(View.GONE);
            rl_ans2.setVisibility(View.GONE);
            rl_ans3.setVisibility(View.GONE);
            rl_ans4.setVisibility(View.GONE);
            rl_ans5.setVisibility(View.GONE);
            rl_ans6.setVisibility(View.GONE);

            if (!jsonObject.getString("ans_1").isEmpty()) {
                rl_ans1.setVisibility(View.VISIBLE);
                tv_ans1.setText(jsonObject.getString("ans_1"));
            }
            if (!jsonObject.getString("ans_2").isEmpty()) {
                rl_ans2.setVisibility(View.VISIBLE);
                tv_ans2.setText(jsonObject.getString("ans_2"));
            }
            if (!jsonObject.getString("ans_3").isEmpty()) {
                rl_ans3.setVisibility(View.VISIBLE);
                tv_ans3.setText(jsonObject.getString("ans_3"));
            }
            if (!jsonObject.getString("ans_4").isEmpty()) {
                rl_ans4.setVisibility(View.VISIBLE);
                tv_ans4.setText(jsonObject.getString("ans_4"));
            }
            if (!jsonObject.getString("ans_5").isEmpty()) {
                rl_ans5.setVisibility(View.VISIBLE);
                tv_ans5.setText(jsonObject.getString("ans_5"));
            }
            if (!jsonObject.getString("ans_6").isEmpty()) {
                rl_ans6.setVisibility(View.VISIBLE);
                tv_ans6.setText(jsonObject.getString("ans_6"));
            }

            updateAnswerUI(qus_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateAnswerUI(String id) {

        selected_ans = "";

        List<HashMap<String, String>> qus_ans = db.getAnsById(id);
        if (qus_ans != null && !qus_ans.isEmpty()) {

            String get_current_ans = qus_ans.get(0).get("user_ans");

            if (get_current_ans.equals("1")) {
                selected_ans = "1";
                tv_ans1.setBackgroundResource(R.drawable.xml_left_rounded_selected);
                tv_ans1.setBackgroundColor(Color.GREEN);
            } else if (get_current_ans.equals("2")) {
                selected_ans = "2";
                tv_ans2.setBackgroundResource(R.drawable.xml_left_rounded_selected);
                tv_ans2.setBackgroundColor(Color.GREEN);
            } else if (get_current_ans.equals("3")) {
                selected_ans = "3";
                tv_ans3.setBackgroundResource(R.drawable.xml_left_rounded_selected);
                tv_ans3.setBackgroundColor(Color.GREEN);
            } else if (get_current_ans.equals("4")) {
                selected_ans = "4";
                tv_ans4.setBackgroundResource(R.drawable.xml_left_rounded_selected);
                tv_ans4.setBackgroundColor(Color.GREEN);
            } else if (get_current_ans.equals("5")) {
                selected_ans = "5";
                tv_ans5.setBackgroundResource(R.drawable.xml_left_rounded_selected);
                tv_ans5.setBackgroundColor(Color.GREEN);
            } else if (get_current_ans.equals("6")) {
                selected_ans = "6";
                tv_ans6.setBackgroundResource(R.drawable.xml_left_rounded_selected);
                tv_ans6.setBackgroundColor(Color.GREEN);
            }

            Log.e("DATA ", "Total true: " + db.getAllTrueAns() + "\nTotal false: " +
                    db.getAllFalseAns() + "\nright ans: " + qus_ans.get(0).get("r_ans") + "\nuser ans: " + get_current_ans);
        }
    }

    private void showResult(String msg) {

        myAlertDialog.setCancelable(false);

        myAlertDialog.setTitle(msg + title + getResources().getString(R.string.exam_finish));
        myAlertDialog.setMessage(getResources().getString(R.string.total_questions) + total_qus +
                "\n" + getResources().getString(R.string.total_right_answer) + db.getAllTrueAns() +
                "\n" + getResources().getString(R.string.total_false_answer) + db.getAllFalseAns() +
                "\n" + getResources().getString(R.string.exam_time) + time);
        myAlertDialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                submitData();

                dialogInterface.dismiss();
                is_dialog_show = false;
            }
        });

        myAlertDialog.create();
        myAlertDialog.show();

    }

    private void submitData() {
        ArrayList<HashMap<String, String>> items = db.getAllAns();

        if (items.size() > 0) {
            final JSONArray passArray = new JSONArray();
            for (int i = 0; i < items.size(); i++) {
                HashMap<String, String> map = items.get(i);

                JSONObject jObjP = new JSONObject();

                try {
                    jObjP.put("attempt_qus_id", map.get("ques_id"));
                    jObjP.put("attempt_ans", map.get("user_ans"));
                    jObjP.put("attempt_r_ans", map.get("r_ans"));

                    passArray.put(jObjP);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            new setQuestionData(id, "" + db.getAllTrueAns(), time, passArray).execute();
        }
    }

    private class setQuestionData extends AsyncTask<Void, Void, String> {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        String response;

        setQuestionData(String subject_id, String total_right_ans, String total_time, JSONArray passArray) {

            nameValuePairs.add(new BasicNameValuePair("quiz_student_id", common.getSession(ConstValue.COMMON_KEY)));
            nameValuePairs.add(new BasicNameValuePair("quiz_subject_id", subject_id));
            nameValuePairs.add(new BasicNameValuePair("quiz_student_standard", j_reader.getJSONkeyString("student_data", "student_standard")));
            nameValuePairs.add(new BasicNameValuePair("quiz_school_id", j_reader.getJSONkeyString("student_data", "school_id")));
            nameValuePairs.add(new BasicNameValuePair("quiz_total_right_ans", total_right_ans));
            nameValuePairs.add(new BasicNameValuePair("quiz_student_time", total_time));
            nameValuePairs.add(new BasicNameValuePair("data", passArray.toString()));

            Log.e("DATA", passArray.toString());

        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String responseString = null;

            JSONParser jsonParser = new JSONParser();

            try {
                String json_responce = jsonParser.makeHttpRequest(ConstValue.SET_RESULT_URL, "POST", nameValuePairs);
                Log.e("responce", json_responce);
                JSONObject jObj = new JSONObject(json_responce);
                if (jObj.has("responce") && !jObj.getBoolean("responce")) {
                    responseString = jObj.getString("error");
                } else {
                    if (jObj.has("data")) {
                        response = jObj.getString("data");
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

            if (success == null) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                db.clearAns();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
        }
    }

    class GetCommentAsynctask extends AsyncTask<String, String, String> {
        String result = "",qus,ans,idd;
        SharedPreferences sharedPreferences= getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);;

        public GetCommentAsynctask(int current_qus, String selected_ans, int id) {
            this.qus= String.valueOf(current_qus);
            this.ans=selected_ans;
            this.idd= String.valueOf(id);
        }




        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            /*arComentChat = new ArrayList<>();*/

        }

        @Override
        protected String doInBackground(String... params) {

            String urlParameters = "";

            try {
                urlParameters = "standard_id=" + URLEncoder.encode(sharedPreferences.getString("standard_Id",""), "UTF-8")+
                        "&school_id="+URLEncoder.encode(sharedPreferences.getString("school_Id",""),"UTF-8")+
                        "&student_id="+URLEncoder.encode(sharedPreferences.getString("student_Id",""),"UTF-8")+
                        "&user_ans="+URLEncoder.encode(ans,"UTF-8")+
                        "&ques_id="+URLEncoder.encode(qus,"UTF-8")+
                        "&user_time="+URLEncoder.encode("30","UTF-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String register_url= "http://www.spellclasses.co.in/education_demo/index.php/Api/next_questionBysubject?"+urlParameters;
            Log.e("sever_url>>>>>>>>>", register_url);
            result = HttpHandler.makeServiceCall(register_url);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

                super.onPostExecute(result);
            Log.e("getcomment_url", ""+result);
            System.out.println("getcomment_url" + result);
            }
        }










    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
    }
}
