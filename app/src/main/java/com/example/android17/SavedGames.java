package com.example.android17;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.Button;
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
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.Toast;

public class SavedGames extends AppCompatActivity implements OnItemClickListener {
    public Button open_btn, name_btn, date_btn, delete_btn;
    private ListView games;
    private String[] gamesNames;
    long mLastClickTime;
    View lastViewed;
    Context con;
    int index = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_games);
        con = this;
        try {
            loadInFiles();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        games = findViewById(R.id.games_list);
        gamesNames = GameView.views.stream().map(GameView::toString).collect(Collectors.toList()).toArray(new String[GameView.views.size()]);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, R.layout.game, gamesNames);
        games.setAdapter(adapter);
        games.setOnItemClickListener(this);

        name_btn = findViewById(R.id.name_btn);
        delete_btn = findViewById(R.id.delete_btn);
        date_btn = findViewById(R.id.date_btn);
        open_btn = findViewById(R.id.open_btn);
        open_btn.setEnabled(false);
        delete_btn.setEnabled(false);

        name_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v ) {
                gamesNames = GameView.views.stream().sorted(Comparator.comparing(GameView::getName)).
                        collect(Collectors.toList()).stream().map(GameView::toString).collect(Collectors.toList()).toArray(new String[GameView.views.size()]);
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<>(con, R.layout.game, gamesNames);
                games.setAdapter(adapter);
                index = -1;
            }
        });
        delete_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v ) {
                if (index < 0) {
                    return;
                }
                GameView temp = GameView.views.get(index);
                GameView.views.remove(index);
                File dir = new File(GameView.context.getFilesDir()+"/");
                File file = new File(dir, temp.name+".ser");
                file.delete();
                gamesNames = GameView.views.stream().map(GameView::toString).collect(Collectors.toList()).toArray(new String[GameView.views.size()]);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(con, R.layout.game, gamesNames);
                games.setAdapter(adapter);
                index = -1;
                open_btn.setEnabled(false);
                delete_btn.setEnabled(false);
            }
        });
        date_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v ) {
                gamesNames = GameView.views.stream().sorted().
                        collect(Collectors.toList()).stream().map(GameView::toString).collect(Collectors.toList()).toArray(new String[GameView.views.size()]);
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<>(con, R.layout.game, gamesNames);
                games.setAdapter(adapter);
                index = -1;
            }
        });
        open_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v ) {
                GameView.index = index;
                Intent intent = new Intent(con, ReplayGame.class);
                index = -1;
                open_btn.setEnabled(false);
                delete_btn.setEnabled(false);
                //lastViewed.setBackgroundColor(0x00000000);
                lastViewed.setSelected(false);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (lastViewed != null) {
            lastViewed.setSelected(false);
        }
        view.setBackgroundColor(0x00000000);
        open_btn.setEnabled(true);
        delete_btn.setEnabled(true);
        view.setSelected(true);
        lastViewed = view;
        index = position;
    }


    private void loadInFiles() throws IOException, ClassNotFoundException {
        File dir = new File(GameView.context.getFilesDir()+"/");
        /*
        File file = new File(dir, "Second.ser");
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
