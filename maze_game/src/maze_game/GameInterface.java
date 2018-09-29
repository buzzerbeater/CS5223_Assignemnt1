package maze_game;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface GameInterface extends Remote {
	//GameInterface findPrimary(Vector<GameInterface> PlayerList) throws RemoteException;
	GameInterface findPrimary() throws RemoteException;
	GameState takeMove(int option) throws RemoteException;
	GameInterface getPrimaryServer() throws RemoteException;
	GameState addNewPlayer(String playerId) throws RemoteException;
	GameState takeMoveServer(int option, String playerId) throws RemoteException;
	boolean ping() throws RemoteException;
	void setBackup() throws RemoteException;
	void setPrimary() throws RemoteException;
	public boolean isPrimary() throws RemoteException;
	public boolean isBackup() throws RemoteException;
	void addToGame(GameInterface g) throws RemoteException;
	void sync(GameState gs) throws RemoteException;
	void sync() throws RemoteException;
	public void removePlayer (String playerId) throws RemoteException;
	void syncState() throws RemoteException;
	Player getCurrentPlayer() throws RemoteException;
	public void removePlayer(int idx) throws RemoteException;
	void syncList(Vector<GameInterface> listOfGames) throws RemoteException;
	GameState getGameState() throws RemoteException;
	void contactTracker() throws RemoteException;
	public GameState syncGameState() throws RemoteException;
	void promoteToBeBackup(GameInterface p) throws RemoteException;
}
