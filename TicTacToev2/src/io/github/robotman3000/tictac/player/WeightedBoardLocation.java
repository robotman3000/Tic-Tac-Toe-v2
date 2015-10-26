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
	public boolean equals(Object obj){
		if(super.equals(obj)){
			if(obj instanceof WeightedBoardLocation){
				WeightedBoardLocation move = (WeightedBoardLocation) obj;
				if(move.getWeight() == this.weight){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String toString(){
		return super.toString() + " Weight: " + weight;
	}
}
