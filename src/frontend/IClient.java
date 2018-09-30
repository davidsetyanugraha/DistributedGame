package frontend;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote {
  void getWord(final String json) throws RemoteException;

  void getVote(final boolean accept) throws RemoteException;

  void getPass(final String playerName) throws RemoteException;

  void getVotingSystem(final String json) throws RemoteException;

  void getBoard(final String jsonCoordinates) throws RemoteException;

  void getGeneralMessage(final String message) throws RemoteException;
}
