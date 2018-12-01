package com.example.julia.escapethecity;

import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity{

    LocationManager lm;
    public static double latitude,longitude,elevation;
    public static String username = "";
    public static String password = "";

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
    public static InputStream clueInputStream;
    boolean hasLoaded = false;
    String response = "";



    // TODO: fix this using this link: https://stackoverflow.com/questions/22755063/not-receiving-params-in-php-from-android-post
    public void loadTrailFromURL(){
        Log.log("loadTrailFromURL");
        try {
            /*URL url = new URL("http://kiralee.ddns.net/etc.php");
            String data = URLEncoder.encode("username", "UTF-8")
                    + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8")
                    + "=" + URLEncoder.encode(password, "UTF-8");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            String line="";

            while((line = reader.readLine()) != null){
                Log.log(line);
                InputStream is = new URL(line).openStream();
                clueInputStream=is;
                Log.log("Opened input stream: "+is);
            }*/
            /*HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://kiralee.ddns.net/etc.php");
            //post.setHeader("content-type", "application/json");
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username", username));
            pairs.add(new BasicNameValuePair("password", password));
            post.setEntity(new UrlEncodedFormEntity(pairs));

            HttpResponse resp = httpClient.execute(post);
            String line = EntityUtils.toString(resp.getEntity());*/
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://kiralee.ddns.net/etc.php");
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username", username));
            pairs.add(new BasicNameValuePair("password", password));
            httppost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
            HttpResponse response = httpclient.execute(httppost); // Execute Post to URL
            String st = EntityUtils.toString(response.getEntity()); // This is the result from php web
            String line = st; // You should register a variable for finalResult;
            Log.log("Response from server is: ["+line+"]");

            hasLoaded = true;

            this.response = line;
            if(line.contains("ERROR")){
                return;
            }

            InputStream is = new URL(line).openStream();
            clueInputStream=is;
            Log.log("Opened input stream: "+is);
        }catch(Exception e){
            Log.log(e.getMessage());
            e.printStackTrace();
            hasLoaded=true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
//                    InputStream is = new URL("http://kiralee.ddns.net/trail1.txt").openStream();
//                    clueInputStream=is;
//                    Log.log("Opened input stream: "+is);
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });

        thread.start();
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
        addTextHandlers();
    }

    private void addTextHandlers(){
        EditText username = findViewById(R.id.txt_username);
        EditText password = findViewById(R.id.txt_password);

        View.OnKeyListener okl = new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    Button login = findViewById(R.id.btn_login);
                    login.callOnClick();
                    return true;
                }

                return false;
            }
        };

        username.setOnKeyListener(okl);
        password.setOnKeyListener(okl);
    }

    private void addButtonHandlers(){
        // Login button
        final Button button = findViewById(R.id.btn_login);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                username = findViewById(R.id.txt_username).toString();
                EditText et = findViewById(R.id.txt_username);
                EditText pt = findViewById(R.id.txt_password);
                username=et.getText().toString();
                password=pt.getText().toString();

                button.setEnabled(false);

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadTrailFromURL();
                    }
                });
                t.start();

                // Wait until the text file has been loaded correctly
                do{
                    try {
                        Thread.sleep(1000);
                    }catch(Exception e){
                        Log.log(e.getMessage());
                        e.printStackTrace();
                        button.setEnabled(false);
                    }
                }while(!hasLoaded);

                button.setEnabled(true);

                if(!response.contains("ERROR")) {
                    Log.log("Response is not error, continuing to new activity");
                    // Code here executes on main thread after user presses button
                    Intent i = new Intent(getApplicationContext(), TrailListActivity.class);
                    startActivity(i);
                }else{
                    Log.log("Response apparently contains error: ["+response+"] not continuing");
                }
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
