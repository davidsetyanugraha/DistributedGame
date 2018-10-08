package frontend;

import java.rmi.Remote;
import java.rmi.RemoteException;
import org.json.JSONException;

public interface IClient extends Remote {
  void getVote(final boolean accept) throws RemoteException;

  void getPass(final String playerName) throws RemoteException;

  void addLetter() throws RemoteException;

  void renderVotingSystem(final String[] words) throws RemoteException;

  void renderBoardSystem() throws RemoteException;

  void getGeneralMessage(final String message) throws RemoteException;

  String getUniqueName() throws RemoteException;

  Boolean isLoginValid(String username) throws RemoteException, JSONException;

  void createNewBoard() throws RemoteException;
}
