package com.example.shris.currentforegroundapp2;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static int end = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseAdapter dba = new DatabaseAdapter(this);

        String ans = dba.getLock("Pin");
        if (ans == null) {
            Intent setLockIntent = new Intent(this, SetLock.class);
            startActivity(setLockIntent);
        }
        else {
                Intent intent = new Intent(this, ListOfAppsActivity.class);
                if (CurrentAppService.flag == 0) {
                    Intent intent2 = new Intent(this, CurrentAppService.class);
                    getBaseContext().startService(intent2);
                }
                if (end == 1) {
                    finish();
                }
                else {
                    startActivity(intent);
                }
        }
        //Intent intent1 = new Intent(this, CurrentAppService.class);
        //getBaseContext().startService(intent);
    }
}
