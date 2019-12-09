package com.example.android17.model;

/**
 * This class implements the logic that governs how a Pawn
 * operates in a game of chess.
 * @author Natalia Bryzhatenko nb631
 * @author Christopher Taglieri cat197
 */
public class Pawn extends Piece {
	/**
	 * Flag for if Pawn is susceptible to en Passant.
	 */
	boolean passant;
	
	/**
	 * Initializes a Pawn with given color and position.
	 * @param color Color
	 * @param x X Position
	 * @param y Y Position
	 */
	public Pawn(int color, int x, int y) {
		super(color,'p',x,y);
	}
	
	/**
	 * Implementation of super class abstract method. Creates
	 * a copy of the Pawn and returns the duplicate.
	 * @return A Copied Piece dynamically bound as Pawn.
	 */
	public Piece copy() {
		Pawn temp = new Pawn(this.color, this.xPos, this.yPos);
		temp.validMoves = this.validMoves;
		temp.hasValidMove = this.hasValidMove;
		temp.hasMoved = this.hasMoved;
		temp.passant = this.passant;
		temp.hollow = true;
		return temp;
	}
	
	/**
	 * Move method specific to how a Pawn operates when they move given their special move set.
	 * @param board The game board with all current available pieces located on it.
	 * @param x The x position on the board you wish to move to on your turn;
	 * @param y The y position on the board you wish to move to on your turn;
	 * @param color The color that is attempting to move, ensures strict order is adhered to in chess.
	 * @return True if move is valid and no parameters are wrong, False if something prevents the move from legally occurring
	 */
	public boolean move(Piece board[][], int x, int y, int color) {
<<<<<<< HEAD
=======
//		System.out.println("Moving a pawn");
>>>>>>> 711f652145201df145771dee21c26f754933c397
		if (this.color != color) {
//			System.out.println("On 50");
			return false;
		}
		if (x > 7 || x < 0 || y > 7 || y < 0) {
//			System.out.println("On 54");

			return false;
		}
		if (this.validMoves[x][y] == 0) {
//			System.out.println("On 59");

			return false;
		}
		if (this.color == -1) {
			Piece.wKingIsInDanger = new int[] {-1, -1, -1, -1};
		}
		else {
			Piece.bKingIsInDanger = new int[] {-1, -1, -1, -1};
		}
		if (Math.abs(y-this.yPos) > 1) {
			this.passant = true;
		}
		if (this.validMoves[x][y] == 2) {
			board[x][y-this.color] = null;
		}
<<<<<<< HEAD
=======
//		System.out.println("On 75");

>>>>>>> 711f652145201df145771dee21c26f754933c397
		int i = this.xPos;
		int j = this.yPos;
		this.xPos = x;
		this.yPos = y;
		board[x][y] = this;
		board[i][j] = null;
		this.hasMoved = true;
		return true;
	}

	public void moveBack(Piece board[][], int x, int y) {
		int i = this.xPos;
		int j = this.yPos;
		this.xPos = x;
		this.yPos = y;
		board[x][y] = this;
		board[i][j] = null;
	}
	
