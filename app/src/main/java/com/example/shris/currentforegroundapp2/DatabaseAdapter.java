package com.example.shris.currentforegroundapp2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.security.PublicKey;

/**
 * Created by shris on 3/21/2018.
 */

public class DatabaseAdapter {
    private static final String DATABASE_NAME = "FaceDetectionAppLock";
    private static final int VERSION = 8;
    private static final String TABLE_NAME = "Locked_Apps";
    private static final String KEY_ID = "_id";
    private static final String NAME = "App_Name";
    private static final String PACKAGE = "Package_Name";
    private static final String FLAG = "Flag";
    private static final String TABLE_NAME2 = "Passwords";
    private static final String TYPE = "Lock_Type";
    private static final String LOCK = "Pattern";
    private static final String TABLE_NAME3 = "Setting_Log";
    private static final String SETTING_TYPE = "Setting_Type";
    private static final String SETTING_VALUE = "Setting_Value";

    private static final String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " ( " +
            KEY_ID + " integer primary key autoincrement, " +
            NAME + " text not null, " +
            PACKAGE + " text not null, " +
            FLAG + " integer not null );";

    private static final String CREATE_QUERY2 = "CREATE TABLE " + TABLE_NAME2 + " ( " +
            TYPE + " text primary key, " +
            LOCK + " text not null );";

    private static final String CREATE_QUERY3 = "CREATE TABLE " + TABLE_NAME3 + " ( " +
            SETTING_TYPE + " text primary key, " +
            SETTING_VALUE + " text not null );";


    private Context context;
    DBAdapter dbAdapter;
    public DatabaseAdapter(Context context) {
        this.context = context;
        dbAdapter = new DBAdapter(context, DATABASE_NAME, null, VERSION);
    }

    public void insertApp (String appName, String packageName) {
        SQLiteDatabase sqLiteDatabase = dbAdapter.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, appName);
        contentValues.put(PACKAGE, packageName);
        contentValues.put(FLAG, 0);
        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
    }

    public Cursor getAllApps() {
        SQLiteDatabase sqLiteDatabase = dbAdapter.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        sqLiteDatabase.close();
        return cursor;
    }

    public boolean isPresent(String packageName) {
        SQLiteDatabase sqLiteDatabase = dbAdapter.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + PACKAGE + "='" + packageName + "'", null);
        if (cursor.getCount()==0)
            return false;
        else
            return true;
    }

    public void setFlag (int flag, String packageName) {
        SQLiteDatabase sqLiteDatabase = dbAdapter.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FLAG, flag);
        sqLiteDatabase.update(TABLE_NAME, values, PACKAGE + " = '" + packageName + "'", null);
    }

    public boolean shouldLock(String packageName) {
        SQLiteDatabase sqLiteDatabase = dbAdapter.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, PACKAGE + " = '" + packageName + "'", null, null, null, null);
        cursor.moveToFirst();
        int fl=0;
        if (cursor.getCount()!=0)
            fl = cursor.getInt(cursor.getColumnIndex(FLAG));
        if (fl==0)
            return true;
        else
            return false;
    }

    public void deleteApp (String packageName) {
        SQLiteDatabase sqLiteDatabase = dbAdapter.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, PACKAGE + "='" + packageName + "'", null);
        sqLiteDatabase.close();
    }

    public void setLock(String lock, String lock_type) {
        SQLiteDatabase sqLiteDatabase = dbAdapter.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TYPE, lock_type);
        values.put(LOCK, lock);
        sqLiteDatabase.insert(TABLE_NAME2, null, values);
    }

    public String getLock(String lock_type) {
        SQLiteDatabase sqLiteDatabase = dbAdapter.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME2, null, TYPE + " = '" + lock_type + "'", null, null, null, null);
        Log.e("Potato", String.valueOf(cursor.getCount()));
        cursor.moveToFirst();
        if (cursor.getCount()==0)
                return null;
        String ans = cursor.getString(cursor.getColumnIndex(LOCK));
        return ans;
    }

    public void setSettingValue(String settingType, String settingValue) {
        SQLiteDatabase sqLiteDatabase = dbAdapter.getWritableDatabase();
        String current_s = getSettingValue(settingType);
        ContentValues values = new ContentValues();
        values.put(SETTING_VALUE, settingValue);
        if (current_s==null) {
            values.put(SETTING_TYPE, settingType);
            sqLiteDatabase.insert(TABLE_NAME3, null, values);
        }
        else
            sqLiteDatabase.update(TABLE_NAME3, values, SETTING_TYPE + " = '" + settingType + "'", null);
    }

    public String getSettingValue(String settingType) {
        SQLiteDatabase sqLiteDatabase = dbAdapter.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME3, null, SETTING_TYPE + " = '" + settingType + "'", null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount()==0)
            return null;
        String ans = cursor.getString(cursor.getColumnIndex(SETTING_VALUE));
        return ans;
    }

    private static class DBAdapter extends SQLiteOpenHelper {
        public DBAdapter(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public void onCreate (SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_QUERY);
            sqLiteDatabase.execSQL(CREATE_QUERY2);
            sqLiteDatabase.execSQL(CREATE_QUERY3);
        }

        public void onUpgrade (SQLiteDatabase sqLiteDatabase, int v1, int v2) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
            onCreate(sqLiteDatabase);
        }
    }
}
