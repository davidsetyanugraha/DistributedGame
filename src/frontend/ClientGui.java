package frontend;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.JOptionPane;
import backend.IRemoteGame;

public class ClientGui {

  public static void main(String[] args) {
    Registry registry;
    IRemoteGame remoteGame;
    try {
      registry = LocateRegistry.getRegistry("localhost");
      remoteGame = (IRemoteGame) registry.lookup("GameServer");

      Login login = new Login(remoteGame);
      login.setVisible(true);
    } catch (RemoteException | NotBoundException e) {
      JOptionPane.showMessageDialog(null, "Server is down try again later", "Error",
          JOptionPane.PLAIN_MESSAGE);
    }
  }

}
