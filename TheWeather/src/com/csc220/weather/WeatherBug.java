package com.csc220.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * @author Dharmdeo Singh
 * 
 */
public class WeatherBug implements LocationListener {
	// The API Key required by WB Service
	private String APIKey = "4x334tn2k4kdymuhjkezhpbh";
	private JSONObject json;

	// The base URL for hourly forecast with zip
	private String baseURL_hourly_zip = "http://i.wxbug.net/REST/Direct/GetForecastHourly.ashx?zip=ZZZZZ&ht=t&ht=d&api_key=XXXXX";
	// The base URL for hourly forecast with lat and lon
	private String baseURL_hourly_loc = "http://i.wxbug.net/REST/Direct/GetForecastHourly.ashx?la=LAT&lo=LONG&ht=t&ht=i&ht=d&api_key=XXXXX";

	private Handler UIHandler; // Used to handle updates from WeatherBug object
	private LocationManager locManager;

	private String data = ""; // stores the data read from server
	private String zip = "11419"; // A default zip code to use
	private double lat = 0; // latitude of current network location
	private double log = 0; // longiture of current network location
	private int temp = 0; // Temperature of current weather
	private String desc; // Description of current weather

	private String urlString; // used when requesting data from WeatherBug

	// Constants to indicate what has been updated
	public static final int CURRENT = 1;

	/**
	 * @param handler
	 *            A Handler which will be notified when a weather update has
	 *            occurred. The message sent will contain the type of update
	 *            that has occurred (current weather, forecast, etc.)
	 */
	public WeatherBug(Handler handler, Context context) {
		UIHandler = handler;

		// The location manager for getting the coarse location of the user
		locManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
	}

	/**
	 * Update the current weather. When results are available, a message will be
	 * dispatched to the handler notifying it that the current weather has been
	 * updated via the message's arg1 variable. The variable will be set to
	 * WeatherBug.CURRENT.
	 * 
	 * This is a generic update function. The string storing the URL is changed
	 * using the other update functions
	 */
	public void updateCurent() {
		/*
		 * Create a new thread to run in the background. This is to ensure the
		 * UI does not freeze up while retrieving data from the web server.
		 * Without this background thread, the UI would stall until the data is
		 * downloaded.
		 */
		Thread background = new Thread() {
			@Override
			public void run() {

				Log.i("WeatherBug", urlString);
				// Download the JSON data
				try {
					// Open a new URL Connection and download the data
					URL url = new URL(urlString);
					URLConnection weatherBugConnection = url.openConnection();
					BufferedReader input = new BufferedReader(
							new InputStreamReader(
									weatherBugConnection.getInputStream()));
					String inputLine;
					// Keep reading lines until there is no more to be read
					while ((inputLine = input.readLine()) != null) {
						data += inputLine;
					}
					Log.i("WeatherBug", data);

					// Create a JSON object from the data that was read
					json = new JSONObject(data);
					data = "";
					input.close(); // Close the input stream
					/*
					 * Obtain a list of the hourly forecast data. This is a list
					 * of JSON objects which each contain information about a
					 * certain hour's weather data. The first JSON object is the
					 * current weather status.
					 */
					JSONArray hourlyForecast = json
							.getJSONArray("forecastHourlyList");

					// Pick the first JSON object from the array
					JSONObject hour1 = hourlyForecast.getJSONObject(0);

					// Obtain the temperature and description of the current
					// weather
					temp = hour1.getInt("temperature");
					desc = hour1.getString("desc");

					// A new runnable to post to the UI thread
					Runnable update = new Runnable() {

						@Override
						public void run() {
							/*
							 * Create and send a message to the handler running
							 * on the UI thread indicating that the current
							 * weather status has been updated.
							 */
							Message msg = new Message();
							msg.arg1 = CURRENT;
							UIHandler.dispatchMessage(msg);
						}
					};
					/*
					 * Post the runnable to the handler. This allows the handler
					 * to continue to run on the UI thread so the views can be
					 * updated
					 */
					UIHandler.post(update);

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		background.start();
	}

	public void updateCurrentWithZip() {
		/*
		 * Modifying the base url for hourly updates to include the correct zip
		 * code and API key.
		 */
		urlString = baseURL_hourly_zip.replace("ZZZZZ", zip);
		urlString = urlString.replace("XXXXX", APIKey);
		// Call the generic update function to get the current weather
		updateCurent();
	}

	public void updateCurrentWithLoc() {

		// Start listening for location updates
		locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
				0, this);

		/*
		 * A new thread for running in the background. This waits for the
		 * updated location therefore this thread will wait a while.
		 */
		Thread wait = new Thread() {
			@Override
			public void run() {
				/*
				 * Synchronized because this function will wait for updates.
				 */
				synchronized (locManager) {

					try {
						// wait until the the LocationManager updates the
						// location
						locManager.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Log.i("WeatherBug", "Lat: " + lat + " Long: " + log);

					/*
					 * Modify the base url for hourly updates to include the
					 * correct latitude and longitude
					 */
					urlString = baseURL_hourly_loc.replace("LAT",
							Double.toString(lat));
					urlString = urlString.replace("LONG", Double.toString(log));
					urlString = urlString.replace("XXXXX", APIKey);
					// Call the generic update function to get the current
					// weather
					updateCurent();
					super.run();
				}
			}
		};
		wait.start();
	}

	public void updateForecast() {
		// TODO Implement forecast update
		// TODO Implement forecast class (to store each day's forecast)
	}

	/**
	 * This method retrieves the temperature of the current weather
	 * 
	 * @return The current temperature
	 */
	public int getTemp() {
		return temp;
	}

	/**
	 * This method retrieves the condition of the current weather conditions.
	 * 
	 * @return The current weather conditions
	 */
	public String getDescription() {
		return desc;
	}

	/**
	 * This allows changing the zip code that will be used to update the
	 * weather.
	 * 
	 * @param x
	 *            The new zip code.
	 */
	public void setZip(String x) {
		zip = x;
	}

	// ===============LOCATION LISTENER METHODS===========================

	@Override
	public void onLocationChanged(Location location) {
		synchronized (locManager) {
			// Save the latitude and longitude of the current location
			lat = location.getLatitude();
			log = location.getLongitude();
			// Stop listening for updates (only need one location)
			locManager.removeUpdates(this);

			// Notify the location manager which is waiting for the location
			locManager.notify();

		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// unimplemented
	}

	@Override
	public void onProviderEnabled(String provider) {
		// unimplemented
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// unimplemented
	}
}
