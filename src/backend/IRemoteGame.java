package backend;

import java.rmi.Remote;

/**
 * RMI Remote interface - must be shared between client and server. All methods must throw
 * RemoteException. All parameters and return types must be either primitives or Serializable.
 * 
 * Any object that is a remote object must implement this interface. Only those methods specified in
 * a "remote interface" are available remotely.
 */
public interface IRemoteGame extends Remote {

  /** list of Commands for method execute */
  public final static int COMMAND_GET_DETAILS = 0; // Check players online (new game), coordinates,
                                                   // How to sync the screen? check every 5 Sec!
  public final static int COMMAND_POST_WORD = 1; // Add new word

  /** Client-> Server; Start Game; return JSON success or failed */
  public String start();

  /** Client-> Server; get Coordinates or Put word; return JSON success or failed */
  public String execute(int command, String Json);

  /** Client-> Server; return JSON success or failed */
  public String vote(boolean accept);

  /** Cclient-> Server; return JSON success or failed */
  public String pass();
}
