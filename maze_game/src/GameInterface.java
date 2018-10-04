import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface GameInterface extends Remote {
	//GameInterface findPrimary(Vector<GameInterface> PlayerList) throws RemoteException;
	public GameInterface findPrimary() throws RemoteException;
	
	public GameState takeMove(int option) throws RemoteException;
	
	public GameInterface getPrimaryServer() throws RemoteException;
	
	public GameState addNewPlayer(String playerId) throws RemoteException;
	
	public GameState takeMoveServer(int option, String playerId) throws RemoteException;
	
	public boolean ping() throws RemoteException;
	
	public void setBackup() throws RemoteException;
	
	public void setPrimary() throws RemoteException;
	
	public boolean isPrimary() throws RemoteException;
	
	public boolean isBackup() throws RemoteException;
	
	public void addToGame(GameInterface g) throws RemoteException;
	
	public void sync(GameState gs) throws RemoteException;
	
	public void sync() throws RemoteException;
	
	public void removePlayer (String playerId) throws RemoteException;
	
	public void syncState() throws RemoteException;
	
	public Player getCurrentPlayer() throws RemoteException;
	
	public void removePlayer(int idx) throws RemoteException;
	
	public void syncList(Vector<GameInterface> listOfGames) throws RemoteException;
	
	public GameState getGameState() throws RemoteException;
	
	public void contactTracker() throws RemoteException;
	
	public void promoteToBeBackup(GameInterface p) throws RemoteException;
}
