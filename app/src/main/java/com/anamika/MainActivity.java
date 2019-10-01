package com.anamika;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Config.ConstValue;
import adapter.AnnounceAdapter;
import adapter.ManuAdapter;
import adapter.NewTimeTableAdapter;
import fcm.MyFirebaseRegister;
import util.CommonClass;

public class MainActivity extends AppCompatActivity implements GridView.OnItemClickListener {
    RecyclerView recycler_list;
    String server_url;
    String School_id;
    GridView grid_table;
    ArrayList<String> table_list;
    HashMap<String,String>hashMap;
    ArrayList<HashMap<String,String>> arrayHashList;
    AnnounceAdapter announceAdapter;
    ImageView logot;
    ArrayList<HashMap<String, String>> ann_list;
    String Day_id;
    AsyncTask<Void, Void, Void> mRegisterTask;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    String school_Id,student_Id,standard_Id;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;


    ImageView logout;
    List<String> menu_name;
    List<Integer> menu_icon;
    CommonClass common;
    private String SCHOOL_ID;
    private String STUDENT_ID;
    private String STANDARD_ID;
    private  String start_time,end_time,teacher_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        arrayHashList= new ArrayList<>();
        sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        school_Id = sharedPreferences.getString("school_Id","");
        student_Id = sharedPreferences.getString("student_Id","");
        standard_Id = sharedPreferences.getString("standard_Id" , "");
        SCHOOL_ID= school_Id;STUDENT_ID=student_Id; STANDARD_ID = standard_Id;
        Log.e("school_Id",school_Id);
        Log.e("student_Id",student_Id);

        logot = (ImageView)findViewById(R.id.logot);

        getValue();
        grid_table = (GridView)findViewById(R.id.grid_table);
        grid_table.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TimeAcitivity.class);
                startActivity(intent);
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
       // getSupportActionBar().hide();

