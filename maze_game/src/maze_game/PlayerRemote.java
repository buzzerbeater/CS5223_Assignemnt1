package maze_game;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PlayerRemote extends Remote {
    void takeMove(int option) throws RemoteException;
}
