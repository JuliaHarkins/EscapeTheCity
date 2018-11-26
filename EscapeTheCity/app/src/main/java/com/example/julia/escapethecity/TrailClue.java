package com.example.julia.escapethecity;

import android.location.Location;
import android.location.LocationManager;

import java.util.ArrayList;

public class TrailClue {

    public static double CLUE_RADIUS = Double.MAX_VALUE;
    double latitude, longitude;
    String clue, answer;
    String[] answers;
    boolean isFinished=false;

    public TrailClue(double latitude, double longitude, String clue, String answer){
        this(latitude,longitude,clue,new String[]{answer});
    }

    public TrailClue(double lat, double lon, String clue, String... answers){
        this.latitude=latitude;
        this.longitude=longitude;
        this.clue=clue;
        this.answers=answers;
    }

    public boolean isClueInRange(double phoneLat, double phoneLong){

        double diffLat = Math.abs(phoneLat-latitude);
        double diffLong = Math.abs(phoneLong-longitude);

        return(distanceBetweenPoints(latitude,longitude,phoneLat,phoneLong)<CLUE_RADIUS);
    }

    public String getClue(){
        return clue;
    }

    public boolean isAnswerCorrect(String answer){

        answer = answer.toLowerCase();

        isFinished=true;
        //return(this.answer.contains(answer));
        for(String s : answers){
            if(s.matches(answer)){
                return true;
            }
        }
        return false;
    }

    public boolean isFinished(){
        return isFinished;
    }

    public double distanceFromPhone(double phoneLat, double phoneLong,double phoneEl){

        //System.out.println("Phone lat: "+phoneLat+" Phone long: "+phoneLong);

        double diffLat = Math.abs(phoneLat-latitude);
        double diffLong = Math.abs(phoneLong-longitude);

        //return distanceBetweenPoints(latitude,longitude,phoneLat,phoneLong);
        //return Trail.distance(latitude,phoneLat,longitude,phoneLong,phoneEl,phoneEl);
        return Trail.distance(phoneLat,latitude,phoneLong,longitude,phoneEl,phoneEl);
    }
    @Override
    public String toString() {
        return "Clue: ["+clue+"] Answer: ["+answer+"] Lat: ["+latitude+"] Lon: ["+longitude+"]";
    }

    private double distanceBetweenPoints(double lat1, double lon1, double lat2, double lon2){
        double R = 6378.137; // Radius of earth in KM
        double dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
        double dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
       double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
       double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d * 1000; // meters
    }


}
