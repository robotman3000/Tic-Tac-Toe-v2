package io.github.robotman3000.tictac.player;

import io.github.robotman3000.tictac.BoardLocation;
import io.github.robotman3000.tictac.GameBoard;
import io.github.robotman3000.tictac.GameBoard.CellState;

public abstract class Player {
	
	protected String name;
	protected CellState peice;
	
	public Player(String name, CellState peice){
		this.name = name;
		this.peice = peice;
	}
	
	public abstract BoardLocation doMove(GameBoard theBoard);
	
	public String toString(){
		return name;
	}
}
