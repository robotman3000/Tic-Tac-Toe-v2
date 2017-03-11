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
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Computer extends Player {

	public Computer(String name, CellState peice) {
		super(name, peice);
	}

	@Override
	public BoardLocation doMove(GameBoard theBoard) {
		List<BoardLocation> openMoves = Main.getOpenMoves(theBoard);
		GameBoardNode root = new GameBoardNode(null, theBoard, null, (9 - openMoves.size()));
		Set<WeightedBoardLocation> possibleMoves = new HashSet<>();
		
		List<GameBoard> originals = new ArrayList<>();
		
		for(BoardLocation move : openMoves){
			boolean saveBoard = false;
			GameBoard board = tryMove(root.getNode(), move, getPiece());
			if(originals.isEmpty()){
				originals.add(board);
				saveBoard = true;
			} else {
				// Rotate the original board into each of the 4 rotations
				// If none of those rotations match the original then keep this new board
				
				boolean testFailed = false;
				for(GameBoard brd : originals){
					GameBoard temp = brd;
					for(int distance = 0; distance < 4; distance++){
						temp = rotateBoard(temp);
						if(board.equals(temp)){
							testFailed = true;
							//System.out.print("F");
						}
					}
				}
				
				if(!testFailed){
					saveBoard = true;
				}
			}
			
			if(saveBoard){
				GameBoardNode childNode = new GameBoardNode(root, board, move, root.getNodeZ() + 1);
				root.addChild(childNode);
				int moveScore = makeGameTree(childNode, getOpponent(getPiece()));
				WeightedBoardLocation scoredMove = new WeightedBoardLocation(move, moveScore);
				possibleMoves.add(scoredMove);
				saveGameTree(childNode);
			}
			
		}
		
		saveGameTree(root);
		// We pick the move with the largest score
		WeightedBoardLocation bestMove = null;
		Set<WeightedBoardLocation> list = new HashSet<>();
		for(WeightedBoardLocation move : possibleMoves){
			System.out.println("Posible Move: " + move);
			if(bestMove == null){
				bestMove = move;
				list.add(move);
			} else if(move.getWeight() == bestMove.getWeight()){
				list.add(move);
			} else if(move.getWeight() > bestMove.getWeight()){
				// Use the largest value to maximize our winning potential
				bestMove = move;
				list.clear();
				list.add(move);
			}
		}
		System.out.println("Moves: " + list);
		return bestMove;
	}
	
	private void saveGameTree(GameBoardNode childNode) {
		StringBuilder str = new StringBuilder();
		appendTree(str, childNode);
		String result = str.toString();
		//System.out.println(result);
		
		// Write Result to file
		try {
			String string = "root";
			if (childNode.getNodeXY() != null){
				string = childNode.getNodeXY().getX() + "_" + childNode.getNodeXY().getY();
			}
			FileWriter writer = new FileWriter("GameTree-" + (childNode.getNodeZ() + (childNode.getNodeXY() == null ? 1 : 0)) +  "-" + string + ".txt");
			writer.write(result);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void appendTree(StringBuilder str, GameBoardNode childNode){
		str.append(tabLength(childNode.getNodeZ()) + childNode + " {\n");
		for(GameBoardNode child : childNode.getChildren()){
			appendTree(str, child);
		}
		str.append(tabLength(childNode.getNodeZ()) + "}\n");
	}
	
	private int makeGameTree(GameBoardNode root, CellState currentTurn) {
		// Get all the open moves
		List<BoardLocation> openMoves = Main.getOpenMoves(root.getNode());
		
		// Determine if the game is already over
		// If the winner is a player OR the winner is nobody and there are no more moves
		if(Main.getWinner(root.getNode()) != CellState.UNCLAIMED || openMoves.isEmpty()){
			// Calculate the score for this board and return it
			
			int score = minimax(root.getNode(), root.getNodeZ(), currentTurn);
			root.setScore(score);
			return score;
		}
		
		// Otherwise repeat for the nodes children
		Integer[] scores = new Integer[openMoves.size()];
		int scoreIndex = 0;
		
		List<GameBoard> originals = new ArrayList<>();
		for(BoardLocation move : openMoves){
			// Try that move
			boolean saveBoard = false;
			GameBoard board = tryMove(root.getNode(), move, currentTurn);
			if(originals.isEmpty()){
				originals.add(board);
				saveBoard = true;
			} else {
				// Rotate the original board into each of the 4 rotations
				// If none of those rotations match the original then keep this new board
				
				boolean testFailed = false;
				for(GameBoard brd : originals){
					GameBoard temp = brd;
					for(int distance = 0; distance < 4; distance++){
						temp = rotateBoard(temp);
						if(board.equals(temp)){
							System.out.print("F");
							testFailed = true;
						}
					}
				}
				
				if(!testFailed){
					saveBoard = true;
				}
			}
		
/*			System.out.println("-----------------");
			for(GameBoard boardss : originals){
				System.out.println(boardss);
			}
			System.out.println("-----------------");*/
			if(saveBoard){
				GameBoardNode childNode = new GameBoardNode(root, board, move, root.getNodeZ() + 1);
				root.addChild(childNode);
				
				// Repeat and save the score returned
				scores[scoreIndex++] = makeGameTree(childNode, getOpponent(currentTurn));
			}
		}
		
		// Now we have evaluated all the root nodes children
		// so the next step is to give a score to the root node from the scores of the children
		int result = 0;
		if((root.getNodeZ() & 1) == 0){// The number is even
			int maxScore = Integer.MIN_VALUE;
		
			// If the depth of the root node means that X made the move,
			// we go with the maximum child score
			for(Integer score : scores){
				if(score != null){
					if(score > maxScore){
						maxScore = score;
					}
				}
			}
			result = maxScore;
		} else {// The number is odd
			int minScore = Integer.MAX_VALUE;
			
			// If the depth of the root node means that O made the move,
			// we go with the minimum child score 	
			for(Integer score : scores){
				if(score != null){
					if(score < minScore){
						minScore = score;
					}
				}
			}
			result = minScore;
		}
		root.setScore(result);
		//System.out.println("        " + tabLength(root.getNodeZ()) + root);
		//System.out.println("Scores: " + tabLength(root.getNodeZ()) + arrayToString(scores));
		//System.out.println("   Use: " + tabLength(root.getNodeZ()) + result + "\n");
		return result;
	}
	
	private GameBoard rotateBoard(GameBoard brd) {
		//					={0, 1, 2, 3, 4, 5, 6, 7, 8}
		int[] translations = {2, 5, 8, 1, 4, 7, 0, 3, 6};
		CellState[] board = new CellState[brd.getWidth() * brd.getHeight()];
		CellState[] boardNew = new CellState[brd.getWidth() * brd.getHeight()];
		
		// Convert the board to be single dimensional
		int index = 0;
		for(int x = 0; x < brd.getWidth(); x++){
			for(int y = 0; y < brd.getHeight(); y++){
				board[index++] = brd.getCellState(x, y);
			}
		}
		
		// Rotate everything
		for(int index2 = 0; index2 < translations.length; index2++){
			boardNew[index2] = board[translations[index2]];
		}
		
		GameBoard newBoard = new GameBoard(brd.getWidth(), brd.getHeight());
		// Return the board to being in two dimensions
		index = 0;
		for(int x = 0; x < brd.getWidth(); x++){
			for(int y = 0; y < brd.getHeight(); y++){
				newBoard.setCellState(x, y, boardNew[index++]);
			}
		}
		
		return newBoard;
	}

	private String arrayToString(int[] scores) {
		StringBuilder str = new StringBuilder();
		str.append("[");
		for(int inte : scores){
			str.append(inte + ", ");
		}
		str.delete(str.length() - 2, str.length());
		str.append("]");
		return str.toString();
	}

	private int minimax(GameBoard board, int depth, CellState me) {
		if(Main.getWinner(board) == getMaxPlayer()){
			return 10 - depth;
		} else if(Main.getWinner(board) == getMinPlayer()){
			return depth - 10;
		} else {
			return 0;
		}
	}

	// ------------------------------------------------------------------------------------------------

	private GameBoard tryMove(GameBoard board, BoardLocation location, CellState currentTurn){
		GameBoard updatedBoard = new GameBoard(board);
		updatedBoard.setCellState(location.getX(), location.getY(), currentTurn);
		return updatedBoard;
	}
	
	private CellState getMaxPlayer(){
		return getPiece();
	}
	
	private CellState getMinPlayer(){
		return getOpponent(getPiece());
	}
	
	private GameBoard.CellState getOpponent(GameBoard.CellState me) {
		return (me.equals(GameBoard.CellState.X_PLAYER) ? GameBoard.CellState.O_PLAYER
				: GameBoard.CellState.X_PLAYER);
	}

	private String tabLength(int len) {
		StringBuilder str = new StringBuilder();
		for (int index = 0; index < len; index++) {
			str.append("\t");
		}
		return str.toString();
	}
}