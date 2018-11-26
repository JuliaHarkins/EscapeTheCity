package com.example.julia.escapethecity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Trail {

    ArrayList<TrailClue> clues = new ArrayList<TrailClue>();
    int currentClue = 0;

    public void addTrailClue(TrailClue tc){
        clues.add(tc);
    }

    public boolean isTrailFinished(){

        //Log.log("isTrailFinished - currentClue = "+currentClue+" clues.size = "+clues.size());

        return currentClue >= clues.size();
    }

    public TrailClue getCurrentClue(){


        try {
            return clues.get(currentClue);
        }catch(Exception e){
            Log.log(e.getMessage());
            return clues.get(clues.size()-1);
        }
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

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    public void advance(){
        currentClue++;
    }

    public void fromInputStream(InputStream is){
        try {
            Scanner br = new Scanner(is);
            while(br.hasNext()) {
                String line = br.nextLine();

                if(line.startsWith("#")){
                    continue;
                }
                        String[] lines = line.split(",");

                double lat = Double.parseDouble(lines[0]);
                double lon = Double.parseDouble(lines[1]);
                String clue = lines[2];
                String[] answers = Arrays.copyOfRange(lines,3,lines.length);

                TrailClue tc = new TrailClue(lat, lon, clue, answers);
                clues.add(tc);
            }
        }catch(Exception e){
            Log.log(e.getMessage());
        }
    }
}
