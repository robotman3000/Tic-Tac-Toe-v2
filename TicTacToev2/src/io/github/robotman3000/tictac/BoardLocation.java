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
	
	@Override
	public String toString(){
		return "X: " + x + " Y: " + y;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BoardLocation))
			return false;
		BoardLocation other = (BoardLocation) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
}
