package io.github.robotman3000.tictac.player;

import io.github.robotman3000.tictac.BoardLocation;
import io.github.robotman3000.tictac.GameBoard;
import io.github.robotman3000.tictac.GameBoard.CellState;
import io.github.robotman3000.tictac.Main;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GameBoardNode {

	private GameBoardNode parent = null;
	private final Set<GameBoardNode> children = new HashSet<>();
	private final GameBoard node;

	private BoardLocation nodeXY = null;
	private int nodeZ = 0;
	private int score = 0;

	public GameBoardNode(GameBoardNode parent, GameBoard node, BoardLocation nodeXY, int nodeZ) {
		this.parent = parent;
		this.node = node;
		this.nodeXY = nodeXY;
		this.nodeZ = nodeZ;
	}

	public GameBoardNode getParent() {
		return parent;
	}

	public Set<GameBoardNode> getChildren() {
		return Collections.unmodifiableSet(children);
	}

	public boolean addChild(GameBoardNode child) {
		return children.add(child);
	}

	public boolean removeChild(GameBoardNode child) {
		return children.remove(child);
	}

	public GameBoard getNode() {
		return node;
	}

	public BoardLocation getNodeXY() {
		return nodeXY;
	}

	public int getNodeZ() {
		return nodeZ;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeXY == null) ? 0 : nodeXY.hashCode());
		result = prime * result + nodeZ;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof GameBoardNode)) {
			return false;
		}
		GameBoardNode other = (GameBoardNode) obj;
		if (nodeXY == null) {
			if (other.nodeXY != null) {
				return false;
			}
		} else if (!nodeXY.equals(other.nodeXY)) {
			return false;
		}
		if (nodeZ != other.nodeZ) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "GameBoardNode [node=" + node + ", nodeXY=" + nodeXY + ", nodeZ=" + nodeZ + ", score=" + score + "]";
	}

	public int getChildrenTotal() {
		return children.size();
	}

	public int getScore(CellState perspective, boolean maxormin) {
		// If I represent an ended game return our score
		if(Main.gameWon(getNode()) || Main.getOpenMoves(getNode()).isEmpty()){
			return Computer.minimax(getNode(), getNodeZ(), perspective);
		}
		
		// Otherwise ask all of my children what their scores are
		// and return the one that makes me look the best
		int[] scores = new int[getChildrenTotal()];
		int index = 0;
		for(GameBoardNode child : getChildren()){
			scores[index] = child.getScore(perspective, !maxormin);
			index++;
		}
		
		// Sort the arrays values from least to greatest
		Arrays.sort(scores);
		//System.out.println(maxormin + " " + getNodeZ() + " " + Computer.arrayToString(scores) + " = " + scores[(maxormin ? scores.length - 1 : 0)]);
		// If return max use end of the array; If use min return start of the array
		return scores[(maxormin ? scores.length - 1 : 0)];
	}

}
