package maze_game;

import java.io.Serializable;
import java.util.Random;
import java.util.Vector;

public class GameState implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8081213284690149009L;
	public String [][] maze;
	int n;
	int k;
	Vector<Player> listOfCurrentPlayer;
	
	public GameState(int n, int k) {
		this.n = n;
		this.k = k;
		listOfCurrentPlayer = new Vector<Player>();
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
	
	public int indexOfPlayer(String playerId) {
		int idx = 0;
		while (!this.listOfCurrentPlayer.get(idx).getPlayerId().equals(playerId)) {
			//System.out.println("in GameState, indexOfPlayer");
			idx ++;
		}
		return idx;
	}
	
	public  boolean addNewTreasure(int x, int y) {
		synchronized(this) {
		if (maze[x][y] == null) {
			maze[x][y] = "*";
			return true;
		}
		else 
			return false;
	}
	}
	
	public  boolean addPlayerInCell(int x, int y, Player p) {
		
		String playerId = p.getPlayerId();
		if (maze[x][y] == null) {
			maze[x][y] = playerId;
			p.getPosition().setXY(x, y);
			return true;
		}
		return false;
	}
	
	public  GameState addNewPlayer(String playerId) {
		synchronized(this) {
		Random r = new Random();
		Player p = new Player(playerId);
		boolean proceed = true;
		while (proceed) {
			//System.out.println("trying to find a new location for the new player " + playerId);
			int x = r.nextInt(n);
			int y = r.nextInt(n);
			if (this.addPlayerInCell(x, y, p)){
				proceed = false;
			}
			p.getPosition().setXY(x, y);
		}
		this.listOfCurrentPlayer.add(p);
		return this;
	}
	}

	public boolean movePlayer(int x, int y, int newX, int newY, Player p) {
		String playerId = p.getPlayerId();
	    if (maze[newX][newY] == null) {
	    	maze[newX][newY] = playerId;
	    	p.getPosition().setXY(newX, newY);
	    	removeMazeCell(x,y);
	    	return true;
	    } else if (maze[newX][newY].equals("*")) {
	    	maze[newX][newY] = playerId;
	    	p.getPosition().setXY(newX, newY);
	    	removeMazeCell(x,y);
	    	collectTreasure(p);
	    	return true;
	    }
	    return false;
	}
	
	public synchronized boolean move(int option, String playerId) {
		synchronized(this) {
		//System.err.println(" GameState Class, Taking Move, player is " + playerId);
		int idx = indexOfPlayer(playerId);
		Player p = this.listOfCurrentPlayer.get(idx);
		int x = p.getPosition().getX();
		int y = p.getPosition().getY();
		int newX=0, newY=0;	
		switch (option){
		case 0:
			newX = x;
			newY = y;
			return false;
		case 1:
			newX = x;
			newY = y - 1;
			break;
		case 2:
			newX = x + 1;
			newY = y;
			break;
		case 3:
			newX = x;
			newY = y + 1;
			break;
		case 4:
			newX = x - 1;
			newY = y;
			break;
		case 9:
			this.removePlayer(playerId);
			return false;
		}
		
	    if(newX < 0 || newX > n-1 || newY < 0 || newY > n-1) {
	        return false;
	      }
	    return this.movePlayer(x, y, newX, newY, p);
		}
	}
	
	public  void collectTreasure(Player p) {
		p.addScore(1);
		Random r = new Random();
		while(!addNewTreasure(r.nextInt(n), r.nextInt(n))) {
			System.out.println("Generating a new treasure");
		}
	}
	
	public synchronized void removeMazeCell(int x, int y) {
		maze[x][y] = null;
	}
	
	public  void removePlayer(String playerId) {
		synchronized(this) {
		int idx = this.indexOfPlayer(playerId);
		Player p = this.listOfCurrentPlayer.get(idx);
		int x = p.getPosition().getX();
		int y = p.getPosition().getY();
		this.removeMazeCell(x, y);
		this.listOfCurrentPlayer.remove(idx);
		}
	}
	
	public synchronized void removePlayer(int idx) {
		Player p = this.listOfCurrentPlayer.get(idx);
		int x = p.getPosition().getX();
		int y = p.getPosition().getY();
		this.removeMazeCell(x, y);
		this.listOfCurrentPlayer.remove(idx);
	}
	
	public String[][] getMaze(){
		return this.maze;
	}
	
	public Vector<Player> getListOfCurrentPlayer(){
		return this.listOfCurrentPlayer;
	}
	
	public int getN() {
		return this.n;
	}
	
	public int getK() {
		return this.k;
	}
	
	public void printMaze() {
		for (int i=0; i<n;i++) {
			for (int j=0;j<n;j++) {
				if(maze[i][j]==null) {
					System.out.print(" - ");
				}else if(maze[i][j].equals("*")){
					System.out.print(" * ");
				}
				else
					System.out.print(" "+maze[i][j]);
				}
			System.out.println("");
			}
		System.out.println(" ------------ ");
			
	}
	
	public void printScore() {
		int size = this.listOfCurrentPlayer.size();
		for (int i = 0; i<size; i++) {
			System.out.print("[ " + this.listOfCurrentPlayer.get(i).playerId +": " + 
		    ""+this.listOfCurrentPlayer.get(i).getScore()+" ] ");
		}
		System.out.println("\n --------------- ");
	}

}
