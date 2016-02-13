package data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.Place;
import model.Weather;
import util.Utils;

public class JSONWeatherParser {

	public static Weather getWeather(String data)
	{
		Weather weather = new Weather();
		// create a json object from data
		try {
			JSONObject jsonObject = new JSONObject(data);
			
			Place place = new Place();
			JSONObject coords = Utils.getObject("coord", jsonObject);
			place.setLat(Utils.getFloat("lat", coords));
			place.setLon(Utils.getFloat("lon", coords));
			
			JSONObject sysObj = Utils.getObject("sys", jsonObject);
			place.setCountry(Utils.getString("country", sysObj));
			place.setLastupdated(Utils.getInt("dt", jsonObject));
			place.setSunrise(Utils.getInt("sunrise", sysObj));
			place.setSunset(Utils.getInt("sunset", sysObj));
			place.setCity(Utils.getString("name", jsonObject));
			
			weather.place = place;
			
			JSONArray jsonArray = jsonObject.getJSONArray("weather");
			JSONObject jsonWeather = jsonArray.getJSONObject(0);
			weather.currCond.setWeatherId(Utils.getInt("id", jsonWeather));
			weather.currCond.setDescription(Utils.getString("description", jsonWeather));
			weather.currCond.setCondition(Utils.getString("main", jsonWeather));
			weather.currCond.setIcon(Utils.getString("icon", jsonWeather));
			
			JSONObject mainObj = Utils.getObject("main", jsonObject);
			weather.currCond.setHumidity(Utils.getInt("humidity", mainObj));
			weather.currCond.setPressure(Utils.getInt("pressure", mainObj));
			weather.currCond.setTemperature(Utils.getFloat("temp", mainObj));
			weather.currCond.setMinTemp(Utils.getFloat("temp_min", mainObj));
			weather.currCond.setMaxTemp(Utils.getFloat("temp_max", mainObj));
			
			JSONObject windObj = Utils.getObject("wind", jsonObject);
			weather.wind.setSpeed(Utils.getFloat("speed", windObj));
			weather.wind.setDeg(Utils.getFloat("deg", windObj));
			
			JSONObject cloudObj = Utils.getObject("clouds", jsonObject);
			weather.clouds.setPrecipitation(Utils.getInt("all", cloudObj));
			
			return weather;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
