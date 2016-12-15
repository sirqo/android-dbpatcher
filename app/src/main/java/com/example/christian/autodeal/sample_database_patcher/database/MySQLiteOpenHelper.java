package com.example.christian.autodeal.sample_database_patcher.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Christian on 08/12/2016.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private final String TAG = MySQLiteOpenHelper.class.getSimpleName();
    private Context context;
    private SQLiteDatabase database;
    private static final String DATABASE_NAME = "SQLiteOpenHelperDatabase";
    private static final int DATABASE_VERSION = 1;

    public MySQLiteOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user TEXT, age INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void openDB(){
        database = this.getWritableDatabase();
    }

    public void closeDB(){
        if (database.isOpen())
            database.close();
    }




}
