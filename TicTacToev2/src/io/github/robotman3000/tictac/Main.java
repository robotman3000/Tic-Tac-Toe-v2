package io.github.robotman3000.tictac;

import io.github.robotman3000.tictac.GameBoard.CellState;
import io.github.robotman3000.tictac.player.Computer;
import io.github.robotman3000.tictac.player.Human;
import io.github.robotman3000.tictac.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Main {

	// This class could be made an interface or an abstract class to become a generic "GameSession" class
	private static final int TICTAC_WIDTH = 3;
	private static final int TICTAC_HEIGHT = 3;
	private static final int MAX_PLAYERS = 2;
	private static final int MIN_PLAYERS = 2;
	
	public static void main(String[] args) {
		if(!(args.length > 0)){
			showUsage(1);
		}
		
		int humanCount = 0, computerCount = 0;
		
		ArrayList<String> playerOrder = new ArrayList<String>();
		
		for(String str : args){
			switch(str){
			case ("-h"):
				humanCount++;
				playerOrder.add("h");
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
		if (playerCount < MIN_PLAYERS || playerCount > MAX_PLAYERS){
			showUsage(1);
		}
		
		List<Player> players = new ArrayList<Player>();
		for(int index = 0; index < playerCount; index++){  // the index = 1 is to skip the peice that means unclaimed
/*			for(int index = 1; index <= humanCount; index++){
				Player player = null;

				
				GameBoard.CellState nextPiece = GameBoard.CellState.values()[index];
				String playerName = "Player " + index;

				// TODO: Consider using a PlayerFactory instead of putting the logic directly here.
				if (aiFirst)
					player = new Computer(playerName, nextPiece);
				else
					player = new Human(playerName, nextPiece);

				if (player != null)
					players.add(player);
			}*/
			String type = playerOrder.get(index);
			
			GameBoard.CellState nextPiece = GameBoard.CellState.values()[index + 1];
			String playerName = "Player " + (index + 1);

			switch(type){
			case ("c"):
				players.add(new Computer(playerName, nextPiece));
			default:
				players.add(new Human(playerName, nextPiece));
			}
		}

		GameBoard board = new GameBoard(TICTAC_WIDTH, TICTAC_HEIGHT);
		Main var = new Main();
		
		boolean gameOver = false;
		int nextPlayer = 0;
		while (!gameOver){
			Player thePlayer = players.get(nextPlayer);

			var.drawScreen(board, thePlayer, false); // Print gameboard to screen
			BoardLocation move = thePlayer.doMove(board);
			if(board.isValid(move) && board.getCellState(move).equals(GameBoard.CellState.UNCLAIMED)){
				board.setCellState(move.getX(), move.getY(), thePlayer.getPeice());
			} else {
				System.out.println("Invalid move!!");
				continue;
			}

			GameBoard.CellState winner;
			if(gameOver = gameWon(board)){
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

	public static CellState getWinner(GameBoard board) {
		for(int height = 0; height < TICTAC_HEIGHT; height++){ // Check all up/down columns
			if(board.getCellState(height, 0).equals(board.getCellState(height, 1)) &&
			   board.getCellState(height, 2).equals(board.getCellState(height, 1)) &&
			   board.getCellState(height, 0) != CellState.UNCLAIMED){
				return board.getCellState(height, 0);
			}
		}
		
		for(int width = 0; width < TICTAC_WIDTH; width++){ // Check all left/right rows
			if(board.getCellState(0, width).equals(board.getCellState(1, width)) &&
			   board.getCellState(2, width).equals(board.getCellState(1, width)) &&
			   board.getCellState(0, width) != CellState.UNCLAIMED){
				return board.getCellState(0, width);
			}
		}

		if(board.getCellState(0, 0).equals(board.getCellState(1, 1)) &&
		   board.getCellState(2, 2).equals(board.getCellState(1, 1)) &&
		   board.getCellState(1, 1) != CellState.UNCLAIMED){
			return board.getCellState(1, 1);
		}
		
		if(board.getCellState(0, 2).equals(board.getCellState(1, 1)) &&
		   board.getCellState(2, 0).equals(board.getCellState(1, 1)) &&
		   board.getCellState(1, 1) != CellState.UNCLAIMED){
			return board.getCellState(1, 1);
		}

		return GameBoard.CellState.UNCLAIMED;
	}

	public static boolean gameWon(GameBoard board) {
		return (getWinner(board) != CellState.UNCLAIMED);
	}

	private void drawScreen(GameBoard gameBoard, Player currentPlayer, boolean gamedone) {
		// System.out.println("a"+"\n"+"hi"); // new line test
		clearScreen(50);
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
			System.out.println(currentPlayer + "'s turn");
			System.out.println();
			System.out.println("================================================================================");
			System.out.println();
			System.out.println();
			System.out.println(" Slot Key");
			System.out.println(gameBoard.getCellState(0, 0) + " | " + gameBoard.getCellState(0, 1) + " | " + gameBoard.getCellState(0, 2) + " 0,0 | 0,1 | 0,2");
			System.out.println("--------- ---------");
			System.out.println(gameBoard.getCellState(1, 0) + " | " + gameBoard.getCellState(1, 1) + " | " + gameBoard.getCellState(1, 2) + " 1,0 | 1,1 | 1,2");
			System.out.println("--------- ---------");
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

	private static void showUsage(int errorCode){
		// TODO: Put usage information
		System.out.println("Invalid options");
		System.exit(errorCode);
	}
}