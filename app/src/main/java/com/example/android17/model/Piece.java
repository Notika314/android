package com.example.android17.model;

/**
 * This abstract class is the parent class to all the other
 * pieces. It allows us to execute some common functionality. It
 * has two abstract methods, copy and generateValidMoves, along
 * with the move, danger, flag, shield, and toString methods.
 * 
 * @author Natalia Bryzhatenko nb631
 * @author Christopher Taglieri cat197
 */
public abstract class Piece {
	/**
	 * Defines color of the Piece.
	 */
	public int color;
	
	/**
	 * Defines the x Position of the Piece.
	 */
	public int xPos;
	
	/**
	 * Defines the y Position of the Piece.
	 */
	public int yPos;
	
	/**
	 * Defines the type of Piece that it is 
	 * initialized to.
	 */
	public char type;
	
	/**
	 * Defines whether the Piece has a valid and legal
	 * move that it can make.
	 */
	boolean hasValidMove;
	
	/**
	 * Defines whether the Piece has moved this game.
	 */
	boolean hasMoved;
	
	/**
	 * Defines an array that each Piece has that corresponds to
	 * all the valid moves it can make on the board. Some pieces
	 * designate greater values than 1 to denote special moves.
	 */
	public int validMoves[][];
	
	/**
	 * Defines whether the piece is protecting its King from check.
	 */
	int kingShield[];

	/**
	 * Defines whether the piece is a copy.
	 */
	boolean hollow;
	
	/** 
	 * Defines and statically tracks the location of the Pieces that
	 * have the White King in check.
	 */
	static int wKingIsInDanger[] = {-1, -1, -1, -1};
	
	/**
	 * Defines and statically tracks the location of the Pieces that
	 * have the Black King in check.
	 */
	static int bKingIsInDanger[] = {-1, -1, -1, -1};
	
	/**
	 * Initializes an instance of the Piece, though since Piece
	 * is an abstract class we never directly have any instances of Piece.
	 * @param color Color
	 * @param type Type
	 * @param xPos X Position
	 * @param yPos Y Position
	 */
	public Piece(int color, char type, int xPos, int yPos ){
		this.color = color;
		this.type = type;
		this.xPos = xPos;
		this.yPos = yPos;
		this.hasMoved = false;
		this.hollow = false;
		this.validMoves = new int[8][8];
	}

	/**
	 * Abstract method that is implemented in subclasses. Allows 
	 * us to duplicate a Piece without altering the original.
	 * @return A Copied Piece.
	 */
	public abstract Piece copy();
	
	
	/**
	 * Abstract method that is implemented in the subclasses. Allows
	 * generate the valid moves of each piece on the board. Each sub Piece
	 * has its own logic that governs this process.
	 * @param board The game board with all current available pieces located on it.
	 */
	public abstract void generateValidMoves(Piece[][] board);


	/**
	 * A general move method that handles most pieces just fine. Some
	 * subclasses will override for their special functionality.
	 * @param board The game board with all current available pieces located on it.
	 * @param x The x position on the board you wish to move to on your turn;
	 * @param y The y position on the board you wish to move to on your turn;
	 * @param color The color that is attempting to move, ensures strict order is adhered to in chess.
	 * @return True if move is valid and no parameters are wrong, False if something prevents the move from legally occurring.
	 */
	public boolean move(Piece board[][], int x, int y, int color) {
//		System.out.println("Moving a piece");
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
		this.hasMoved = true;
		int i = this.xPos;
		int j = this.yPos;
		this.xPos = x;
		this.yPos = y;
		board[x][y] = this;
		board[i][j] = null;

		return true;
	}
	
	/**
	 * Makes a copy of our static variable dependent on the color seeking it.
	 * @param color The color of the piece requesting a copy.
	 * @return An array that is made up of the location of potential dangers to the king.
	 */
	public int[] danger(int color) {
		if (color == -1) {
			return Piece.wKingIsInDanger;
		}
		else {
			return Piece.bKingIsInDanger;
		}
	}
	
	/**
	 * Flag method that populates the potential dangers to the King if called
	 * from an opponent piece having a valid move on the King.
	 */
	public void flag() {
		if (this.color == 1) {
			if (Piece.wKingIsInDanger[0] == -1) {
				Piece.wKingIsInDanger[0] = this.xPos;
				Piece.wKingIsInDanger[1] = this.yPos;
			}
			else {
				Piece.wKingIsInDanger[2] = this.xPos;
				Piece.wKingIsInDanger[3] = this.yPos;
			}
		}
		else {
			if (Piece.bKingIsInDanger[0] == -1) {
				Piece.bKingIsInDanger[0] = this.xPos;
				Piece.bKingIsInDanger[1] = this.yPos;
			}
			else {
				Piece.bKingIsInDanger[2] = this.xPos;
				Piece.bKingIsInDanger[3] = this.yPos;
			}
		}
	}
	
	/**
	 * Designates the location of the attacker that the piece is shielding the King from.
	 * @param board The game board with all current available pieces located on it.
	 * @param x The initial x position of the shielding Piece.
	 * @param y The initial y position of the shielding Piece.
	 * @param deltaX How much that piece changes in the x direction on its path.
	 * @param deltaY How much that piece changes in the y direction on its path.
	 */
	public void shield(Piece[][] board, int x, int y, int deltaX, int deltaY) {
		int i = x, j = y;
		i += deltaX;
		j += deltaY;
		while (i < 8 && i >= 0 && j < 8 && j >= 0) {
			if (board[i][j] != null) {
				if (board[i][j].color == board[x][y].color && board[i][j].type == 'k') {
					board[x][y].kingShield = new int[] {i,j};
				}
				else {
					return;
				}
			}
			i += deltaX;
			j += deltaY;
		}
	}
	
	/**
	 * Simple toString implementation so that Piece appears on board.
	 */
	public String toString() {
		if (color == 1) { 
			return "b" + this.type;
		}
		return "w" + this.type;
	}	
}
