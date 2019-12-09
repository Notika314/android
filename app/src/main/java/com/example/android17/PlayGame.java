package com.example.android17;

import com.example.android17.model.Piece;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import com.example.android17.model.Game;
import com.example.android17.model.Queen;
import com.example.android17.model.King;
import java.util.concurrent.TimeUnit;
import android.graphics.Color;


public class PlayGame extends AppCompatActivity implements OnItemClickListener {
    private boolean pieceIsChosen;
    private Game game;
    private int xFinal;
    private int yFinal;
    private GridView board;
    private SquareAdapter adapter;
    private TextView status;
    private Piece pieceToMove;
    private King whiteKing;
    private King blackKing;
    private View tempView;
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
        whiteKing = (King)game.board[4][7];
        blackKing = (King)game.board[4][0];
        boolean drawOffer = false;
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
                view.setBackgroundColor(0x990000FF);
                legalMoves(pieceToMove);
                tempView = view;
            }
        } else {
            status.setText("Trying to move a piece");
            xFinal = position%8;
            yFinal = position/8;
            status.setText("Moving piece to "+xFinal+", "+yFinal+" , color:"+pieceToMove.color);
            if (pieceToMove.move(game.board, xFinal, yFinal, game.currMove)) {
                status.setText("Choose a piece to move");
                //sleep(1);
                //adapter = new SquareAdapter(this, game.board);
                //setContentView(R.layout.play_game);
                //adapter.refresh();
                //final GridView chessBoardGridView = findViewById(R.id.board);
                //chessBoardGridView.setOnItemClickListener(this);
                //this.board = chessBoardGridView;
                board.setAdapter(adapter);
                if (game.currMove == -1) {
                    whiteKing.isInCheck = false;
                }
                else {
                    blackKing.isInCheck = false;
                }
                game.currMove *= -1;
                game.clearPassant(game.currMove);
                game.updateValidMoves(-game.currMove);
                game.updateValidMoves(game.currMove);
                board.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        return event.getAction() == MotionEvent.ACTION_MOVE;
                    }
                });
                if (game.currMove == -1) {
                    blackKing.generateValidMoves(game.board);
                    whiteKing.generateValidMoves(game.board);
                    if (whiteKing.isInCheck) {
                        status.setText("White in Check");
                    }
                }
                else {
                    whiteKing.generateValidMoves(game.board);
                    blackKing.generateValidMoves(game.board);
                    if (blackKing.isInCheck) {
                        status.setText("Black in Check");
                    }
                }
                tempView.setBackgroundColor(0x00000000);
                clearMoves();
                pieceIsChosen = false;
            }
            else {
                if (xFinal == pieceToMove.xPos && yFinal == pieceToMove.yPos) {
                    status.setText("delselecting the piece");
                    tempView.setBackgroundColor(0x00000000);
                    clearMoves();
                    pieceIsChosen = false;
                }
                else if (game.board[xFinal][yFinal] != null && game.board[xFinal][yFinal].color == game.currMove) {
                    pieceToMove = (Piece) adapter.getItem(position);
                    pieceIsChosen=true;
                    tempView.setBackgroundColor(0x00000000);
                    status.setText("New Piece Selected");
                    view.setBackgroundColor(0x990000FF);
                    tempView = view;
                    clearMoves();
                    legalMoves(pieceToMove);
                }
                else {
                    status.setText("illegal move please try again");
                    status.setTextColor(0xFFD2000F);
                }
            }
        }
    }

    public void legalMoves(Piece p) {
        for (int i = 0; i < board.getChildCount(); i++) {
            if (p.validMoves[i%8][i/8] != 0 && p.color == game.currMove) {
                ImageView view = (ImageView) board.getChildAt(i);
                view.setBackgroundColor(0x9900FF00);
            }
        }
    }

    public void clearMoves() {
        for (int i = 0; i < board.getChildCount(); i++) {
            ImageView view = (ImageView) board.getChildAt(i);
            view.setBackgroundColor(0x00000000);
        }
    }

}
