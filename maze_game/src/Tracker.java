import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface Tracker extends Remote {
	// when player first joins the game, need to call Tracker's join()	
	public Vector<GameInterface> join(GameInterface p) throws RemoteException;
    
    public Vector<GameInterface> getPlayerList() throws RemoteException;
    
    public int getSize() throws RemoteException;
    
    public int getTreasureNum() throws RemoteException;
    
    // when primary or backup server crashes, need to refresh Tracker's player list
    public void refresh(Vector<GameInterface> players) throws RemoteException;
   
    public void remove(int idx) throws RemoteException;
}
