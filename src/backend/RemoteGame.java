package backend;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import frontend.IClient;

public class RemoteGame extends UnicastRemoteObject implements IRemoteGame {

  private final ArrayList<IClient> clients;
  private ArrayList<IClient> players;
  private HashMap<String, Integer> vote_count = new HashMap<String, Integer>();
  private int accept_count_word1 = 0;
  private int accept_count_word2 = 0;
  private int count_pass_player = 0;
  private int index_current_player = 0;
  private String json;
  private ArrayList<String> currentWords;
  private boolean isGameRunning = false;

  public RemoteGame() throws RemoteException, JSONException {
    clients = new ArrayList<>();
    players = new ArrayList<>();
    buildInitialJson();
  }

  public String getJsonString() {
    return this.json.toString();
  }

  public ArrayList<IClient> getAllClientList() throws RemoteException {
    return this.clients;
  }

  public void appendJsonClient(String username, String password, String firstName, String lastName)
      throws RemoteException {
    System.out.println("APPEND New JSON Client: " + username);

    if (json != null) {
      JSONObject jsonObj;
      try {
        jsonObj = new JSONObject(json);
        JSONArray arrPlayer = jsonObj.getJSONArray("client");

        JSONObject objPlayer = new JSONObject();
        objPlayer.put("username", username);
        objPlayer.put("password", password);
        objPlayer.put("firstname", firstName);
        objPlayer.put("lastname", lastName);
        arrPlayer.put(objPlayer);

        jsonObj.put("client", arrPlayer);

        json = jsonObj.toString();
      } catch (JSONException e) {
        e.printStackTrace();
      }

    }

    System.out.println("Final JSON = " + json.toString());
  }

  private void appendJsonPlayer(ArrayList<String> player) throws JSONException, RemoteException {
    System.out.println("APPEND New JSON Player: " + player.toString());

    if (json != null) {
      JSONObject jsonObj = new JSONObject(json);
      JSONArray arrPlayer = jsonObj.getJSONArray("player");

      for (int i = 0; i < player.size(); i++) {
        JSONObject objPlayer = new JSONObject();
        objPlayer.put("username", player.get(i));
        objPlayer.put("score", 0);
        if (i == 0)
          objPlayer.put("turn", true);
        else
          objPlayer.put("turn", false);

        arrPlayer.put(objPlayer);
      }

      jsonObj.put("player", arrPlayer);

      json = jsonObj.toString();
    }

    System.out.println("Final JSON = " + json.toString());
  }


  private void buildInitialJson() throws JSONException {
    JSONObject jsonObj = new JSONObject();
    JSONArray arrWord = new JSONArray();
    jsonObj.put("word", arrWord);

    JSONArray arrPlayer = new JSONArray();
    jsonObj.put("player", arrPlayer);

    JSONArray arrClient = new JSONArray();
    JSONObject objClient = new JSONObject();
    objClient.put("username", "a");
    objClient.put("password", "a");
    objClient.put("firstname", "a");
    objClient.put("lastname", "a");
    arrClient.put(objClient);
    objClient = new JSONObject();
    objClient.put("username", "b");
    objClient.put("password", "b");
    objClient.put("firstname", "b");
    objClient.put("lastname", "b");
    arrClient.put(objClient);

    jsonObj.put("client", arrClient);

    json = jsonObj.toString();
    System.out.println("Building shared json: " + json);
  }

  private String getNextPlayerName() throws RemoteException {
    index_current_player++;
    if (index_current_player >= players.size())
      index_current_player = 0;
    return players.get(index_current_player).getUniqueName();
  }

  public String startNewGame(ArrayList<String> clientPlayList) throws RemoteException {
    try {
      appendJsonPlayer(clientPlayList);
      this.players = extractPlayers();
    } catch (JSONException e1) {
      e1.printStackTrace();
    }

    int i = 0;

    // show board for every people
    // tell others to update board
    while (i < players.size()) {
      players.get(i).createNewBoard();
      players.get(i).renderBoardSystem();
      i = i + 1;
    }
    isGameRunning = true;
    return this.json;
  }

