package backend;

import java.rmi.Remote;
import java.rmi.RemoteException;
import frontend.IClient;

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

  public void registerGameClient(IClient client) throws RemoteException;

  /** Client-> Server -> all Clients; return JSON success or failed */
  public String broadcastGeneralMessage(String message) throws RemoteException;

  /** Client-> Server -> all Clients; return JSON success or failed */
  public String broadcastWord(String json) throws RemoteException;

  /** Client-> Server -> all Clients; return JSON success or failed */
  public String broadcastVote(boolean accept, String word) throws RemoteException;

  /** Client-> Server -> all Clients; return JSON success or failed */
  public String broadcastPass(String playerName) throws RemoteException;

  public void disconnectClient() throws RemoteException;
}
