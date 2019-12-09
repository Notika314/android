package com.example.android17.model;

import java.util.LinkedList;
import java.io.Serializable;
//import java.util.Date;
import java.util.*;
import java.io.*;
import android.content.Context;

public class GameView implements Serializable {
    public static ArrayList<GameView> views=new ArrayList<GameView>();
    public String name;
    static Context context;
    public Date date;
    public LinkedList<String> moves;
    static final long serialVersionUID = 1L;
    public GameView() {
        moves = new LinkedList<String>();
    }

    public void addView(String view){
        moves.add(view);
    }

    public boolean equals(Object o) {
        if (!( o instanceof GameView)) return false;
        GameView gV = (GameView) o;
        if (gV.date.equals(this.date)) return true;
        else return false;
    }

    public void saveName(String name) {
        this.name = name;
        this.date = new Date();
//        views.add(this);
    }

    public int compareTo(GameView o) {
        return this.date.compareTo(o.date);
    }


    public static void writeGameView (GameView view) throws IOException {
        String filename = "data/games/"+view.name+".ser";
        FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(view);
        os.close();
        fos.close();
    }
   
}