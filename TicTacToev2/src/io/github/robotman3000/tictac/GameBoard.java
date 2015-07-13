package io.github.robotman3000.tictac;

public class GameBoard {
	public enum CellState {
		UNCLAIMED(" "),
		X_PLAYER("X"),
		O_PLAYER("O");
		
		private String toString = "";
		
		private CellState(String toStringStr){
			this.toString = toStringStr;
		}
		
		public String toString(){
			return toString;
		}
	}
	
	private CellState[][] board;
	
	public GameBoard(int x, int y){
		board = new CellState[x][y];
		
		for(int indexX = 0; indexX > x; indexX++){
			for(int indexY = 0; indexY > x; indexY++){
				board[indexX][indexY] = CellState.UNCLAIMED;
			}
		}
	}
	
	public CellState getState(int x, int y){
		if(isValid(x, y)){
			return board[x][y];
		}
		return CellState.UNCLAIMED; // Lets avoid null if we can
	}
	
	protected void setLocation(int x, int y, CellState value){
		if(isValid(x, y)){
			board[x][y] = value;
		}
	}
	
	private boolean isValid(int x, int y){
		return (x < board.length && y < board[x].length ? true : false);
	}
}
