package maze_game;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

public class TrackerImpl implements Tracker {
	Vector<PlayerRemote> playerList;
	int n;
	int k;
	
	public TrackerImpl(int n, int k) {
		this.playerList = new Vector<PlayerRemote>();
		this.n = n;
		this.k = k;
	}

	@Override
	public Vector<PlayerRemote> join(PlayerRemote p) {
		this.playerList.add(p);
		return this.playerList;
	}

	@Override
	public int getSize() {
		return this.n;
	}

	@Override
	public int getTreasureNum() {
		return this.k;
	}

	@Override
	public void refresh(Vector<PlayerRemote> players) {
		this.playerList = players;
	}
	
	public static void main(String args[]) {
		Tracker stub = null;
		Registry registry = null;
		
		try {
			// TODO: get n and k from command line
			TrackerImpl obj = new TrackerImpl(5, 10);
		    stub = (Tracker) UnicastRemoteObject.exportObject(obj, 0);
		    // registry = LocateRegistry.getRegistry();
		    registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
		    registry.rebind("Tracker", stub);

		    System.err.println("Tracker ready");
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
	    }
	}

}
