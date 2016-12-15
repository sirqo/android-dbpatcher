package com.example.christian.autodeal.sqlitedatabasepatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Christian on 06/12/2016.
 */

public class SQLiteDatabasePatcher {
    private final String TAG = SQLiteDatabasePatcher.class.getSimpleName();
    private Context context;
    private String path;
    private SQLiteDatabase sqLiteDatabase;
    private String api_url;
    private BroadcastReceiver broadcastReceiver;


    public SQLiteDatabasePatcher(Context context, SQLiteDatabase sqLiteDatabase, String api_url, BroadcastReceiver broadcastReceiver){
        this.context = context;
        this.sqLiteDatabase = sqLiteDatabase;
        this.api_url = api_url;
        this.broadcastReceiver = broadcastReceiver;

        //path to local directory of sqlite patches
        path = context.getFilesDir().getPath() + context.getString(R.string.repository_path);
        File directory = new File(path);
        if (!directory.exists())
            directory.mkdirs();

    }

    /*
    method to use for writing files
    statement = to content of the
    file to be written
    filename = filename to be used
    */
    public Boolean writeFile(String statement, String filename){
        File file = new File(path, filename);

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(statement.getBytes());
            outputStream.close();
        }
        catch (IOException e){
            Log.e(TAG, "writeFile: " + e.toString());
            Log.e(TAG, "writeFile: failed writing file " + filename);

        }

        Log.d(TAG, "writeFile: success writing file " + filename);

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
        File directory = new File(path);

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

    /*
    * post request to API for available updates
    * */
    public void requestPatchFromAPI(){
        Log.d(TAG, "requestAPI: Start");
        Log.d(TAG, "requestAPI: url=" + api_url );
        RequestQueue queue = Volley.newRequestQueue(context);

        final StringRequest request = new StringRequest(Request.Method.POST,
                api_url,
                new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "requestPatchFromAPI: response =" + response);
                try {
                    JSONObject rootObject = new JSONObject(response);
                    JSONArray patchArray = rootObject.getJSONArray("patches");
                    if (patchArray.length() > 0){
                        for (int x = 0; x < patchArray.length(); x++){
                            JSONObject object = patchArray.getJSONObject(x);
                            JSONArray objectArray = object.names();
                            Log.d(TAG, "requestPatchFromAPI: key: " + objectArray.get(0).toString()
                            + " value: " + object.getString(String.valueOf(objectArray.get(0))));

                            applyPatch(object.getString(String.valueOf(objectArray.get(0))),
                                    objectArray.get(0).toString());
                        }
                    }

                    Log.d(TAG, "requestPatchFromAPI is_complete: " + rootObject.getBoolean("is_complete"));
                    if (!rootObject.getBoolean("is_complete")) {
                        requestPatchFromAPI();
                    }
                    else{
                        Intent intent = new Intent();
                        intent.setAction("test");
                        context.sendBroadcast(intent);
                        Log.d(TAG, "requestPatchFromAPI(): Broadcast Start");
                    }

                }
                catch (JSONException e){
                    Log.e(TAG, "requestAPI: " + e.toString());
                }

                Log.d(TAG, "requestAPI: END");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "requestAPI: " + error.toString());
                Log.e(TAG, "requestAPI: END");


            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                    headers.put("api-key", "3BXAFBTR9L324JA9QSBQ");
                    headers.put("x-api-request", "1");
                    headers.put("Content-Type", "application/x-www-form-urlencoded");
                Log.d(TAG, "headers: " + headers.toString());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> post = new HashMap<>();
                post.put("versions", getCurrentDatabasePatches());
                return post;
            }
        };

        queue.add(request);
    }


}
