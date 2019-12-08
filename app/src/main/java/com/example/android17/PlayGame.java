package com.example.android17;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import com.example.android17.model.Game;

public class PlayGame extends AppCompatActivity implements OnItemClickListener {
    private Game game;
    private GridView board;
    private SquareAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.game = new Game();
        adapter = new SquareAdapter(this, game.board);
        setContentView(R.layout.play_game);
        final GridView chessBoardGridView = findViewById(R.id.board);
        chessBoardGridView.setAdapter(adapter);
        chessBoardGridView.setOnItemClickListener(this);
        this.board = chessBoardGridView;
        board.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
