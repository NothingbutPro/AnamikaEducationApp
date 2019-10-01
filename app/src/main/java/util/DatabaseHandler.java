package util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rajesh on 2017-08-17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static String DB_NAME = "Education_question_answer";
    private static int DB_VERSION = 1;
    private SQLiteDatabase db;

    public static final String ANS_TABLE = "qus_ans";
    public static final String COLUMN_ID = "ques_id";
    public static final String COLUMN_ANS = "r_ans";
    public static final String COLUMN_USER_ANS = "user_ans";

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        String exe = "CREATE TABLE IF NOT EXISTS " + ANS_TABLE
                + "(" + COLUMN_ID + " integer primary key, "
                + COLUMN_ANS + " TEXT NOT NULL,"
                + COLUMN_USER_ANS + " TEXT NOT NULL"
                + ")";

        db.execSQL(exe);

    }

    public boolean setAns(HashMap<String, String> map) {
        db = getWritableDatabase();
        if (isInAns(map.get(COLUMN_ID))) {
            db.execSQL("update " + ANS_TABLE + " set " +
                    COLUMN_USER_ANS + " = '" + map.get(COLUMN_USER_ANS) +
                    "' where " + COLUMN_ID + "=" + map.get(COLUMN_ID));
            return false;
        } else {
            ContentValues values = new ContentValues();

            values.put(COLUMN_ID, map.get(COLUMN_ID));
            values.put(COLUMN_ANS, map.get(COLUMN_ANS));
            values.put(COLUMN_USER_ANS, map.get(COLUMN_USER_ANS));

            db.insert(ANS_TABLE, null, values);
            return true;
        }
    }

    public boolean isInAns(String id) {
        db = getReadableDatabase();
        String qry = "Select *  from " + ANS_TABLE + " where " + COLUMN_ID + " = " + id;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) return true;

        return false;
    }

    public int getAnsCount() {
        db = getReadableDatabase();
        String qry = "Select *  from " + ANS_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        return cursor.getCount();
    }

    public ArrayList<HashMap<String, String>> getAllAns() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        db = getReadableDatabase();
        String qry = "Select *  from " + ANS_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put(COLUMN_ID, cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
            map.put(COLUMN_ANS, cursor.getString(cursor.getColumnIndex(COLUMN_ANS)));
            map.put(COLUMN_USER_ANS, cursor.getString(cursor.getColumnIndex(COLUMN_USER_ANS)));

            list.add(map);
            cursor.moveToNext();
        }
        return list;
    }

    public int getAllTrueAns() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        db = getReadableDatabase();
        String qry = "Select *  from " + ANS_TABLE + " where " + COLUMN_ANS + " = " + COLUMN_USER_ANS;
        Cursor cursor = db.rawQuery(qry, null);

        return cursor.getCount();
    }

    public int getAllFalseAns() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        db = getReadableDatabase();
        String qry = "Select *  from " + ANS_TABLE + " where " + COLUMN_ANS + " != " + COLUMN_USER_ANS;
        Cursor cursor = db.rawQuery(qry, null);

        return cursor.getCount();
    }

    public ArrayList<HashMap<String, String>> getAnsById(String id) {
        if (isInAns(id)) {
            ArrayList<HashMap<String, String>> list = new ArrayList<>();
            db = getReadableDatabase();
            String qry = "Select *  from " + ANS_TABLE + " where " + COLUMN_ID + " = " + id;
            Cursor cursor = db.rawQuery(qry, null);
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                HashMap<String, String> map = new HashMap<>();
                map.put(COLUMN_ID, cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                map.put(COLUMN_ANS, cursor.getString(cursor.getColumnIndex(COLUMN_ANS)));
                map.put(COLUMN_USER_ANS, cursor.getString(cursor.getColumnIndex(COLUMN_USER_ANS)));

                list.add(map);
                cursor.moveToNext();
            }
            return list;
        } else {
            return null;
        }
    }

    public void clearAns() {
        db = getReadableDatabase();
        db.execSQL("delete from " + ANS_TABLE);
    }

    public void removeItemFromCart(String id) {
        db = getReadableDatabase();
        db.execSQL("delete from " + ANS_TABLE + " where " + COLUMN_ID + " = " + id);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
