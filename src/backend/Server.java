package backend;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Creates an instance of the RemoteMath class and publishes it in the rmiregistry
 * 
 */
public class Server {

  public static void main(String[] args) {
    final int port = 1099;
    Registry registry = null;

    try {
      IRemoteGame remoteGame = new RemoteGame();
      registry = LocateRegistry.createRegistry(port);
      registry.bind("game", remoteGame);
      System.out.println("Game Server Ready!");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
