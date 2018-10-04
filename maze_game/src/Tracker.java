import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

public class Tracker implements TrackerInterface {
	private Vector<GameInterface> playerList;
	private int n;
	private int k;
	public final Object gameStateLock = new Object();

	private static final Logger LOGGER = Logger.getLogger(Game.class.getSimpleName());
	
	public Tracker(int n, int k) {
		this.playerList = new Vector<GameInterface>();
		this.n = n;
		this.k = k;
	}

	public synchronized Vector<GameInterface> join(GameInterface p) {
		this.playerList.add(p);
		return this.playerList;
	}

	public Vector<GameInterface> getPlayerList() throws RemoteException { return this.playerList; }

	public  int getSize() {
		return this.n;
	}
	
	public  void remove(int idx) {
		//synchronized (gameStateLock) {
		this.playerList.remove(idx);
		//}
	}

	public  int getTreasureNum() {
		return this.k;
	}
	
	private static void initializeLogging() {
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
		initializeLogging();
		TrackerInterface stub = null;
		Registry registry = null;
		int portNumber = Integer.parseInt(args[0]);
		int n = Integer.parseInt(args[1]);
		int k = Integer.parseInt(args[2]);
		
		try {
			Tracker obj = new Tracker(n, k);
		    stub = (TrackerInterface) UnicastRemoteObject.exportObject(obj, 0);
		    //registry = LocateRegistry.getRegistry("192.168.1.124", portNumber);
		    //registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
		    registry = LocateRegistry.createRegistry(portNumber);
		    //System.setProperty("java.rmi.server.hostname","192.168.1.124");
		    registry.rebind("Tracker", stub);
		    System.err.println("Tracker ready");
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
	    }
	}

}
