package com.example.android17;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.example.android17.model.Game;
import com.example.android17.model.GameView;
import com.example.android17.model.King;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class ReplayGame extends AppCompatActivity {

    public Button back_btn, next_btn;
    private SquareAdapter adapter;
    private GridView board;
    private GameView recording;
    private ListIterator<String[][]> iterate;
    private String[][] current;
    private Context con = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        recording = GameView.views.get(GameView.index);
        super.onCreate(savedInstanceState);
        LinkedList<String[][]> first = recording.moves;
        iterate = first.listIterator();
        adapter = new SquareAdapter(this, iterate.next());
        setContentView(R.layout.replay_game);
        this.board = findViewById(R.id.board);
        board.setAdapter(adapter);
        back_btn = findViewById(R.id.back_btn);
        next_btn = findViewById(R.id.next_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v ) {
                if (iterate.hasPrevious()) {
                    adapter = new SquareAdapter(con, iterate.previous());
                    board.setAdapter(adapter);
                }
                else {
                    return;
                }
            }
        });
        next_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v ) {
                if (iterate.hasNext()) {
                    adapter = new SquareAdapter(con, iterate.next());
                    board.setAdapter(adapter);
                }
                else {
                    System.out.println("END OF THE ROAD");
                    return;
                }
            }
        });

        board.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }
        });
    }



}
