package com.example.shris.currentforegroundapp2;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by shris on 3/10/2018.
 */

public class CurrentAppService extends Service {
    public static int flag = 0;
    @Override
    public int onStartCommand(final Intent intent, final int flags, int startId) {
        final Timer timer = new Timer();
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        final String str = "";
        TimerTask timerTask = new TimerTask() {
            //int pl=0, pc=0, p_count=1;
            String packageName = "";
            @Override
            public void run() {
                flag = 1;
                String className = "";
                UsageStatsManager usageStatsManager = (UsageStatsManager) getApplication().getSystemService(Context.USAGE_STATS_SERVICE);
                if (usageStatsManager != null) {
                    UsageEvents usageEvents = usageStatsManager.queryEvents(System.currentTimeMillis()-1000, System.currentTimeMillis());

                    if (usageEvents != null) {
                        UsageEvents.Event event = new UsageEvents.Event();

                        while (usageEvents.hasNextEvent()) {
                            UsageEvents.Event aux = new UsageEvents.Event();
                            usageEvents.getNextEvent(aux);

                            if (aux.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                                event = aux;
                                Log.d("WORK1", aux.getPackageName());
                            }
                        }
                        packageName = event.getPackageName();
                        className = event.getClassName();
                    }
                }
                if (packageName != null) {
                    Log.e("WORK", packageName + " a ");
                    Log.e("PLEASE", className + " b ");
                }
                final DatabaseAdapter adapter = new DatabaseAdapter(getApplicationContext());
                if (packageName!= null && adapter.shouldLock(packageName) && adapter.isPresent(packageName)) {
                    adapter.setFlag(1, packageName);
                    String svalue = adapter.getSettingValue("Time");
                    final int s = Integer.parseInt(svalue);
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(s * 60 * 1000);
                                adapter.setFlag(0, packageName);
                                Log.e("Robin", "Worked");
                            }
                            catch (Exception e) {

                            }
                        }
                    };
                    thread.start();
                    timer.cancel();
                    Intent intent1 = new Intent(getApplication(), Main2Activity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                    //Log.e("Present", "It worked");
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 5000, 1000);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
