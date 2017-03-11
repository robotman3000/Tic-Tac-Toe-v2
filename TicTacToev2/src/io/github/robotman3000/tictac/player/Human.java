package io.github.robotman3000.tictac.player;

import java.util.Scanner;

import io.github.robotman3000.tictac.BoardLocation;
import io.github.robotman3000.tictac.GameBoard;
import io.github.robotman3000.tictac.GameBoard.CellState;

public class Human extends Player {

	Scanner in = new Scanner(System.in);
	
	public Human(String name, CellState peice) {
		super(name, peice);
	}

	@Override
	public BoardLocation doMove(GameBoard theBoard) {
		String input = "";
		System.out.println("Enter the slot you wish to change");
		//System.out.println("Slot 1-3 for x, Slot 8 is the bottom right");
		input = in.nextLine();
		String[] digits = input.split(",");
		if(digits.length == 2){
			try {
				int moveX = Integer.parseInt(digits[0].trim());
				int moveY = Integer.parseInt(digits[1].trim());
				return new BoardLocation(moveX, moveY);
			} catch (NumberFormatException e){
				e.printStackTrace();
			}
		}
		return null;
	}
}
