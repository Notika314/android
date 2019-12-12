package com.example.android17.model;

/**
 * This class implements the logic that governs how a Knight
 * operates in a game of chess.
 * @author Natalia Bryzhatenko nb631
 * @author Christopher Taglieri cat197
 */
public class Knight extends Piece {
	/**
	 * Initializes a Knight with given color and position.
	 * @param color Color
	 * @param x X Position
	 * @param y Y Position
	 */
	public Knight(int color, int x, int y) {
		super(color,'n',x,y);
	}
	
	/**
	 * Implementation of super class abstract method. Creates
	 * a copy of the Knight and returns the duplicate.
	 * @return A Copied Piece dynamically bound as Knight.
	 */
	public Piece copy() {
		Knight temp = new Knight(this.color, this.xPos, this.yPos);
		temp.validMoves = this.validMoves;
		temp.hasValidMove = this.hasValidMove;
		temp.hollow = true;
		return temp;
	}
	public Piece createCopy() {
		Knight temp = new Knight(this.color, this.xPos, this.yPos);
		temp.validMoves = this.validMoves;
		temp.hasValidMove = this.hasValidMove;
		temp.hollow = false;
		return temp;
	}
	/**
	 * The logic for how a Knight generates the legal moves available to it. Starts
	 * with flags set to false and a clear board that is only populated and set to 
	 * true if there is some available move allowed. All moves generated are legal
	 * and do not require further pruning.
	 * @param board The game board with all current available pieces located on it.
	 */
	public void generateValidMoves(Piece board[][]) {
		this.hasValidMove = false;
		this.validMoves = new int[8][8];
		int i = this.xPos, j = this.yPos;
		int[] danger = danger(this.color);
		if (danger[0] != -1) {
			if (danger[2] != -1 || this.kingShield != null) {
				return;
			}
			if ((i+2 < 8 && i+2 >= 0 && j+1 < 8 && j+1 >= 0) && 
					(board[i+2][j+1] == null || board[i+2][j+1].color != this.color) &&
					((i+2 == danger[0] && j+1 == danger[1] || board[danger[0]][danger[1]].validMoves[i+2][j+1] == 2))) {
				this.hasValidMove = true;
				this.validMoves[i+2][j+1] = 1;
			}
			if ((i+1 < 8 && i+1 >= 0 && j+2 < 8 && j+2 >= 0) && 
					(board[i+1][j+2] == null || board[i+1][j+2].color != this.color) &&
					((i+1 == danger[0] && j+2 == danger[1] || board[danger[0]][danger[1]].validMoves[i+1][j+2] == 2))) {
				this.hasValidMove = true;
				this.validMoves[i+1][j+2] = 1;
			}
			if ((i-1 < 8 && i-1 >= 0 && j+2 < 8 && j+2 >= 0) && 
					(board[i-1][j+2] == null || board[i-1][j+2].color != this.color) &&
					((i-1 == danger[0] && j+2 == danger[1] || board[danger[0]][danger[1]].validMoves[i-1][j+2] == 2))) {
				this.hasValidMove = true;
				this.validMoves[i-1][j+2] = 1;
			}
			if ((i-2 < 8 && i-2 >= 0 && j+1 < 8 && j+1 >= 0) && 
					(board[i-2][j+1] == null || board[i-2][j+1].color != this.color) &&
					((i-2 == danger[0] && j+1 == danger[1] || board[danger[0]][danger[1]].validMoves[i-2][j+1] == 2))) {
				this.hasValidMove = true;
				this.validMoves[i-2][j+1] = 1;
			}
			
			if ((i-2 < 8 && i-2 >= 0 && j-1 < 8 && j-1 >= 0) && 
					(board[i-2][j-1] == null || board[i-2][j-1].color != this.color) &&
					((i-2 == danger[0] && j-1 == danger[1] || board[danger[0]][danger[1]].validMoves[i-2][j-1] == 2))) {
				this.hasValidMove = true;
				this.validMoves[i-2][j-1] = 1;
			}
			if ((i-1 < 8 && i-1 >= 0 && j-2 < 8 && j-2 >= 0) && 
					(board[i-1][j-2] == null || board[i-1][j-2].color != this.color) &&
					((i-1 == danger[0] && j-2 == danger[1] || board[danger[0]][danger[1]].validMoves[i-1][j-2] == 2))) {
				this.hasValidMove = true;
				this.validMoves[i-1][j-2] = 1;
			}
			if ((i+1 < 8 && i+1 >= 0 && j-2 < 8 && j-2 >= 0) && 
					(board[i+1][j-2] == null || board[i+1][j-2].color != this.color) &&
					((i+1 == danger[0] && j-2 == danger[1] || board[danger[0]][danger[1]].validMoves[i+1][j-2] == 2))) {
				this.hasValidMove = true;
				this.validMoves[i+1][j-2] = 1;
			}
			if ((i+2 < 8 && i+2 >= 0 && j-1 < 8 && j-1 >= 0) && 
					(board[i+2][j-1] == null || board[i+2][j-1].color != this.color) &&
					((i+2 == danger[0] && j-1 == danger[1] || board[danger[0]][danger[1]].validMoves[i+2][j-1] == 2))) {
				this.hasValidMove = true;
				this.validMoves[i+2][j-1] = 1;
			}
			return;
		}
		if (this.kingShield != null) {
			return;
		}
		if ((i+2 < 8 && i+2 >= 0 && j+1 < 8 && j+1 >= 0) && 
				(board[i+2][j+1] == null || board[i+2][j+1].color != this.color)) {
			if (board[i+2][j+1] != null && board[i+2][j+1].type == 'k') {
				flag();
				((King)board[i+2][j+1]).isInCheck = true;
			}
			this.hasValidMove = true;
			this.validMoves[i+2][j+1] = 1;
		}
		if ((i+1 < 8 && i+1 >= 0 && j+2 < 8 && j+2 >= 0) && 
				(board[i+1][j+2] == null || board[i+1][j+2].color != this.color)) {
			if (board[i+1][j+2] != null && board[i+1][j+2].type == 'k') {
				flag();
				((King)board[i+1][j+2]).isInCheck = true;
			}
			this.hasValidMove = true;
			this.validMoves[i+1][j+2] = 1;
		}
		if ((i-1 < 8 && i-1 >= 0 && j+2 < 8 && j+2 >= 0) && 
				(board[i-1][j+2] == null || board[i-1][j+2].color != this.color)) {
			if (board[i-1][j+2] != null && board[i-1][j+2].type == 'k') {
				flag();
				((King)board[i-1][j+2]).isInCheck = true;
			}
			this.hasValidMove = true;
			this.validMoves[i-1][j+2] = 1;
		}
		if ((i-2 < 8 && i-2 >= 0 && j+1 < 8 && j+1 >= 0) && 
				(board[i-2][j+1] == null || board[i-2][j+1].color != this.color)) {
			if (board[i-2][j+1] != null && board[i-2][j+1].type == 'k') {
				flag();
				((King)board[i-2][j+1]).isInCheck = true;
			}
			this.hasValidMove = true;
			this.validMoves[i-2][j+1] = 1;
		}
		if ((i-2 < 8 && i-2 >= 0 && j-1 < 8 && j-1 >= 0) && 
				(board[i-2][j-1] == null || board[i-2][j-1].color != this.color)) {
			if (board[i-2][j-1] != null && board[i-2][j-1].type == 'k') {
				flag();
				((King)board[i-2][j-1]).isInCheck = true;
			}
			this.hasValidMove = true;
			this.validMoves[i-2][j-1] = 1;
		}
		if ((i-1 < 8 && i-1 >= 0 && j-2 < 8 && j-2 >= 0) && 
				(board[i-1][j-2] == null || board[i-1][j-2].color != this.color)) {
			if (board[i-1][j-2] != null && board[i-1][j-2].type == 'k') {
				flag();
				((King)board[i-1][j-2]).isInCheck = true;
			}
			this.hasValidMove = true;
			this.validMoves[i-1][j-2] = 1;
		}
		if ((i+1 < 8 && i+1 >= 0 && j-2 < 8 && j-2 >= 0) && 
				(board[i+1][j-2] == null || board[i+1][j-2].color != this.color)) {
			if (board[i+1][j-2] != null && board[i+1][j-2].type == 'k') {
				flag();
				((King)board[i+1][j-2]).isInCheck = true;
			}
			this.hasValidMove = true;
			this.validMoves[i+1][j-2] = 1;
		}
		if ((i+2 < 8 && i+2 >= 0 && j-1 < 8 && j-1 >= 0) && 
				(board[i+2][j-1] == null || board[i+2][j-1].color != this.color)) {
			if (board[i+2][j-1] != null && board[i+2][j-1].type == 'k') {
				flag();
				((King)board[i+2][j-1]).isInCheck = true;
			}
			this.hasValidMove = true;
			this.validMoves[i+2][j-1] = 1;
		}
	}
}
