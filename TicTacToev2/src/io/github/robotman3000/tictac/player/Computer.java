package io.github.robotman3000.tictac.player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import io.github.robotman3000.tictac.BoardLocation;
import io.github.robotman3000.tictac.GameBoard;
import io.github.robotman3000.tictac.GameBoard.CellState;
import io.github.robotman3000.tictac.Main;

public class Computer extends Player {

	private HashSet<WeightedBoardLocation> possMoveList = new HashSet<WeightedBoardLocation>();
	//private HashSet<WeightedBoardLocation> tieList = new HashSet<WeightedBoardLocation>();
	//private HashSet<WeightedBoardLocation> looseList = new HashSet<WeightedBoardLocation>();
	private ArrayList<GameBoardNode> moveList = new ArrayList<>();
	private String dateFolder = new SimpleDateFormat("yyyy-MM-dd-hh.mm.ss").format(Calendar.getInstance().getTime());
	private GameBoardNode rootNode;
	StringBuilder str = new StringBuilder();
	StringBuilder gameStr = new StringBuilder();

	public Computer(String name, CellState peice) {
		super(name, peice);
	}

	@Override
	public BoardLocation doMove(GameBoard theBoard) {
		new File(dateFolder).mkdir();
		long startTimeMS = System.nanoTime() / 1000000;
		resetData();
		GameBoard board = new GameBoard(theBoard); // Clone the gameboard
		rootNode = new GameBoardNode(board); // Make the root node of our tree with the board we were given
		List<BoardLocation> moves = getOpenMoves(board);
		String value = "[ " + rootNode.board + " ] | Move: " + (9 - moves.size()) + " | Winner: " + Main.getWinner(rootNode.board) + " | Changed Move: " + rootNode.move + " | Player: " + rootNode.changedValue + " &&&&";
		System.out.println(value);
		gameStr.append(value + "\n");
		for(BoardLocation move : moves){
			GameBoard workBoard = new GameBoard(board); // Clone the gameboard
			workBoard.setCellState(move.getX(), move.getY(), getPiece()); // Peform our move on that clone
			GameBoardNode node = new GameBoardNode(workBoard);
			node.changedValue = getPiece();
			node.setParent(rootNode);
			node.setChangedMove(move);
			rootNode.addChild(node); // Adds that board to our list of children
			String value2 = "" + tabMe(9 - moves.size()) + "[ " + node.board + " ] | Changed Move: " + node.move + " | Winner: " + Main.getWinner(node.board) + " | Move: " + (9 - moves.size()) +  " | Player: " + node.changedValue + " %%%%";
			////$$System.out.println(value2);
			gameStr.append(value2 + "\n");
			makeGameTree(workBoard, getOpponent(getPiece()), node); // Get gametree for that node
			
			getChildren(node);
			
			HashSet<WeightedBoardLocation> scoreList = new HashSet<WeightedBoardLocation>();
			//System.out.println("==================================================");
			//System.out.println(node.board);
			//System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++");
			for(GameBoardNode aNode : moveList){
				int myWins = 0, oppWins = 0, tieWins = 0;
				//System.out.println("Win List: " + winList.size());
				//System.out.println("Loose List: " + looseList.size());
				//System.out.println("Tie List: " + tieList.size());
				//System.out.println("Move List: " + moveList.size());
				//System.out.println(aNode.board + "\n");
				if(Main.gameWon(aNode.board) || moveList.size() == 0){
					myWins = countWins(getPiece(), aNode.board);
					oppWins = countWins(getOpponent(getPiece()), aNode.board);
					if(moveList.size() == 0){
						tieWins = 1; // you can only tie once
					}
					//scoreList.add(new WeightedBoardLocation(move, calculateScore((9 - moves.size()), myWins, oppWins, tieWins))); // I mean "moves" not "movesList" because "moves" is our current turn in the game and "moveList" is a count of possible moves

/*					if (aNode.parent.parent == null){ //TODO: Replace this "
						scoreList.add(new WeightedBoardLocation(move, (calculateScore(moves.size(), myWins, oppWins, tieWins) + 1000000))); // Make it impossible for this move to not be picked
					} else {*/
						scoreList.add(new WeightedBoardLocation(move, calculateScore(moves.size(), myWins, oppWins, tieWins)));
					//}
				}
			}
			//System.out.println("==================================================");
			int score = 0;
			for(WeightedBoardLocation loc : scoreList){
				score = score + loc.getWeight();
			}
			possMoveList.add(new WeightedBoardLocation(move, score));
		}
		
		long endTimeMS = System.nanoTime() / 1000000;
		System.out.println("Calculation Time: " + (endTimeMS - startTimeMS) + " ms");
		try (BufferedWriter w = new BufferedWriter(new FileWriter(dateFolder + "/gameTree_" + name + "_" + (9 - moves.size()) + ".txt"))) {
			w.write(gameStr.toString());
		} catch (IOException e) {}
		
		// Our move is picked by the "move" with the highest score
		WeightedBoardLocation myMove = new WeightedBoardLocation(new BoardLocation(0, 0), 0);
		for(WeightedBoardLocation wMove : possMoveList){
			System.out.println("Move & Score: " + wMove);
			if(wMove.getWeight() > myMove.getWeight()){
				myMove = wMove;
			}
		}
		return myMove;
	}

