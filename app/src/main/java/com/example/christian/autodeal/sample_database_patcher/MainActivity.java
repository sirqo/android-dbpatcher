package com.example.christian.autodeal.sample_database_patcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.christian.autodeal.sample_database_patcher.database.MySQLiteOpenHelper;
import com.example.christian.autodeal.sqlitedatabasepatcher.SQLiteDatabasePatcher;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private String TAG = MainActivity.class.getSimpleName();
    private Button btn_update_patch, btn_list_files;
    private SQLiteDatabasePatcher sqLiteDatabasePatcher;
    private BroadcastReceiver SQLiteDatabasePatcherReciever;
    private String intentFilter = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabasePatcherReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(getApplicationContext(), "Update Successful!", Toast.LENGTH_LONG).show();
            }
        };

        sqLiteDatabasePatcher = new SQLiteDatabasePatcher(getApplicationContext(),
                new MySQLiteOpenHelper(getApplicationContext()).getWritableDatabase(),
                getString(R.string.api_url),
                SQLiteDatabasePatcherReciever,
                intentFilter);

        btn_update_patch = (Button) findViewById(R.id.btn_update_patch);
        btn_list_files = (Button) findViewById(R.id.btn_list_files);

        btn_list_files.setOnClickListener(this);
        btn_update_patch.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(SQLiteDatabasePatcherReciever,
                new IntentFilter(intentFilter));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(SQLiteDatabasePatcherReciever);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_update_patch:
                //writes file
                sqLiteDatabasePatcher.requestPatchFromAPI();
                break;
            case R.id.btn_list_files:
                sqLiteDatabasePatcher.getCurrentDatabasePatches(); // returns null if empty
                break;
        }
    }


}