  private ArrayList<String> orderClientPlayList() throws JSONException {
    ArrayList<String> sortedClientPlayList2 = new ArrayList<>();
    JSONObject jsonObject = new JSONObject(json); // JSON Object to store the json file
    JSONArray playerArray = jsonObject.getJSONArray("player");
    JSONObject playerObject; // JSON Object to store a player's JSON details

    for (int i = 0; i < playerArray.length(); i++) {
      playerObject = playerArray.getJSONObject(i);
      sortedClientPlayList2.add((String) playerObject.get("username"));
    }

    return sortedClientPlayList2;
  }

  private ArrayList<IClient> extractPlayers() throws RemoteException, JSONException {
    ArrayList<String> sortedClientPlayList2 = this.orderClientPlayList();
    ArrayList<IClient> players = new ArrayList<>();

    for (String playerName : sortedClientPlayList2) {
      for (IClient client : clients) {
        if (client.getUniqueName().equals(playerName)) {
          players.add(client);
          vote_count.put(playerName, 0);
        }
      }
    }

    return players;
  }

  public synchronized String registerGameClient(IClient client) throws RemoteException {
    this.clients.add(client);
    return this.json;
  }

  public String broadcastNewLetter(String json) {
    try {
      this.count_pass_player = 0;
      this.json = json;
      int i = 0;

      // tell others to update board
      while (i < players.size()) {
        players.get(i++).renderBoardSystem();
      }

    } catch (RemoteException e) {
      e.printStackTrace();
    }

    return this.json;
  }

  public synchronized void broadcastExit() throws RemoteException {

    endGame();

    int i = 0;
    while (i < players.size()) {
      players.get(i++).renderBoardSystem();
    }
  }

  public String performVoting(String json) {
    try {
      this.currentWords = extractWords(json);
      int i = 0;
      // tell others about voting system
      while (i < players.size()) {
        players.get(i).changeStateIntoVotingShow(currentWords);
        players.get(i).renderBoardSystem();
        i = i + 1;
      }
    } catch (RemoteException e) {
      e.printStackTrace();
    }

    return this.json;
  }

  private ArrayList<String> extractWords(String json2) {
    char[][] letterArray = new char[20][20]; // 20x20 board
    ArrayList<String> words = new ArrayList<>();
    int x = 0;
    int y = 0;

    JSONObject jsonObject;
    try {
      jsonObject = new JSONObject(json);
      JSONArray letterObject = jsonObject.getJSONArray("word");
      JSONObject eachLetter;

      for (int i = 0; i < letterObject.length(); i++) {
        eachLetter = letterObject.getJSONObject(i);
        x = eachLetter.getInt("x");
        y = eachLetter.getInt("y");
        letterArray[x][y] = eachLetter.getString("ch").charAt(0);
      }

      String tempX = lookUpXAxis(letterArray, x, y);
      String tempY = lookUpYAxis(letterArray, x, y);

      if ((tempX.length() == 1) && (tempY.length() == 1)) {
        words.add(tempX);
      } else if ((tempX.length() > 1) && (tempY.length() == 1)) {
        words.add(tempX);
      } else if ((tempX.length() == 1) && (tempY.length() > 1)) {
        words.add(tempY);
      } else if ((tempX.length() > 1) && (tempY.length() > 1)) {
        words.add(tempX);
        words.add(tempY);
      }


    } catch (JSONException e) {
      e.printStackTrace();
    }

    return words;
  }

  private String lookUpXAxis(char[][] letterArray, int x, int y) {

    final int MAX_X = 19;
    int leftX = -1; // default number -1

    String word = "";

    // Loop through and find the most left character index
    for (int i = x; i >= 0; i--) {
      if (letterArray[i][y] == 0) {
        leftX = i + 1;
        break;
      }
    }

    // If haven't found an empty char, set most left as coordinate x=0
    if (leftX == -1) {
      leftX = 0;
    }

    int j = leftX;

    while (letterArray[j][y] != 0 && j <= MAX_X) {
      word = word + letterArray[j][y];
      j++;
    }

    return word;
  }

