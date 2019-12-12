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
import android.widget.TextView;
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
    private TextView status;

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

        ///
        back_btn.setEnabled(false);
         ////       ///

        status = findViewById(R.id.statusView);
        status.setTextColor(0xFFFFFFFF);
        status.setText("Press next to play the game");
        back_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v ) {
                status.setTextSize(20);
                status.setTextColor(0xFFFFFFFF);
                if (iterate.hasPrevious()) {
                    status.setText("Press next to go forward, back to go previous move");
                    adapter = new SquareAdapter(con, iterate.previous());
                    board.setAdapter(adapter);

                }
                else {
                    status.setText("Press next to go forward");
                    return;
                }
            }
        });
        next_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v ) {
                if (iterate.hasNext()) {
                    status.setText("Press next to go forward, back to go previous move");
                    adapter = new SquareAdapter(con, iterate.next());
                    board.setAdapter(adapter);
                    back_btn.setEnabled(true);
                    //
//                    if (!iterate.hasNext()) {
//                        next_btn.setEnabled(false);
//                    }
                    //
//                    back_btn.setEnabled(true);
                }
                else {
                    status.setTextColor(0xFFD2000F);
                    status.setTextSize(40);
                    status.setText("GAME OVER");
//                    next_btn.setEnabled(false);
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