	/**
	 * The logic for how a Pawn generates the legal moves available to it. Starts
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
			if (this.hasMoved == false && board[this.xPos][this.yPos+1*this.color]==null 
					&& board[this.xPos][this.yPos+2*this.color] == null &&
					board[danger[0]][danger[1]].validMoves[this.xPos][this.yPos+(2*this.color)] == 2) {
				this.validMoves[this.xPos][this.yPos+(2*this.color)] = 1;
				this.hasValidMove = true;
			}

			if ((this.yPos+(1*this.color) < 8 && this.yPos+(1*this.color) >= 0) 
					&& board[this.xPos][this.yPos+1*this.color] == null && 
					board[danger[0]][danger[1]].validMoves[this.xPos][this.yPos+(2*this.color)] == 2) {
				this.validMoves[this.xPos][this.yPos+(1*this.color)] = 1;
				this.hasValidMove = true;
			}			
			if ((this.yPos+(1*this.color) < 8 && this.yPos+(1*this.color) >= 0) 
					&& this.xPos+1 < 8 && board[this.xPos+1][this.yPos+(1*this.color)] != null
					&& board[this.xPos+1][this.yPos+(1*this.color)].color != this.color &&
					danger[0] == this.xPos+1 && danger[1] == this.yPos+(1*this.color)) {
				if (board[this.xPos+1][this.yPos+(1*this.color)].type == 'k') {
					flag();
					((King)board[this.xPos+1][this.yPos+(1*this.color)]).isInCheck = true;
				}
				this.validMoves[this.xPos+1][this.yPos+(1*this.color)] = 1;
				this.hasValidMove = true;
			}
			if ((this.yPos+(1*this.color) < 8 && this.yPos+(1*this.color) >= 0) 
					&& this.xPos-1 >= 0 && board[this.xPos-1][this.yPos+(1*this.color)] != null
					&& board[this.xPos-1][this.yPos+(1*this.color)].color != this.color &&
					danger[0] == this.xPos-1 && danger[1] == this.yPos+(1*this.color)) {
				if (board[this.xPos-1][this.yPos+(1*this.color)].type == 'k') {
					flag();
					((King)board[this.xPos-1][this.yPos+(1*this.color)]).isInCheck = true;
				}
				this.validMoves[this.xPos-1][this.yPos+(1*this.color)] = 1;
				this.hasValidMove = true;
			}
			if (this.xPos+1 < 8 && board[this.xPos+1][this.yPos]!=null && board[this.xPos+1][this.yPos].type=='p'
					&& ((Pawn)board[this.xPos+1][this.yPos]).passant == true && board[this.xPos+1][this.yPos+(1*this.color)] == null &&
					board[danger[0]][danger[1]].validMoves[this.xPos+1][this.yPos+(1*this.color)] == 2) {
				this.validMoves[this.xPos+1][this.yPos+(1*this.color)] = 2;
				this.hasValidMove = true;
			}
			if (this.xPos-1 >= 0 && board[this.xPos-1][this.yPos]!=null && board[this.xPos-1][this.yPos].type=='p'
					&& ((Pawn)board[this.xPos-1][this.yPos]).passant == true && board[this.xPos-1][this.yPos+(1*this.color)] == null &&
					board[danger[0]][danger[1]].validMoves[this.xPos-1][this.yPos+(1*this.color)] == 2) {
				this.validMoves[this.xPos-1][this.yPos+(1*this.color)] = 2;
				this.hasValidMove = true;
			}
			return;
		}
		if (this.kingShield != null) {
			if (this.xPos+1 == this.kingShield[0] && this.yPos-(1*this.color) == this.kingShield[1]) {
				this.validMoves[this.xPos-1][this.yPos+(1*this.color)] = 1;
				this.hasValidMove = true;
				return;
			}
			if (this.xPos-1 == this.kingShield[0] && this.yPos-(1*this.color) == this.kingShield[1]) {
				this.validMoves[this.xPos+1][this.yPos+(1*this.color)] = 1;
				this.hasValidMove = true;
				return;
			}
			if (this.xPos != this.kingShield[0]) {
				return;
			}
			if (hasMoved == false && board[this.xPos][this.yPos+1*this.color]==null 
					&& board[this.xPos][this.yPos+2*this.color] == null) {
				this.validMoves[this.xPos][this.yPos+(2*this.color)] = 1;
				this.hasValidMove = true;
			}
			if ((this.yPos+(1*this.color) < 8 && this.yPos+(1*this.color) >= 0) 
					&& board[this.xPos][this.yPos+1*this.color] == null) {
				this.validMoves[this.xPos][this.yPos+(1*this.color)] = 1;
				this.hasValidMove = true;
			}
			return;
		}
		if (hasMoved == false && board[this.xPos][this.yPos+1*this.color]==null 
				&& board[this.xPos][this.yPos+2*this.color] == null) {
			this.validMoves[this.xPos][this.yPos+(2*this.color)] = 1;
			this.hasValidMove = true;
		}
		if ((this.yPos+(1*this.color) < 8 && this.yPos+(1*this.color) >= 0) 
				&& board[this.xPos][this.yPos+1*this.color] == null) {
			this.validMoves[this.xPos][this.yPos+(1*this.color)] = 1;
			this.hasValidMove = true;
		}		
		if ((this.yPos+(1*this.color) < 8 && this.yPos+(1*this.color) >= 0) 
				&& this.xPos+1 < 8 && board[this.xPos+1][this.yPos+(1*this.color)] != null
				&& board[this.xPos+1][this.yPos+(1*this.color)].color != this.color) {
			if (board[this.xPos+1][this.yPos+(1*this.color)].type == 'k') {
				flag();
				((King)board[this.xPos+1][this.yPos+(1*this.color)]).isInCheck = true;
			}
			this.validMoves[this.xPos+1][this.yPos+(1*this.color)] = 1;
			this.hasValidMove = true;
		}
		if ((this.yPos+(1*this.color) < 8 && this.yPos+(1*this.color) >= 0) 
				&& this.xPos-1 >= 0 && board[this.xPos-1][this.yPos+(1*this.color)] != null
				&& board[this.xPos-1][this.yPos+(1*this.color)].color != this.color) {
			if (board[this.xPos-1][this.yPos+(1*this.color)].type == 'k') {
				flag();
				((King)board[this.xPos-1][this.yPos+(1*this.color)]).isInCheck = true;
			}
			this.validMoves[this.xPos-1][this.yPos+(1*this.color)] = 1;
			this.hasValidMove = true;
		}
		if (this.xPos+1 < 8 && board[this.xPos+1][this.yPos]!=null && board[this.xPos+1][this.yPos].type=='p'
				&& ((Pawn)board[this.xPos+1][this.yPos]).passant == true && board[this.xPos+1][this.yPos+(1*this.color)] == null) {
			this.validMoves[this.xPos+1][this.yPos+(1*this.color)] = 2;
			this.hasValidMove = true;
		}
		if (this.xPos-1 >= 0 && board[this.xPos-1][this.yPos]!=null && board[this.xPos-1][this.yPos].type=='p'
				&& ((Pawn)board[this.xPos-1][this.yPos]).passant == true && board[this.xPos-1][this.yPos+(1*this.color)] == null) {
			this.validMoves[this.xPos-1][this.yPos+(1*this.color)] = 2;
			this.hasValidMove = true;
		}
	}	
}