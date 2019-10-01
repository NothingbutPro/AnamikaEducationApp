package adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anamika.R;

import java.util.ArrayList;

/**
 * Created by ICS on 16/03/2018.
 */

public class NewTimeTableAdapter extends BaseAdapter {

    Context context;
    String logos[];
    ArrayList<String> arrayList1stFloor;
    String[] color = new String[]{"#060080", "#f7dd7d", "#a6d471", "#6ed4d7", "#7a85e4", "#665b5b", "#cf6ada", "#ed883f", "#ef5c5c", "#f7dd7d", "#a6d471", "#6ed4d7", "#7a85e4", "#665b5b", "#cf6ada"};
    LayoutInflater inflter;
    private String TeacherNmae,StartTime,EndTime;

    public NewTimeTableAdapter(Context applicationContext, ArrayList<String> arrayList1stfloor) {
        this.context = applicationContext;
        this.arrayList1stFloor = arrayList1stfloor;

        this.logos = logos;

        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return arrayList1stFloor.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.table_row, null); // inflate the layout
        TextView lessonNo = (TextView) view.findViewById(R.id.lesson_no);
        TextView icon = (TextView) view.findViewById(R.id.timetable_row);
        LinearLayout lin = (LinearLayout) view.findViewById(R.id.linearlayout);
        String dd = color[i];
        /*int col = Integer.parseInt();*/
        int col = Color.parseColor(dd);
        lin.setBackgroundColor(col);
        String aa = arrayList1stFloor.get(i);
        /*StartTime=arrayList1stFloor.get(i);
        EndTime=arrayList1stFloor.get(i);
        TeacherNmae=arrayList1stFloor.get(i);*/
        //AppConstant.value.Starttime=aa;
        icon.setText(arrayList1stFloor.get(i));
        Log.e("ttt",String.valueOf(i++));

        lessonNo.setText(String.valueOf(i++));// set logo images
       // icon.setTextColor(Color.parseColor(dd));
        return view;
    }
}
