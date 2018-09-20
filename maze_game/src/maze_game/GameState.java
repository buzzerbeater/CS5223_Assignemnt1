package maze_game;

import java.util.Random;
import java.util.Vector;

public class GameState {

	String [][] maze;
	int n;
	int k;
	Vector<Player> listOfPlayers;
	
	public GameState(int n, int k) {
		this.n = n;
		this.k = k;
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
	
	public boolean addNewTreasure(int x, int y) {
		if (maze[x][y] == null) {
			maze[x][y] = "*";
			return true;
		}
		else 
			return false;
	}
	
	public synchronized boolean addPlayerInCell(int x, int y, String playerId) {
		if (maze[x][y] == null) {
			maze[x][y] = playerId;
			return true;
		}
		return false;
	}
	
	/*
	 * Place a player's id in the maze, call this when a new player joins the game
	 * */
	public synchronized GameState addNewPlayer(String playerId) {
		Random r = new Random();
		while (!this.addPlayerInCell(r.nextInt(n), r.nextInt(n), playerId)) {
			System.out.println("finding a new location for the new player" + playerId);
		}
		return this;
	}

	public synchronized boolean movePlayer(int newX, int newY, Player p) {
		String playerId = p.getPlayerId();
	    if (maze[newX][newY] == null) {
	    	maze[newX][newY] = playerId;
	    	p.getPosition().setXY(newX, newY);
	    	return true;
	    }else if (maze[newX][newY] == "*") {
	    	collectTreasure(p);
	    	maze[newX][newY] = playerId;
	    	p.getPosition().setXY(newX, newY);
	    	return true;
	    }
	    return false;
	}
	
	private synchronized boolean move(int newX, int newY, Player p) {
		
		int x = p.getPosition().getX();
		int y = p.getPosition().getY();
		
	    if(newX < 0 || newX > n-1 || newY < 0 || newY > n-1) {
	        return false;
	      }
	    
	    if(this.movePlayer(newX, newY,p)) {
	    	removeMazeCell(x,y);
	    	return true;
	    }
	    return false;
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
}
