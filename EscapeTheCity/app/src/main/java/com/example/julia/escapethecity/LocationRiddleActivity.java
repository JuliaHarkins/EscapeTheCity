package com.example.julia.escapethecity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;

import java.io.InputStream;

public class LocationRiddleActivity extends Activity {

    private Handler handler = new Handler();
    private int secondsDelayed;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            secondsDelayed++;
            queryTrails();
            debug();
            handler.postDelayed(this, 1000);
        }
    };

    Trail trail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_UserLocation);
        //bmapFragment.getMapAsync(this);

        setContentView(R.layout.locationriddle);

        trail = new Trail();
        InputStream is = this.getApplicationContext().getResources().openRawResource(R.raw.trail1);
        trail.fromInputStream(is);
        handler.postDelayed(runnable,1000);

        findViewById(R.id.btn_submitAnswer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processAnswer();
            }
        });

        updateClueBox();
    }

    public void debug(){
        TextView tv = findViewById(R.id.txt_debug);
        String debug = "Phone lat: "+Main.latitude+" long: "+Main.longitude+" Clue lat: "+trail.getCurrentClue().latitude+" clue long "+trail.getCurrentClue().longitude+ " distance: "+trail.getCurrentClue().distanceFromPhone(Main.latitude,Main.longitude,Main.elevation);
        tv.setText(debug);
    }

    public void updateClueBox(){
        EditText clueBox = findViewById(R.id.txt_locationRiddle);
        clueBox.setText(trail.getCurrentClue().getClue());
    }

    public void processAnswer(){
        EditText et = findViewById(R.id.txt_answerBox);
        String answer = et.getText().toString();
        if(trail.getCurrentClue().isAnswerCorrect(answer)){
            trail.advance();

        }
    }

    public void queryTrails(){

        if(trail.isTrailFinished()){
            Intent i = new Intent(getApplicationContext(),LeaderBoardActivity.class);
            startActivity(i);
            handler.removeCallbacks(runnable);
        }else{

            TrailClue tc = trail.getCurrentClue();

            EditText tb = findViewById(R.id.txt_answerBox);
            Button submit = findViewById(R.id.btn_submitAnswer);

            int v = (tc.isClueInRange(Main.latitude,Main.longitude)) ? View.VISIBLE : View.INVISIBLE;

            tb.setVisibility(v);
            submit.setVisibility(v);
        }
    }
}