  private String lookUpYAxis(char[][] letterArray, int x, int y) {

    final int MAX_Y = 19;
    int topY = -1; // default number -1

    String word = "";

    // Loop through and find the most left character index
    for (int i = y; i >= 0; i--) {
      if (letterArray[x][i] == 0) {
        topY = i + 1;
        break;
      }
    }

    // If haven't found an empty char, set most left as coordinate x=0
    if (topY == -1) {
      topY = 0;
    }

    int j = topY;

    while (letterArray[x][j] != 0 && j <= MAX_Y) {
      word = word + letterArray[x][j];
      j++;
    }

    return word;

  }

  public String disconnectClient() throws RemoteException {
    isGameRunning = false;
    return this.json;
  }

  public void removeClient(IClient client) throws RemoteException {
    clients.remove(client);
    return;
  }

  /*
   * Update the player turn on the Json String
   */
  public void updateTurn(String name) {
    try {

      JSONObject jsonObject = new JSONObject(json); // JSON Object to store the json file
      JSONArray playerArray = jsonObject.getJSONArray("player");
      JSONArray newPlayerArray = new JSONArray(); // New Player Array for rewrite
      JSONObject playerObject; // JSON Object to store a player's JSON details

      for (int i = 0; i < playerArray.length(); i++) {
        playerObject = playerArray.getJSONObject(i);
        /* Change the current player's turn to false */
        if (playerObject.get("turn").equals(true)) {
          playerObject.put("turn", false);
          newPlayerArray.put(playerObject);
        }
        /* Change the next player's turn to true */
        else if (playerObject.get("username").equals(name)) {
          playerObject.put("turn", true);
          newPlayerArray.put(playerObject);
        } else {
          newPlayerArray.put(playerObject);
        }
      }

      jsonObject.put("player", newPlayerArray);

      this.json = jsonObject.toString();
      System.out.println("Update turn JSON: " + this.json.toString());
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return;

  }

  /*
   * Update the current player's score to the Json String
   */
  public void updateScore(String name, int newScore) {
    try {
      JSONObject jsonObject = new JSONObject(json); // JSON Object to store the json file
      JSONArray playerArray = jsonObject.getJSONArray("player");
      JSONObject playerObject; // JSON Object to store a player's JSON details

      int playerSize = playerArray.length();
      for (int i = 0; i < playerSize; i++) {
        playerObject = playerArray.getJSONObject(i);
        if (playerObject.get("username").equals(name)) {
          int finalScore = playerObject.getInt("score") + newScore;
          playerObject.put("score", finalScore);
          break;
        }
      }

      jsonObject.put("player", playerArray);
      this.json = jsonObject.toString();
      System.out.println("Final JSON after update: " + this.json.toString());

    } catch (JSONException e) {
      e.printStackTrace();
    }

    return;
  }

  /*
   * Calculate score from each word (1 letter = 1 score)
   */
  private int calculateScore(String word) {
    return word.length();
  }

  @Override
  public String broadcastVote(String name, boolean accept, String word) throws RemoteException {
    int i = 0;
    vote_count.put(name, vote_count.get(name) + 1);

    if (word.equals(currentWords.get(0))) {
      if (accept) {
        this.accept_count_word1 = this.accept_count_word1 + 1;
      }
    } else {
      if (accept) {
        this.accept_count_word2 = this.accept_count_word2 + 1;
      }
    }

    int idVoter;
    try {
      if (vote_count.get(name) == currentWords.size()) {
        System.out.println("One player has voted all word(s), his name is " + name);

        idVoter = this.getId(name);
        players.get(idVoter).changeStateIntoVotingWait(accept);
        players.get(idVoter).renderBoardSystem();
        vote_count.put(name, 0);
      }

      if (this.accept_count_word1 == players.size()) { // everyone has voted accept word 1, update
                                                       // score

        int wordScore = calculateScore(currentWords.get(0));
        updateScore(players.get(index_current_player).getUniqueName(), wordScore);
        this.accept_count_word1 = 0;
      }

      if (this.accept_count_word2 == players.size()) { // everyone has voted accept word 2, update
                                                       // score
        int wordScore = calculateScore(currentWords.get(1));
        updateScore(players.get(index_current_player).getUniqueName(), wordScore);
        this.accept_count_word2 = 0;
      }

      int count_update_turn = 0;
      for (IClient player : players) {
        if (player.getCurrentState() == player.STATE_VOTING_WAIT) {
          count_update_turn = count_update_turn + 1;
        }
      }

      if (count_update_turn == players.size()) {// if everyone has voted
        if (players.size() > 1)
          updateTurn(getNextPlayerName());

        i = 0;
        while (i < players.size()) {
          players.get(i).changeStateIntoWait();
          players.get(i).renderBoardSystem();
          i = i + 1;
        }
      }

    } catch (

    JSONException e) {
      e.printStackTrace();
    }

    return this.json;
  }

  private int getId(String name) throws JSONException, RemoteException {
    int id = -1;

    for (int i = 0; i < players.size(); i++) {
      if (players.get(i).getUniqueName().equals(name)) {
        id = i;
      }
    }

    return id;
  }

  private void endGame() throws RemoteException {
    int i = 0;
    while (i < players.size()) {
      isGameRunning = false;
      players.get(i).changeStateIntoEndGame();
      i = i + 1;
    }
  }

  @Override
  public synchronized String broadcastPass(String name) throws RemoteException {
    this.count_pass_player = this.count_pass_player + 1;
    int i;
    if (this.count_pass_player == players.size()) {
      endGame();
    } else {

      // tell others about pass message
      i = 0;
      while (i < players.size()) {
        players.get(i).getPass(name);
        i = i + 1;
      }

      if (players.size() > 1)
        updateTurn(getNextPlayerName());
    }

    i = 0;
    while (i < players.size()) {
      players.get(i++).renderBoardSystem();
    }

    return this.json;
  }

  @Override
  public boolean isGameRunning() throws RemoteException {
    return isGameRunning;
  }

  @Override
  public ArrayList<IClient> getAllPlayerList() throws RemoteException {
    return players;
  }

  @Override
  public Boolean isLoginValid(String username, String password) throws RemoteException {
    if (!json.isEmpty()) {
      JSONObject jsonObject;
      try {
        jsonObject = new JSONObject(json);
        JSONArray playerArray = jsonObject.getJSONArray("client");
        JSONObject playerObject; // JSON Object to store a player's JSON details

        for (int i = 0; i < playerArray.length(); i++) {
          playerObject = playerArray.getJSONObject(i);
          if (playerObject.get("username").equals(username)
              && playerObject.get("password").equals(password)) {
            return true;
          }
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

    return false;
  }

  @Override
  public boolean isClientLoggedIn(String username) throws RemoteException {

    for (int i = 0; i < clients.size(); i++) {
      if (clients.get(i).getUniqueName().equals(username)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public void resetJson() throws RemoteException {
    try {

      JSONObject jsonObject = new JSONObject(json); // JSON Object to store the json file
      JSONArray newPlayerArray = new JSONArray(); // New Player Array for rewrite
      JSONArray newWordArray = new JSONArray(); // New Player Array for rewrite
      jsonObject.put("player", newPlayerArray);
      jsonObject.put("word", newWordArray);

      this.json = jsonObject.toString();
      System.out.println("Reset JSON: " + this.json.toString());
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return;

  }

  public Boolean isUsernameExisted(String username) throws RemoteException {
    if (!json.isEmpty()) {
      JSONObject jsonObject;

      try {
        jsonObject = new JSONObject(json);
        JSONArray playerArray = jsonObject.getJSONArray("client");
        JSONObject playerObject; // JSON Object to store a player's JSON details

        for (int i = 0; i < playerArray.length(); i++) {
          playerObject = playerArray.getJSONObject(i);
          if (playerObject.get("username").equals(username)) {
            return true;
          }
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

    return false;
  }
}
