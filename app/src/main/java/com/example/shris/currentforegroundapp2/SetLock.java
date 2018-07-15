package com.example.shris.currentforegroundapp2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SetLock extends AppCompatActivity implements View.OnClickListener {
    TextView textView;
    EditText editText;
    Button button;
    public static Activity setlock;
    public static final String intentKey = "Lock";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_lock);
        setlock = this;
        textView = findViewById(R.id.textView2);
        editText = findViewById(R.id.pinEntry);
        button = findViewById(R.id.set);
        button.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.set) {
            String s = editText.getText().toString();
            if (s.length() < 4) {
                textView.setText("Pin must be of length at least 4!!");
            }
            else {
                Intent confirmIntent = new Intent(this, ConfirmLock.class);
                confirmIntent.putExtra(intentKey, s);
                startActivity(confirmIntent);
            }
        }
    }
}
