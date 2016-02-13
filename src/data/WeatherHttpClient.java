package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import util.Utils;

public class WeatherHttpClient {
	
	public String getWeatherData(String place){
		HttpURLConnection connection = null;
		InputStream inputStream = null;
		try {
			//String encodedURL = URLEncoder.encode(Utils.BASE_URL + place + "&appid=" + Utils.API_KEY, "UTF-8");
			connection = (HttpURLConnection) (new URL(Utils.BASE_URL + URLEncoder.encode(place, "UTF-8") + "&appid=" + Utils.API_KEY)).openConnection();
			//connection = (HttpURLConnection) (new URL("http://www.google.com")).openConnection();
			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
			connection.setRequestProperty("Accept","*/*");
			connection.connect();
			
			
			int status = connection.getResponseCode();
			
			// Read the response
			StringBuffer stringBuffer = new StringBuffer();
			if(status > 200)
			{
				inputStream = connection.getErrorStream();
			} else if(status == 200)
				inputStream = connection.getInputStream();
						
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;
			while((line = bufferedReader.readLine()) != null)
			{
				stringBuffer.append(line + "\r\n");
			}
			inputStream.close();
			connection.disconnect();
			
			return stringBuffer.toString();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
