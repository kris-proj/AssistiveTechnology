package com.csc220.weather;

import java.util.Date;

/**
 * @author Dharmdeo Singh
 * 
 */
public class HourlyForecast {
	private int temp; // The temperature for the particular time
	private String time; // The time described by this HourlyForecast

	private String description; // A description about this hour
	private String skyCover; // The sky cover percentage
	private String chanceOfPrecip; // Chance of precipitation
	private String feelsLike; // What it feels like
	private String feelsLikeLabel; // A label for what it feels like
	private String windDirection; // The wind direction for the time
	private String windSpeed; // The wind speed predicted for this hourly
								// forecast
	private String dewPoint; // The dew point
	private String humidity; // The humidity of the time
	
	private boolean outofscope = false;

	/**
	 * Create a HourlyForecast object to store all the information describing a
	 * particular hour in the forecast
	 * 
	 * @param time
	 *            The time described by this hourly forecast
	 * @param temp
	 *            The temperature of the time
	 * @param desc
	 *            A description about the forecast
	 * @param sc
	 *            The sky cover percentage
	 * @param cop
	 *            The chance of precipitation
	 * @param fl
	 *            What it feels like
	 * @param fll
	 *            A label for what it feels like
	 * @param wd
	 *            The wind direction
	 * @param ws
	 *            The wind speed
	 * @param dp
	 *            Dew point
	 * @param h
	 *            Humidity
	 */
	public HourlyForecast(long time, int temp, String desc, String sc,
			String cop, String fl, String fll, String wd, String ws, String dp,
			String h) {
		// First make sure the data isnt old
		Date now = new Date();
		if(now.getTime() > time)
			outofscope = true;
		else{
		// Converting the time since epoch to a readable format
			Date date = new Date(time);
			this.time = date.toLocaleString();
			int len = this.time.length();
			// Filtering out only the time from the date
			this.time = this.time.substring(len - 11, len);

			// Saving all the other relavent data
			this.temp = temp;
			description = desc;
			skyCover = sc;
			chanceOfPrecip = cop;
			feelsLike = fl;
			feelsLikeLabel = fll;
			windDirection = wd;
			windSpeed = ws;
			dewPoint = dp;
			humidity = h;
		}
	}

	/**
	 * @return The temperature
	 */
	public String getTemp() {
		return Integer.toString(temp);
	}

	/**
	 * @return The time described by the hourly forecast
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @return A description of the forecast
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return Sky cover percentage
	 */
	public String getSkyCover() {
		return skyCover;
	}

	/**
	 * @return The chance of precipitation
	 */
	public String getChanceOfPrecip() {
		return chanceOfPrecip;
	}

	/**
	 * @return What it currently feels like
	 */
	public String getFeelsLike() {
		return feelsLike;
	}

	/**
	 * @return A label for what it feels like
	 */
	public String getFeelsLikeLabel() {
		return feelsLikeLabel;
	}

	/**
	 * @return The wind direction
	 */
	public String getWindDirection() {
		return windDirection;
	}

	/**
	 * @return The wind speed
	 */
	public String getWindSpeed() {
		return windSpeed;
	}

	/**
	 * @return The dew point
	 */
	public String getDewPoint() {
		return dewPoint;
	}

	/**
	 * @return The humidity
	 */
	public String getHumidity() {
		return humidity;
	}
	
	public boolean outOfScope(){
		return outofscope;
	}
}
