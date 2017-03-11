package io.github.robotman3000.tictac;

import io.github.robotman3000.tictac.GameBoard.CellState;
import io.github.robotman3000.tictac.player.AssistedHuman;
import io.github.robotman3000.tictac.player.Computer;
import io.github.robotman3000.tictac.player.Human;
import io.github.robotman3000.tictac.player.LegacyComputer;
import io.github.robotman3000.tictac.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Main {

	// This class could be made an interface or an abstract class to become a
	// generic "GameSession" class
	private static final int TICTAC_WIDTH = 3;
	private static final int TICTAC_HEIGHT = 3;
	private static final int MAX_PLAYERS = 2;
	private static final int MIN_PLAYERS = 2;
	private final static int invalidThreshold = 10;

	public static void main(String[] args) {
		if (!(args.length > 0)) {
			showUsage(1);
		}

		int humanCount = 0, computerCount = 0;

		ArrayList<String> playerOrder = new ArrayList<String>();

		for (String str : args) {
			switch (str) {
				case ("-a"):
					humanCount++;
					playerOrder.add("a");
					break;
				case ("-h"):
					humanCount++;
					playerOrder.add("h");
					break;
				case ("-l"):
					computerCount++;
					playerOrder.add("l");
					break;
				case ("-c"):
					computerCount++;
					playerOrder.add("c");
					break;
				default:
					showUsage(1);
			}
		}

		int playerCount = humanCount + computerCount;
		if (playerCount < MIN_PLAYERS || playerCount > MAX_PLAYERS) {
			showUsage(1);
		}

		List<Player> players = new ArrayList<Player>();
		for (int index = 0; index < playerCount; index++) { // the index = 1 is
															// to skip the peice
															// that means
															// unclaimed
			String type = playerOrder.get(index);

			GameBoard.CellState nextPiece = GameBoard.CellState.values()[index + 1];
			String playerName = "Player " + (index + 1);

			switch (type) {
				case ("c"):
					players.add(new Computer(playerName, nextPiece));
					break;
				case ("l"):
					players.add(new LegacyComputer(playerName, nextPiece));
					break;
				case ("a"):
					players.add(new AssistedHuman(playerName, nextPiece));
					break;
				default:
					players.add(new Human(playerName, nextPiece));
					break;
			}
		}

		GameBoard board = new GameBoard(TICTAC_WIDTH, TICTAC_HEIGHT);
		Main var = new Main();

		boolean gameOver = false;
		int nextPlayer = 0;
		int invalidMoveCount = 0;
		while (!gameOver) {
			Player thePlayer = players.get(nextPlayer);

			var.drawScreen(board, thePlayer, false); // Print gameboard to
														// screen
			BoardLocation move = thePlayer.doMove(board);
			if (board.isValid(move) && board.getCellState(move).equals(GameBoard.CellState.UNCLAIMED)) {
				board.setCellState(move.getX(), move.getY(), thePlayer.getPiece());
			} else {
				System.out.println("Invalid move!!");
				System.out.println("Move was: " + move);
				if (invalidMoveCount > invalidThreshold) {
					System.err.println("Invalid move threshold reached!!");
					System.exit(1);
				} else {
					invalidMoveCount++;
				}
				continue;
			}

			GameBoard.CellState winner;
			if (gameOver = gameWon(board) || getOpenMoves(board).size() == 0) {
				winner = getWinner(board);

				var.drawScreen(board, null, gameOver);
				System.out.println("Game Over!");
				if (winner.ordinal() == 0) {
					System.out.println("Aww, it's a tie");
				} else {
					System.out.println(thePlayer + " has won!");
				}
			}

			nextPlayer = ((nextPlayer + 1) % players.size());
		}
	}

	public static List<BoardLocation> getOpenMoves(GameBoard board) {
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

	public static CellState getWinner(GameBoard board) {
		for (int width = 0; width < TICTAC_WIDTH; width++) { // Check all
																// up/down
																// columns
			if (board.getCellState(width, 0).equals(
					board.getCellState(width, 1))
					&& board.getCellState(width, 2).equals(
							board.getCellState(width, 1))
					&& board.getCellState(width, 0) != CellState.UNCLAIMED) {
				return board.getCellState(width, 0);
			}
		}

		for (int heght = 0; heght < TICTAC_HEIGHT; heght++) { // Check all
																// left/right
																// rows
			if (board.getCellState(0, heght).equals(
					board.getCellState(1, heght))
					&& board.getCellState(2, heght).equals(
							board.getCellState(1, heght))
					&& board.getCellState(0, heght) != CellState.UNCLAIMED) {
				return board.getCellState(0, heght);
			}
		}

		if (board.getCellState(0, 0).equals(board.getCellState(1, 1))
				&& board.getCellState(2, 2).equals(board.getCellState(1, 1))
				&& board.getCellState(1, 1) != CellState.UNCLAIMED) {
			return board.getCellState(1, 1);
		}

		if (board.getCellState(0, 2).equals(board.getCellState(1, 1))
				&& board.getCellState(2, 0).equals(board.getCellState(1, 1))
				&& board.getCellState(1, 1) != CellState.UNCLAIMED) {
			return board.getCellState(1, 1);
		}

		return GameBoard.CellState.UNCLAIMED;
	}

	public static boolean gameWon(GameBoard board) {
		return (getWinner(board) != CellState.UNCLAIMED);
	}

	private void drawScreen(GameBoard gameBoard, Player currentPlayer, boolean gamedone) {
		// System.out.println("a"+"\n"+"hi"); // new line test
		// clearScreen(50);
		clearScreen(2);
		System.out.println(gameBoard);
		if (gamedone == true) {
			System.out.println("================================================================================");
			System.out.println();
			System.out.println(gameBoard.getCellState(0, 0) + " | " + gameBoard.getCellState(0, 1) + " | " + gameBoard.getCellState(0, 2));
			System.out.println("---------");
			System.out.println(gameBoard.getCellState(1, 0) + " | " + gameBoard.getCellState(1, 1) + " | " + gameBoard.getCellState(1, 2));
			System.out.println("---------");
			System.out.println(gameBoard.getCellState(2, 0) + " | " + gameBoard.getCellState(2, 1) + " | " + gameBoard.getCellState(2, 2));
			System.out.println();
			System.out.println("================================================================================");
		} else {
			System.out.println("================================================================================");
			System.out.println(currentPlayer + "'s turn (" + currentPlayer.getPiece() + ")");
			System.out.println();
			System.out.println("================================================================================");
			System.out.println();
			System.out.println();
			System.out.println("             Slot  Key");
			System.out.println(gameBoard.getCellState(0, 0) + " | " + gameBoard.getCellState(0, 1) + " | " + gameBoard.getCellState(0, 2) + " 0,0 | 0,1 | 0,2");
			System.out.println("--------- ---------------");
			System.out.println(gameBoard.getCellState(1, 0) + " | " + gameBoard.getCellState(1, 1) + " | " + gameBoard.getCellState(1, 2) + " 1,0 | 1,1 | 1,2");
			System.out.println("--------- ---------------");
			System.out.println(gameBoard.getCellState(2, 0) + " | " + gameBoard.getCellState(2, 1) + " | " + gameBoard.getCellState(2, 2) + " 2,0 | 2,1 | 2,2");
			System.out.println();
			System.out.println();
			System.out.println("================================================================================");
		}
	}

	private void clearScreen(int makeLines) {
		for (int count = 0; count <= makeLines; count++) {
			System.out.println();
		}
	}

	private static void showUsage(int errorCode) {
		// TODO: Put usage information
		System.out.println("Invalid options");
		System.exit(errorCode);
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
}