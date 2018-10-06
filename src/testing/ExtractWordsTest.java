package testing;

import java.rmi.RemoteException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import backend.RemoteGame;

public class ExtractWordsTest {
	
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
	
	}
	
	private static String lookUpXAxis(char[][] letterArray, int x, int y) {
		
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
	
	private static String lookUpYAxis(char[][] letterArray, int x, int y) {
		
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
}
