package frontend;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import backend.IRemoteGame;

public class Client extends UnicastRemoteObject implements IClient {
  // Runnable {

  private final IRemoteGame remoteGame;
  private String name = null;

  public Client(String name, IRemoteGame remoteGame) throws RemoteException {
    this.name = name;
    this.remoteGame = remoteGame;
    remoteGame.registerGameClient(this);
    remoteGame.broadcastGeneralMessage(name + " Has Connected");

    // test purpose
    remoteGame.broadcastPass(name);
    remoteGame.broadcastVote(true, "word");
    remoteGame.broadcastWord("{[json]}");
  }

  @Override
  public void getWord(String json) throws RemoteException {
    // TODO Auto-generated method stub
    System.out.println("getWord: " + json);
  }

  @Override
  public void getVote(boolean accept) throws RemoteException {
    // TODO Auto-generated method stub
    System.out.println("getVote: " + accept);
  }

  @Override
  public void getPass(String playerName) throws RemoteException {
    // TODO Auto-generated method stub
    System.out.println("getPass: " + playerName);
  }

  @Override
  public void getVotingSystem(String json) throws RemoteException {
    // TODO Auto-generated method stub
    System.out.println("getVotingSystem: " + json);
    remoteGame.broadcastPass("b");
  }

  @Override
  public void getBoard(String jsonCoordinates) throws RemoteException {
    // TODO Auto-generated method stub
    System.out.println("getBoard: " + jsonCoordinates);
  }

  @Override
  public void getGeneralMessage(String message) throws RemoteException {
    System.out.println("General Message: " + message);
  }

  // public void run() {
  // // while (true) {
  // // check();
  // // try {
  // // checkDisconnect();
  // // } catch (RemoteException e) {
  // // e.printStackTrace();
  // // }
  // // }
  // }
  //
  // private void check() {
  //
  // // essential
  // Boolean isWordAdded = true; // from GUI, ex: if (ClientGUI.isWordAdded) {
  // Boolean isVoteAdded = true; // from GUI, ex: if (ClientGUI.isVoteAdded) {
  // Boolean isPassAdded = true; // from GUI, ex: if (ClientGUI.isWordAdded) {
  //
  // // optional
  // Boolean isGeneralMessageAdded = true;
  //
  // try {
  // if (isWordAdded) {
  // System.out.println("1");
  // String json = "<COORDINATES + WORD>"; // JSON from GUI, String text =
  // // ClientGUI.field.getText();
  // remoteGame.broadcastWord(json);
  // isWordAdded = false; // from GUI, ClientGUI.isWordAdded = false;
  //
  // } else if (isVoteAdded) {
  // System.out.println("2");
  // Boolean accept = true; // Boolean passing from GUI
  // String word = "play";
  //
  // remoteGame.broadcastVote(accept, word);
  // isVoteAdded = false; // from GUI, ClientGUI.isWordAdded = false;
  //
  // } else if (isPassAdded) {
  // System.out.println("3");
  // remoteGame.broadcastPass(this.name);
  // isPassAdded = false; // from GUI, ClientGUI.isWordAdded = false;
  // } else if (isGeneralMessageAdded) {
  // System.out.println("4");
  // String message = "new message";
  // remoteGame.broadcastGeneralMessage(message);
  // isGeneralMessageAdded = false; // from GUI, ClientGUI.isWordAdded = false;
  // }
  // } catch (RemoteException e) {
  // e.printStackTrace();
  // }
  // }
  //
  // private void checkDisconnect() throws RemoteException {
  // Boolean disconnected = false; // from GUI, ex: if (ClientGUI.disconnected)
  //
  // if (disconnected) {
  // remoteGame.broadcastGeneralMessage(name + " Has Disconnected");
  // remoteGame.disconnectClient();
  // }
  // }
}
