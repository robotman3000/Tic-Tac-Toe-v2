package io.github.robotman3000.tictac.player;

import io.github.robotman3000.tictac.BoardLocation;

public class WeightedBoardLocation extends BoardLocation {

	int weight = 0;
	
	public WeightedBoardLocation(BoardLocation move, int y) {
		super(move.getX(), move.getY());
		weight = y;
	}
	
	void setWeight(int newWeight){
		this.weight = newWeight;
	}
	
	public int getWeight(){
		return weight;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + weight;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof WeightedBoardLocation))
			return false;
		WeightedBoardLocation other = (WeightedBoardLocation) obj;
		if (weight != other.weight)
			return false;
		return true;
	}

	@Override
	public String toString(){
		return super.toString() + " Weight: " + weight;
	}
}
