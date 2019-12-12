package com.example.android17.model;

/**
 * This class implements the logic that governs how a Rook
 * operates in a game of chess.
 * @author Natalia Bryzhatenko nb631
 * @author Christopher Taglieri cat197
 */
public class Rook extends Piece {
	/**
	 * Initializes a Rook with given color and position.
	 * @param color Color
	 * @param x X Position
	 * @param y Y Position
	 */
	public Rook(int color, int x, int y) {
		super(color,'r',x,y);
	}
	
	/**
	 * Implementation of super class abstract method. Creates
	 * a copy of the Rook and returns the duplicate.
	 * @return A Copied Piece dynamically bound as Rook.
	 */
	public Piece copy() {
		Rook temp = new Rook(this.color, this.xPos, this.yPos);
		temp.validMoves = this.validMoves;
		temp.hasValidMove = this.hasValidMove;
		temp.hasMoved = this.hasMoved;
		temp.hollow = true;
		return temp;
	}

	public Piece createCopy() {
		Rook temp = new Rook(this.color, this.xPos, this.yPos);
		temp.validMoves = this.validMoves;
		temp.hasValidMove = this.hasValidMove;
		temp.hasMoved = this.hasMoved;
		temp.hollow = false;
		return temp;
	}
	/**
	 * Generates valid moves in a line of attack as defined by generateValidMoves.
	 * Also checks to ensure the piece is not shielding the King from check.
	 * @param board The game board with all current available pieces located on it.
	 * @param deltaX How much that piece changes in the x direction on its path.
	 * @param deltaY How much that piece changes in the y direction on its path.
	 */
	public void path(Piece[][] board, int deltaX, int deltaY) {
		int i = this.xPos + deltaX;
		int j = this.yPos + deltaY;
		while (i < 8 && i >= 0 && j < 8 && j >= 0) {
			if (board[i][j] != null) {
				if (board[i][j].color != this.color) {
					if (!this.hollow) {
						shield(board, i, j, deltaX, deltaY);
					}
					this.validMoves[i][j] += 1;
					this.hasValidMove = true;
					if (board[i][j].type == 'k' && this.validMoves[i][j] == 1) {
						flag();
						((King)board[i][j]).isInCheck = true;
						path(board, deltaX, deltaY);
					}
				}	
				return;
			}
			this.validMoves[i][j] += 1;
			this.hasValidMove = true;
			i += deltaX;
			j += deltaY;
		}
	}
	
	/**
	 * Modified version of path when under the added constraint that the King of 
	 * this current color is under check. Only generates the parts of the path that would
	 * ensure the survival of the King.
	 * @param board The game board with all current available pieces located on it.
	 * @param deltaX How much that piece changes in the x direction on its path.
	 * @param deltaY How much that piece changes in the y direction on its path.
	 */
	public void pathUnderCheck(Piece[][] board, int deltaX, int deltaY) {
		int i = this.xPos + deltaX;
		int j = this.yPos + deltaY;
		while (i < 8 && i >= 0 && j < 8 && j >= 0) {
			if (board[i][j] == null ) {
				if (this.color == -1) {
					if (board[Piece.wKingIsInDanger[0]][Piece.wKingIsInDanger[1]].validMoves[i][j] == 2) {
						this.validMoves[i][j] = 1;
						this.hasValidMove = true;
					}
				}
				else {
					if (board[Piece.bKingIsInDanger[0]][Piece.bKingIsInDanger[1]].validMoves[i][j] == 2) {
						this.validMoves[i][j] = 1;
						this.hasValidMove = true;
					}
				}
			}
			else {
				if (this.color == -1) {
					if (i == Piece.wKingIsInDanger[0] && j == Piece.wKingIsInDanger[1]) {
						this.validMoves[i][j] = 1;
						this.hasValidMove = true;
					}
				}
				else {
					if (i == Piece.bKingIsInDanger[0] && j == Piece.bKingIsInDanger[1]) {
						this.validMoves[i][j] = 1;
						this.hasValidMove = true;
					}
				}
				return;
			}
			i += deltaX;
			j += deltaY;
		}
	}
	
	/**
	 * The logic for how a Rook generates the legal moves available to it. Starts
	 * with flags set to false and a clear board that is only populated and set to 
	 * true if there is some available move allowed. All moves generated are legal
	 * and do not require further pruning.
	 * @param board The game board with all current available pieces located on it.
	 */
	public void generateValidMoves(Piece board[][]) {
		this.hasValidMove = false;
		this.validMoves = new int[8][8];
		int[] danger = danger(this.color);
		if (danger[0] != -1) {
			if (danger[2] != -1 || this.kingShield != null) {
				return;
			}
			pathUnderCheck(board,1,0);
			pathUnderCheck(board,-1,0);
			pathUnderCheck(board,0,1);
			pathUnderCheck(board,0,-1);
			return;
		}
		if (this.kingShield != null) {
			int deltaX = this.xPos - this.kingShield[0];
			int deltaY = this.yPos - this.kingShield[1];
			if (deltaY == 0) {
				path(board,1,0);
				path(board,-1,0);
				return;
			}
			if (deltaX == 0) {
				path(board,0,1);
				path(board,0,-1);
				return;
			}
			return;
		}
		path(board,1,0);
		path(board,-1,0);
		path(board,0,1);
		path(board,0,-1);
	}	
}
