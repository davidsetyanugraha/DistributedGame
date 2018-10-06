package testing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateTurnScoreTest {

	private static String json = "{\"word\": [\n" + 
			"        {\n" + 
			"            \"x\": 1,\n" + 
			"            \"y\": 1,\n" + 
			"            \"ch\": \"p\"\n" + 
			"        },\n" + 
			"        {\n" + 
			"            \"x\": 1,\n" + 
			"            \"y\": 2,\n" + 
			"            \"ch\": \"l\"\n" + 
			"        },\n" + 
			"        {\n" + 
			"            \"x\": 2,\n" + 
			"            \"y\": 2,\n" + 
			"            \"ch\": \"a\"\n" + 
			"        },\n" + 
			"        {\n" + 
			"            \"x\": 3,\n" + 
			"            \"y\": 2,\n" + 
			"            \"ch\": \"y\"\n" + 
			"        }\n" + 
			"    ],\n" + 
			"    \"player\": [\n" + 
			"        {\n" + 
			"        	\"username\": \"andrelee\",\n" + 
			"            \"password\": 123456,\n" + 
			"            \"firstname\": \"Andre\",\n" + 
			"            \"lastname\": \"Andre\",\n" + 
			"            \"score\": 10,\n" + 
			"            \"turn\": true\n" + 
			"        },\n" + 
			"        {\n" + 
			"            \"username\": \"kelly\",\n" + 
			"            \"password\": 123456,\n" + 
			"            \"firstname\": \"Kelly\",\n" + 
			"            \"lastname\": \"Kelly\",\n" + 
			"            \"score\": 10,\n" + 
			"            \"turn\": false\n" + 
			"        }\n" + 
			"    ]}";

	public static void main(String[] args) {
		updateTurn("kelly");
		System.out.println(json);
		
		updateScore("andrelee",20);
		System.out.println(json);
		
	}
	
	private static void updateTurn(String name) {
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
	private static void updateScore(String name, int newScore) {
		try {

			JSONObject jsonObject = new JSONObject(json); // JSON Object to store the json file
			JSONArray playerArray = jsonObject.getJSONArray("player");
			JSONArray newPlayerArray = new JSONArray(); // New Player Array for rewrite
			JSONObject playerObject; // JSON Object to store a player's JSON details

			for (int i = 0; i < playerArray.length(); i++) {
				playerObject = playerArray.getJSONObject(i);
				if (playerObject.get("username").equals(name)) {
					playerObject.put("score", newScore);
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
}