//       new  GetTableAsynctask().execute();
//
//        new GetAnnounceAsynctask().execute();

        if (isOnline()) {
            new  GetTableAsynctask().execute();

            new GetAnnounceAsynctask().execute();
        } else {
            Toast.makeText(MainActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }

        ann_list = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#CC000000"));
        }

        if (getIntent().getStringExtra("notification") != null) {
            Intent startmainIntent = new Intent(this, NotificationActivity.class);
            startActivity(startmainIntent);
        }

        common = new CommonClass(this);

      //  logout = (ImageView) findViewById(R.id.imglogout);
        logot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* SharedPreferences myPrefs = getSharedPreferences("MY_PREF",
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(getApplicationContext(),getString(R.string.main_activity_logout), Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
*/
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                // Setting Dialog Title
                alertDialog.setTitle("Logout...");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want to Logout this App?");


                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User pressed YES button. Write Logic Here
                        SharedPreferences myPrefs = getSharedPreferences(ConstValue.PREF_NAME,
                                MODE_PRIVATE);
                        SharedPreferences.Editor editor = myPrefs.edit();
                        editor.clear();
                        editor.commit();
                        Toast.makeText(getApplicationContext(), getString(R.string.main_activity_logout), Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        finish();
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User pressed No button. Write Logic Here
                        Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent1);
                    }
                });

                // Showing Alert Message
                alertDialog.show();

            }

        });

        final Button fab = (Button) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, EnquiryActivity.class);
                startActivity(intent);
            }
        });
      //  final FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
       /* fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, TopStudentActivity.class);
                startActivity(intent);
            }
        });*/

        menu_icon = new ArrayList<Integer>();
        menu_name = new ArrayList<String>();

        menu_icon.add(R.drawable.ic_menu_01);
        menu_icon.add(R.drawable.ic_menu_08);
        menu_icon.add(R.drawable.ic_menu_03);
        menu_icon.add(R.drawable.ic_menu_14);
        menu_icon.add(R.drawable.ic_menu_12);
        menu_icon.add(R.drawable.ic_menu_06);
       // menu_icon.add(R.drawable.ic_menu_02);
      //  menu_icon.add(R.drawable.ic_menu_04);
      //  menu_icon.add(R.drawable.ic_menu_05);
      //  menu_icon.add(R.drawable.ic_menu_07);
     //   menu_icon.add(R.drawable.ic_menu_09);
        //menu_icon.add(R.drawable.ic_menu_10);
      //  menu_icon.add(R.drawable.ic_menu_11);
       // menu_icon.add(R.drawable.ic_menu_13);
      //  menu_icon.add(R.drawable.ic_menu_15);


        menu_name.add(getString(R.string.menu_profile));
        menu_name.add(getString(R.string.menu_news));
        menu_name.add(getString(R.string.menu_exam));
        menu_name.add(getString(R.string.menu_book));
        menu_name.add(getString(R.string.menu_quiz));
        menu_name.add(getString(R.string.menu_growth));
     //   menu_name.add(getString(R.string.menu_attendence));
      //  menu_name.add(getString(R.string.menu_result));
     //   menu_name.add(getString(R.string.menu_teacher));
       // menu_name.add(getString(R.string.menu_holiday));
      //  menu_name.add(getString(R.string.menu_notice));
      //  menu_name.add(getString(R.string.menu_school));
      //  menu_name.add(getString(R.string.menu_time_table));
      //  menu_name.add(getString(R.string.menu_fees));
       // menu_name.add(getString(R.string.menu_notification));


        GridView gridview = (GridView) findViewById(R.id.gridView);
        ManuAdapter adapter = new ManuAdapter(getApplicationContext(), menu_name, menu_icon);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(this);

        gridview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                /*if(scrollState == SCROLL_STATE_TOUCH_SCROLL){
                    fab.setVisibility(View.INVISIBLE);
                    fab2.setVisibility(View.INVISIBLE);
                }else{
                    fab.setVisibility(View.VISIBLE);
                    fab2.setVisibility(View.VISIBLE);
                }*/
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                /*int lastItem = firstVisibleItem + visibleItemCount;
                if (lastItem == totalItemCount) {
                    fab.setVisibility(View.INVISIBLE);
                }else {
                    fab.setVisibility(View.VISIBLE);
                }*/
            }
        });

        MyFirebaseRegister myFirebaseRegister = new MyFirebaseRegister(this);
        myFirebaseRegister.RegisterUser();

    }

    private void getValue() {
       /* SCHOOL_ID=LoginActivity.school_Id;
        STUDENT_ID= LoginActivity.student_Id;*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_attendence) {
            Intent intent = new Intent(MainActivity.this, AttendenceActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_exam) {
            Intent intent = new Intent(MainActivity.this, ExamActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_result) {
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_teacher) {
            Intent intent = new Intent(MainActivity.this, TeacherActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_growth) {
            Intent intent = new Intent(MainActivity.this, GrowthActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_holiday) {
            Intent intent = new Intent(MainActivity.this, HolidaysActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_news) {
            Intent intent = new Intent(MainActivity.this, NewsActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_notice) {
            Intent intent = new Intent(MainActivity.this, NoticeActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_school) {
            Intent intent = new Intent(MainActivity.this, SchoolProfileActivity.class);
            intent.putExtra("school_Id",school_Id);
            startActivity(intent);
        }
        if (id == R.id.action_time_table) {
            Intent intent = new Intent(MainActivity.this, TimetableActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_quiz) {
            Intent intent = new Intent(MainActivity.this, Quiz_subjectActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_fees) {
            Intent intent = new Intent(MainActivity.this, FeesActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_book) {
            Intent intent = new Intent(MainActivity.this, BookActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_notification) {
            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(intent);
        }

           /* AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

            // Setting Dialog Title
            alertDialog.setTitle("Logout...");

            // Setting Dialog Message
            alertDialog.setMessage("Are you sure you want to Logout this App?");


            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // User pressed YES button. Write Logic Here
                    SharedPreferences myPrefs = getSharedPreferences(ConstValue.PREF_NAME,
                            MODE_PRIVATE);
                    SharedPreferences.Editor editor = myPrefs.edit();
                    editor.clear();
                    editor.commit();
                    Toast.makeText(getApplicationContext(), getString(R.string.main_activity_logout), Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent1);
                    finish();
                }
            });

            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // User pressed No button. Write Logic Here
                    Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent1);
                }
            });

            // Showing Alert Message
            alertDialog.show();
           // return true;*/


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        common.open_screen(position);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));*/

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(ConstValue.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(ConstValue.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        //NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

//---------------------------------------------------------

    class GetAnnounceAsynctask extends AsyncTask<String, String, String> {
        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
           /* arComentChat = new ArrayList<>();*/
        }

        @Override
        protected String doInBackground(String... params) {

            try {
              // server_url = "http://twors.in/education_portal/index.php/api/get_school_noticeboard?school_id="+SCHOOL_ID;
                server_url = ConstValue.GET_school_noticeboard+SCHOOL_ID;

            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("sever_url>>>>>>>>>", server_url);
            output = HttpHandler.makeServiceCall(server_url);
            Log.e("getcomment_url", output);
            System.out.println("getcomment_url" + output);
            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            if (output == null) {
                dialog.dismiss();
            } else {
                try {
                    dialog.dismiss();
                    JSONObject json = new JSONObject(output);
                    String responce = json.getString("responce");
                    JSONArray data_array = json.getJSONArray("data");
                    for (int i=0; i<data_array.length(); i++) {
                        JSONObject c = data_array.getJSONObject(i);
                        String notice_id = c.getString("notice_id");
                        String school_id = c.getString("school_id");
                        String notice_description = c.getString("notice_description");
                        String notice_type = c.getString("notice_type");
                        String notice_status = c.getString("notice_status");
                        String on_date = c.getString("on_date");

                        HashMap<String, String> contact = new HashMap<>();
                        contact.put("notice_id", notice_id);
                        contact.put("school_id", school_id);
                        contact.put("notice_description", notice_description);
                        contact.put("notice_type", notice_type);
                        contact.put("notice_status", notice_status);
                        contact.put("on_date", on_date);
                        ann_list.add(contact);
                    }

                    announceAdapter = new AnnounceAdapter(MainActivity.this, ann_list);
                    recycler_list = (RecyclerView) findViewById(R.id.recycler_list);
                    recycler_list.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recycler_list.setLayoutManager(linearLayoutManager);
                    recycler_list.setAdapter(announceAdapter);

                  /*  if (output.equalsIgnoreCase("1")) {
                        dialog.dismiss();


                        Toast.makeText(MainActivity.this, "response", Toast.LENGTH_LONG).show();
                    *//*    Intent intent = new Intent(MainActivity.this,MainActivity.class);
                        startActivity(intent);*//*


                    } else {
                        //Toast.makeText(ScrollingActivity.this, output, Toast.LENGTH_LONG).show();
                    }
*/
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
                super.onPostExecute(output);
            }
        }
    }

    //---------------------------------------------------------------

    class GetTableAsynctask extends AsyncTask<String, String, String> {
        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
           /* arComentChat = new ArrayList<>();*/
        }

        @Override
        protected String doInBackground(String... params) {

            try {
              //  server_url = "http://twors.in/education_portal/index.php/api/timetable?standard_id="+STANDARD_ID+"&school_id="+SCHOOL_ID+"&student_id="+STUDENT_ID;
                server_url = ConstValue.GET_TIMETABLE+STANDARD_ID+"&school_id="+SCHOOL_ID+"&student_id="+STUDENT_ID;

            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("sever_url>>>>>>>>>", server_url);
            output = HttpHandler.makeServiceCall(server_url);
            Log.e("getcomment_url", output);
            System.out.println("getcomment_url" + output);
            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            if (output == null) {
                dialog.dismiss();
            } else {
                try {
                    dialog.dismiss();
                    JSONObject json = new JSONObject(output);
                    String responce = json.getString("responce");
                    JSONArray data_array = json.getJSONArray("data");
                    table_list = new ArrayList<String>();
                    for (int i = 0; i < data_array.length(); i++) {
                        JSONObject c = data_array.getJSONObject(i);
                        String id = c.getString("id");
                        String standard_id = c.getString("standard_id");
                        String teacher_id = c.getString("teacher_id");
                        String school_id = c.getString("school_id");
                        String subject = c.getString("subject");
                        String day_id = c.getString("day_id");
                        String start_time1 = c.getString("start_time");
                        String end_time1 = c.getString("end_time");
                        String standard_title = c.getString("standard_title");
                         String teacher_name1 = c.getString("teacher_name");
                        String day_name = c.getString("day_name");
                        hashMap= new HashMap<>();
                        table_list.add(subject);
                        hashMap.put("start_time1",start_time1);
                        hashMap.put("end_time1",end_time1);
                        hashMap.put("teacher_name1",teacher_name1);

                        arrayHashList.add(hashMap);
                    }

                    NewTimeTableAdapter newTimeTableAdapter = new NewTimeTableAdapter(getApplicationContext(), table_list);
                    grid_table.setAdapter(newTimeTableAdapter);
                    grid_table.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Toast.makeText(MainActivity.this, "response", Toast.LENGTH_LONG).show();
                            start_time=arrayHashList.get(i).get("start_time1");
                            teacher_name=arrayHashList.get(i).get("teacher_name1");
                            end_time=arrayHashList.get(i).get("end_time1");
                            Toast.makeText(MainActivity.this, "response", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, TimeAcitivity.class);
                            intent.putExtra("start_time", start_time);
                            intent.putExtra("end_time", end_time);
                            intent.putExtra("teacher_name", teacher_name);
                            startActivity(intent);

                        }
                    });

                    if (output.equalsIgnoreCase("1")) {
                        dialog.dismiss();

                    } else {
                        //Toast.makeText(ScrollingActivity.this, output, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Error",e.toString());
                    dialog.dismiss();
                }
                super.onPostExecute(output);
            }
        }

    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }


}
