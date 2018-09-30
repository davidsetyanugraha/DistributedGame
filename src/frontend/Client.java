package frontend;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import backend.IRemoteGame;

public class Client extends UnicastRemoteObject implements IClient {
  // Runnable {

  private IRemoteGame remoteGame;
  private String name = null;
  private boolean debug = true;

  public Client(String name) throws RemoteException {
    this.name = name;
  }

  public void joinGame(IRemoteGame remoteGame) {
    String response;
    System.out.println("[Log] " + name + " has joined game.");

    try {
      this.remoteGame = remoteGame;
      response = remoteGame.registerGameClient(this);
    } catch (RemoteException e) {
      response = "Join Game has been failed!";
      if (debug)
        response = response + " caused by: " + e.getMessage();
    }

    System.out.println(response);
  }

  public void sendMessage(String message) throws RemoteException {
    String response;
    System.out.println("[Log] " + name + " has sent message: [ " + message + " ]");

    try {
      response = remoteGame.broadcastGeneralMessage(message);
    } catch (RemoteException e) {
      response = "Send Message has been failed!";
      if (debug)
        response = response + " caused by: " + e.getMessage();
    }

    System.out.println(response);
  }

  public void pass() throws RemoteException {
    String response;
    System.out.println("[Log] " + name + " has chosen pass");

    try {
      response = remoteGame.broadcastPass(this.name);
    } catch (RemoteException e) {
      response = "Send Message has been failed!";
      if (debug)
        response = response + " caused by: " + e.getMessage();
    }

    System.out.println(response);
  }

  public void vote(Boolean accept, String word) throws RemoteException {
    String response;
    System.out.println("[Log] " + name + " has voted [ " + word + " ] : [ " + accept + " ] ");

    try {
      response = remoteGame.broadcastVote(accept, word);
    } catch (RemoteException e) {
      response = "Vote has been failed!";
      if (debug)
        response = response + " caused by: " + e.getMessage();
    }

    System.out.println(response);
  }

  public void addWord(String json) throws RemoteException {
    String response;
    System.out.println("[Log] " + name + " has added new word: [ " + json + " ] ");

    try {
      response = remoteGame.broadcastWord(json);
    } catch (RemoteException e) {
      response = "Add Word has been failed!";
      if (debug)
        response = response + " caused by: " + e.getMessage();
    }

    System.out.println(response);
  }

  public void logout() throws RemoteException {
    String response;
    System.out.println("[Log] " + name + " has logged out");

    try {
      response = remoteGame.disconnectClient();
    } catch (RemoteException e) {
      response = "Logout has been failed!";
      if (debug)
        response = response + " caused by: " + e.getMessage();
    }

    System.out.println(response);
  }

  /** These getter methods are called by RemoteGame */
  @Override
  public void getWord(String json) {
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
  }

  @Override
  public void getBoard(String jsonCoordinates) throws RemoteException {
    // TODO Auto-generated method stub
    System.out.println("getBoard: " + jsonCoordinates);
  }

  @Override
  public void getGeneralMessage(String message) throws RemoteException {
    // TODO Auto-generated method stub
    System.out.println("getGeneralMessage: " + message);
  }
}
