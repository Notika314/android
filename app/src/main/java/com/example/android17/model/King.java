package com.example.android17.model;

import java.util.Arrays;

/**
 * This class implements the logic that governs how a King
 * operates in a game of chess.
 * @author Natalia Bryzhatenko nb631
 * @author Christopher Taglieri cat197
 */
public class King extends Piece {
	/**
	 * Defines whether the King is in check or not.
	 */
	public boolean isInCheck;
	
	/**
	 * Initializes a King with given color and position.
	 * @param color Color
	 * @param x X Position
	 * @param y Y Position
	 */
	public King(int color, int x, int y) {
		super(color,'k',x,y);
		isInCheck = false;
	}
	
	/**
	 * Implementation of super class abstract method. Creates
	 * a copy of the King and returns the duplicate.
	 * @return A Copied Piece dynamically bound as King.
	 */
	public Piece copy() {
		King temp = new King(this.color, this.xPos, this.yPos);
		temp.validMoves = this.validMoves;
		temp.hasValidMove = this.hasValidMove;
		temp.isInCheck = this.isInCheck;
		temp.hasMoved = this.hasMoved;
		temp.hollow = true;
		return temp;
	}

	/**
	 * Move method specific to how the King operates when they move given their special move set.
	 * @param board The game board with all current available pieces located on it.
	 * @param x The x position on the board you wish to move to on your turn;
	 * @param y The y position on the board you wish to move to on your turn;
	 * @param color The color that is attempting to move, ensures strict order is adhered to in chess.
	 * @return True if move is valid and no parameters are wrong, False if something prevents the move from legally occurring
	 */
	public boolean move(Piece board[][], int x, int y, int color) {
		if (this.color != color) {
			return false;
		}
		if (x > 7 || x < 0 || y > 7 || y < 0) {
			return false;
		}
		if (this.validMoves[x][y] == 0) {
			return false;
		}
		if (this.color == -1) {
			Piece.wKingIsInDanger = new int[] {-1, -1, -1, -1};
		}
		else {
			Piece.bKingIsInDanger = new int[] {-1, -1, -1, -1};
		}
		if (this.validMoves[x][y] == 1) {
			this.hasMoved = true;
			int i = this.xPos;
			int j = this.yPos;
			this.xPos = x;
			this.yPos = y;
			board[x][y] = this;
			board[i][j] = null;
		} else if (this.validMoves[x][y]==2) { 
			if (x==6) {
				Rook theRook = (Rook)board[7][y];
				int lastX = this.xPos;
				int lastY = this.yPos;
				theRook.xPos = 5;
				theRook.yPos = y;
				theRook.hasMoved = true;
				board[7][y]=null;
				this.xPos = x;
				this.yPos = y;
				this.hasMoved = true;
				board[x][y]=this;
				board[5][y]=theRook;
				board[lastX][lastY]=null;
			} else if (x==2) {
				Rook theRook = (Rook)board[0][y];
				theRook.xPos = 3;
				theRook.yPos = y;
				theRook.hasMoved = true;
				board[0][y]=null;
				int lastX = this.xPos;
				int lastY = this.yPos;
				this.xPos = x;
				this.yPos = y;
				this.hasMoved = true;
				board[x][y]=this;
				board[3][y]=theRook;
				board[lastX][lastY]=null;
			}
		}
		return true;
	}
	
