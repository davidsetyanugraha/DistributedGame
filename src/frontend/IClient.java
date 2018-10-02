package frontend;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote {
  void getWord(final String json, String nextPlayerName) throws RemoteException;

  void getVote(final boolean accept) throws RemoteException;

  void getPass(final String playerName) throws RemoteException;

  void addLetter() throws RemoteException;

  void renderVotingSystem(final String[] words) throws RemoteException;

  void renderBoardSystem() throws RemoteException;

  void getGeneralMessage(final String message) throws RemoteException;

  String getUniqueName() throws RemoteException;
}
