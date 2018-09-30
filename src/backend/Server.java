package backend;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

  public static void main(String[] args) throws RemoteException, MalformedURLException {
    final int port = 1099;

    try {
      Registry registry = LocateRegistry.createRegistry(port);
      registry.bind("GameServer", new RemoteGame());

      System.out.println("GAME SERVER IS RUNNING!");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
