package maze_game;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Vector;

public class Game implements PlayerRemote{
	PlayerRemote primaryServer;
	PlayerRemote backupServer;
	Player currentPlayer;
	PlayerRemote stub = null;
	Tracker tracker;
	GameState gameState;
	int n, k;
	
	public Game() {
	}
	
	public void Initialize(String ipAddress, String playerId) {
		currentPlayer = new Player(playerId);
		try {
		stub = (PlayerRemote) UnicastRemoteObject.exportObject(this, 0);
		Registry registry = LocateRegistry.getRegistry(ipAddress);
	    tracker = (Tracker) registry.lookup("Tracker");
	    //System.out.println(tracker.getSize());
	    n = tracker.getSize();
	    k = tracker.getTreasureNum();
	    Vector<PlayerRemote> PlayerList = tracker.join(stub);
	    System.out.println(PlayerList.size());
	    primaryServer = queryPrimaryServer(PlayerList);
	    if(primaryServer == stub) {
	    	gameState = new GameState(n, k);
	    	gameState.initialize();
	    }
	    gameState = primaryServer.addNewPlayer(currentPlayer);
	    System.out.println(Arrays.deepToString(gameState.maze));
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}
	
	public PlayerRemote queryPrimaryServer(Vector<PlayerRemote> PlayerList) {
		if(PlayerList.size() == 1) {
			return stub;
		}
		
		for(int i=0;i<PlayerList.size();i++) {
			try {
				if(PlayerList.get(i).getPrimaryServer() != null) {
					return PlayerList.get(i);
				}
			} catch (Exception e) {
				System.err.println("Client exception: " + e.toString());
				e.printStackTrace();
			}
			
		}
		return null;
	}
	
	@Override
	public PlayerRemote getPrimaryServer() {
		return primaryServer;
	}
	
	@Override
	public GameState addNewPlayer(Player player) {
		gameState.addNewPlayer(player);
		return gameState;
	}

	
	@Override
	 public void takeMove(int option) {
		try {
			gameState = primaryServer.takeMoveServer(option);
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
		
	}
	
	@Override
	public GameState takeMoveServer(int option) {
		return gameState;
	}
	
	public static void main(String args[]) {
	Game game = new Game();
	game.Initialize(null, "sh");
	}
	

}
