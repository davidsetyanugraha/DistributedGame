package backend;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.JOptionPane;

public class Server {

  public static void main(String[] args) throws RemoteException, MalformedURLException {
    final int port = 1099;

    try {
      Registry registry = LocateRegistry.createRegistry(port);
      registry.bind("GameServer", new RemoteGame());

      System.out.println("Game Server is ready!");
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null,
          "Port not available, try reconnecting with different port number", "Error",
          JOptionPane.PLAIN_MESSAGE);
    }
  }
}
