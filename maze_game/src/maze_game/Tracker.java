package maze_game;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface Tracker extends Remote {
	// when player first joins the game, need to call Tracker's join()	
    Vector<PlayerRemote> join(PlayerRemote p) throws RemoteException;
    
    int getSize() throws RemoteException;
    
    int getTreasureNum() throws RemoteException;
    
    // when primary or backup server crashes, need to refresh Tracker's player list
    void refresh(Vector<PlayerRemote> players) throws RemoteException;
}