	private int calculateScore(int currentMove, int myWins, int oppWins, int tieWins) {
		StringBuilder strBuild = new StringBuilder();
		final int BASE_2 = 2;
		
		// The order of the appends are very important to the move picking algorithm
		strBuild.append(Integer.toBinaryString(currentMove));
		strBuild.append(Integer.toBinaryString(myWins));
		strBuild.append(Integer.toBinaryString(tieWins));
		strBuild.append(Integer.toBinaryString(oppWins));
		return Integer.parseInt(strBuild.toString(), BASE_2);
	}

	private void resetData() {
		//tieList.clear();
		possMoveList.clear();
		//looseList.clear();
		moveList.clear();
		gameStr = new StringBuilder();
	}

	private void getChildren(GameBoardNode node) {
		if(node.getChildren().size() == 0){
			for(GameBoardNode aNode : node.parent.getChildren()){
				if(aNode != null){
					if(Main.gameWon(aNode.board) || getOpenMoves(aNode.board).size() == 0){
						moveList.add(aNode);
					}
				}
			}
		}
		for(GameBoardNode child : node.getChildren()){
			getChildren(child);
		}
	}

	private void makeGameTree(GameBoard theBoard, CellState opponent, GameBoardNode theRootNode) {
		GameBoard myBoard = new GameBoard(theBoard);
		List<BoardLocation> moves = getOpenMoves(myBoard);
		
		for(BoardLocation move : moves){ // This won't run if there are no moves because there is nothing to iterate over
			GameBoard board = new GameBoard(myBoard);
			board.setCellState(move.getX(), move.getY(), opponent);
			GameBoardNode node = new GameBoardNode(board);
			
			node.changedValue = opponent;
			node.setParent(theRootNode);
			node.setChangedMove(move);
			theRootNode.getChildren().add(node);
			
			String value = tabMe(9 - moves.size()) + "[ " + node.board + " ] | Move: " + (9 - moves.size()) + " | Winner: " + Main.getWinner(node.board) + " | Changed Move: " + node.move + " | Player: " + node.changedValue;
			////$$System.out.println(value);
			gameStr.append(value);

			if(Main.gameWon(board)){
				gameStr.append(" **** @@@@" + charMe((9 - moves.size()) + 1, "") + "\n");
				value = null;// This is so the we don't store the strs value in the stack
				return;
			}
			if(getOpenMoves(node.board).size() == 0){  // this only is reached if there is no winner and the board is full
				gameStr.append(" ****" + charMe((9 - moves.size()) + 1, ""));
				// TODO: Tiecount++;
			}
			gameStr.append("\n");
			value = null;// This is so the we don't store the strs value in the stack
			makeGameTree(board, getOpponent(opponent), node); // The get opponent swaps the game piece
		}
		// If we get here there must have been no moves
	}

	private int countWins(CellState opponent, GameBoard board) {
		int winCount = 0;
		for(int width = 0; width < board.getWidth(); width++){ // Check all up/down columns
			if(board.getCellState(width, 0).equals(board.getCellState(width, 1)) &&
			   board.getCellState(width, 2).equals(board.getCellState(width, 1)) &&
			   board.getCellState(width, 0) == opponent){
				winCount++;
			}
		}
		
		for(int heght = 0; heght < board.getHeight(); heght++){ // Check all left/right rows
			if(board.getCellState(0, heght).equals(board.getCellState(1, heght)) &&
			   board.getCellState(2, heght).equals(board.getCellState(1, heght)) &&
			   board.getCellState(0, heght) == opponent){
				winCount++;
			}
		}

		if(board.getCellState(0, 0).equals(board.getCellState(1, 1)) &&
		   board.getCellState(2, 2).equals(board.getCellState(1, 1)) &&
		   board.getCellState(1, 1) == opponent){
			winCount++;
		}
		
		if(board.getCellState(0, 2).equals(board.getCellState(1, 1)) &&
		   board.getCellState(2, 0).equals(board.getCellState(1, 1)) &&
		   board.getCellState(1, 1) == opponent){
			winCount++;
		}

		return winCount;
	}

	private String charMe(int i, String strPlace) {
		StringBuilder str = new StringBuilder();
		for(int index = 0; index <= i; index++){
			str.append(strPlace);
		}
		return str.toString();
	}

	private String tabMe(int size) {
		StringBuilder str = new StringBuilder();
		for(int index = 0; index <= size; index++){
			str.append("\t");
		}
		return str.toString();
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
}
