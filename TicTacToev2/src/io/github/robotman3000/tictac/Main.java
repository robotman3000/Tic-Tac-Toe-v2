package io.github.robotman3000.tictac;

public class Main {

	public static void main(String[] args) {
		Main var = new Main();
		do { // Main gameloop
			var.drawScreen(currentTurn, false); // Print gameboard to screen
			returnedInput = var.getInput(aiIq, aiTurn, currentTurn, dualAi); // Get input from current player
			inputIsValid = var.checkInput(returnedInput, currentTurn); // Make sure the input is valid - the slot picked is aval and a real
																		// slot
			if (inputIsValid) {
				var.setBoard(returnedInput, currentTurn); // Change Board
			}
			currentTurn = var.setTurn(currentTurn, inputIsValid); // Set the turn
			if ((winner = var.checkForWinner()) != 0) { // Check game over
				break;
			} else {
				winner = var.checkGameTie();
			}
		} while (winner == 0);
		var.drawScreen(currentTurn, true);
		System.out.println("Game Over!");
		if (winner == 3) {
			System.out.println("Aww, it's a tie");
		} else {
			System.out.println("Player " + winner + " has won!");
		}
		System.exit(0);
	}

	private int setTurn(int currentTurn, boolean inputIsValid) {
		if (inputIsValid) {
			currentTurn = 3 - currentTurn;
			currentMove++;
		}
		return currentTurn;
	}

	private void drawScreen(int currentTurn2, boolean gamedone) {
		// System.out.println("a"+"\n"+"hi"); // new line test
		clearScreen(50);
		if (gamedone == true) {
			System.out.println("================================================================================");
			System.out.println();
			System.out.println((getPosition(0)) + " | " + (getPosition(1)) + " | " + (getPosition(2)));
			System.out.println("---------");
			System.out.println((getPosition(3)) + " | " + (getPosition(4)) + " | " + (getPosition(5)));
			System.out.println("---------");
			System.out.println((getPosition(6)) + " | " + (getPosition(7)) + " | " + (getPosition(8)));
			System.out.println();
			System.out.println("================================================================================");
		} else {
			System.out.println("================================================================================");
			System.out.println("Player " + currentTurn2 + "'s turn");
			System.out.println();
			System.out.println("================================================================================");
			System.out.println();
			System.out.println();
			System.out.println(" Slot Key");
			System.out.println((getPosition(0)) + " | " + (getPosition(1)) + " | " + (getPosition(2)) + " 0 | 1 | 2");
			System.out.println("--------- ---------");
			System.out.println((getPosition(3)) + " | " + (getPosition(4)) + " | " + (getPosition(5)) + " 3 | 4 | 5");
			System.out.println("--------- ---------");
			System.out.println((getPosition(6)) + " | " + (getPosition(7)) + " | " + (getPosition(8)) + " 6 | 7 | 8");
			System.out.println();
			System.out.println();
			System.out.println("================================================================================");
		}
	}

	private boolean checkInput(int input, int currentTurn1) {
		boolean valid = false;
		if (gameBoard[input] == 0) {// value 0 means unclaimed
			valid = true;
		} else {
			valid = false;
			System.out.println("That slot has already been used");
		}
		return valid;
	}
	
	private int checkForWinner() {
		int winner = 0;
		loop1: for (int combo = 0; combo < 8; combo++) {
			if (winner != 0) {
				break loop1;
			}
			switch (combo) {
			case 0:
				winner = clogic.compareThree(gameBoard[0], gameBoard[1], gameBoard[2]);
				break;
			case 1:
				winner = clogic.compareThree(gameBoard[3], gameBoard[4], gameBoard[5]);
				break;
			case 2:
				winner = clogic.compareThree(gameBoard[6], gameBoard[7], gameBoard[8]);
				break;
			case 3:
				winner = clogic.compareThree(gameBoard[0], gameBoard[3], gameBoard[6]);
				break;
			case 4:
				winner = clogic.compareThree(gameBoard[1], gameBoard[4], gameBoard[7]);
				break;
			case 5:
				winner = clogic.compareThree(gameBoard[2], gameBoard[5], gameBoard[8]);
				break;
			case 6:
				winner = clogic.compareThree(gameBoard[0], gameBoard[4], gameBoard[8]);
				break;
			case 7:
				winner = clogic.compareThree(gameBoard[2], gameBoard[4], gameBoard[6]);
				break;
			}
		}
		return winner;
	}

	private int checkGameTie() {
		int isTie = 3;
		loop: for (int index = 0; index <= 8; index++) {
			if (gameBoard[index] == 0) {
				isTie = 0;
				break loop;
			}
		}
		return isTie;
	}

	private void clearScreen(int makeLines) {
		for (int count = 0; count <= makeLines; count++) {
			System.out.println();
		}
	}
}