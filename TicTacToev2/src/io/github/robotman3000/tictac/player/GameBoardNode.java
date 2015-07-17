package io.github.robotman3000.tictac.player;

import java.util.ArrayList;
import java.util.List;

import io.github.robotman3000.tictac.BoardLocation;
import io.github.robotman3000.tictac.GameBoard;

public class GameBoardNode {

	String name = "rootNode";
	String childOf = "rootNode";
	GameBoard.CellState changedValue = GameBoard.CellState.UNCLAIMED;
	GameBoard board;
	GameBoardNode parent = null;
	List<GameBoardNode> children = new ArrayList<>();
	BoardLocation move = null;
	
	public GameBoardNode(GameBoard board, String name){
		this.board = board;
		this.name = name;
	}
	
	protected List<GameBoardNode> getChildren(){
		return children;
	}
	
	protected void setChildOfName(String name){
		this.childOf = name;
	}
	
	public String getName(){
		return name;
	}
	
	public String getChildOf(){
		return childOf;
	}
	
	public void setParent(GameBoardNode node){
		this.parent = node;
	}

	public void setChangedMove(BoardLocation move) {
		this.move = move;
	}
}
