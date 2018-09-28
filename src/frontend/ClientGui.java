package frontend;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import backend.IRemoteGame;

/**
 * This class retrieves a reference to the remote object from the RMI registry. It invokes the
 * methods on the remote object as if it was a local object of the type of the remote interface.
 *
 */
public class ClientGui {

  public static void main(String[] args)
      throws MalformedURLException, RemoteException, NotBoundException {
    new ClientGui();
    String chatServerURL = "rmi://localhost/GameServer";
    IRemoteGame remoteGame = (IRemoteGame) Naming.lookup(chatServerURL);
    System.out.println("Enter your username: ");
    Scanner scanner = new Scanner(System.in);
    String username = scanner.nextLine();
    new Client(username, remoteGame);

    // if we use thread
    // new Thread(new Client(username, remoteGame)).start();
  }
}
