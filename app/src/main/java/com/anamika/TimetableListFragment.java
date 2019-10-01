package com.anamika;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;

import adapter.TimetableAdapter;

/**
 * Created by Rajesh on 2017-09-05.
 */

public class TimetableListFragment extends Fragment {

    public TimetableListFragment() {
        // Required empty public constructor
    }

    public static TimetableListFragment newInstance(String day_id) {

        TimetableListFragment f = new TimetableListFragment();
        Bundle b = new Bundle();
        b.putString("day_id", day_id);

        f.setArguments(b);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_quiz_result, container, false);

        ListView lv_timetable = (ListView) view.findViewById(R.id.lv_result);

        String day_id = getArguments().getString("day_id");

        TimetableAdapter adapter = new TimetableAdapter(getActivity(), new JSONArray(),day_id);
        lv_timetable.setAdapter(adapter);

        return view;
    }
}
