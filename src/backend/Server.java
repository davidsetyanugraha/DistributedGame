package backend;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Creates an instance of the RemoteMath class and publishes it in the rmiregistry
 * 
 */
public class Server {

  public static void main(String[] args) throws RemoteException, MalformedURLException {
    final int port = 1099;

    try {
      Registry registry = LocateRegistry.createRegistry(port);
    } catch (Exception e) {
      e.printStackTrace();
    }

    Naming.rebind("GameServer", new RemoteGame());

    System.out.println("GAME SERVER IS RUNNING!");
  }
}
