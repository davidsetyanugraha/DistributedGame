package frontend;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import backend.IRemoteGame;

public interface IClient extends Remote {

  public final int STATE_WAIT = 0;
  public final int STATE_INSERTION = 1;
  public final int STATE_VOTING_SHOW = 2;
  public final int STATE_VOTING_WAIT = 3;
  public final int STATE_END = 4;

  void getPass(final String playerName) throws RemoteException;

  void addLetter() throws RemoteException;

  void vote(Boolean accept, String word) throws RemoteException;

  void pass() throws RemoteException;

  void changeStateIntoVotingShow(final ArrayList<String> words) throws RemoteException;

  void changeStateIntoVotingWait(boolean accept) throws RemoteException;

  void renderBoardSystem() throws RemoteException;

  void getGeneralMessage(final String message) throws RemoteException;

  String getUniqueName() throws RemoteException;

  public ArrayList<IClient> getAllClientList() throws RemoteException;

  public void createNewGame(IRemoteGame remoteGame, ArrayList<String> clientPlayList)
      throws RemoteException;

  void createNewBoard() throws RemoteException;

  void appendJsonLetter(int x, int y, String ch) throws RemoteException;

  public void performVoting() throws RemoteException;

  public void changeStateIntoWait() throws RemoteException;

  public void exitGame() throws RemoteException;

  public int getCurrentState() throws RemoteException;

  public void changeStateIntoEndGame() throws RemoteException;

  public void exit() throws RemoteException;

}
