package com.example.christian.autodeal.sqlitedatabasepatcher;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.io.File;



/**
 * Created by Christian on 06/12/2016.
 */

public class SQLiteDatabasePatcher {
    private final String TAG = SQLiteDatabasePatcher.class.getSimpleName();
    private Context context;
    private String parent_directory;
    private SQLiteDatabase sqLiteDatabase;

    public SQLiteDatabasePatcher(Context context, SQLiteDatabase sqLiteDatabase, String parent_directory){
        this.context = context;
        this.sqLiteDatabase = sqLiteDatabase;

        //path to local directory of sqlite patches
        this.parent_directory = parent_directory + context.getString(R.string.repository_path);
        Log.d(TAG, "parent_directory= " + this.parent_directory);
        File directory = new File(this.parent_directory);
        if (!directory.exists())
            directory.mkdirs();

    }

    /*
    Applying patch to database and writing to statement to a file
     */
    public Boolean applyPatch(String statement, String filename){
        try{
                sqLiteDatabase.execSQL(statement);
        }
        catch (SQLiteException e){
            Log.e(TAG, "applyPatch() : " + "file: " + filename + " " + e.toString());
            return false;
        }

        return true;
    }
}
