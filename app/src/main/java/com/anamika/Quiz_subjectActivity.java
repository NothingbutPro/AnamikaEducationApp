package com.anamika;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import adapter.Quiz_subject_rv_adapter;
import util.DatabaseHandler;

public class Quiz_subjectActivity extends CommonAppCompatActivity {

    private ListView lv_subject;
    Quiz_subject_rv_adapter adapter;
    private String SCHOOL_ID;
    private String STUDENT_ID;
    private String STANDARD_ID;
    String school_Id,student_Id,standard_Id;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_subject);

        lv_subject = (ListView) findViewById(R.id.lv_quiz_subject);

        loadSubjectData();

        sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        school_Id = sharedPreferences.getString("school_Id","");
        student_Id = sharedPreferences.getString("student_Id","");
        standard_Id = sharedPreferences.getString("standard_Id" , "");
        SCHOOL_ID= school_Id;STUDENT_ID=student_Id; STANDARD_ID = standard_Id;
        Log.e("school_Id",school_Id);
        Log.e("student_Id",student_Id);


        lv_subject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DatabaseHandler db = new DatabaseHandler(Quiz_subjectActivity.this);
                db.clearAns();

                try {
                    String id = adapter.getItem(i).getString("subject_id");
                    String title = adapter.getItem(i).getString("subject_title");
                    String time = adapter.getItem(i).getString("quiz_time");
                    String total_qus = adapter.getItem(i).getString("total_qes");
                    String total_right_ans = adapter.getItem(i).getString("quiz_total_right_ans");

                    if (total_right_ans.equals("null")) {
                        showMessage(total_qus, time, title, id);
                    } else {
                        Intent ii = new Intent(Quiz_subjectActivity.this, Quiz_resultActivity.class);
                        ii.putExtra("id", id);
                        ii.putExtra("title", title);
                        startActivity(ii);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void showMessage(final String total_qus, final String time, final String title, final String id) {

        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(Quiz_subjectActivity.this);
        myAlertDialog.setCancelable(false);

        myAlertDialog.setTitle(getResources().getString(R.string.are_you_sure));
        myAlertDialog.setMessage(getResources().getString(R.string.total_questions) + total_qus +
                "\n" + getResources().getString(R.string.exam_time) + time);
        myAlertDialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                Intent ii = new Intent(Quiz_subjectActivity.this, Quiz_answerActivity.class);
                ii.putExtra("title", title);
                ii.putExtra("time", time);
                ii.putExtra("total_qus", total_qus);
                ii.putExtra("id", id);
                startActivity(ii);
            }
        });

        myAlertDialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = myAlertDialog.create();
        if (!dialog.isShowing()) {
            dialog.show();
        }

    }

    //==========================================================================

    class GetquestionBysubject extends AsyncTask<String, String, String> {

        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Quiz_subjectActivity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
            /*arComentChat = new ArrayList<>();*/
        }

        @Override
        protected String doInBackground(String... params) {

            String sever_url = "http://www.spellclasses.co.in/education_demo/index.php/Api/get_questionBysubject?standard_id="+STANDARD_ID+"&school_id="+SCHOOL_ID+"&student_id="+STANDARD_ID;

            Log.e("sever_url>>>>>>>>>", sever_url);
            output = HttpHandler.makeServiceCall(sever_url);
            Log.e("getcomment_url", output);
            System.out.println("getcomment_url" + output);
            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            if (output == null) {
                dialog.dismiss();
            } else {
                dialog.dismiss();
                JSONObject jsonObject = null;
                try {

                    jsonObject = new JSONObject(output);
                   String time = jsonObject.getString("time");
                   String responce = jsonObject.getString("responce");
                   JSONArray dataArray = jsonObject.getJSONArray("data");
                   for (int i= 0; i<dataArray.length(); i++){
                       JSONObject dataObj = dataArray.getJSONObject(i);
                       String ques_id = dataObj.getString("ques_id");
                       String subject_id = dataObj.getString("subject_id");
                       String question = dataObj.getString("question");
                       String ans_1 = dataObj.getString("ans_1");
                       String ans_2 = dataObj.getString("ans_2");
                       String ans_3 = dataObj.getString("ans_3");
                       String ans_4 = dataObj.getString("ans_4");
                       String ans_5 = dataObj.getString("ans_5");
                       String ans_6 = dataObj.getString("ans_6");
                       String total_ans = dataObj.getString("total_ans");
                       String r_ans = dataObj.getString("r_ans");
                   }


                    if (output.equalsIgnoreCase("1")) {
                        dialog.dismiss();
                        Intent ii = new Intent(Quiz_subjectActivity.this, Quiz_answerActivity.class);
                        ii.putExtra("time", time);
                       /* ii.putExtra("ques_id", ques_id);
                        ii.putExtra("subject_id", subject_id);
                        ii.putExtra("question", question);
                        ii.putExtra("ans_1", ans_1);
                        ii.putExtra("ans_2", ans_2);
                        ii.putExtra("ans_3", ans_3);
                        ii.putExtra("ans_4", ans_4);
                        ii.putExtra("ans_5", ans_5);
                        ii.putExtra("ans_6", ans_6);
                        ii.putExtra("total_ans", total_ans);
                        ii.putExtra("r_ans", r_ans);*/
                        startActivity(ii);


                    } else {
                        //Toast.makeText(ScrollingActivity.this, output, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
                super.onPostExecute(output);
            }
        }
    }

    //=========================================================================

    private void loadSubjectData() {
        adapter = new Quiz_subject_rv_adapter(this, new JSONArray());
        lv_subject.setAdapter(adapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadSubjectData();
    }
}
