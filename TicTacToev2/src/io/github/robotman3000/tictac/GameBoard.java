package io.github.robotman3000.tictac;

public class GameBoard {
	public enum CellState {
		UNCLAIMED(" "),
		X_PLAYER("X"),
		O_PLAYER("O");
		
		private String playerName = "";
		
		private CellState(String toStringStr){
			this.playerName = toStringStr;
		}
		
		public String toString(){
			return playerName;
		}
	}
	
	private CellState[][] board;
	int height;
	
	public GameBoard(int x, int y){
		board = new CellState[x][y];
		height = y;
		for(int indexX = 0; indexX < x; indexX++){
			for(int indexY = 0; indexY < y; indexY++){
				board[indexX][indexY] = CellState.UNCLAIMED;
			}
		}
	}
	
	public GameBoard(GameBoard theBoard) {
		board = new CellState[theBoard.getWidth()][theBoard.getHeight()];
		height = theBoard.getHeight();
		for(int indexX = 0; indexX < theBoard.getWidth(); indexX++){
			for(int indexY = 0; indexY < theBoard.getHeight(); indexY++){
				board[indexX][indexY] = theBoard.getCellState(indexX, indexY);
			}
		}
	}

	public CellState getCellState(int x, int y){
		if(isValid(x, y)){
			return board[x][y];
		}
		return CellState.UNCLAIMED; // Lets avoid null if we can
	}
	
	public CellState getCellState(BoardLocation move) {
		return getCellState(move.getX(), move.getY());
	}
	
	public void setCellState(int x, int y, CellState value){
		if(isValid(x, y)){
			board[x][y] = value;
		}
	}
	
	private boolean isValid(int x, int y){
		return (x < board.length && y < board[x].length ? true : false);
	}

	public boolean isValid(BoardLocation move) {
		if(move == null){
			return false;
		}
		return isValid(move.getX(), move.getY());
	}
	
	public int getWidth(){
		return board.length;
	}
	
	public int getHeight(){
		return height;
	}
}
