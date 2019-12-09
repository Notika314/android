package com.example.android17;

import android.os.Bundle;
import android.widget.ListView;
import java.io.File;
import java.io.*;
import java.util.*;
import com.example.android17.model.GameView;
import java.util.Collections;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class SavedGames extends AppCompatActivity {
    GameView[] gameViews;
    private ListView games;
    private String[] gamesNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_games);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        games = findViewById(R.id.games_list);
//        try {
//            gameViews = findGames();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }

        gamesNames = getResources().getStringArray(R.array.games_array);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, R.layout.game, gamesNames);
        games.setAdapter(adapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



}
