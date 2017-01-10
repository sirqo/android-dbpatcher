package com.example.christian.autodeal.sqlitedatabasepatcher;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;




/**
 * Created by Christian on 06/12/2016.
 */

public class SQLiteDatabasePatcher {
    private final String TAG = SQLiteDatabasePatcher.class.getSimpleName();
    private SQLiteDatabase sqLiteDatabase;

    public SQLiteDatabasePatcher(SQLiteDatabase sqLiteDatabase){
        this.sqLiteDatabase = sqLiteDatabase;
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
