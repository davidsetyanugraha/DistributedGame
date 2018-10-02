package backend;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import frontend.IClient;

public interface IRemoteGame extends Remote {

  /** list of Commands for method execute */
  public final static int COMMAND_GET_DETAILS = 0; // Check players online (new game), coordinates,
                                                   // How to sync the screen? check every 5 Sec!
  public final static int COMMAND_POST_WORD = 1; // Add new word

  public String registerGameClient(IClient client) throws RemoteException;

  /** Client-> Server -> all Clients; return JSON success or failed */
  public String broadcastGeneralMessage(String message) throws RemoteException;

  /** Client-> Server -> all Clients; return JSON success or failed */
  public String broadcastWord(String json) throws RemoteException;

  /** Client-> Server -> all Clients; return JSON success or failed */
  public String broadcastVote(boolean accept, String word) throws RemoteException;

  /** Client-> Server -> all Clients; return JSON success or failed */
  public String broadcastPass(String playerName) throws RemoteException;
  
  public ArrayList<String> broadcastPlayerList() throws RemoteException;

  public String disconnectClient() throws RemoteException;
}
