package com.example.shris.currentforegroundapp2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    Button button;
    EditText editText;
    TextView textView;
    Intent intent;
    public static Activity main2activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        main2activity = this;
        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.pin);
        textView = (TextView) findViewById(R.id.textView);
        button.setOnClickListener(this);
        intent = new Intent(Main2Activity.this, CurrentAppService.class);
        stopService(intent);
    }


    public void onClick(View view) {
        if (view.getId() == R.id.button) {
            String pin = editText.getText().toString();
                DatabaseAdapter adapter = new DatabaseAdapter(this);
                String dbPin = adapter.getLock("Pin");
                Log.e("Shreya", dbPin);
                if (pin.equalsIgnoreCase(dbPin)) {
                    ListOfAppsActivity.setEndZero();
                    finish();
                }
                else {
                    Toast.makeText(this, "Invalid Pin", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
