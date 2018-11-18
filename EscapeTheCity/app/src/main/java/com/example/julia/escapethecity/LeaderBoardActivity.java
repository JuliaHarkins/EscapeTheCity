package com.example.julia.escapethecity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.TreeMap;

public class LeaderBoardActivity extends Activity {

    TreeMap<Integer,String> leaderBoard = new TreeMap<>();
    int seconds;

    @Override
    public void onCreate( Bundle savedInstanceState) {

        Log.log("Creating leaderboard");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard);

        seconds = getIntent().getIntExtra("secondsDelayed",0);

        loadLeaderboard();
        newEntry();
        displayLeaderboard();
        saveLeaderboard();

        Log.log("Finished creating leaderboard");
    }

    public void displayLeaderboard(){
        //RecyclerView rv = findViewById(R.id.txt_leaderboardList);
        //rv.
        TextView et1 = findViewById(R.id.txt_leader1);
        TextView et2 = findViewById(R.id.txt_leader2);
        TextView et3 = findViewById(R.id.txt_leader3);

        NavigableSet<Integer> sortedKeys = leaderBoard.descendingKeySet();
        Iterator<Integer> i = sortedKeys.descendingIterator();

        int time1 = i.next();
        int time2 = i.next();
        int time3 = i.next();

        et1.setText(getFormattedEntry(leaderBoard.get(time1),time1));
        et2.setText(getFormattedEntry(leaderBoard.get(time2),time2));
        et3.setText(getFormattedEntry(leaderBoard.get(time3),time3));

        et1.setText(getFormattedEntry(Main.username,this.seconds));

        et1.clearFocus();
        et2.clearFocus();
        et3.clearFocus();

    }

    private String getFormattedEntry(String name, int numSeconds){

        String result = "";

        Date d = new Date(numSeconds * 1000L);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss"); // HH for 0-23
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        String time = df.format(d);

        return result+name+" : "+time;
    }

    public void newEntry(){
        String name = Main.username;
        int secondsDelayed = getIntent().getIntExtra("secondsDelayed",0);
        leaderBoard.put(secondsDelayed,name);
    }

    public void loadLeaderboard(){
        InputStream is = this.getApplicationContext().getResources().openRawResource(R.raw.leaderboard);
        try {
            Scanner br = new Scanner(is);
            while(br.hasNext()) {
                String line = br.nextLine();
                String[] lines = line.split(",");

                int time = Integer.parseInt(lines[0]);
                String name = lines[1];

                leaderBoard.put(time,name);
            }
        }catch(Exception e){
            Log.log(e.getMessage());
        }
    }

    public void saveLeaderboard(){

    }

}
