package io.github.robotman3000.tictac.player;

import java.util.ArrayList;
import java.util.List;

import io.github.robotman3000.tictac.BoardLocation;
import io.github.robotman3000.tictac.GameBoard;

public class WinnerCalculator {

	public static int calc(GameBoard theBoard, GameBoard.CellState me) {
		/*
		 * System.out.println("Debug: Enter Winner Score:"); System.out.println(theBoard); return new Scanner(System.in).nextInt();
		 */
		// Calculation Truth Table
		// W2 W T L L2 Score
		// 0 0 0 0 0 = 0
		// 0 0 0 0 1 = 1
		// 0 0 0 1 0 = 2
		// 0 0 1 0 0 = 4
		// 0 1 0 0 0 = 8
		// 1 0 0 0 0 = 16

		// If me is 1 then opp is 2
		// if me is 2 then opp is 1
		int opp = (3 - me.ordinal());
		int winCount = 0;
		int looseCount = 0;

		int[] results = {
				compareThree(theBoard.getCellState(0, 0).ordinal(), theBoard.getCellState(0, 1).ordinal(), theBoard
						.getCellState(0, 2).ordinal()),
				compareThree(theBoard.getCellState(1, 0).ordinal(), theBoard.getCellState(1, 1).ordinal(), theBoard
						.getCellState(1, 2).ordinal()),
				compareThree(theBoard.getCellState(2, 0).ordinal(), theBoard.getCellState(2, 1).ordinal(), theBoard
						.getCellState(2, 2).ordinal()),
				compareThree(theBoard.getCellState(0, 0).ordinal(), theBoard.getCellState(1, 0).ordinal(), theBoard
						.getCellState(2, 0).ordinal()),
				compareThree(theBoard.getCellState(0, 1).ordinal(), theBoard.getCellState(1, 1).ordinal(), theBoard
						.getCellState(2, 1).ordinal()),
				compareThree(theBoard.getCellState(0, 2).ordinal(), theBoard.getCellState(1, 2).ordinal(), theBoard
						.getCellState(2, 2).ordinal()),
				compareThree(theBoard.getCellState(0, 0).ordinal(), theBoard.getCellState(1, 1).ordinal(), theBoard
						.getCellState(2, 2).ordinal()),
				compareThree(theBoard.getCellState(0, 2).ordinal(), theBoard.getCellState(1, 1).ordinal(), theBoard
						.getCellState(2, 0).ordinal()) };
		
		for(int i : results){
			if(i == me.ordinal()){
				winCount++;
			} else if(i == opp){
				looseCount++;
			}
		}
		
/*		System.out.println("Debug: Enter Winner Score:");
		System.out.println(theBoard);*/
		if(winCount > 0){
			if(winCount > 1){
				return 16;
			}
			return 8;
		} else if(looseCount > 0){
			if(looseCount > 1){
				return 1;
			}
			return 2;
		}

		if(getOpenMoves(theBoard).size() == 0){
			// We tied
			return 4;
		}
		return 0;
	}

	private static int compareThree(int a, int b, int c) {
		int winner = 0;
		if ((a != 0) && (b != 0) && (c != 0)) {
			if ((a == b) && (b == c) && (c == a)) {
				winner = a;
			}
		}
		return winner;
	}
	
	private static List<BoardLocation> getOpenMoves(GameBoard board) {
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
}
