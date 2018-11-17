package com.example.julia.escapethecity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.RecyclerView;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.TreeMap;

public class LeaderBoardActivity extends Activity {

    TreeMap<Integer,String> leaderBoard = new TreeMap<>();

    @Override
    public void onCreate( Bundle savedInstanceState) {

        Log.log("Creating leaderboard");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard);

        //loadLeaderboard();
        //newEntry();
        //displayLeaderboard();
        //saveLeaderboard();

        Log.log("Finished creating leaderboard");
    }

    public void displayLeaderboard(){
        RecyclerView rv = findViewById(R.id.txt_leaderboardList);
        //rv.
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
