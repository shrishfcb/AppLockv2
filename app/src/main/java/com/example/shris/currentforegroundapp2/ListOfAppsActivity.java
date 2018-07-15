package com.example.shris.currentforegroundapp2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class ListOfAppsActivity extends AppCompatActivity implements View.OnClickListener {
    PackageManager packageManager;
    RecyclerView recyclerView;
    StringBuffer buffer = new StringBuffer();
    Button button;
    public static Activity loa;
    public static int end = 0;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_apps);
        loa = this;
        context = getApplicationContext();
        DatabaseAdapter dba = new DatabaseAdapter(this);
        packageManager = getPackageManager();
        List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo applicationInfo: packages) {
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)==1) {

            }
            else {
                buffer.append(applicationInfo.packageName + ",");
            }
        }
        button = findViewById(R.id.settings);
        button.setOnClickListener(this);
        String[] apps = buffer.toString().split(",");
        recyclerView = findViewById(R.id.rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewCustomAdapter adapter = new RecyclerViewCustomAdapter(this, apps);
        recyclerView.setAdapter(adapter);
        DatabaseAdapter adapter1 = new DatabaseAdapter(this);
        String svalue = adapter1.getSettingValue("Time");
        //Log.d("Pooh", svalue);
        if (svalue == null) {
            Intent intent1 = new Intent(this, SettingsActivity.class);
            intent1.putExtra("Source", "Automatic");
            startActivity(intent1);
        }
        else {
            if (CurrentAppService.flag == 0) {
                Intent intent = new Intent(this, CurrentAppService.class);
                getBaseContext().startService(intent);
            }
        }
        String ans = dba.getLock("Pin");
        if (ans == null) {
            Intent setLockIntent = new Intent(this, SetLock.class);
            startActivity(setLockIntent);
        }
        else {
            if (CurrentAppService.flag == 0) {
                Intent intent2 = new Intent(this, CurrentAppService.class);
                getBaseContext().startService(intent2);
            }
        }
    }

    public static void setEndZero() {
        end = 1;
        checkEnd();
    }

    public static void checkEnd() {
        if (end == 1) {
            Intent intent = new Intent(context, CurrentAppService.class);
            context.startService(intent);
            loa.finish();
        }
    }

    public void onClick (View view) {
        if (view.getId() == R.id.settings) {
            Intent intent = new Intent(ListOfAppsActivity.this, SettingsActivity.class);
            intent.putExtra("Source", "Manual");
            startActivity(intent);
        }
    }
}
