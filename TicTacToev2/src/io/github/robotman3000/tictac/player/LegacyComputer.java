package io.github.robotman3000.tictac.player;

import io.github.robotman3000.tictac.BoardLocation;
import io.github.robotman3000.tictac.GameBoard;
import io.github.robotman3000.tictac.GameBoard.CellState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LegacyComputer extends Player {

	Random rand = new Random();
	
	public LegacyComputer(String name, CellState peice) {
		super(name, peice);
	}

	@Override
	public BoardLocation doMove(GameBoard theBoard) {
		BoardLocation move = null;
		BoardLocation var = new BoardLocation(0, 0);
		//int oppPlayer = 0;
		//int me = 0;
		BoardLocation[] moves;

		// me is 1 for EVEN, 2 for ODD..
		//me = (currentTurn % 2) + 1;
		// Then set oppPlayer to the "opposite"..
		//oppPlayer = 3 - me;

			if ((var = aboutToWin(getPiece(), theBoard)) != null) {// check for a winning move for me
				return var;
			} else if ((var = aboutToWin(getOpponent(getPiece()), theBoard)) != null) {
				// check for a winning move for opp
				return var;
			} else {// else use my strategy to pick move
				move = new BoardLocation(rand.nextInt(theBoard.getWidth()), rand.nextInt(theBoard.getHeight())); // pick random move in case of error
				switch ((9 - getOpenMoves(theBoard).size())) {
				case (0):
					// Player 1
					moves = getAvalMoves(true, true, false, theBoard);
					var = pickOne(moves);
					move = var;
					break;
				case (1):
					// Player 2
					moves = getAvalMoves(false, true, false, theBoard);
					if (moves.length == 0) {
						moves = getAvalMoves(true, false, false, theBoard);
						var = pickOne(moves);
						move = var;
					} else {
						move = new BoardLocation(1, 1);
					}
					break;
				case (2):
					// Player 1
					moves = getAvalMoves(true, true, false, theBoard);
					var = pickOne(moves);
					move = var;
					break;
				case (3):
					// Player 2
					if((move = finddoublecross(toOldGameboard(theBoard), getOpponent(getPiece()))) != null){
						break;
					}
					if (theBoard.getCellState(1, 1) == getPiece()) {
						moves = getAvalMoves(false, false, true, theBoard);
						var = pickOne(moves);
						move = var;
					} else {
						moves = getAvalMoves(true, false, false, theBoard);
						var = pickOne(moves);
						move = var;
					}
					break;
				case (4):
					// Player 1
					moves = getAvalMoves(true, true, false, theBoard);
					var = pickOne(moves);
					move = var;
					break;
				case (5):
					// Player 2
					moves = getAvalMoves(true, true, true, theBoard);
					var = pickOne(moves);
					move = var;
					break;
				case (6):
					// Player 1
					moves = getAvalMoves(true, true, true, theBoard);
					var = pickOne(moves);
					move = var;
					break;
				case (7):
					// Player 2
					moves = getAvalMoves(true, true, true, theBoard);
					var = pickOne(moves);
					move = var;
					break;
				case (8):
					// Player 1
					moves = getAvalMoves(true, true, true, theBoard);
					var = pickOne(moves);
					move = var;
					break;
				}
			}

		return move;
	}

	private int[] toOldGameboard(GameBoard board){
		// Convert the board to be single dimensional
		int[] convBoard = new int[9];
		int index = 0;
		for(int x = 0; x < board.getWidth(); x++){
			for(int y = 0; y < board.getHeight(); y++){
				convBoard[index++] = board.getCellState(x, y).ordinal();
			}
		}
		return convBoard;
	}
	
	private BoardLocation finddoublecross(int[] gameBoard, GameBoard.CellState oppPlayer) {
		if(compareThree(gameBoard[3], gameBoard[1], oppPlayer.ordinal()) == oppPlayer.ordinal()){
			return new BoardLocation(0, 0);
		}
		if(compareThree(gameBoard[3], gameBoard[7], oppPlayer.ordinal()) == oppPlayer.ordinal()){
			return new BoardLocation(2, 0);
		}
		if(compareThree(gameBoard[5], gameBoard[7], oppPlayer.ordinal()) == oppPlayer.ordinal()){
			return new BoardLocation(2, 2);
		}
		if(compareThree(gameBoard[5], gameBoard[1], oppPlayer.ordinal()) == oppPlayer.ordinal()){
			return new BoardLocation(0, 2);
		}
		return null;
	}

	private boolean isWinningMove(int[] gameBoard, int list, int forplayer) {
		switch (list) {// for every move check all posible three in a row combos
		case (0):
			if (compareThree(gameBoard[1], gameBoard[2], forplayer) == forplayer) {
				return true;
			}
			if (compareThree(gameBoard[3], gameBoard[6], forplayer) == forplayer) {
				return true;
			}
			if (compareThree(gameBoard[4], gameBoard[8], forplayer) == forplayer) {
				return true;
			}
			break;
		case (1):
			if (compareThree(gameBoard[0], gameBoard[2], forplayer) == forplayer) {
				return true;
			}
			if (compareThree(gameBoard[4], gameBoard[7], forplayer) == forplayer) {
				return true;
			}
			break;
		case (2):
			if (compareThree(gameBoard[5], gameBoard[8], forplayer) == forplayer) {
				return true;
			}
			if (compareThree(gameBoard[0], gameBoard[1], forplayer) == forplayer) {
				return true;
			}
			if (compareThree(gameBoard[6], gameBoard[4], forplayer) == forplayer) {
				return true;
			}
			break;
		case (3):
			if (compareThree(gameBoard[0], gameBoard[6], forplayer) == forplayer) {
				return true;
			}
			if (compareThree(gameBoard[4], gameBoard[5], forplayer) == forplayer) {
				return true;
			}
			break;
		case (4):
			if (compareThree(gameBoard[1], gameBoard[7], forplayer) == forplayer) {
				return true;
			}
			if (compareThree(gameBoard[3], gameBoard[5], forplayer) == forplayer) {
				return true;
			}
			if (compareThree(gameBoard[0], gameBoard[8], forplayer) == forplayer) {
				return true;
			}
			if (compareThree(gameBoard[6], gameBoard[2], forplayer) == forplayer) {
				return true;
			}
			break;
		case (5):
			if (compareThree(gameBoard[8], gameBoard[2], forplayer) == forplayer) {
				return true;
			}
			if (compareThree(gameBoard[3], gameBoard[4], forplayer) == forplayer) {
				return true;
			}
			break;
		case (6):
			if (compareThree(gameBoard[0], gameBoard[3], forplayer) == forplayer) {
				return true;
			}
			if (compareThree(gameBoard[7], gameBoard[8], forplayer) == forplayer) {
				return true;
			}
			if (compareThree(gameBoard[4], gameBoard[2], forplayer) == forplayer) {
				return true;
			}
			break;
		case (7):
			if (compareThree(gameBoard[6], gameBoard[8], forplayer) == forplayer) {
				return true;
			}
			if (compareThree(gameBoard[4], gameBoard[1], forplayer) == forplayer) {
				return true;
			}
			break;
		case (8):
			if (compareThree(gameBoard[7], gameBoard[6], forplayer) == forplayer) {
				return true;
			}
			if (compareThree(gameBoard[5], gameBoard[2], forplayer) == forplayer) {
				return true;
			}
			if (compareThree(gameBoard[0], gameBoard[4], forplayer) == forplayer) {
				return true;
			}
			break;
		}

		return false;
	}

	private BoardLocation aboutToWin(GameBoard.CellState player, GameBoard board) {
		BoardLocation[] list;
		boolean var = false;

		list = getAvalMoves(true, true, true, board);

		for (int index = 0; index < list.length; index++) {
			var = isWinningMove(toOldGameboard(board), toOldLocation(list[index]), player.ordinal());
			if (var == true) {
				return list[index];
			}
		}
		return null;
	}

	private int toOldLocation(BoardLocation loc) {
		int[][] table = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}};
		return table[loc.getX()][loc.getY()];
	}

	private BoardLocation[] getAvalMoves(boolean doCheckCorners, boolean doCheckCenter, boolean doCheckSides, GameBoard board) {
		BoardLocation[] avalMoves;
		BoardLocation[] moves = new BoardLocation[9];
		int count = 0;

		if (doCheckCorners == true) {
			if ((getBoardState("corner", 0, board)) == GameBoard.CellState.UNCLAIMED.ordinal()) {
				moves[count] = new BoardLocation(0, 0);
				count++;
			}
			if ((getBoardState("corner", 1, board)) == GameBoard.CellState.UNCLAIMED.ordinal()) {
				//moves[count] = 2;
				moves[count] = new BoardLocation(0, 2);
				count++;

			}
			if ((getBoardState("corner", 2, board)) == GameBoard.CellState.UNCLAIMED.ordinal()) {
				//moves[count] = 6;
				moves[count] = new BoardLocation(2, 0);
				count++;

			}
			if ((getBoardState("corner", 3, board)) == GameBoard.CellState.UNCLAIMED.ordinal()) {
				//moves[count] = 8;
				moves[count] = new BoardLocation(2, 2);
				count++;

			}
		}

		if (doCheckCenter == true) {
			if ((getBoardState("center", 0, board)) == GameBoard.CellState.UNCLAIMED.ordinal()) {
				//moves[count] = 4;
				moves[count] = new BoardLocation(1, 1);
				count++;

			}
		}

		if (doCheckSides == true) {
			if ((getBoardState("side", 0, board)) == GameBoard.CellState.UNCLAIMED.ordinal()) {
				//moves[count] = 3;
				moves[count] = new BoardLocation(1, 0);
				count++;

			}
			if ((getBoardState("side", 1, board)) == GameBoard.CellState.UNCLAIMED.ordinal()) {
				//moves[count] = 1;
				moves[count] = new BoardLocation(0, 1);
				count++;

			}
			if ((getBoardState("side", 2, board)) == GameBoard.CellState.UNCLAIMED.ordinal()) {
				//moves[count] = 5;
				moves[count] = new BoardLocation(1, 2);
				count++;

			}
			if ((getBoardState("side", 3, board)) == GameBoard.CellState.UNCLAIMED.ordinal()) {
				//moves[count] = 7;
				moves[count] = new BoardLocation(2, 1);
				count++;

			}
		}
		avalMoves = new BoardLocation[count];
		for (int index = 0; index < avalMoves.length; index++) {
			avalMoves[index] = moves[index];
		}
		System.out.println("done");
		return avalMoves;
	}

	private int getBoardState(String checkFor, int boardLocation, GameBoard board) {
		int reply = GameBoard.CellState.UNCLAIMED.ordinal();
		loop: switch (checkFor) {
		case ("corner"):// Are the corners taken
			switch (boardLocation) {
			case (0):
				//reply = gameBoard[0];
			    reply = board.getCellState(0, 0).ordinal();
				break loop;
			case (1):
				//reply = gameBoard[2];
				reply = board.getCellState(0, 2).ordinal();
				break loop;
			case (2):
				//reply = gameBoard[6];
				reply = board.getCellState(2, 0).ordinal();
				break loop;
			case (3):
				//reply = gameBoard[8];
				reply = board.getCellState(2, 2).ordinal();
				break loop;
			}
		case ("center"):// Is the center taken
			//reply = gameBoard[4];
			reply = board.getCellState(1, 1).ordinal();
			break loop;
		case ("side"):// Are the sides taken
			switch (boardLocation) {
			case (0):
				//reply = gameBoard[3];
				reply = board.getCellState(1, 0).ordinal();
				break loop;
			case (1):
				//reply = gameBoard[1];
				reply = board.getCellState(0, 1).ordinal();
				break loop;
			case (2):
				//reply = gameBoard[5];
				reply = board.getCellState(1, 2).ordinal();
				break loop;
			case (3):
				//reply = gameBoard[7];
				reply = board.getCellState(2, 1).ordinal();
				break loop;
			}
		}

		return reply;
	}
	
	public int compareThree(int a, int b, int c) {
		int winner = 0;
		if ((a != 0) && (b != 0) && (c != 0)) {
			if ((a == b) && (b == c) && (c == a)) {
				winner = a;
			}
		}
		return winner;
	}
	
	private GameBoard.CellState getOpponent(GameBoard.CellState me) {
		GameBoard.CellState opponent;
		if (me.equals(GameBoard.CellState.X_PLAYER)) {
			opponent = GameBoard.CellState.O_PLAYER;
		} else {
			opponent = GameBoard.CellState.X_PLAYER;
		}

		return opponent;
	}

	private List<BoardLocation> getOpenMoves(GameBoard board) {
		List<BoardLocation> list = new ArrayList<BoardLocation>();
		for (int width = 0; width < board.getWidth(); width++) {
			for (int height = 0; height < board.getHeight(); height++) {
				if (board.getCellState(width, height) == GameBoard.CellState.UNCLAIMED) {
					list.add(new BoardLocation(width, height));
				}
			}
		}
		return list;
	}
	
	/**
	 * Returns a pseudo-random number between min and max, inclusive. The
	 * difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 * 
	 * @param min
	 *            Minimum value
	 * @param max
	 *            Maximum value. Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */

	public static int randInt(int min, int max) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	private BoardLocation pickOne(BoardLocation[] moves) {
		return moves[randInt(0, moves.length - 1)];
	}
	
	public static CellState getWinner(GameBoard board, CellState value) {
		for(int height = 0; height < 3; height++){ // Check all up/down columns
			if(board.getCellState(height, 0).equals(value) &&
			   board.getCellState(height, 2).equals(value)){
				return board.getCellState(height, 0);
			}
		}
		
		for(int width = 0; width < 3; width++){ // Check all left/right rows
			if(board.getCellState(0, width).equals(value) &&
			   board.getCellState(2, width).equals(value)){
				return board.getCellState(0, width);
			}
		}

		if(board.getCellState(0, 0).equals(value) &&
		   board.getCellState(2, 2).equals(value)){
			return board.getCellState(1, 1);
		}
		
		if(board.getCellState(0, 2).equals(value) &&
		   board.getCellState(2, 0).equals(value)){
			return board.getCellState(1, 1);
		}

		return GameBoard.CellState.UNCLAIMED;
	}
}
