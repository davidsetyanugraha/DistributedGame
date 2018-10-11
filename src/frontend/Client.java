package frontend;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
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
  private ClientBoard clientBoard;

  // Constant Game State


  public Client(String name) throws RemoteException {
    this.name = name;
  }

  public boolean isGameRunning() throws RemoteException {
    return remoteGame.isGameRunning();
  }

  public JSONObject getJsonObject() throws JSONException {
    JSONObject jsonObj = new JSONObject(json);
    return jsonObj;
  }

  public void joinClientList(IRemoteGame remoteGame) throws RemoteException {
    String response;
    System.out.println("[Log] " + name + " has join client list.");

    if (this.json == null) {
      this.json = remoteGame.getJsonString();
    }

    try {
      this.remoteGame = remoteGame;
      response = remoteGame.registerGameClient(this);
    } catch (RemoteException e) {
      response = "Join Client list has been failed!";
      if (debug)
        response = response + " caused by: " + e.getMessage();
    }
  }

  public ArrayList<IClient> getAllClientList() throws RemoteException {
    return remoteGame.getAllClientList();
  }

  public ArrayList<IClient> getAllPlayerList() throws RemoteException {
    return remoteGame.getAllPlayerList();
  }

  public Boolean isLoginValid(String username) throws RemoteException, JSONException {
    return remoteGame.isLoginValid(username);
  }

  public void createNewGame(IRemoteGame remoteGame, ArrayList<String> clientPlayList)
      throws RemoteException {
    String response;
    System.out.println("[Log] " + name + " has created new game. contains: " + clientPlayList);

    if (this.json == null) {
      this.json = remoteGame.getJsonString();
    }

    try {
      response = remoteGame.startNewGame(clientPlayList);
    } catch (RemoteException e) {
      response = "create new Game has been failed!";
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

  public void performVoting() throws RemoteException {// first player run voting
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

  public void appendJsonLetter(int x, int y, String ch) throws RemoteException {
    System.out.println("APPEND JSON Letter: x = " + x + " , y = " + y + " , ch = " + ch);
    json = remoteGame.getJsonString();
    System.out.println("Initial JSON: " + json.toString());

    if (json != null) {
      JSONObject jsonObj;
      try {
        jsonObj = new JSONObject(json);
        JSONArray arrWord = jsonObj.getJSONArray("word");
        JSONObject obj = new JSONObject();
        obj.put("x", x);
        obj.put("y", y);
        obj.put("ch", ch);
        arrWord.put(obj);

        jsonObj.put("word", arrWord);

        this.json = jsonObj.toString();
      } catch (JSONException e) {
        e.printStackTrace();
      }

    }

    System.out.println("Final JSON = " + json.toString());
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
    System.out.println("renderVotingSystem: " + words);

    // render voting system
    this.clientBoard.renderVotingSystem(words);
  }

  @Override
  public void createNewBoard() throws RemoteException {
    System.out.println("Open new Board!");
    this.clientBoard = new ClientBoard(this);
    clientBoard.frmClient.setVisible(true);
  }

  @Override
  public void renderBoardSystem() throws RemoteException {
    try {
      this.json = remoteGame.getJsonString();
      checkState();
      this.clientBoard.renderBasedOnJson(this.json, this.currentState);
      System.out.println("RenderBoardSystem: " + this.json);
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void checkState() throws JSONException, RemoteException {

    JSONObject jsonObject = new JSONObject(json); // JSON Object to store the json file
    JSONArray playerArray = jsonObject.getJSONArray("player");
    JSONArray newPlayerArray = new JSONArray(); // New Player Array for rewrite
    JSONObject playerObject; // JSON Object to store a player's JSON details

    for (int i = 0; i < playerArray.length(); i++) {
      playerObject = playerArray.getJSONObject(i);
      /* Change the current player's turn to false */
      if (playerObject.get("username").equals(this.getUniqueName())) {
        if (playerObject.get("turn").equals(true)) {
          this.setCurrentState(STATE_INSERTION);
        } else {
          this.setCurrentState(STATE_WAIT);
        }
      }
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
