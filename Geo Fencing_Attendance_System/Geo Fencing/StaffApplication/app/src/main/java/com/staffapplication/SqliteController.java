package com.staffapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by dell on 07/02/2018.
 */

public class SqliteController extends SQLiteOpenHelper {
    private static final String LOGCAT = null;

    public SqliteController(Context applicationcontext) {
        super(applicationcontext, "staffapplication.db", null, 1);
        Log.d(LOGCAT, "Created");
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE Login ( Username TEXT)";
        database.execSQL(query);
        Log.d(LOGCAT,"Tables Created");
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS Login";
        database.execSQL(query);
        onCreate(database);
    }

    public int directLogin(){
        SQLiteDatabase db=getReadableDatabase();
        int cnt=0;
        String qry="Select * from Login";
        Cursor cursor=db.rawQuery(qry,null);
        if(cursor.moveToFirst()){
            do{
                cnt++;
            }while (cursor.moveToNext());
        }

        return cnt;
    }

    public void checkLogin(String username) {

        String uname=username;
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM Login";
        database.execSQL(deleteQuery);

        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Username", uname);
        database.insert("Login", null, values);
        database.close();
    }

    public void funLogout() {
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM Login";
        database.execSQL(deleteQuery);
    }

    public String returnUsername() {
        String val = "";
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM Login";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                val = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        return val;
    }

}
