package com.example.julia.escapethecity;

import java.util.ArrayList;

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
}
