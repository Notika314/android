package com.example.android17.model;

/** 
 * This class has some general Game rules along with game states that
 * are vital to the functioning of Chess.
 * @author Natalia Bryzhatenko nb631
 * @author Christopher Taglieri cat197
 */
public class Game {
	/**
	 * Defines the game board, made up of all Pieces on the board at
	 * any time in an 8 by 8 array.
	 */
	public Piece[][] board;
	
	/**
	 * Defines which player has the current turn. Is set
	 * to -1 for white and 1 for black.
	 */
	public int currMove;
	
	/**
	 * Constructs the game board and fills in all Pieces that would
	 * be at the start of your typical Chess game.
	 */
	public Game() {
		currMove = -1;
		board = new Piece[8][8];
		board[0][0] = new Rook(1,0,0);
		board[1][0] = new Knight(1,1,0);
		board[2][0] = new Bishop(1,2,0);
		board[3][0] = new Queen(1,3,0);
		board[4][0] = new King(1,4,0);
		board[5][0] = new Bishop(1,5,0);
		board[6][0] = new Knight(1,6,0);
		board[7][0] = new Rook(1,7,0);
		board[0][7] = new Rook(-1,0,7);
		board[1][7] = new Knight(-1,1,7);
		board[2][7] = new Bishop(-1,2,7);
		board[3][7] = new Queen(-1,3,7);
		board[4][7] = new King(-1,4,7);
		board[5][7] = new Bishop(-1,5,7);
		board[6][7] = new Knight(-1,6,7);
		board[7][7] = new Rook(-1,7,7);
		for (int i=0;i<8;i++) {
			board[i][1] = new Pawn(1,i,1);
			board[i][6] = new Pawn(-1,i,6);
		}
		return;
	}

	public Piece[][] getBoard() {
		return this.board;
	}

	/**
	 * Splits up input from the user into tokens so that their instructions
	 * can be parsed into what move they wish to take.
	 * @param input The string that is received from the Scanner.
	 * @return Each part of the string split by its white space.
	 */
	public String[] tokenizeInput(String input) {
		String[] tokens = input.split("\\s+");
		return tokens;
	}
	
	/**
	 * One of the methods used to determine a stalemate.
	 * @return True if no Piece on the board has a valid move, or False if at least one does.
	 */
	public boolean hasNoValidMoves() {
		for (int i=0;i<8;i++) {
			for (int j=0;j<8;j++) {
				if (this.board[i][j]!=null && this.board[i][j].color==this.currMove) {
					if (this.board[i][j].hasValidMove) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Method used to check if there is a unit on your side that
	 * can defeat a Piece that has your King in check.
	 * @return True if there is some unit, false if otherwise.
	 */
	public boolean protector() {
		int[] danger = null;
		if (this.currMove == -1) {
			danger = Piece.wKingIsInDanger;
		}
		else {
			danger = Piece.bKingIsInDanger;
		}
		if (danger[0] == -1) {
			return true;
		}
		for (int i=0;i<8;i++) {
			for (int j=0;j<8;j++) {
				if (this.board[i][j]!=null && this.board[i][j].color==this.currMove) {
					Piece piece = this.board[i][j];
					if (piece.kingShield != null) {
						continue;
					}
					if (piece.validMoves[danger[0]][danger[1]]>0) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Method used to check if there is a unit on your side that
	 * can block a Piece that has your King in check.
	 * @return True if there is some unit, false if otherwise.
	 */
	public boolean blocker() {
		int[] danger = null;
		if (this.currMove == -1) {
			danger = Piece.wKingIsInDanger;
		}
		else {
			danger = Piece.bKingIsInDanger;
		}
		if (danger[0] == -1) {
			return true;
		}
		for (int i=0;i<8;i++) {
			for (int j=0;j<8;j++) {
				if (this.board[i][j]!=null && this.board[i][j].color==this.currMove) {
					Piece piece = this.board[i][j];
					for (int k = 0; k < 8; k++) {
						for (int l = 0; l < 8; l++) {
							if (piece.validMoves[k][l] > 0 && this.board[danger[0]][danger[1]].validMoves[k][l] == 2) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Iterates through the board and generates valid moves according to a color.
	 * @param color Color that we wish to update.
	 */
	public void updateValidMoves(int color) {
		int i,j;
		for (i=0;i<8;i++) {
			for (j=0;j<8;j++) {
				if (board[j][i]!=null && board[j][i].color == color && board[j][i].type != 'K') {
					board[j][i].generateValidMoves(board);
				}
			}
		}
		return;
	}
	
	/**
	 * Clear the passant flag from units that have no longer need of it.
	 * @param color Color that we wish to update.
	 */
	public void clearPassant(int color) {
		int i,j;
		for (i=0;i<8;i++) {
			for (j=0;j<8;j++) {
				if (board[j][i]!=null && board[j][i].color == color && board[j][i].type == 'p') {
					((Pawn)board[j][i]).passant = false;
				}
			}
		}
		return;
	}
	
	/**
	 * Removes the shield from each Piece so that, if needed, it is 
	 * generated in a proper way on next turn.
	 */
	public void disarmShields() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[j][i] != null) {
					board[j][i].kingShield = null;
				}
			}
		}
	}
	
	/**
	 * Finds and returns the Piece on the Board given a certain
	 * input from the user.
	 * @param token The instruction that the user input.
	 * @return The Piece that is occupying that slot (or null).
	 */
	public Piece findPiece(String token) {
		Piece piece; 
		int i = (int)(token.charAt(0)-97);
		int j = (int)(8-(token.charAt(1)-48));
		if (i>7 || j>7) {
			return null;
		}
		piece = board[i][j];
		return piece;
	}
	
	/**
	 * Prints the game Board as needed to understand how Chess operates.
	 */
	public void printBoard() {
		System.out.println();
		System.out.println();
		for (int i=0;i<8;i++) {
			for (int j=0;j<8;j++) {
				if (board[j][i]==null) {
					if (i%2==j%2) System.out.print("   ");
					else System.out.print("## ");
				} else {
					System.out.print(board[j][i]+" "); 
				}
			}
			System.out.println(8-i);
		}
		System.out.println(" a  b  c  d  e  f  g  h  ");
		System.out.println();
	}
}
