package maze_game;

import java.util.Random;
import java.util.Vector;

public class GameState {

	String [][] maze;
	int n;
	int k;
	Vector<Player> listOfPlayers;
	
	public GameState() {
		
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
	
	public synchronized boolean addPlayerInCell(int x, int y, Player p) {
		if (maze[x][y] == null) {
			maze[x][y] = p.getPlayerId();
			return true;
		}
		return false;
	}
	
	/*
	 * Place a player's id in the maze, call this when a new player joins the game
	 * */
	public synchronized void addNewPlayer(Player p) {
		Random r = new Random();
		while (!this.addPlayerInCell(r.nextInt(n), r.nextInt(n), p)) {
			System.out.println("finding a new location for the new player" + p.getPlayerId());
		}
	}
	
	public synchronized GameState movePlayer(Player p, Character direction) {
		int x = p.getPosition().getX();
		int y = p.getPosition().getY();
		
		int newX, newY;
		
		switch (direction){
		case '1':
			newX = x;
			newY = y - 1;
			move(newX, newY);
			break;
		case '2':
			newX = x + 1;
			newY = y;
			move(newX, newY);
			break;
		case '3':
			newX = x;
			newY = y + 1;
			move(newX, newY);
			break;
		case '4':
			newX = x - 1;
			newY = y;
			move(newX, newY);
			break;
		case '9':
			break;
		}

		return this;
	}
	
	private synchronized void move(int x, int y) {
		
	}
	
	public synchronized void collectTreasure() {
		
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