	/**
	 * Helper method used to ensure that castling is a valid more for the king.
	 * @param x X position on the board that is being checked for safety.
	 * @param y Y position on the board that is being checked for safety.
	 * @param board The game board with all current available pieces located on it.
	 * @return True if the area where the castling would occur is free of harm, false if otherwise.
	 */
	private boolean isSafeForSliding(int x, int y,Piece[][] board) {
		for (int i=0;i<8;i++) {
			for (int j=0;j<8;j++) {
				if (board[i][j]!=null && board[i][j].color!=this.color) {
					Piece opponent = board[i][j];
					if (opponent.validMoves[x][y]>0) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Simulates what would happen if the King moved to a new square. Used to make sure the
	 * King does not move himself into a checked position.
	 * @param board The game board with all current available pieces located on it.
	 * @return True if King can move to this position without being endangered, false if otherwise.
	 */
	public boolean updateStatus(Piece[][] board) {
		int[] tempDanger = new int[4];
		if (this.color == -1) {
			tempDanger = Arrays.copyOf(Piece.wKingIsInDanger, 4);
		} 
		else {
			tempDanger = Arrays.copyOf(Piece.bKingIsInDanger, 4);
		}
		for (int i=0;i<8;i++) {
			for (int j=0;j<8;j++) {
				if (board[i][j]!=null && board[i][j].color!=this.color ) {
					Piece opponent = board[i][j].copy();
					if (opponent.type!='k') {
						opponent.generateValidMoves(board);
					}
					if (this.color == -1) {
						Piece.wKingIsInDanger = Arrays.copyOf(tempDanger, 4);
					} 
					else {
						Piece.bKingIsInDanger = Arrays.copyOf(tempDanger, 4);
					}
					if (opponent.validMoves[this.xPos][this.yPos]>0) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * The logic for how a King generates the legal moves available to it. Starts
	 * with flags set to false and a clear board that is only populated and set to 
	 * true if there is some available move allowed. All moves generated are legal
	 * and do not require further pruning.
	 * @param board The game board with all current available pieces located on it.
	 */
	public void generateValidMoves(Piece board[][]) {
		this.hasValidMove = false;
		this.validMoves = new int[8][8];
		boolean tempCheck = this.isInCheck;
		if ((this.yPos+(1)<8 && this.yPos+(1)>=0 )&& (board[this.xPos][this.yPos+1]==null 
				|| board[this.xPos][this.yPos+1].color!=this.color)) {
			Piece temp = board[this.xPos][this.yPos+1];
			int i = this.xPos;
			int j = this.yPos;
			this.yPos = this.yPos+1;
			board[this.xPos][this.yPos] = this;
			board[i][j]=null;
			if (this.updateStatus(board))	{
				this.validMoves[this.xPos][this.yPos] = 1;
				this.hasValidMove = true;
			}
			board[i][j]=this;
			this.xPos=i;
			this.yPos = j;
			board[this.xPos][this.yPos+1]=temp;
		}	
		if (this.yPos-(1)<8 && this.yPos-(1)>=0 && (board[this.xPos][this.yPos-1]==null 
				|| board[this.xPos][this.yPos-1].color!=this.color)) {
			Piece temp = board[this.xPos][this.yPos - 1];
			int i = this.xPos;
			int j = this.yPos;
			this.yPos = this.yPos - 1;
			board[this.xPos][this.yPos] = this;
			board[i][j] = null;
			if (this.updateStatus(board)) {
				this.validMoves[this.xPos][this.yPos] = 1;
				this.hasValidMove = true;
			}
			board[i][j] = this;
			this.xPos = i;
			this.yPos = j;
			board[this.xPos][this.yPos - 1] = temp;
		}
		if (this.xPos+1<8 && this.xPos+1>=0 && (board[this.xPos+1][this.yPos]==null 
				|| board[this.xPos+1][this.yPos].color!=this.color)) {
			Piece temp = board[this.xPos + 1][this.yPos];
			int i = this.xPos;
			int j = this.yPos;
			this.xPos = this.xPos + 1;
			board[this.xPos][this.yPos] = this;
			board[i][j] = null;
			if (this.updateStatus(board)) {
				this.validMoves[this.xPos][this.yPos] = 1;
				this.hasValidMove = true;
			}
			board[i][j] = this;
			this.xPos = i;
			this.yPos = j;
			board[this.xPos + 1][this.yPos] = temp;
		}
		if (this.xPos-1<8 && this.xPos-1>=0 && (board[this.xPos-1][this.yPos]==null 
				|| board[this.xPos-1][this.yPos].color!=this.color)) {
			Piece temp = board[this.xPos - 1][this.yPos];
			int i = this.xPos;
			int j = this.yPos;
			this.xPos = this.xPos - 1;
			board[this.xPos][this.yPos] = this;
			board[i][j] = null;
			if (this.updateStatus(board)) {
				this.validMoves[this.xPos][this.yPos] = 1;
				this.hasValidMove = true;
			}
			board[i][j] = this;
			this.xPos = i;
			this.yPos = j;
			board[this.xPos - 1][this.yPos] = temp;
		}
		if (this.yPos+(1)<8 && this.yPos+1>=0 && (this.xPos+1)<8 && (this.xPos-1)>=0 && (board[this.xPos+1][this.yPos+1]==null 
				|| board[this.xPos+1][this.yPos+1].color!=this.color)) {
			Piece temp = board[this.xPos + 1][this.yPos+1];
			int i = this.xPos;
			int j = this.yPos;
			this.xPos = this.xPos + 1;
			this.yPos = this.yPos+1;
			board[this.xPos][this.yPos] = this;
			board[i][j] = null;
			if (this.updateStatus(board)) {
				this.validMoves[this.xPos][this.yPos] = 1;
				this.hasValidMove = true;
			}
			board[i][j] = this;
			this.xPos = i;
			this.yPos = j;
			board[this.xPos + 1][this.yPos+1] = temp;
		}
		if (this.yPos+(1)<8 && this.yPos+1>=0 && (this.xPos-1)<8 && (this.xPos-1)>=0 && (board[this.xPos-1][this.yPos+1]==null 
				|| board[this.xPos-1][this.yPos+1].color!=this.color)) {
			Piece temp = board[this.xPos - 1][this.yPos+1];
			int i = this.xPos;
			int j = this.yPos;
			this.xPos = this.xPos - 1;
			this.yPos = this.yPos+1;
			board[this.xPos][this.yPos] = this;
			board[i][j] = null;
			if (this.updateStatus(board)) {
				this.validMoves[this.xPos][this.yPos] = 1;
				this.hasValidMove = true;
			}
			board[i][j] = this;
			this.xPos = i;
			this.yPos = j;
			board[this.xPos - 1][this.yPos+1] = temp;
		}
		if (this.yPos-(1)<8 && this.yPos-1>=0 && (this.xPos+1)<8 && (this.xPos+1)>=0 && (board[this.xPos+1][this.yPos-1]==null 
				|| board[this.xPos+1][this.yPos-1].color!=this.color)) {
			Piece temp = board[this.xPos + 1][this.yPos-1];
			int i = this.xPos;
			int j = this.yPos;
			this.xPos = this.xPos + 1;
			this.yPos = this.yPos-1;
			board[this.xPos][this.yPos] = this;
			board[i][j] = null;
			if (this.updateStatus(board)) {
				this.validMoves[this.xPos][this.yPos] = 1;
				this.hasValidMove = true;
			}
			board[i][j] = this;
			this.xPos = i;
			this.yPos = j;
			board[this.xPos + 1][this.yPos-1] = temp;
		}
		if ((this.yPos-1)<8 && (this.yPos-1)>=0 && (this.xPos-1)<8 && (this.xPos-1)>=0 && (board[this.xPos-1][this.yPos-1]==null 
				|| board[this.xPos-1][this.yPos-1].color!=this.color)) {
			Piece temp = board[this.xPos - 1][this.yPos-1];
			int i = this.xPos;
			int j = this.yPos;
			this.xPos = this.xPos - 1;
			this.yPos = this.yPos-1;
			board[this.xPos][this.yPos] = this;
			board[i][j] = null;
			if (this.updateStatus(board)) {
				this.validMoves[this.xPos][this.yPos] = 1;
				this.hasValidMove = true;
			}
			board[i][j] = this;
			this.xPos = i;
			this.yPos = j;
			board[this.xPos - 1][this.yPos-1] = temp;
		}
		this.isInCheck = tempCheck;
		if (!this.isInCheck && !this.hasMoved) {
			if ( board[this.xPos+3][this.yPos]!=null && board[this.xPos+3][this.yPos].type=='r' &&
					!(board[this.xPos+3][this.yPos].hasMoved) ) {
				boolean clear = true;
				if (!(isSafeForSliding(this.xPos+1,this.yPos,board)) || !(isSafeForSliding(this.xPos+2,this.yPos,board))) {
					clear =false;
				}
				for (int i=1;i<3;i++) {
					if ( board[this.xPos+i][this.yPos]!=null ) {
						clear = false;
					}
				}
				if (clear) {
					this.validMoves[this.xPos+2][this.yPos] = 2;
				}
			}
			if ( board[this.xPos-4][this.yPos]!=null && board[this.xPos-4][this.yPos].type=='r' &&
					!(board[this.xPos-4][this.yPos].hasMoved) ) {
				boolean clear = true;
				if (!(isSafeForSliding(this.xPos-1,this.yPos,board)) || !(isSafeForSliding(this.xPos-2,this.yPos,board))) {
					clear =false;
				}
				for (int i=1;i<4;i++) {
					if ( board[this.xPos-i][this.yPos]!=null ) {
						clear = false;
					}
				}
				if (clear) {
					this.validMoves[this.xPos-2][this.yPos] = 2;
				}
			}
		} 
	}	
}
