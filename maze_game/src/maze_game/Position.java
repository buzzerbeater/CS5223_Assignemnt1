package maze_game;

import java.io.Serializable;

public class Position implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -35690947783185053L;
	
	int x;
	int y;
	
	public Position() {}
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
