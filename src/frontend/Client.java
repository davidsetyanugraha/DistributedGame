package frontend;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import backend.IRemoteGame;

public class Client extends UnicastRemoteObject implements IClient {

  private IRemoteGame remoteGame;
  private String name = null; // MUST UNIQUE!
  private boolean debug = true;
  private int currentState;
  private int score; // final score
  private String json = null;

  // Constant Game State
  public final int STATE_WAIT = 0;
  public final int STATE_INSERTION = 1;
  public final int STATE_VOTING = 2;

  public Client(String name) throws RemoteException {
    this.name = name;
  }

  public JSONObject getJsonObject() throws JSONException {
    JSONObject jsonObj = new JSONObject(json);
    return jsonObj;
  }

  public void joinGame(IRemoteGame remoteGame) throws RemoteException {
    String response;
    System.out.println("[Log] " + name + " has joined game.");

    if (this.json == null) {
      this.json = remoteGame.getJsonString();
    }

    try {
      this.remoteGame = remoteGame;
      response = remoteGame.registerGameClient(this);
    } catch (RemoteException e) {
      response = "Join Game has been failed!";
      if (debug)
        response = response + " caused by: " + e.getMessage();
    }
  }

  public void sendMessage(String message) throws RemoteException {
    String response;
    System.out.println("[Log] " + name + " has sent message: [ " + message + " ]");

    try {
      response = remoteGame.broadcastGeneralMessage(message);
      this.json = remoteGame.getJsonString();
    } catch (RemoteException e) {
      response = "Send Message has been failed!";
      if (debug)
        response = response + " caused by: " + e.getMessage();
    }
  }

  public void addLetter() throws RemoteException {
    String response;
    System.out.println("[Log] " + name + " has added letter: [ " + json + " ]");

    try {
      response = remoteGame.broadcastNewLetter(json).toString();
      this.json = remoteGame.getJsonString();
    } catch (RemoteException e) {
      response = "Add New Letter has been failed!";
      if (debug)
        response = response + " caused by: " + e.getMessage();
    }
  }

  public void pass() throws RemoteException {
    String response;
    System.out.println("[Log] " + name + " has chosen pass");

    try {
      response = remoteGame.broadcastPass(this.name);
      this.json = remoteGame.getJsonString();
    } catch (RemoteException e) {
      response = "Send Message has been failed!";
      if (debug)
        response = response + " caused by: " + e.getMessage();
    }
  }

  public void vote(Boolean accept, String word) throws RemoteException {
    String response;
    System.out.println("[Log] " + name + " has voted [ " + word + " ] : [ " + accept + " ] ");

    try {
      response = remoteGame.broadcastVote(accept, word);
      this.json = remoteGame.getJsonString();
    } catch (RemoteException e) {
      response = "Vote has been failed!";
      if (debug)
        response = response + " caused by: " + e.getMessage();
    }
  }

  public void performVoting(String json) throws RemoteException {// first player run voting
                                                                 // system
    String response;
    System.out.println("[Log] " + name + " proposed new word: [ " + json + " ] ");

    try {
      response = remoteGame.performVoting(json);
      this.json = remoteGame.getJsonString();
    } catch (RemoteException e) {
      response = "Word proposal has been failed!";
      if (debug)
        response = response + " caused by: " + e.getMessage();
    }
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

  public void appendJson(int x, int y, String ch) throws JSONException, RemoteException {
    System.out.println("APPEND JSON: x = " + x + " , y = " + y + " , ch = " + ch);
    json = remoteGame.getJsonString();

    if (json != null) {
      JSONObject jsonObj = new JSONObject(json);
      JSONArray arrWord = jsonObj.getJSONArray("word");
      JSONObject obj = new JSONObject();
      obj.put("x", x);
      obj.put("y", y);
      obj.put("ch", ch);
      arrWord.put(obj);

      JSONObject objScore = jsonObj.getJSONObject("score");
      objScore.put(this.getUniqueName(), 0);
      this.json = jsonObj.toString();
    }

    System.out.println("Final JSON = " + json.toString());
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
  public void renderVotingSystem(String[] words) throws RemoteException {
    this.json = remoteGame.getJsonString();
    this.setCurrentState(STATE_VOTING);
    System.out.println("renderVotingSystem: " + words.toString());

    // render voting system
    // ClientFrame.renderVotingSystem(words);

    /** after this point, voting will be arranged by pressing vote button : broadcastVote */
  }

  @Override
  public void renderBoardSystem() throws RemoteException {
    try {
      this.json = remoteGame.getJsonString();
      ClientFrame.renderBasedOnJson(this.json);
      System.out.println("RenderBoardSystem: " + this.json);
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
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
}
