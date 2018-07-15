package com.example.shris.currentforegroundapp2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ConfirmLock extends AppCompatActivity implements View.OnClickListener {
    EditText editText;
    TextView textView;
    Button button;
    String key;
    public static Activity confirmLock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_lock);
        editText = findViewById(R.id.confirmEntry);
        textView = findViewById(R.id.textView3);
        button = findViewById(R.id.confirm);
        button.setOnClickListener(this);
        key = getIntent().getStringExtra(SetLock.intentKey);
        confirmLock=this;
    }

    public void onClick (View view) {
        if (view.getId() == R.id.confirm) {
            String ckey = editText.getText().toString();
            if (ckey.equalsIgnoreCase(key)) {
                DatabaseAdapter adapter = new DatabaseAdapter(this);
                adapter.setLock(ckey, "Pin");
                Intent appsIntent = new Intent(this, ListOfAppsActivity.class);
                startActivity(appsIntent);
            }
            else {
                textView.setText("Passwords Do Not match!!");
            }
        }
    }
}
