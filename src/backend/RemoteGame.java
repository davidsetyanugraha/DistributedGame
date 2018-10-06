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

	public void appendJsonPlayer(String username, String password, String firstName, String lastName)
			throws JSONException, RemoteException {
		System.out.println("APPEND New JSON Player: " + username);

		if (json != null) {
			JSONObject jsonObj = new JSONObject(json);
			JSONArray arrPlayer = jsonObj.getJSONArray("player");

			JSONObject objPlayer = new JSONObject();
			objPlayer.put("username", username);
			objPlayer.put("password", password);
			objPlayer.put("firstname", firstName);
			objPlayer.put("lastname", lastName);
			objPlayer.put("score", 0);
			objPlayer.put("turn", true);
			arrPlayer.put(objPlayer);

			jsonObj.put("player", arrPlayer);

			json = jsonObj.toString();
		}

		System.out.println("Final JSON = " + json.toString());
	}
	
	private void appendJsonPlayer(ArrayList<String> player) throws JSONException, RemoteException {
	    System.out.println("APPEND New JSON Player: " + player.toString());

	    if (json != null) {
	      JSONObject jsonObj = new JSONObject(json);
	      JSONArray arrPlayer = jsonObj.getJSONArray("player");

	      for (String name : player) {
	        JSONObject objPlayer = new JSONObject();
	        objPlayer.put("name", name);
	        objPlayer.put("score", 0);
	        objPlayer.put("turn", true);
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

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

	private ArrayList<IClient> extractPlayers(ArrayList<String> clientPlayList2) throws RemoteException {
		ArrayList<IClient> players = new ArrayList<>();

		for (IClient client : clients) {
			for (String playerName : clientPlayList2) {
				if (client.getUniqueName().equals(playerName)) {
					players.add(client);
					System.out.println(playerName);
				}
			}
		}

		return players;
	}

	public synchronized String registerGameClient(IClient client) throws RemoteException {
		String response = "success";

		this.clients.add(client);
		client_count++;

		return response;
	}

	public String broadcastNewLetter(String json) {
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

	public String performVoting(String json) {
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
		// TODO Extract words from json
		
		char[][] letterArray = new char[20][20]; //20x20 board
		String[] words = new String[2];
		int x=0;
		int y=0;

		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONArray letterObject = jsonObject.getJSONArray("word");
			JSONObject eachLetter;
			
			for (int i=0; i<letterObject.length() ; i++) {
				eachLetter = letterObject.getJSONObject(i);
				x = eachLetter.getInt("x");
				y = eachLetter.getInt("y");
				letterArray[x][y] = eachLetter.getString("ch").charAt(0); //Any Better Option?
			}
			
			words[0] = lookUpXAxis(letterArray,x,y);
			System.out.println(words[0]);
			words[1] = lookUpYAxis(letterArray,x,y);
			System.out.println(words[1]);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // JSON Object to store the json file
	
		
		return words;
		
	}

	private String lookUpXAxis(char[][] letterArray, int x, int y) {
		
		final int MAX_X = 19;
		int leftX=-1; //default number -1
		
		String word = "";
		
		//Loop through and find the most left character index
		for (int i=x; i>=0; i--) {
			if (letterArray[i][y] == 0) {
				leftX = i+1;
				break;
			}
		}
		
		//If haven't found an empty char, set most left as coordinate x=0
		if (leftX == -1) {
			leftX = 0;
		}
		
		int j=leftX;
		
		while (letterArray[j][y] != 0 && j<= MAX_X) {
			word = word + letterArray[j][y];
			j++;
		}
		
		return word;
		
	}
	
	private String lookUpYAxis(char[][] letterArray, int x, int y) {
		
		final int MAX_Y = 19;
		int topY=-1; //default number -1
		
		String word = "";
		
		//Loop through and find the most left character index
		for (int i=y; i>=0; i--) {
			if (letterArray[x][i] == 0) {
				topY = i+1;
				break;
			}
		}
		
		//If haven't found an empty char, set most left as coordinate x=0
		if (topY == -1) {
			topY = 0;
		}
		
		int j=topY;
		
		while (letterArray[x][j] != 0 && j<= MAX_Y) {
			word = word + letterArray[x][j];
			j++;
		}
		
		return word;
		
	}
	
	public String disconnectClient() throws RemoteException {
		client_count--;
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

			json = jsonObject.toString();

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

			for (int i = 0; i < playerArray.length(); i++) {
				playerObject = playerArray.getJSONObject(i);
				if (playerObject.get("username").equals(name)) {
					playerObject.put("score", newScore);
					playerArray.put(playerObject);
				} else {
					playerArray.put(playerObject);
				}
			}

			jsonObject.put("player", playerArray);

			json = jsonObject.toString();

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return;
	}

	/*
	 * Calculate score from each word (1 letter = 1 score)
	 */
	private int calculateScore(String[] currentWords2) {
		int score = 0;
		int numOfWords = 2;

		for (int i = 0; i < numOfWords; i++) {
			score = score + currentWords2[i].length();
		}

		return score;
	}

	@Override
	public String broadcastVote(boolean accept, String word) throws RemoteException {
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
		int i = 0;
		while (i < clients.size()) {
			clients.get(i++).getGeneralMessage(message);
		}

		return "success";
	}
}
