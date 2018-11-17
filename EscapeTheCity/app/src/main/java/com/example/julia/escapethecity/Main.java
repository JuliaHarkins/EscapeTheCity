package com.example.julia.escapethecity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.util.ArrayList;

public class Main extends AppCompatActivity{

    LocationManager lm;
    public static double latitude,longitude,elevation;
    public static String username;

    ArrayList<TrailClue> tempClues = new ArrayList<TrailClue>();
    ArrayList<TrailClue> demoTrail = new ArrayList<TrailClue>();
    private CountDownTimer timer;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            queryTrails();
            handler.postDelayed(this, 1000);
        }
    };

    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.log("Status change");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.log("provider disable");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.log("provider enable");
            }

            @Override
            public void onLocationChanged(Location location) {
                //Log.log("IN ON LOCATION CHANGE, lat=" + location.getLatitude() + ", lon=" + location.getLongitude());
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                elevation = location.getAltitude();
            }
        };

        try {
            Log.log("Location provider enabled");
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            //Log.log("Is location enabled: "+lm.isLocationEnabled());
        }catch(SecurityException e){
            Log.log(e.getMessage());
        }

        // Five guys
        tempClues.add(new TrailClue(52.627152, 1.294375,"What restaurant is this?","Five Guys"));

        handler.postDelayed(runnable, 1000);

        addButtonHandlers();

    }

    private void addButtonHandlers(){
        // Login button
        final Button button = findViewById(R.id.btn_login);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                username = findViewById(R.id.txt_username).toString();

                // Code here executes on main thread after user presses button
                Intent i = new Intent(getApplicationContext(),TrailListActivity.class);
                startActivity(i);
            }
        });
    }

    private void queryTrails(){
        for(TrailClue tc : tempClues){
            //Log.log("Is clue nearby: "+tc.isClueInRange(latitude,longitude));
        }
        //System.out.println("Phone lat: "+latitude+" Phone long: "+longitude);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // This is a comment to test GitKraken
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
