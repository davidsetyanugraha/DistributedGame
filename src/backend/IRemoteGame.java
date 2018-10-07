package backend;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import frontend.IClient;

public interface IRemoteGame extends Remote {

  public String registerGameClient(IClient client) throws RemoteException;

  public String getJsonString() throws RemoteException;

  /** Client-> Server -> all Clients; return JSON success or failed */
  public String broadcastGeneralMessage(String message) throws RemoteException;

  /** Client-> Server -> all Clients; return JSON success or failed */
  public String performVoting(String json) throws RemoteException;

  /** Client-> Server -> all Clients; return JSON success or failed */
  public String broadcastNewLetter(String json) throws RemoteException;

  /** Client-> Server -> all Clients; return JSON success or failed */
  public String broadcastVote(boolean accept, String word) throws RemoteException;

  /** Client-> Server -> all Clients; return JSON success or failed */
  public String broadcastPass(String playerName) throws RemoteException;

  public String disconnectClient() throws RemoteException;

  public String startNewGame(ArrayList<String> clientPlayList) throws RemoteException;

  public ArrayList<IClient> getAllClientList() throws RemoteException;

  public boolean isGameRunning() throws RemoteException;
}
