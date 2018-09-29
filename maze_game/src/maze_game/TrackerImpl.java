package maze_game;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

public class TrackerImpl implements Tracker {
	Vector<GameInterface> playerList;
	int n;
	int k;
	Hashtable<String, GameInterface> listOfGames;
	public final Object gameStateLock = new Object();
	Vector<String> vec;
	private static final Logger LOGGER = Logger.getLogger(Game.class.getSimpleName());
	public TrackerImpl(int n, int k) {
		this.playerList = new Vector<GameInterface>();
		listOfGames = new Hashtable<String, GameInterface>();
		vec = new Vector<String>();
		this.n = n;
		this.k = k;
	}

	public synchronized Vector<GameInterface> join(GameInterface p) {
		this.playerList.add(p);
		return this.playerList;
	}

	public Hashtable<String, GameInterface> getListOfGames(){
		return this.listOfGames;
	}
	
	public Vector<GameInterface> getPlayerList() throws RemoteException {
		/*
		for (int i=0;i<this.playerList.size();i++) {
			System.out.println("In getPlayList() Tracker: position "+ i + " is " + 
					this.playerList.get(i).getCurrentPlayer().getPlayerId());
		}
		System.out.println("---------");
		*/
		
		//System.out.println("In getPlayList(), playerList size is " + this.playerList.size());
		//System.out.println("In getPlayList(), vec size is " + this.vec.size());
		return this.playerList;
		
		
	}
	
	@Override
	public  int getSize() {
		return this.n;
	}
	
	public  void remove(int idx) {
		//synchronized (gameStateLock) {
		this.playerList.remove(idx);
		//}
	}
	@Override
	public  int getTreasureNum() {
		return this.k;
	}
	
	  private static void initLogger() {
		    LOGGER.setUseParentHandlers(false);
		    LogFormatter formatter = new LogFormatter();
		    ConsoleHandler handler = new ConsoleHandler();
		    handler.setFormatter(formatter);
		    LOGGER.addHandler(handler);
			  }
	  
	public  void refresh(Vector<GameInterface> players) throws RemoteException {
		//synchronized (this) {
		LOGGER.info("Tracker refresh -----------------------");
		this.playerList = players;
		//vec = new Vector<String>();
		for (int i=0;i<this.playerList.size();i++) {
			try {
				LOGGER.info("Position "+ i +" is "+ this.playerList.get(i).getCurrentPlayer().getPlayerId());
			}catch (Exception e) {
				
			}
		}
		System.out.println("---------");
		//}
	}
	
	public static void main(String args[]) {
		initLogger();
		Tracker stub = null;
		Registry registry = null;
		@SuppressWarnings("unused")
		int portNumber = 1099;
		
		try {
			TrackerImpl obj = new TrackerImpl(10, 10);
		    stub = (Tracker) UnicastRemoteObject.exportObject(obj, 0);
		    // registry = LocateRegistry.getRegistry();
		    //registry = LocateRegistry.getRegistry("localhost", portNumber);
		    registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
		    //System.setProperty("java.rmi.server.hostname","192.168.1.124");
		    registry.rebind("Tracker", stub);
		    System.err.println("Tracker ready");
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
	    }
	}

}
