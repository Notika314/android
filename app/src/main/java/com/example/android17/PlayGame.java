package com.example.android17;

import com.example.android17.model.Bishop;
import com.example.android17.model.Knight;
import com.example.android17.model.Pawn;
import com.example.android17.model.Piece;
import android.content.Intent;
import android.content.DialogInterface;
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
import com.example.android17.model.Piece;
import com.example.android17.model.Queen;
import com.example.android17.model.King;
import com.example.android17.model.Rook;
import android.widget.Button;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.LinkedList;
import android.graphics.Color;
import android.app.AlertDialog;


public class PlayGame extends AppCompatActivity implements OnItemClickListener {
    //need this for undo - to know if there are previous steps to revert back to
    public boolean canUndo;

    public Button resign_btn, draw_btn, undo_btn, ai_btn;
    private boolean pieceIsChosen;
    String color;
    public boolean drawOfferred;
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
//        this.allMoves = new LinkedList<Piece[8][8]>();
        this.game = new Game();
        //set hasMoved to false until first move happens
        canUndo = false;

        this.drawOfferred = true;
        whiteKing = (King)game.board[4][7];
        blackKing = (King)game.board[4][0];
//        boolean drawOffer = false;
        game.updateValidMoves(-1);
        game.updateValidMoves(1);
        whiteKing.generateValidMoves(game.board);
        blackKing.generateValidMoves(game.board);

        adapter = new SquareAdapter(this, game.board);
        setContentView(R.layout.play_game);
        final GridView chessBoardGridView = findViewById(R.id.board);
        chessBoardGridView.setAdapter(adapter);
        chessBoardGridView.setOnItemClickListener(this);
//        resign_btn.setOnClickListener()
        pieceIsChosen=false;
        this.board = chessBoardGridView;
        resign_btn = (Button) findViewById(R.id.resign_btn);
        draw_btn = (Button) findViewById(R.id.draw_btn);
        ai_btn = (Button) findViewById(R.id.ai_btn);
        undo_btn = (Button) findViewById(R.id.undo_btn);

