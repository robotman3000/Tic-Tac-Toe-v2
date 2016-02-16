package io.github.robotman3000.tictac.player;

import io.github.robotman3000.tictac.BoardLocation;
import io.github.robotman3000.tictac.GameBoard;
import io.github.robotman3000.tictac.GameBoard.CellState;
import io.github.robotman3000.tictac.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Computer extends Player {

	StringBuilder str = new StringBuilder();
	int counter = 0;
	
	public Computer(String name, CellState peice) {
		super(name, peice);
	}

	@Override
	public BoardLocation doMove(GameBoard theBoard) {
		str = new StringBuilder();
		HashSet<WeightedBoardLocation> moves = new HashSet<>();
		str.append(theBoard + "\n");
		for (BoardLocation loc : getOpenMoves(theBoard)){
			counter = 0;
			str.append("\n========================================\n");
			GameBoard safeBoard = new GameBoard(theBoard);
			safeBoard.setCellState(loc.getX(), loc.getY(), getPiece());
			
			if ((Main.gameWon(safeBoard) && !Main.getWinner(safeBoard).equals(getOpponent(getPiece()))) || getOpenMoves(safeBoard).size() == 0){
				return loc;
			}
			for(int index = 0; index <= (9 - getOpenMoves(safeBoard).size()); index++){
				str.append("\t");
			}
			str.append(safeBoard + " " + new WeightedBoardLocation(loc, calculateScore(safeBoard)) + " TOKEN: " + getPiece() + "\n");
			int moveScore = makeGameTree(safeBoard, getOpponent(getPiece()), new LinkedHashSet<WeightedBoardLocation>());
			moves.add(new WeightedBoardLocation(loc, moveScore));
			str.append("counter: " + counter);
		}
		
		File file = new File("ticTac-Move" + (9 - getOpenMoves(theBoard).size()) + ".txt");
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(str.toString());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return findTheBestMove(moves);
	}

	private BoardLocation findTheBestMove(Set<WeightedBoardLocation> moves) {
		WeightedBoardLocation bestLoc = null;
		int bestScore = 0;
		for(WeightedBoardLocation loc : moves){
			if(moves.size() == 1){
				return loc;
			}
			
			if(loc.getWeight() > bestScore){
				bestLoc = loc;
				bestScore = bestLoc.getWeight();
			} else if (loc.getWeight() >= bestScore){
				if(new Random().nextBoolean()){
					bestLoc = loc;
					bestScore = bestLoc.getWeight();
				}
			}
		}
		return bestLoc;
	}

	private int calculateScore(GameBoard theBoard) {
		// First get the number of moves to ended game
		int boardWinnerScore = WinnerCalculator.calc(theBoard, getPiece());
/*		System.out.println("Board Score: " + boardWinnerScore);
		System.out.println("Score: " + (((9 - getOpenMoves(theBoard).size()) << 5) | (boardWinnerScore)) + "\n");*/
		
		return ((9 - getOpenMoves(theBoard).size()) << 5) | (boardWinnerScore);
		//return ((boardWinnerScore) << 4 | (9 - getOpenMoves(theBoard).size()));
		//return ((boardWinnerScore) | (9 - getOpenMoves(theBoard).size()));
	}

	private int makeGameTree(GameBoard theBoard, CellState opponent, Set<WeightedBoardLocation> savedMoves) {
		// When we get to an ended game score the board and if the score is bigger than the current score then save the new score
		Set<WeightedBoardLocation> moves = new HashSet<>();
		int score = 0;
		
		for(BoardLocation loc : getOpenMoves(theBoard)){
			GameBoard safeBoard = new GameBoard(theBoard);
			safeBoard.setCellState(loc.getX(), loc.getY(), opponent);
			
			if(Main.gameWon(safeBoard) || getOpenMoves(safeBoard).size() == 0){
				moves.add(new WeightedBoardLocation(loc, calculateScore(safeBoard)));
				
				for(int index = 0; index <= (9 - getOpenMoves(safeBoard).size()); index++){
					str.append("\t");
				}
				counter++;
				str.append(safeBoard + " " + new WeightedBoardLocation(loc, calculateScore(safeBoard)) + " TOKEN: " + opponent + "\n");
			} else {
				for(int index = 0; index < (9 - getOpenMoves(safeBoard).size()); index++){
					//str.append("\t");
				}
				//str.append(safeBoard + " " + loc + " CURR SCORE: " + score + " TOKEN: " + getOpponent(opponent) + "\n");
				
				score = makeGameTree(safeBoard, getOpponent(opponent), savedMoves);
			}
		}
		
		if(!moves.isEmpty()){
			int moveScore = ((WeightedBoardLocation) findTheBestMove(moves)).getWeight();
			return (score > moveScore ? score : moveScore);
		}
		return score;
	}

	private GameBoard.CellState getOpponent(GameBoard.CellState me) {
		return ( me.equals(GameBoard.CellState.X_PLAYER) ? 
				GameBoard.CellState.O_PLAYER : 
					GameBoard.CellState.X_PLAYER);
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
