package com.example.julia.escapethecity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;

public class TrailStartActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.log("Setting content view");
        setContentView(R.layout.trailstart);
    }
}
