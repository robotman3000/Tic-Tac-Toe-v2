package io.github.robotman3000.tictac.player;

import io.github.robotman3000.tictac.BoardLocation;
import io.github.robotman3000.tictac.GameBoard;
import io.github.robotman3000.tictac.GameBoard.CellState;

public class AssistedHuman extends Human {

	public AssistedHuman(String name, CellState peice) {
		super(name, peice);
	}

	@Override
	public BoardLocation doMove(GameBoard theBoard){
		System.out.println("Calculating the best move...");
		BoardLocation advisedMove = new Computer("", getPiece()).doMove(theBoard);
		System.out.println("The best move for you to use would be: " + advisedMove);
		return super.doMove(theBoard);
	}
}
