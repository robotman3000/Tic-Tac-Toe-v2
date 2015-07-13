package io.github.robotman3000.tictac;

public class BoardLocation {

	private int x = 0;
	private int y = 0;
	
	public BoardLocation(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
}
