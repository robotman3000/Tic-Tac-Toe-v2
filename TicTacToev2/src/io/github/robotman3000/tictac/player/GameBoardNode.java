package io.github.robotman3000.tictac.player;

import java.util.ArrayList;
import java.util.List;

import io.github.robotman3000.tictac.BoardLocation;
import io.github.robotman3000.tictac.GameBoard;

public class GameBoardNode {
	
	GameBoard.CellState changedValue = GameBoard.CellState.UNCLAIMED;
	GameBoard board;
	GameBoardNode parent = null;
	List<GameBoardNode> children = new ArrayList<>();
	BoardLocation move = null;
	private int endMoves = 9;
	
	public GameBoardNode(GameBoard board){
		this.board = board;
	}
	
	protected List<GameBoardNode> getChildren(){
		return children;
	}
	
	protected void addChild(GameBoardNode node){
		children.add(node);
	}

	public void setParent(GameBoardNode node){
		this.parent = node;
	}

	public void setChangedMove(BoardLocation move) {
		this.move = move;
	}

	public void setMovesToEnd(int i) {
		this.endMoves  = i;
	}
	
	public int getEndMoves(){
		return endMoves;
	}
}
