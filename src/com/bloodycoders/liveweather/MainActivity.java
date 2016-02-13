package com.bloodycoders.liveweather;

/* Sample Live weather app
   This I developed while working on Android Development Course
   Yogesh Mali

*/
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import data.CityPreference;
import data.JSONWeatherParser;
import data.WeatherHttpClient;
import model.Weather;
import util.Utils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


public class MainActivity extends ActionBarActivity {

	private TextView cityName;
	private TextView temp;
	private ImageView iconView;
	private TextView description;
	private TextView humidity;
	private TextView pressure;
	private TextView wind;
	private TextView sunrise;
	private TextView sunset;
	private TextView updated;

	Weather weather = new Weather();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		cityName =  (TextView) findViewById(R.id.cityText);
		iconView = (ImageView) findViewById(R.id.thumbnailIcon);
		temp = (TextView) findViewById(R.id.tempText);
		description = (TextView) findViewById(R.id.cloudText);
		humidity = (TextView) findViewById(R.id.humidText);
		pressure = (TextView) findViewById(R.id.pressureText);
		wind = (TextView) findViewById(R.id.windText);
		sunrise = (TextView) findViewById(R.id.riseText);
		sunset = (TextView) findViewById(R.id.setText);
		updated = (TextView) findViewById(R.id.updateText);

		CityPreference cityPreference = new CityPreference(MainActivity.this);

		renderWeatherData(cityPreference.getCity());
	}

	public void renderWeatherData(String city) {

		WeatherTask weatherTask = new WeatherTask();
		weatherTask.execute(new String[]{city + "&unit=Imperial"});

	}

	private class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap>
	{

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			return downloadImage(params[0]);

		}

		@Override
		protected void onPostExecute(Bitmap bitmap){
			//super.onPostExecute(bitmap);
			iconView.setImageBitmap(bitmap);
		}

		private Bitmap downloadImage(String code){
			HttpURLConnection httpurlconnect = null;
			//final DefaultHttpClient client = new DefaultHttpClient();
			//final HttpGet getRequest = new HttpGet(Utils.ICON_URL + code + ".png");

			try {
				httpurlconnect = (HttpURLConnection) (new URL(Utils.ICON_URL + code + ".png")).openConnection();
				httpurlconnect.setRequestMethod("GET");
				httpurlconnect.setDoInput(true);
				//httpurlconnect.setDoOutput(true);
				httpurlconnect.connect();

				final int statusCode = httpurlconnect.getResponseCode();
				if(statusCode != HttpURLConnection.HTTP_OK)
				{
					Log.e("DownloadImage", "Error: " + statusCode);
					return null;
				}
				InputStream inputStream = null;
				inputStream = httpurlconnect.getInputStream();

				final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

				return bitmap;
			} catch (MalformedURLException e1) {

				e1.printStackTrace();
			} catch (IOException e1) {

				e1.printStackTrace();
			}


			return null;
		}
	}

	private class WeatherTask extends AsyncTask<String, Void, Weather>
	{

		@Override
		protected Weather doInBackground(String... params) {
			// TODO Auto-generated method stub

			String data = ((new WeatherHttpClient()).getWeatherData(params[0]));

			weather = JSONWeatherParser.getWeather(data);
			weather.iconData = weather.currCond.getIcon();

			Log.v("Data: ", weather.currCond.getDescription());

			new DownloadImageAsyncTask().execute(weather.iconData);
			return weather;
		}

		@Override
		protected void onPostExecute(Weather weather) {
			super.onPostExecute(weather);

			DateFormat df = DateFormat.getTimeInstance();
			String sunriseDate = df.format(new Date(weather.place.getSunrise()));
			String sunsetDate = df.format(new Date(weather.place.getSunset()));
			String lastupdated = df.format(new Date(weather.place.getLastupdated()));

			DecimalFormat decimalFormat = new DecimalFormat("#.#");

			String tempFormat = decimalFormat.format(weather.currCond.getTemperature());

			cityName.setText(weather.place.getCity() + "," + weather.place.getCountry());
			temp.setText("" + tempFormat + " °C");
			humidity.setText("Humidity: " + weather.currCond.getHumidity() + "%");
			pressure.setText("Pressure: " + weather.currCond.getPressure() + " hPa");
			wind.setText("Wind: " + weather.wind.getSpeed() + " mps");
			sunrise.setText("Sunrise: " + sunriseDate);
			sunset.setText("SunSet: " + sunsetDate);
			updated.setText("Last Updated: " + lastupdated);
			description.setText("Condition: " + weather.currCond.getCondition() + "( " + weather.currCond.getDescription() + " )");
		}
	}

	private void showInputDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("Change City");

		final EditText cityInput = new EditText(MainActivity.this);
		cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
		cityInput.setHint("Portland");
		builder.setView(cityInput);
		builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				CityPreference cityPreference = new CityPreference(MainActivity.this);
				cityPreference.setCity(cityInput.getText().toString());

				String newCity = cityPreference.getCity();
				renderWeatherData(newCity);
			}
		});
		builder.show();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.changeCityId) {
			showInputDialog();
			//return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
