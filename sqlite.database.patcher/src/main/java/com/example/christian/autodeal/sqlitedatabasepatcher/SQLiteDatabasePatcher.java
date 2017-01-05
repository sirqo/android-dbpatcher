package com.example.christian.autodeal.sqlitedatabasepatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;



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
    method to use for writing files
    statement = to content of the
    file to be written
    filename = filename to be used
    */
    private Boolean writeFile(String statement, String filename){
        File file = new File(parent_directory, filename);

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(statement.getBytes());
            outputStream.close();
            Log.d(TAG, "writeFile: success writing file " + filename);
        }
        catch (IOException e){
            Log.e(TAG, "writeFile: " + e.toString());
            Log.e(TAG, "writeFile: failed writing file " + filename);

        }

        return true;
    }

    /*
    Applying patch to database and writing to statement to a file
     */
    public Boolean applyPatch(String statement, String filename){
        try{
                sqLiteDatabase.execSQL(statement);
                writeFile(statement, filename);
        }
        catch (SQLiteException e){
            Log.e(TAG, "applyPatch() : " + "file: " + filename + " " + e.toString());
            return false;
        }

        return true;
    }

    public String getCurrentDatabasePatches(){
        String files = "";
        File directory = new File(parent_directory);

        if (directory.exists()) {
            for (int x = 0; x < directory.list().length; x++) {
                files += (x == 0) ? directory.list()[x].toString() : "," + directory.list()[x].toString();

            }
        }
        else
            Log.e(TAG, "test: empty directory");

        Log.d(TAG, "getCurrentDatabasePatches: " + files);

        return files;
    }
}
