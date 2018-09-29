package maze_game;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

public class Game implements GameInterface {
	//private static final long serialVersionUID = -6454747573584944020L;
	GameInterface primaryServer;
	GameInterface backupServer;
	Player currentPlayer;
	GameInterface stub = null;
	Tracker tracker;
	GameState gameState;
	int n, k;
	Vector<GameInterface> listOfGames;
	ScheduledExecutorService executorService;
	boolean isPrimary = false;
	boolean isBackup = false;
	public final Object gameStateLock = new Object();
	
	MazeGUI gui;
	
	private static final Logger LOGGER = Logger.getLogger(Game.class.getSimpleName());
	
	public Game() {
		listOfGames = new Vector<GameInterface>();
		executorService = Executors.newSingleThreadScheduledExecutor();
	}
	
	  private static void initLogger() {
		    LOGGER.setUseParentHandlers(false);
		    LogFormatter formatter = new LogFormatter();
		    ConsoleHandler handler = new ConsoleHandler();
		    handler.setFormatter(formatter);
		    LOGGER.addHandler(handler);
		  }

	
	public void init(String ipAddress, int port, String playerId) {
		LOGGER.info("Game init -----------------------");
		currentPlayer = new Player(playerId);
		try {
			//System.setProperty("java.rmi.server.hostname","192.168.1.124");
			stub = (GameInterface) UnicastRemoteObject.exportObject(this, 0);
			Registry registry = LocateRegistry.getRegistry();
		    tracker = (Tracker) registry.lookup("Tracker");
		    n = tracker.getSize();
		    k = tracker.getTreasureNum();
		    try {
		    	contactTracker();
			}catch (Exception e) {
				LOGGER.info("Why you -----------------------");
				TimeUnit.MILLISECONDS.sleep(500);
				try {
					contactTracker();
				}catch (Exception e2) {
					LOGGER.info("Why you again?-----------------------");
					TimeUnit.MILLISECONDS.sleep(500);
					contactTracker();
				}
			}
		    
		    executorService.scheduleAtFixedRate(new KeepAlive(), 0, 500, TimeUnit.MILLISECONDS);
			} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void initialize() throws InterruptedException, RemoteException {
		LOGGER.info("Game initialize -----------------------");
		if(this.isPrimary) {
			gameState = new GameState(n, k);
		}
		//synchronized (this) {
		
		LOGGER.info("In initalize, primary is ----------" + this.primaryServer.getCurrentPlayer().getPlayerId());
		//this.gameState = primaryServer.addToGame(this);
		primaryServer.addToGame(this);
		this.gameState = primaryServer.getGameState();
		/**
		try {
			this.gameState = primaryServer.addToGame(this);
		}catch (Exception e) {
			LOGGER.info("Sleeping -----------------------");
			TimeUnit.MILLISECONDS.sleep(500);
			try {
				this.gameState = primaryServer.addToGame(this);
			}catch (Exception e2) {
				LOGGER.info("Sleeping AGAIN-----------------------");
				TimeUnit.MILLISECONDS.sleep(500);
				this.contactTracker();
				this.gameState = primaryServer.addToGame(this);
			}
		}
		**/
	    this.gameState.printMaze();
	    LOGGER.info("Game Initialization done -----------------------");
		//}
	}
	
	public void addToGame(GameInterface g) throws RemoteException {

		LOGGER.info("Add To Game -----------------------");
		LOGGER.info("Size of listOfGames is "+this.listOfGames.size());
		/**
		try {
			this.backupServer.ping();
		}catch(Exception e) {
			LOGGER.info("Assigning " + g.getCurrentPlayer().getPlayerId() + " to be backup");
			this.promoteToBeBackup(g);
			this.backupServer = g;
		}
		**/
		synchronized(gameStateLock) {
			if (this.listOfGames.size()==1) {
				LOGGER.info("Assigning " + g.getCurrentPlayer().getPlayerId() + " to be backup");
				this.promoteToBeBackup(g);
				this.backupServer = g;
			}
		
		
		LOGGER.info("Primary Server, addToGame Player"+ g.getCurrentPlayer().getPlayerId());
		
		this.listOfGames.add(g);

		refreshTracker(listOfGames);
		this.gameState = primaryServer.addNewPlayer(g.getCurrentPlayer().getPlayerId());
		this.primaryServer.syncState();
		}
		//return this.gameState;
	}
	
	public GameInterface findPrimary() throws RemoteException {
		if(isPrimary()) return this;
		for (int i = 0; i < listOfGames.size(); i++) {
			try {
			if (listOfGames.get(i).isPrimary())
				return listOfGames.get(i);
			}catch (Exception e) {
				System.out.println("In findPrimary, exception "+i);
				//listOfGames.remove(i);
				//i--;
			}
		}
		this.isPrimary = true;
		
		return this;
	}
	
	public void contactTracker() throws RemoteException {
		LOGGER.info("Begin contactTracker -----------------------");
		LOGGER.info("Before Contact Tracker, Size is "+this.listOfGames.size());
		this.listOfGames = tracker.getPlayerList();
		LOGGER.info("After Contact Tracker, Size is "+this.listOfGames.size());
		this.primaryServer = this.findPrimary();
		//for (int i=0;i<this.listOfGames.size();i++) {
		//	LOGGER.info("After contacting Tracker: position "+ i + " is " + 
		//			this.listOfGames.get(i).getCurrentPlayer().getPlayerId());
		//}
		LOGGER.info("After contacting Tracker: primary is " + 
				this.primaryServer.getCurrentPlayer().getPlayerId());
		LOGGER.info("End contactTracker -----------------------");
	}
	
	public  GameState addNewPlayer(String playerId) {
		//synchronized (gameStateLock) {
		if(this.gameState == null) {
			LOGGER.info("gameState is NULL -----------------------");
		}
		this.gameState.addNewPlayer(playerId);
		return this.gameState;

	}

	@SuppressWarnings("resource")
	public void play(MazeGUI gui) {
		
		Scanner scanner = new Scanner(System.in);
		String input;
		int option;

	    while (true) {
	        try {
	          input = scanner.nextLine();
	          if (input.equals("0")||input.equals("1")||input.equals("2")||input.equals("3")||input.equals("4")||input.equals("9")) {
	        	  option = Integer.parseInt(input);
		          if (option == 9) {
		        	  this.primaryServer.removePlayer(this.currentPlayer.getPlayerId());
		        	  System.exit(0);
		          }
		          LOGGER.info("Input is " + input);
		          //LOGGER.info("in method play(), Primary Server now is "+ primaryServer.getCurrentPlayer().getPlayerId());

		          this.gameState = this.takeMove(option);
		          //}
		          System.out.println("----- in play() -----");
		          this.gameState.printMaze();
		          this.gameState.printScore();
		          gui.updatePanels(this.gameState);
	          }
	          //primaryServer = findPrimary(listOfGames);     
	         }catch (Exception e) {
	          //return;
	        }
	    }
	}
	
	public GameState takeMove(int option) throws RemoteException {
			//System.out.println("Taking Move, primary server is " + this.primaryServer.getCurrentPlayer().getPlayerId());
			//System.out.println("Taking Move, current player is " + this.currentPlayer.getPlayerId());
			/**
			if(this.isPrimary()) {
				//return this.takeMoveServer(option, this.currentPlayer.getPlayerId());
				this.gameState.move(option, this.currentPlayer.getPlayerId());
				this.syncState();
				LOGGER.info("----- in takeMove -----");
				this.gameState.printMaze();
				return gameState;
			}
			**/
			try {
				if(this.primaryServer.ping()) {
					return this.primaryServer.takeMoveServer(option, this.currentPlayer.getPlayerId());
				}
			}catch (Exception e) {
				try {
					TimeUnit.MILLISECONDS.sleep(500);
					return this.primaryServer.takeMoveServer(option, this.currentPlayer.getPlayerId());
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			//System.out.println("Client exception: " + e.toString());
			//e.printStackTrace();
		}
			return this.primaryServer.takeMoveServer(option, this.currentPlayer.getPlayerId());
	}

	public synchronized GameState takeMoveServer(int option, String playerId) throws RemoteException {
		LOGGER.info("In Primary, takeMoveServer, option is "+ option +", playerId is "+ playerId);
		//synchronized (gameStateLock) {
		if (option == 9) {
			this.listOfGames.remove(this.indexOfPlayer(playerId));
			// change to this.remove(idx);
			this.refreshTracker(listOfGames);
		}
		LOGGER.info("In Primary, takeMoveServer " + this.primaryServer.getCurrentPlayer().getPlayerId() + "");
		//System.err.println("In Primary Server, takeMoveServer Method");
		this.gameState.move(option, playerId);
		this.syncState();
		this.gameState.printMaze();
		return gameState;
		//}
	}

	public void refreshTracker(Vector<GameInterface> playerList) throws RemoteException {
		tracker.refresh(playerList);
	}
	
	public void promoteToBeBackup(GameInterface p) throws RemoteException {
		try {
			TimeUnit.MILLISECONDS.sleep(400);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		p.setBackup();
		//p.contactTracker();
		this.backupServer = p;
		this.syncState();
		
	}
	
	public  void syncState() throws RemoteException {
		try {
			if(this.backupServer.ping()) {
				this.backupServer.syncList(listOfGames);
				this.backupServer.sync(this.gameState);
			}
		}catch (Exception e) {
			try {
				TimeUnit.MILLISECONDS.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		/**
		if(this.backupServer!=null) {
			//this.backupServer.sync();
			//this.gameState = this.primaryServer.syncGameState();
			this.backupServer.syncList(listOfGames);
			this.backupServer.sync(this.gameState);
			//System.out.println(" --- Maze After Sync State ---");
			//this.backupServer.getGameState().printMaze();
			//System.out.println(" ----------------");
		}
		**/

	}
	
	public GameState syncGameState() {
		return this.gameState;
	}
	
	public void sync() throws RemoteException {
		this.gameState = this.primaryServer.getGameState();
	}
	
	public  void sync(GameState gs) throws RemoteException {
		
		this.gameState = gs;
		
	}
	
	public GameState getGameState() throws RemoteException{
		return this.gameState;
	}
	
	public void syncList(Vector<GameInterface> listOfGames) {
		
		this.listOfGames = listOfGames;
		
	}
	
	public  void removePlayer (String playerId) throws RemoteException {
		synchronized (gameStateLock) {
		int idx = this.gameState.indexOfPlayer(playerId);
		listOfGames.remove(idx);
		this.refreshTracker(listOfGames);
		this.gameState.removePlayer(playerId);
		//this.syncState();
		}
	}
	
	public  void removePlayer(int idx) throws RemoteException {
		synchronized (gameStateLock) {
		listOfGames.remove(idx);
		//this.refreshTracker(listOfGames);
		this.gameState.removePlayer(idx);
		}
	}
	
	public GameInterface getPrimaryServer() { return primaryServer; }

	public boolean ping() { return true; }
	
	public Player getCurrentPlayer() { return this.currentPlayer; }
	
	public void setPrimary() { this.isPrimary = true; }
	
	public void setBackup() { 
		LOGGER.info("Becoming Backup");
		this.isBackup = true; }
	
	public boolean isBackup() throws RemoteException {
		return this.isBackup;
	}
	
	public boolean isPrimary() {
		return this.isPrimary;
	}
	
	private int indexOfPlayer (String playerId) throws RemoteException {
		int idx = 0;
		while (!this.listOfGames.get(idx).getCurrentPlayer().getPlayerId().equals(playerId)) { 
			idx ++; 
		}
		return idx;
	}
	
	public static void main(String args[]) throws RemoteException, InterruptedException {
		LOGGER.info("Game start -----------------------");
		initLogger();
	    String trackerIpAddress = args[0];
	    int portNumber = Integer.parseInt(args[1]);
	    String playerId = args[2];
		Game game = new Game();
		game.init(trackerIpAddress, portNumber, playerId);
		game.initialize();
		//game.play();
		MazeGUI gui = new MazeGUI(game.currentPlayer, game.gameState);
		game.play(gui);
	}
	
	private class KeepAlive implements Runnable {

		public KeepAlive() {}	
		
		public synchronized void run() {
			if(Game.this.isPrimary) {
				runPrimary();
			}else if (Game.this.isBackup) {
				runBackup();
			}else {
				try {
					runNormalPlayer();
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void runPrimary() {
			for (int i=0;i < listOfGames.size(); i++) {
				try {
					//LOGGER.info("in runPrimary(), Size is " + listOfGames.size());
					listOfGames.get(i).ping();
					//LOGGER.info("Primary Pinging " + listOfGames.get(i).getCurrentPlayer().getPlayerId());		
				} catch (Exception e) {
					try {
						LOGGER.info("In runPrimary, Player " + gameState.getListOfCurrentPlayer().get(i).getPlayerId() + " has crashed");
						Game.this.removePlayer(i);
						Game.this.refreshTracker(listOfGames);
						if(i == 1 && listOfGames.size() > 1) {
							Game.this.promoteToBeBackup(listOfGames.get(1));
							LOGGER.info("Assigning " + listOfGames.get(1).getCurrentPlayer().getPlayerId() + " to be backup");
						}
						Game.this.syncState();
						i--;
					} catch (RemoteException e1) {
						System.out.println("Exception in runPrimary() ");
					}
				}
			}
			
			/**
			try {
				Game.this.refreshTracker(listOfGames);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			**/
		}
		
		public void runBackup() {
			try {
				//System.out.println("running as backup");
				Game.this.primaryServer.ping();
			}catch (Exception e) {
				LOGGER.info("In runBackup, Primary Server crashed");
				try {
					Game.this.isPrimary = true;
					Game.this.isBackup = false;
					for (int i=0;i < listOfGames.size(); i++) {
						try {
							listOfGames.get(i).ping();
							//System.out.println("running as primary");
						} catch (Exception e1) {
							try {
								LOGGER.info("In runBackup, Player " + gameState.getListOfCurrentPlayer().get(i).getPlayerId() + " has crashed");
								Game.this.removePlayer(i);
								Game.this.refreshTracker(listOfGames);
								i--;
							}catch (RemoteException e2) {
								System.out.println("Exception in runBackup() ");
							}
						}
					}
					//Game.this.refreshTracker(listOfGames);
					//Game.this.listOfGames.remove(0);

					//Game.this.contactTracker();
					//System.out.println("----------");
					Game.this.primaryServer = Game.this;
					if(Game.this.gameState == null) {
						Game.this.gameState = new GameState(tracker.getSize(), tracker.getTreasureNum());
					}else {
						//Game.this.gameState.removePlayer(0);
					}
					if(listOfGames.size()>1) {
						promoteToBeBackup(listOfGames.get(1));
						LOGGER.info("Assigning " + listOfGames.get(1).getCurrentPlayer().getPlayerId() + " to be backup");
						//Game.this.syncState();
					}
					Game.this.syncState();
				} catch (Exception e1) {
					e1.printStackTrace();
					System.out.println("Exception in runBackup() ");
				}
			}
		}
		
		public void runNormalPlayer() throws InterruptedException, RemoteException {
			try {
				//System.out.println("running as normal");
				//System.out.println("Primary Server is " + primaryServer.getCurrentPlayer().getPlayerId());
				primaryServer.ping();
			}catch (Exception e) {
				LOGGER.info("In normal, Primary Server crashed");
				TimeUnit.MILLISECONDS.sleep(1000);
				//TimeUnit.MILLISECONDS.sleep(1000);
				Game.this.contactTracker();
				
				//System.out.println("----------");
				
				}

			}
		}
}
