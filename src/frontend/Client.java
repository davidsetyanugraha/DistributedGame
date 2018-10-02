package frontend;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import org.json.JSONException;
import backend.IRemoteGame;

public class Client extends UnicastRemoteObject implements IClient {
  // Runnable {

  private IRemoteGame remoteGame;
  private String name = null; // MUST UNIQUE!
  private boolean debug = true;
  private int currentState;
  private int score; // final score

  // Constant Game State
  public final int STATE_WAIT = 0;
  public final int STATE_INSERTION = 1;
  public final int STATE_VOTING = 2;

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

  public void setCurrentState(int state) {
    String status;

    if (state == STATE_INSERTION) {
      status = "INSERTION";
    } else if (state == STATE_VOTING) {
      status = "VOTING";
    } else {
      status = "WAIT";
    }

    System.out.println("[Log] " + name + " change State into: " + status);
    this.currentState = state;
  }

  public int getCurrentState() {
    return currentState;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public int getScore() {
    return score;
  }

  /**
   * These getter methods are called by RemoteGame
   * 
   * @throws RemoteException
   */
  @Override
  public void getWord(String json, String nextPlayerName) throws RemoteException {
    // TODO Auto-generated method stub
    System.out.println("getWord: " + json);
    if (nextPlayerName == name) {
      this.setCurrentState(STATE_INSERTION);
    } else {
      this.setCurrentState(STATE_WAIT);
    }
  }

  @Override
  public void getVote(boolean accept) throws RemoteException {
    // TODO Auto-generated method stub
    this.setCurrentState(STATE_WAIT);
    System.out.println("getVote: " + accept);
  }

  @Override
  public void getPass(String playerName) throws RemoteException {
    // TODO Auto-generated method stub
    this.setCurrentState(STATE_WAIT);
    System.out.println("getPass: " + playerName);
  }

  @Override
  public void getVotingSystem(String json) throws RemoteException {
    // TODO Auto-generated method stub
    this.setCurrentState(STATE_VOTING);
    System.out.println("getVotingSystem: " + json);

    try {
      ClientFrame.renderBasedOnJson(json);
    } catch (FileNotFoundException | JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  @Override
  public void getBoard(String jsonCoordinates) throws RemoteException {
    // TODO Auto-generated method stub
    System.out.println("getBoard: " + jsonCoordinates);
  }

  @Override
  public String getUniqueName() throws RemoteException {
    return this.name;
  }

  @Override
  public void getGeneralMessage(String message) throws RemoteException {
    // TODO Auto-generated method stub
    System.out.println("getGeneralMessage: " + message);
  }
  
  public ArrayList<String> getUserList() throws RemoteException {
	  return this.remoteGame.broadcastPlayerList();
  }
}
