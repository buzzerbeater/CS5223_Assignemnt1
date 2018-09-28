package maze_game;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Vector;

public interface Tracker extends Remote {
	// when player first joins the game, need to call Tracker's join()	
    Vector<GameInterface> join(GameInterface p) throws RemoteException;
    
    Vector<GameInterface> getPlayerList() throws RemoteException;
    
    public Hashtable<String, GameInterface> getListOfGames() throws RemoteException;
    
    int getSize() throws RemoteException;
    
    int getTreasureNum() throws RemoteException;
    
    // when primary or backup server crashes, need to refresh Tracker's player list
    void refresh(Vector<GameInterface> players) throws RemoteException;
    public void remove(int idx) throws RemoteException;
}
