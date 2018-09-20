package maze_game;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

public class Game implements PlayerRemote{
	PlayerRemote primaryServer;
	PlayerRemote backupServer;
	PlayerRemote currentPlayer;
	PlayerRemote stub = null;
	Tracker tracker;
	GameState gameState;
	
	public Game() {
	}
	
	public void Initialize(String ipAddress, String playerId) {
		//currentPlayer = new Player(playerId);
		//String ipAddress = (args.length < 1) ? null : args[0];
		try {
		stub = (PlayerRemote) UnicastRemoteObject.exportObject(this, 0);
		Registry registry = LocateRegistry.getRegistry(ipAddress);
	    tracker = (Tracker) registry.lookup("Tracker");
	    System.out.println(tracker.getSize());
	    Vector<PlayerRemote> PlayerList = tracker.join(stub);
	    System.out.println(PlayerList.size());
	    //primaryServer = getPrimaryServer(PlayerList);
	    //myGameState = primaryServer.(playerId);
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
	 public void takeMove(int option) {
		
	}
	
	public static void main(String args[]) {
	Game game = new Game();
	game.Initialize(null, "id");
	}
	

}
