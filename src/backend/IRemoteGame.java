package backend;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import frontend.IClient;

public interface IRemoteGame extends Remote {

  public String registerGameClient(IClient client) throws RemoteException;

  public String getJsonString() throws RemoteException;

  public String broadcastGeneralMessage(String message) throws RemoteException;

  public String performVoting(String json) throws RemoteException;

  public String broadcastNewLetter(String json) throws RemoteException;

  public String broadcastVote(String name, boolean accept, String word) throws RemoteException;

  public String broadcastPass(String playerName) throws RemoteException;

  public String disconnectClient() throws RemoteException;

  public String startNewGame(ArrayList<String> clientPlayList) throws RemoteException;

  public ArrayList<IClient> getAllClientList() throws RemoteException;

  public boolean isGameRunning() throws RemoteException;

  public ArrayList<IClient> getAllPlayerList() throws RemoteException;

  public Boolean isLoginValid(String username) throws RemoteException;
  
  public boolean isPlayerLoggedIn(String username) throws RemoteException;

  public void appendJsonClient(String username, String password, String firstName, String lastName)
      throws RemoteException;

  public void resetJson() throws RemoteException;

  public void broadcastExit() throws RemoteException;
}