        resign_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v ) {
                PlayGame activity = PlayGame.this;
                SquareAdapter adapter = activity.adapter;
                Game game = activity.game;
//                board.move("resign");
                String loser = game.currMove==-1? "Whites" : "Blacks";
                String winner  = game.currMove==-1? "Blacks": "Whites";
                status.setText(loser+" resigned. "+ winner+" won the game.");
//                activity.movetn.setEnabled(false);
                activity.resign_btn.setEnabled(false);
                activity.draw_btn.setEnabled(false);
                activity.undo_btn.setEnabled(false);
                activity.ai_btn.setEnabled(false);
                chessBoardGridView.setOnItemClickListener(null);
//                saveGame(game);   need to implement saving the game
            }
        });

        draw_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v ) {
                PlayGame activity = PlayGame.this;
                SquareAdapter adapter = activity.adapter;
                Game game = activity.game;
                if (!activity.drawOfferred) {
                    activity.drawOfferred = true;
                    status.setText("Draw?");
                } else {
                    if (activity.drawOfferred) {
                        status.setText("Draw accepted. End of the game");
                        activity.resign_btn.setEnabled(false);
                        activity.draw_btn.setEnabled(false);
                        activity.undo_btn.setEnabled(false);
                        activity.ai_btn.setEnabled(false);
                        chessBoardGridView.setOnItemClickListener(null);
//                        saveGame(game);     need to implement saving the game
                    }
                }
            }
        });

        ai_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v ) {
                PlayGame activity = PlayGame.this;
                SquareAdapter adapter = activity.adapter;
                Game game = activity.game;
                Piece pieceToMove = game.choseRandomPiece();
                int x = pieceToMove.xPos;
                int y = pieceToMove.yPos;
                Random r = new Random();
                int randomInd = r.nextInt(63);
                while (pieceToMove.validMoves[randomInd%8][randomInd/8]<=0) {
                    randomInd = r.nextInt(63);
                }
                xFinal = randomInd%8;
                yFinal = randomInd/8;
                status.setText("Moving piece to "+xFinal+", "+yFinal+" , color:"+pieceToMove.color);
                if (pieceToMove.type == 'p' && ((pieceToMove.color == -1 && yFinal == 0) ||
                        (pieceToMove.color == 1 && yFinal == 7))) {
                    promote();
                    game.addBoard(game.board);
                    canUndo = true;
//                    game.copyBoard();
                    return;
                }
                if (pieceToMove.move(game,game.board, xFinal, yFinal, game.currMove)) {
                    canUndo = true;
                    status.setText("Choose a piece to move");
                    game.addBoard(game.board);
                    color = game.currMove==-1? "black" : "white" ;
                    status.setText("Choose a " + color+" piece to move");
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

//                    board.setOnTouchListener(new View.OnTouchListener() {
//                        public boolean onTouch(View v, MotionEvent event) {
//                            return event.getAction() == MotionEvent.ACTION_MOVE;
//                        }
//                    });
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

                    board.setOnTouchListener(new View.OnTouchListener() {
                        public boolean onTouch(View v, MotionEvent event) {
                            return event.getAction() == MotionEvent.ACTION_MOVE;
                        }
                    });

                    King king2 = game.currMove==-1 ?  whiteKing : blackKing;
                    if (king2.isInCheck && !king2.hasValidMove && !game.protector() && !game.blocker()) {
                        String winner = game.currMove==-1? "Black" : "White" ;
                        status.setText("Checkmate. "+winner+" wins");
                        return;
                    }
                    if (game.hasNoValidMoves() ) {
                        King king = game.currMove==-1 ?  whiteKing : blackKing;
                        if (!king.isInCheck) {
                            status.setText("Draw by stalemate");
                        } else {
                            String winner = game.currMove==-1? "Black" : "White" ;
                            status.setText("Checkmate. "+winner+" wins");
                            return;
                        }
                    }
                    game.disarmShields();
                    tempView.setBackgroundColor(0x00000000);
                    clearMoves();
                    pieceIsChosen = false;
//                    game.copyBoard();

                }

                /////////////////////
            }
        });
        undo_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v ) {
                PlayGame activity = PlayGame.this;
                SquareAdapter adapter = activity.adapter;
                Game game = activity.game;
                if (!canUndo) {
                    status.setText("Cannot use undo button until a move is made");
                } else {
                    game.board = game.copyBoard(game.prevBoard);

                    game.addBoard(game.board);

                    color = game.currMove==-1? "black" : "white" ;
                    status.setText("Choose a " + color+" piece to move");
//                    board.setAdapter(adapter);
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

//                    adapter = new SquareAdapter(PlayGame.this, game.board);
//                    setContentView(R.layout.play_game);
//                    final GridView chessBoardGridView = findViewById(R.id.board);
//                    chessBoardGridView.setAdapter(adapter);
//                    chessBoardGridView.setOnItemClickListener(PlayGame.this);
//
//                    board.setOnTouchListener(new View.OnTouchListener() {
//                        public boolean onTouch(View v, MotionEvent event) {
//                            return event.getAction() == MotionEvent.ACTION_MOVE;
//                        }
//                    });
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

                    King king2 = game.currMove==-1 ?  whiteKing : blackKing;
                    if (king2.isInCheck && !king2.hasValidMove && !game.protector() && !game.blocker()) {
                        String winner = game.currMove==-1? "Black" : "White" ;
                        status.setText("Checkmate. "+winner+" wins");
                        return;
                    }
                    if (game.hasNoValidMoves() ) {
                        King king = game.currMove==-1 ?  whiteKing : blackKing;
                        if (!king.isInCheck) {
                            status.setText("Draw by stalemate");

                        }
                        else {
                            String winner = game.currMove==-1? "Black" : "White" ;
                            status.setText("Checkmate. "+winner+" wins");
                            return;
                        }
                    }
                    game.disarmShields();

                    clearMoves();
                    pieceIsChosen = false;

//                    adapter = new SquareAdapter(PlayGame.this, game.board);
//                    setContentView(R.layout.play_game);
//                    final GridView chessBoardGridView = findViewById(R.id.board);
//                    chessBoardGridView.setAdapter(adapter);
//                    chessBoardGridView.setOnItemClickListener(PlayGame.this);
                    board.setAdapter(adapter);

                    board.setOnTouchListener(new View.OnTouchListener() {
                        public boolean onTouch(View v, MotionEvent event) {
                            return event.getAction() == MotionEvent.ACTION_MOVE;
                        }
                    });

                    canUndo = false;
                }
            }
        });
//        allMoves.add(this.game.board);
        board.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }
        });
    }

