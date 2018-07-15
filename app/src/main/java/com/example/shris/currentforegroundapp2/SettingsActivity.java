package com.example.shris.currentforegroundapp2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    RadioButton r1, r3, r5;
    Button button;
    public static Activity settingsactivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        r1 = findViewById(R.id.one);
        r3 = findViewById(R.id.three);
        r5 = findViewById(R.id.five);
        button = findViewById(R.id.Save);
        button.setOnClickListener(this);
        settingsactivity = this;
    }

    public void onClick(View view) {
        DatabaseAdapter adapter = new DatabaseAdapter(this);
        if (view.getId() == R.id.Save) {
            if (r1.isChecked()) {
                adapter.setSettingValue("Time", "1");
            }
            else if (r3.isChecked()) {
                adapter.setSettingValue("Time", "3");
            }
            else if (r5.isChecked()) {
                adapter.setSettingValue("Time", "5");
            }
            Intent intent = new Intent(SettingsActivity.this, ListOfAppsActivity.class);
            startActivity(intent);
        }
    }
}
