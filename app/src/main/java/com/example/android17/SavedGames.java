package com.example.android17;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import java.io.File;
import java.io.*;
import java.util.*;
import com.example.android17.model.GameView;
import java.util.Collections;
import java.util.stream.Collectors;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.AdapterView.OnItemClickListener;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class SavedGames extends AppCompatActivity implements OnItemClickListener {
    GameView[] gameViews;
    private ListView games;
    private String[] gamesNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_games);
        try {
            loadInFiles();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        games = findViewById(R.id.games_list);
        //gamesNames = getResources().getStringArray(R.array.games_array);
        gamesNames = GameView.views.stream().map(o -> o.toString()).collect(Collectors.toList()).toArray(new String[GameView.views.size()]);
        /*if (gamesNames == null || gamesNames.length == 0) {
            gamesNames = new String[]{"Empty"};
        }*/
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, R.layout.game, gamesNames);
        games.setAdapter(adapter);
        games.setOnItemClickListener(this);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GameView.index = position;
        Intent intent = new Intent(this, ReplayGame.class);
        startActivity(intent);
    }

    private void loadInFiles() throws IOException, ClassNotFoundException {
        File dir = new File(GameView.context.getFilesDir()+"/");
        /*
        File file = new File(dir, "null.ser");
        file.delete();*/
        File[] files = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith("ser");
            }
        });
        if (files == null) {
            return;
        }
        for (int i=0;i<files.length;i++) {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(files[i]));
            GameView view = (GameView) ois.readObject();
            //System.out.println("Reading user "+ user.userName +", with password "+user.password +" from a file .");
            ois.close();
            int j = Collections.binarySearch(GameView.views, view);
            if (j < 0) {
                j = ~j;
                GameView.views.add(j, view);
            }
        }
    }

}
