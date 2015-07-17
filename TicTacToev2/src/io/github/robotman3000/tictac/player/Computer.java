package io.github.robotman3000.tictac.player;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.github.robotman3000.tictac.BoardLocation;
import io.github.robotman3000.tictac.GameBoard;
import io.github.robotman3000.tictac.GameBoard.CellState;
import io.github.robotman3000.tictac.Main;

public class Computer extends Player {

	private ArrayList<BoardLocation> winList = new ArrayList<BoardLocation>();
	private ArrayList<BoardLocation> tieList = new ArrayList<BoardLocation>();
	private ArrayList<BoardLocation> looseList = new ArrayList<BoardLocation>();
	int counter = 0;
	int counter2 = 0;
	private GameBoardNode rootNode;
	StringBuilder str = new StringBuilder();

	public Computer(String name, CellState peice) {
		super(name, peice);
	}

	@Override
	public BoardLocation doMove(GameBoard theBoard) {
		long startTimeMS = System.nanoTime() / 1000000;
		System.out.println("Thinking...");
		rootNode = null;
		rootNode = new GameBoardNode(theBoard, "rootNode");
		winList = new ArrayList<BoardLocation>();
		tieList = new ArrayList<BoardLocation>();
		looseList = new ArrayList<BoardLocation>();
		doWork(theBoard, getPiece(), rootNode);
		long endTimeMS = System.nanoTime() / 1000000;
		System.out.println("Calculation Time: " + (endTimeMS - startTimeMS)
				+ " ms" + "\nCalculation Time: "
				+ ((endTimeMS - startTimeMS) / 1000) + "."
				+ ((endTimeMS - startTimeMS) % 1000) + " s");
		
		try (BufferedWriter w = new BufferedWriter(new FileWriter("log2.txt"))) {
			printGameTree(rootNode, w);
		} catch (IOException e) {
		}
		
		if (winList.size() > 0) {
			return pickOne(winList);
		} else if (tieList.size() > 0) {
			return pickOne(tieList);
		} else if (looseList.size() > 0) {
			return pickOne(looseList);
		}
		// We crash; this state can't be recovered from
		// return null;
		return new BoardLocation(0, 0);
	}

	private void printGameTree(GameBoardNode rootNode2, BufferedWriter w) throws IOException {
		
		w.append("====================================================================================================\n");
		w.append("Name: " + rootNode2.getName() + "\nChild Of: " + rootNode2.getChildOf() + "\n");
		w.append("Root: \n" + "Changed Value: " + getOpponent(rootNode2.changedValue) + "\n" + rootNode2.board.toString() + "\n");
		
		for(GameBoardNode node : rootNode2.getChildren()){
			w.append(node.board.toString() + "\n");
		}
		if(rootNode2.getChildren().size() > 0){
			for(GameBoardNode node : rootNode2.getChildren()){
				/*if(Main.gameWon(node.board)){
					CellState winner = Main.getWinner(node.board);
					if(winner == getPiece()){
						winList.add(findBegining(node));
					}
					if(winner == getOpponent(getPiece())){
						looseList.add(findBegining(node));
					}
				} else if(getOpenMoves(node.board).size() == 0){
					tieList.add(findBegining(node));
				}*/
				printGameTree(node, w);
			}
		}
	}

	private void doWork(GameBoard theBoard, CellState opponent, GameBoardNode theRootNode) {
		GameBoard myBoard = new GameBoard(theBoard);
		List<BoardLocation> moves = getOpenMoves(myBoard);
		
		for(BoardLocation move : moves){ // This won't run if there are no moves because there is nothing to iterate over
			GameBoard board = new GameBoard(myBoard);
			board.setCellState(move.getX(), move.getY(), opponent);
			GameBoardNode node = new GameBoardNode(board, String.valueOf(moves.size()));
			
			node.changedValue = opponent;
			node.setChildOfName(theRootNode.getName());
			node.setParent(theRootNode);
			theRootNode.getChildren().add(node);
			node.setChangedMove(move);
			
			if(Main.gameWon(board)){
				if(Main.getWinner(board) == getPiece()){
					if(findBegining(node) != null){
						winList.add(findBegining(node));
					}
				} else {
					if(findBegining(node) != null){
						looseList.add(findBegining(node));
					}
				}
				return;
			}
			doWork(board, getOpponent(opponent), node); // The get opponent swaps the game piece
		}
		if (moves.size() == 0){
			if(findBegining(theRootNode) != null){
				tieList.add(findBegining(theRootNode));
			}
		}
	}

	private BoardLocation findBegining(GameBoardNode node) {
		if(node.parent != null && node.parent.parent != null){
			for(GameBoardNode theNode : node.parent.parent.getChildren()){ // The double parent is part of the logic
				findBegining(theNode);
			}
		}
		if (!node.getName().equals("rootNode") && node.getChildOf().equals("rootNode") && node.move != null){
			//System.out.println(node.move);
			return node.move;
		}
		return null;
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

	private BoardLocation pickOne(List<BoardLocation> list) {
		return list.get(randInt(0, list.size()));
	}
}
