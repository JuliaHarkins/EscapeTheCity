package com.example.julia.escapethecity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.design.widget.TextInputEditText;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;

import java.io.InputStream;
import java.net.URL;

public class LocationRiddleActivity extends Activity {

    private int gpsCoolDown = 0;
    private final int MAX_GPS_COOLDOWN = 5;


    Trail trail;

    private Handler handler = new Handler();
    private int secondsDelayed;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(!trail.isTrailFinished()) {
                gpsCoolDown--;
                if (gpsCoolDown < 0)
                    gpsCoolDown = 0;
                secondsDelayed++;
                queryTrails();
                debug();
                handler.postDelayed(this, 1000);
            }


        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_UserLocation);
        bmapFragment.getMapAsync(this);*/

        setContentView(R.layout.locationriddle);

        trail = new Trail();
        InputStream is;
        try {
            is = new URL("http://kiralee.ddns.net/trail1.txt").openStream();
        }catch(Exception e){
            Log.log("Cannot load input stream from url - returning built in instead");
            Log.log(e.getMessage());
            is = this.getApplicationContext().getResources().openRawResource(R.raw.trail2);
        }
        trail.fromInputStream(is);
        handler.postDelayed(runnable,1000);

        findViewById(R.id.btn_submitAnswer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processAnswer();
            }
        });

        updateClueBox();
        TrailClue tc = trail.getCurrentClue();
        EditText tb = findViewById(R.id.txt_answerBox);
        Button submit = findViewById(R.id.btn_submitAnswer);

        //int v = (tc.isClueInRange(Main.latitude,Main.longitude)) ? View.VISIBLE : View.INVISIBLE;
        int v = (gpsCoolDown>0) ? View.VISIBLE : View.INVISIBLE;

        if(tc.isClueInRange(Main.latitude,Main.longitude)){
            gpsCoolDown=MAX_GPS_COOLDOWN;
        }

        tb.setVisibility(v);
        submit.setVisibility(v);

        addTextListeners();
    }

    public void addTextListeners(){
        EditText answer = findViewById(R.id.txt_answerBox);

        View.OnKeyListener okl = new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    Button login = findViewById(R.id.btn_submitAnswer);
                    login.callOnClick();
                    return true;
                }

                return false;
            }
        };

        answer.setOnKeyListener(okl);
    }

    public void debug(){
        TextView tv = findViewById(R.id.txt_debug);
        String debug = "Clue Radius: "+TrailClue.CLUE_RADIUS+ "Phone lat: "+Main.latitude+" long: "+Main.longitude+" Clue lat: "+trail.getCurrentClue().latitude+" clue long "+trail.getCurrentClue().longitude+ " distance: "+trail.getCurrentClue().distanceFromPhone(Main.latitude,Main.longitude,Main.elevation);
        tv.setText(debug);
    }

    public void updateClueBox(){
        String clue = trail.getCurrentClue().getClue();
        TextView clueBox = findViewById(R.id.txt_locationRiddle);
        clueBox.setText(clue);
        Log.log("Text view: "+clueBox+" changed text to: "+clue);
    }

    public void processAnswer(){
        EditText et = findViewById(R.id.txt_answerBox);
        String answer = et.getText().toString();
        if(trail.getCurrentClue().isAnswerCorrect(answer)){
            gpsCoolDown=0;
            trail.advance();
            if(trail.isTrailFinished()){
                Log.log("Trail finished - within processAnswer. Attempting to switch to leaderboard");
                //handler.removeCallbacks(runnable);
                Intent i = new Intent(getApplicationContext(),LeaderBoardActivity.class);
                i.putExtra("secondsDelayed",secondsDelayed);
                startActivity(i);
            }

            updateClueBox();
        }
    }

    public void queryTrails(){

        //Log.log("queryTrails is trail finished: "+trail.isTrailFinished());

        if(trail.isTrailFinished()){

            /*handler.removeCallbacks(runnable);
            Intent i = new Intent(getApplicationContext(),LeaderBoardActivity.class);
            i.putExtra("secondsDelayed",secondsDelayed);
            startActivity(i);*/
        }else{

            TrailClue tc = trail.getCurrentClue();

            //Log.log("Trail is not finished, trail clue is : "+tc);

            EditText tb = findViewById(R.id.txt_answerBox);
            Button submit = findViewById(R.id.btn_submitAnswer);

            //int v = (tc.isClueInRange(Main.latitude,Main.longitude)) ? View.VISIBLE : View.INVISIBLE;
            int v = (gpsCoolDown>0) ? View.VISIBLE : View.INVISIBLE;

            if(tc.isClueInRange(Main.latitude,Main.longitude)){
                gpsCoolDown=MAX_GPS_COOLDOWN;
            }

            tb.setVisibility(v);
            submit.setVisibility(v);
        }
    }
}
