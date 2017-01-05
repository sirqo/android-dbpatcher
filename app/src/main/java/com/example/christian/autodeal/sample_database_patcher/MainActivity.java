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
    private int increment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqLiteDatabasePatcher = new SQLiteDatabasePatcher(getApplicationContext(),
                new MySQLiteOpenHelper(getApplicationContext()).getWritableDatabase(),
                getFilesDir().getPath() + "/config/" //make sure your path start and end with this "/"
                );

        btn_update_patch = (Button) findViewById(R.id.btn_update_patch);
        btn_list_files = (Button) findViewById(R.id.btn_list_files);

        btn_list_files.setOnClickListener(this);
        btn_update_patch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_update_patch:
                //writes file
                sqLiteDatabasePatcher.applyPatch("insert into users(user,age) values("+ increment +","+increment+")",
                        "test_" + increment + ".txt");
                increment++;
                break;
            case R.id.btn_list_files:
                sqLiteDatabasePatcher.getCurrentDatabasePatches(); // returns null if empty
                break;
        }
    }


}
