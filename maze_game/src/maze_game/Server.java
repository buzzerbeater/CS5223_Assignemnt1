package maze_game;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
	
public class Server implements Hello {
	
    public Server() {}

    public String sayHello() {
	return "Hello, world!";
    }
	
    public static void main(String args[]) {
	Hello stub = null;
	Registry registry = null;
	
	try {
	    Server obj = new Server();
	    stub = (Hello) UnicastRemoteObject.exportObject(obj, 0);
//	    registry = LocateRegistry.getRegistry();
	    registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
	    registry.rebind("Hello", stub);

	    System.err.println("Server ready");
	} catch (Exception e) {
	    try{
		registry.unbind("Hello");
		registry.bind("Hello",stub);
	    	System.err.println("Server ready");
	    }catch(Exception ee){
		System.err.println("Server exception: " + ee.toString());
	    	ee.printStackTrace();
	    }
	}
    }
}