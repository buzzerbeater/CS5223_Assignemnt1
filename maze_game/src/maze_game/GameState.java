package maze_game;

import java.io.Serializable;
import java.util.Random;

public class GameState implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8081213284690149009L;
	String [][] maze;
	int n;
	int k;
	
	public GameState(int n, int k) {
		this.n = n;
		this.k = k;
		initialize();
	}
	
	public void initialize() {
		this.maze = new String[n][n];
		Random r = new Random();
		for (int i=0; i < k; i++) {
			int x = r.nextInt(n);
			int y = r.nextInt(n);
			if(!this.addNewTreasure(x, y)) {
				// failed to add treasure, no need to increment i
				i = i - 1;
			}
		}
	}
	
	public synchronized boolean addNewTreasure(int x, int y) {
		if (maze[x][y] == null) {
			maze[x][y] = "*";
			return true;
		}
		else 
			return false;
	}
	
	public synchronized boolean addPlayerInCell(int x, int y, Player p) {
		String playerId = p.getPlayerId();
		if (maze[x][y] == null) {
			maze[x][y] = playerId;
			return true;
		}
		return false;
	}
	
	/*
	 * Place a player's id in the maze, call this when a new player joins the game
	 * */
	public synchronized GameState addNewPlayer(Player p) {
		Random r = new Random();
		String playerId = p.getPlayerId();
		while (!this.addPlayerInCell(r.nextInt(n), r.nextInt(n), p)) {
			System.out.println("finding a new location for the new player" + playerId);
		}
		return this;
	}

	public synchronized boolean movePlayer(int x, int y, int newX, int newY, Player p) {
		String playerId = p.getPlayerId();
	    if (maze[newX][newY] == null) {
	    	maze[newX][newY] = playerId;
	    	p.getPosition().setXY(newX, newY);
	    	removeMazeCell(x,y);
	    	return true;
	    }else if (maze[newX][newY] == "*") {
	    	maze[newX][newY] = playerId;
	    	p.getPosition().setXY(newX, newY);
	    	removeMazeCell(x,y);
	    	collectTreasure(p);
	    	return true;
	    }
	    return false;
	}
	
	public synchronized boolean move(int newX, int newY, Player p) {
		
		int x = p.getPosition().getX();
		int y = p.getPosition().getY();
		
	    if(newX < 0 || newX > n-1 || newY < 0 || newY > n-1) {
	        return false;
	      }
	    return this.movePlayer(x, y, newX, newY, p);
	}
	
	public synchronized void collectTreasure(Player p) {
		p.addScore(1);
		Random r = new Random();
		while(!addNewTreasure(r.nextInt(n), r.nextInt(n))) {
			System.out.println("Generating a new treasure");
		}
	}
	
	public synchronized void removeMazeCell(int x, int y) {
		maze[x][y] = null;
	}
	
	/*
	 * remove player p from the current maze, change the position to null;
	 * */
	public synchronized void removePlayer(Player p) {
		int x = p.getPosition().getX();
		int y = p.getPosition().getY();
		this.removeMazeCell(x, y);
	}
	
	public String[][] getMaze(){
		return this.maze;
	}
	
	public int getN() {
		return this.n;
	}
	
	public int getK() {
		return this.k;
	}
}
