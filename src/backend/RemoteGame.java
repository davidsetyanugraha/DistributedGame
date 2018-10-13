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
  private String[] clientPlayList;
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
    System.out.println("New Game started! it contains");
    this.players = extractPlayers(clientPlayList);
    try {
      appendJsonPlayer(clientPlayList);
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

  private ArrayList<IClient> extractPlayers(ArrayList<String> clientPlayList2)
      throws RemoteException {
    ArrayList<IClient> players = new ArrayList<>();

    for (IClient client : clients) {
      for (String playerName : clientPlayList2) {
        if (client.getUniqueName().equals(playerName)) {
          players.add(client);
          vote_count.put(playerName, 0);
        }
      }
    }

    return players;
  }

  public synchronized String registerGameClient(IClient client) throws RemoteException {
    String response = "success";

    this.clients.add(client);
    return response;
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

  public String performVoting(String json) {
    try {
      this.currentWords = extractWords(json);
      System.out.println("Current Words: " + currentWords.toString());
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
        letterArray[x][y] = eachLetter.getString("ch").charAt(0); // Any Better Option?
      }

      String tempX = lookUpXAxis(letterArray, x, y);
      String tempY = lookUpYAxis(letterArray, x, y);

      if ((tempX.length() == 1) && (tempY.length() == 1)) {
        words.add(tempX);
        // System.out.println("Word1 = " + words.get(0));
      } else if ((tempX.length() > 1) && (tempY.length() == 1)) {
        words.add(tempX);
        // System.out.println("Word1 = " + words[0]);
      } else if ((tempX.length() == 1) && (tempY.length() > 1)) {
        words.add(tempY);
        // System.out.println("Word1 = " + words[0]);
      } else if ((tempX.length() > 1) && (tempY.length() > 1)) {
        words.add(tempX);
        words.add(tempY);
        // System.out.println("Word1 = " + words[0]);
        // System.out.println("Word2 = " + words[1]);
      }


    } catch (JSONException e) {
      e.printStackTrace();
    } // JSON Object to store the json file


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
    return "Success";
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
      System.out.println("update turn json" + this.json.toString());
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
      System.out.println("update bro");
      JSONObject jsonObject = new JSONObject(json); // JSON Object to store the json file
      JSONArray playerArray = jsonObject.getJSONArray("player");
      JSONObject playerObject; // JSON Object to store a player's JSON details

      int playerSize = playerArray.length();
      for (int i = 0; i < playerSize; i++) {
        System.out.println("playerarray");
        playerObject = playerArray.getJSONObject(i);
        if (playerObject.get("username").equals(name)) {
          int finalScore = playerObject.getInt("score") + newScore;
          System.out.println("FINAL SCORE:" + finalScore);
          playerObject.put("score", finalScore);
          break;
        }
      }

      jsonObject.put("player", playerArray);
      this.json = jsonObject.toString();
      System.out.println("after update score");
      System.out.println(this.json.toString());

    } catch (JSONException e) {
      e.printStackTrace();
    }

    return;
  }

  /*
   * Calculate score from each word (1 letter = 1 score)
   */
  private int calculateScore(String word) {
    int score = word.length();

    return score;
  }

  @Override
  public String broadcastVote(String name, boolean accept, String word) throws RemoteException {
    int i = 0;
    vote_count.put(name, vote_count.get(name) + 1);

    if (word.equals(currentWords.get(0))) {
      System.out.println("word 1 equals");
      // this.vote_count_word1 = this.vote_count_word1 + 1;
      if (accept) {
        this.accept_count_word1 = this.accept_count_word1 + 1;
      }
    } else {
      System.out.println("word 2 equals");
      // this.vote_count_word2 = this.vote_count_word2 + 1;
      if (accept) {
        this.accept_count_word2 = this.accept_count_word2 + 1;
      }
    }

    int idVoter;
    try {
      System.out.println("vote count get name:" + vote_count.get(name));
      if (vote_count.get(name) == currentWords.size()) {
        // one player has voted all word(s)
        System.out.println("one player has voted all word(s)");
        idVoter = this.getId(name);
        players.get(idVoter).changeStateIntoVotingWait(accept);
        players.get(idVoter).renderBoardSystem();
        vote_count.put(name, 0);
      }

      if (this.accept_count_word1 == players.size()) { // everyone has voted accept word 1, update
                                                       // score
        System.out.println("calculate score word 1");
        int wordScore = calculateScore(word);
        System.out.println("update score word1 " + wordScore);
        updateScore(players.get(index_current_player).getUniqueName(), wordScore);
        this.accept_count_word1 = 0;
      }

      if (this.accept_count_word2 == players.size()) { // everyone has voted accept word 2, update
                                                       // score
        System.out.println("calculate score word 2");
        // int wordScore = calculateScore(word);
        // System.out.println("update score word2 " + wordScore);
        // hardcode
        updateScore(players.get(index_current_player).getUniqueName(), 3);
        this.accept_count_word2 = 0;
      }

      int count_update_turn = 0;
      for (IClient player : players) {
        if (player.getCurrentState() == player.STATE_VOTING_WAIT) {
          count_update_turn = count_update_turn + 1;
        }
      }

      if (count_update_turn == players.size()) {// if everyone has voted
        System.out.println("update turn");
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

    return "success";
  }

  private int getId(String name) throws JSONException {
    int id = -1;

    JSONObject jsonObject = new JSONObject(json); // JSON Object to store the json file
    JSONArray playerArray = jsonObject.getJSONArray("player");
    JSONObject playerObject; // JSON Object to store a player's JSON details

    for (int i = 0; i < playerArray.length(); i++) {
      playerObject = playerArray.getJSONObject(i);
      if (playerObject.get("username").equals(name)) {
        id = i;
      }
    }

    return id;
  }

  @Override
  public synchronized String broadcastPass(String name) throws RemoteException {
    this.count_pass_player = this.count_pass_player + 1;
    int i = 0;
    if (this.count_pass_player == players.size()) {
      while (i < players.size()) {
        isGameRunning = false;
        players.get(i).changeStateIntoEndGame();
        i = i + 1;
      }
    } else {
      // tell others about pass message
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

    return "success";
  }

  @Override
  public String broadcastGeneralMessage(String message) throws RemoteException {
    int i = 0;
    while (i < clients.size()) {
      clients.get(i++).getGeneralMessage(message);
    }

    return "success";
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
  public Boolean isLoginValid(String username) throws RemoteException {
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
