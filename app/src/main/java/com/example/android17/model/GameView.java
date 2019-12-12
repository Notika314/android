package com.example.android17.model;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.io.Serializable;
//import java.util.Date;
import java.util.*;
import java.io.*;
import android.content.Context;

public class GameView implements Serializable, Comparable<GameView> {
    public static ArrayList<GameView> views=new ArrayList<GameView>();
    public static int index;
    public String name;
    public static Context context;
    public Date date;
    public LinkedList<String[][]> moves;
    static final long serialVersionUID = 1L;
    public GameView() {
        this.moves = new LinkedList<String[][]>();
    }

    public void addView(Piece board[][]){
        String[][] temp = new String[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    temp[i][j] = board[i][j].toString();
                }
            }
        }
        this.moves.add(temp);
    }

    public void addToList() {
        int i = Collections.binarySearch(views, this);
        i = ~i;
        views.add(i, this);
        //moves = new LinkedList<String[][]>();
    }


    public void addView(String[][] view){
        moves.add(view);
        printLatsView();
    }

    public boolean equals(Object o) {
        if (!( o instanceof GameView)) return false;
        GameView gV = (GameView) o;
        if (gV.date.equals(this.date)) return true;
        else return false;
    }

    public void saveName(String name) {
        this.name = name;
        this.date = new Date(System.currentTimeMillis());
        //views.add(this);
    }

    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        return this.name + " saved on " + df.format(this.date);
    }

    public int compareTo(GameView o) {
        return this.date.compareTo(o.date);
    }

    public void writeGameView () throws IOException {
        String filename = context.getFilesDir()+"/"+this.name+".ser";
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(this);
        os.close();
        fos.close();
    }

    public void printLatsView() {
        String[][] lastBoard = this.moves.getLast();
          for (int i=0;i<8;i++) {
              for (int j=0;j<8;j++) {
                  System.out.print(lastBoard[j][i] +" ");
              }
              System.out.println();
          }
    }
   
}