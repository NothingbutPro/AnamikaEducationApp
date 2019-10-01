package com.anamika;

import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;

import adapter.BookAdapter;

public class BookActivity extends CommonAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        ListView lv_book = (ListView) findViewById(R.id.lv_book);

        BookAdapter adapter = new BookAdapter(this, new JSONArray());
        lv_book.setAdapter(adapter);

    }
}
