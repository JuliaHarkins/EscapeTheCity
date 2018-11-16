package com.example.julia.escapethecity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Trail {

    ArrayList<TrailClue> clues = new ArrayList<TrailClue>();
    int currentClue = 0;

    public void addTrailClue(TrailClue tc){
        clues.add(tc);
    }

    public boolean isTrailFinished(){
        return currentClue >= clues.size() -1;
    }

    public TrailClue getCurrentClue(){
        return clues.get(currentClue);
    }

    public void formFile(File textFile){
        try {
            BufferedReader br = new BufferedReader(new FileReader(textFile));
            String line = br.readLine();
            String[] lines = line.split(",");

            double lat = Double.parseDouble(lines[0]);
            double lon = Double.parseDouble(lines[1]);
            String clue = lines[2];
            String answer = lines[3];

            TrailClue tc = new TrailClue(lat,lon,clue,answer);
            clues.add(tc);
        }catch(Exception e){
            Log.log(e.getMessage());
        }
    }

    public void advance(){
        currentClue++;
    }

    public void fromInputStream(InputStream is){
        try {
            Scanner br = new Scanner(is);
            while(br.hasNext()) {
                String line = br.nextLine();
                        String[] lines = line.split(",");

                double lat = Double.parseDouble(lines[0]);
                double lon = Double.parseDouble(lines[1]);
                String clue = lines[2];
                String answer = lines[3];

                TrailClue tc = new TrailClue(lat, lon, clue, answer);
                clues.add(tc);
            }
        }catch(Exception e){
            Log.log(e.getMessage());
        }
    }
}
