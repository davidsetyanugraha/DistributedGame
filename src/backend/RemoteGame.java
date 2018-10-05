package backend;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import frontend.IClient;

public class RemoteGame extends UnicastRemoteObject implements IRemoteGame {

  private final ArrayList<IClient> clients;
  private ArrayList<IClient> players;
  private static int client_count = 0;
  private static int vote_count = 0;
  private static int count_pass_player = 0;
  private int index_current_player = 0;
  private String json;
  private String[] currentWords;
  private String[] clientPlayList;

  RemoteGame() throws RemoteException, JSONException {
    clients = new ArrayList<>();
    players = new ArrayList<>();
    buildInitialJson();
  }

  public String getJsonString() {
    return this.json.toString();
  }

  public ArrayList<IClient> getAllClientList() throws RemoteException {
    return this.players;
  }

  private void buildInitialJson() throws JSONException {
    JSONObject jsonObj = new JSONObject();
    JSONArray arrWord = new JSONArray();
    jsonObj.put("word", arrWord);

    JSONArray arrPlayer = new JSONArray();
    jsonObj.put("player", arrPlayer);

    json = jsonObj.toString();
    System.out.println("Building shared json: " + json);
  }

  private String getNextPlayerName() throws RemoteException {
    index_current_player++;
    if (index_current_player >= players.size())
      index_current_player = 0;
    return players.get(index_current_player).getUniqueName();
  }

  public String startNewGame(String[] clientPlayList) throws RemoteException {
    this.players = extractPlayers(clientPlayList);

    try {
      int i = 0;

      // TODO
      // show board for every people
      // tell others to update board
      while (i < players.size()) {
        players.get(i++).renderBoardSystem();
      }
    } catch (RemoteException e) {
      e.printStackTrace();
    }

    return this.json;
  }

  private ArrayList<IClient> extractPlayers(String[] clientPlayList2) {
    // TODO extract player from client
    ArrayList<IClient> players = new ArrayList<>();
    return null;
  }

  public synchronized String registerGameClient(IClient client) throws RemoteException {
    // TODO Auto-generated method stub
    String response = "success";

    this.clients.add(client);
    client_count++;

    return response;
  }

  // perform voting system
  public String broadcastNewLetter(String json) {
    // TODO Auto-generated method stub

    try {
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

  // perform voting system
  public String performVoting(String json) {
    // TODO Auto-generated method stub
    try {
      currentWords = extractWords(json);

      int i = 0;
      // tell others about voting system
      while (i < players.size()) {
        players.get(i++).renderVotingSystem(currentWords);
      }
    } catch (RemoteException e) {
      e.printStackTrace();
    }

    return this.json;
  }

  private String[] extractWords(String json2) {
    // TODO Auto-generated method stub
    String[] words = {"play", "game"};
    return words;
  }

  public String disconnectClient() throws RemoteException {
    // TODO Auto-generated method stub
    client_count--;
    return "Success";
  }

  /*
   * Update the player turn on the Json String
   * */
  public void updateTurn(String name) {
    // TODO update JSON, set true for new player turn "name"
	  
	  try { 
		  
			JSONObject jsonObject = new JSONObject(json); //JSON Object to store the json file
			JSONArray playerArray = jsonObject.getJSONArray("player"); 
			JSONArray newPlayerArray = new JSONArray(); //New Player Array for rewrite
			JSONObject playerObject; //JSON Object to store a player's JSON details
			
			for (int i=0; i<playerArray.length(); i++) {
				playerObject = playerArray.getJSONObject(i);
				/*Change the current player's turn to false*/
				if (playerObject.get("turn").equals(true)) { 
					playerObject.put("turn", false);
					newPlayerArray.put(playerObject);
				} 
				/*Change the next player's turn to true*/
				else if (playerObject.get("name").equals(name)) {
					playerObject.put("turn", true);
					newPlayerArray.put(playerObject);
				}
				else {
					newPlayerArray.put(playerObject);
				}
			}
			
			jsonObject.put("player", newPlayerArray);
			
			json = jsonObject.toString();
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		return;
	  
  }

  /*
   * Update the current player's score to the Json String
   * */
  public void updateScore(String name, int newScore) {
    
	  try { 
		  
		JSONObject jsonObject = new JSONObject(json); //JSON Object to store the json file
		JSONArray playerArray = jsonObject.getJSONArray("player"); 
		JSONObject playerObject; //JSON Object to store a player's JSON details
		
		for (int i=0; i<playerArray.length(); i++) {
			playerObject = playerArray.getJSONObject(i);
			if (playerObject.get("name").equals(name)) {
				playerObject.put("score", newScore);
				playerArray.put(playerObject);
			} else {
				playerArray.put(playerObject);
			}
		}
		
		jsonObject.put("player", playerArray);
		
		json = jsonObject.toString();
		
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
	return;
  }

  /*
   * Calculate score from each word (1 letter = 1 score)
   * */
  private int calculateScore(String[] currentWords2) {
	
	int score = 0;
	int numOfWords = 2;
	
	for (int i=0; i<numOfWords ; i++) {
		score = score + currentWords2[i].length();
	}
	   
    return score;
  }

  @Override
  public String broadcastVote(boolean accept, String word) throws RemoteException {
    // TODO Auto-generated method stub
    int i = 0;

    if (accept) {
      vote_count++;
    }

    // tell others about voting in the board
    while (i < players.size()) {
      players.get(i++).getVote(accept);
    }

    // if voting successful
    // tell others to update score
    if (vote_count >= client_count) {
      updateScore(players.get(index_current_player).getUniqueName(), calculateScore(currentWords));
    }

    updateTurn(getNextPlayerName());

    while (i < players.size()) {
      players.get(i++).renderBoardSystem();
    }

    return "success";
  }

  @Override
  public synchronized String broadcastPass(String playerName) throws RemoteException {
    // TODO Auto-generated method stub
    String passMessage = playerName + " has pass";
    count_pass_player++;

    // tell others about pass message
    int i = 0;
    while (i < players.size()) {
      players.get(i++).getPass(playerName);
    }

    updateTurn(getNextPlayerName());

    while (i < players.size()) {
      players.get(i++).renderBoardSystem();
    }

    return "success";
  }

  @Override
  public String broadcastGeneralMessage(String message) throws RemoteException {
    // TODO Auto-generated method stub
    int i = 0;
    while (i < clients.size()) {
      clients.get(i++).getGeneralMessage(message);
    }

    return "success";
  }
}
