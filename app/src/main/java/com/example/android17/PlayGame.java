package com.example.android17;

import com.example.android17.model.Piece;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import com.example.android17.model.Game;
import com.example.android17.model.Queen;
import com.example.android17.model.King;
import java.util.concurrent.TimeUnit;


public class PlayGame extends AppCompatActivity implements OnItemClickListener {
    private boolean pieceIsChosen;
    private Game game;
    private int xFinal;
    private int yFinal;
    private GridView board;
    private SquareAdapter adapter;
    private TextView status;
    private Piece pieceToMove;
    void sleep(int sec) {
        try {
            TimeUnit.SECONDS.sleep(1);}
        catch(Exception e){
            System.out.println(e);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //from chess class in Chess project
        this.game = new Game();
        King whiteKing = (King)game.board[4][7];
        King blackKing = (King)game.board[4][0];
        boolean drawOffer = false;
        char promote = 'Q';
        game.updateValidMoves(-1);
        game.updateValidMoves(1);
        whiteKing.generateValidMoves(game.board);
        blackKing.generateValidMoves(game.board);

        adapter = new SquareAdapter(this, game.board);
        setContentView(R.layout.play_game);
        final GridView chessBoardGridView = findViewById(R.id.board);
        chessBoardGridView.setAdapter(adapter);
        chessBoardGridView.setOnItemClickListener(this);
        pieceIsChosen=false;
        this.board = chessBoardGridView;
        board.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        status = findViewById(R.id.statusView);
        status.setTextColor(0xFFFFFFFF);
        if (!pieceIsChosen) {
            pieceToMove = (Piece) adapter.getItem(position);
            if (pieceToMove == null) {
                status.setText("Choose a piece to move");
                status.setTextColor(0xFFD2000F);
                return;
            } else {
                pieceIsChosen=true;
                status.setText("OK");
            }
        } else {
            status.setText("Trying to move a piece");
            
            xFinal = position%8;
            yFinal = position/8;
            status.setText("Moving piece to "+xFinal+", "+yFinal+" , color:"+pieceToMove.color);
            if (pieceToMove.move(game.board, xFinal, yFinal, pieceToMove.color)) {

                status.setText("attempting to move a piece");
                sleep(1);
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
                pieceIsChosen = false;
            }
        }
    }

}
