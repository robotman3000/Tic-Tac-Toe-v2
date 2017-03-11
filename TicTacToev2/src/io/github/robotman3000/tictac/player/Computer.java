package io.github.robotman3000.tictac.player;

import io.github.robotman3000.tictac.BoardLocation;
import io.github.robotman3000.tictac.GameBoard;
import io.github.robotman3000.tictac.GameBoard.CellState;
import io.github.robotman3000.tictac.Main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Computer extends Player {
	private int depth = 0;
	private Random rand = new Random();
	
	public Computer(String name, CellState peice) {
		super(name, peice);
	}

	@Override
	public BoardLocation doMove(GameBoard theBoard) {
		GameBoardNode root = new GameBoardNode(null, theBoard, null, depth);
		
		int depth2 = depth;
		makeGameTree(root, getMaxPlayer(), depth2);
		
		WeightedBoardLocation[] scores = new WeightedBoardLocation[root.getChildrenTotal()];
		int index = 0;
		for(GameBoardNode child : root.getChildren()){
			scores[index++] = new WeightedBoardLocation(child.getNodeXY(), child.getScore(getPiece(), false));
		}
		
		//saveGameTree(root);
		// We pick the move with the largest score
		WeightedBoardLocation bestMove = null;
		Set<WeightedBoardLocation> list = new HashSet<>();
		for(WeightedBoardLocation move : scores){
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
		
		
		depth--;
		List<WeightedBoardLocation> theList = new ArrayList<>(list);
		System.out.println("Moves: " + list);
		return theList.get(rand.nextInt(theList.size()));
	}
	
	@SuppressWarnings("unused")
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
	
	private void makeGameTree(GameBoardNode root, CellState currentTurn, int depth) {
		//TODO: Implement logic to remove similar board reflections and rotations
		List<BoardLocation> moves = Main.getOpenMoves(root.getNode());
		if(!(Main.gameWon(root.getNode()) || moves.isEmpty())){
			for(BoardLocation move : moves){
				GameBoard board = tryMove(root.getNode(), move, currentTurn);
				GameBoardNode child = new GameBoardNode(root, board, move, depth + 1);
				root.addChild(child);
				makeGameTree(child, getOpponent(currentTurn), depth + 1);
			}
		}
	}
	
	@SuppressWarnings("unused")
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

	public static String arrayToString(int[] scores) {
		StringBuilder str = new StringBuilder();
		str.append("[");
		for(int inte : scores){
			str.append(inte + ", ");
		}
		str.delete(str.length() - 2, str.length());
		str.append("]");
		return str.toString();
	}

	public static int minimax(GameBoard board, int depth, CellState me) {
		if(Main.getWinner(board) == me){
			return 10 - depth;
		} else if(Main.getWinner(board) == getOpponent(me)){
			return depth - 10;
		} else {
			return 0;
		}
		//return WinnerCalculator.calc(board, me);
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
	
	@SuppressWarnings("unused")
	private CellState getMinPlayer(){
		return getOpponent(getPiece());
	}
	
	public static GameBoard.CellState getOpponent(GameBoard.CellState me) {
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