package util;

import org.json.JSONException;
import org.json.JSONObject;

public class Utils {
	
	public static final String BASE_URL="http://api.openweathermap.org/data/2.5/weather?q=";	                                     
	public static final String ICON_URL="http://api.openweathermap.org/img/w/";
	public static final String API_KEY = "fb4c8dd294a3542a4a4e046d79bd5b4a";

	public static JSONObject getObject(String tagName, JSONObject jsonObject) throws JSONException 
	{
		JSONObject jObj = jsonObject.getJSONObject(tagName);
		return jObj;
		
	}
	
	public static String getString(String tagName, JSONObject jsonObject) throws JSONException 
	{
		return jsonObject.getString(tagName);
	}
	
	public static float getFloat(String tagName, JSONObject jsonObject) throws JSONException {
		return (float) jsonObject.getDouble(tagName);
	}
	
	public static double getDouble(String tagName, JSONObject jsonObject) throws JSONException {
		return (float) jsonObject.getDouble(tagName);
	}
	
	public static int getInt(String tagName, JSONObject jsonObject) throws JSONException {
		return jsonObject.getInt(tagName);
	}
}