//    private void saveGame(Game game) {
//        Bundle bundle = new Bundle();
//        String key = "views";
//        bundle.putParcelable(key,game.allViews);
//    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.drawOfferred = false;
        status = findViewById(R.id.statusView);
        status.setTextColor(0xFFFFFFFF);
        if (!pieceIsChosen) {
            pieceToMove = (Piece) adapter.getItem(position);
            if (pieceToMove == null) {
                String color = game.currMove==-1 ? "White" : "Black";
                status.setText("You need to choose a " + color+" piece to move");
                status.setTextColor(0xFFD2000F);
                return;
            } else {
                pieceIsChosen=true;
                status.setText("OK");
                if (pieceToMove.color==game.currMove) {
                    status.setText("");
                }
                else {
                    color = game.currMove==-1? "Whites" : "Blacks";
                    status.setTextColor(0xFFD2000F);
                    status.setText("It's "+ color +" turn to move.");

                    // ?? need this??
                    pieceIsChosen=false;
                }
                view.setBackgroundColor(0x990000FF);
                legalMoves(pieceToMove);
                tempView = view;
            }
        } else {
            status.setText("Trying to move a piece");
            int x = pieceToMove.xPos;
            int y = pieceToMove.yPos;
            xFinal = position%8;
            yFinal = position/8;
            status.setText("Moving piece to "+xFinal+", "+yFinal+" , color:"+pieceToMove.color);
//            this.game.findPiecesThatCanMove();  we only need this for ai
            if (pieceToMove.type == 'p' &&
                    ((pieceToMove.color == -1 && yFinal == 0) ||
                            (pieceToMove.color == 1 && yFinal == 7))) {
                promote();
                this.game.addBoard(this.game.board);
                canUndo = true;
//                game.copyBoard();
                return;
            }
            if (pieceToMove.move(game,game.board, xFinal, yFinal, game.currMove)) {
                canUndo = true;
                status.setText("Choose a piece to move");
                this.game.addBoard(this.game.board);
                color = game.currMove==-1? "black" : "white" ;
                status.setText("Choose a " + color+" piece to move");
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

                King king2 = game.currMove==-1 ?  whiteKing : blackKing;
                if (king2.isInCheck && !king2.hasValidMove && !game.protector() && !game.blocker()) {
                    String winner = game.currMove==-1? "Black" : "White" ;
                    status.setText("Checkmate. "+winner+" wins");
                    return;
                }
                if (game.hasNoValidMoves() ) {
                    King king = game.currMove==-1 ?  whiteKing : blackKing;
                    if (!king.isInCheck) {
                        status.setText("Draw by stalemate");

                    }
                    else {
                        String winner = game.currMove==-1? "Black" : "White" ;
                        status.setText("Checkmate. "+winner+" wins");
                        return;
                    }
                }
                game.disarmShields();

                tempView.setBackgroundColor(0x00000000);
                clearMoves();
                pieceIsChosen = false;
//                game.copyBoard();
//                allMoves.add(this.game.board);
            }
            else {
                if (xFinal == pieceToMove.xPos && yFinal == pieceToMove.yPos) {
                    status.setText("deselecting the piece");
                    tempView.setBackgroundColor(0x00000000);
                    clearMoves();
                    pieceIsChosen = false;
                }
                else if (game.board[xFinal][yFinal] != null && game.board[xFinal][yFinal].color == game.currMove) {
                    pieceToMove = (Piece) adapter.getItem(position);
                    pieceIsChosen=true;
                    tempView.setBackgroundColor(0x00000000);
                    clearMoves();
                    status.setText("Select new position for the piece");
                    view.setBackgroundColor(0x990000FF);
                    tempView = view;
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
        if (p.color != game.currMove) {
            return;
        }
        for (int i = 0; i < board.getChildCount(); i++) {
            if (p.validMoves[i%8][i/8] != 0) {
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


    public void promote() {
        String[] promo = {"Queen", "Bishop", "Knight", "Rook"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a piece");
        builder.setItems(promo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int chosen) {
                pieceToMove.move(game,game.board, xFinal, yFinal, pieceToMove.color);
                if (promo[chosen].equals("Queen")) {
                    game.board[pieceToMove.xPos][pieceToMove.yPos] = new Queen(pieceToMove.color, pieceToMove.xPos, pieceToMove.yPos);
                }
                else if (promo[chosen].equals("Bishop")) {
                    game.board[pieceToMove.xPos][pieceToMove.yPos] = new Bishop(pieceToMove.color, pieceToMove.xPos, pieceToMove.yPos);
                }
                else if (promo[chosen].equals("Knight")) {
                    game.board[pieceToMove.xPos][pieceToMove.yPos] = new Knight(pieceToMove.color, pieceToMove.xPos, pieceToMove.yPos);
                }
                else if (promo[chosen].equals("Rook")) {
                    game.board[pieceToMove.xPos][pieceToMove.yPos] = new Rook(pieceToMove.color, pieceToMove.xPos, pieceToMove.yPos);
                }
                pieceToMove = game.board[pieceToMove.xPos][pieceToMove.yPos];
                game.addBoard(game.board);
                //record the boardView here instead of top method
                promoteMove();
            }
        });
        builder.setOnCancelListener(dialogInterface -> {
            status.setText("Promotion Cancelled");
            clearMoves();
            pieceIsChosen = false;
        });
        builder.show();
    }

    private void promoteMove() {
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

}
//notes:
// status: feedback when wrong color selected
//