package com.example.julia.escapethecity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class TrailListActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trial);

        addButtonHandlers();
    }

    public void addButtonHandlers(){
        Button b1 = findViewById(R.id.btn_trail1);
        Button b2 = findViewById(R.id.btn_trail2);
        Button b3 = findViewById(R.id.btn_trail3);

        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                switchScreen();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                switchScreen();
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                switchScreen();
            }
        });
    }

    public void switchScreen(){
        Intent i = new Intent(getApplicationContext(),LocationRiddleActivity.class);
        startActivity(i);
    }
}
