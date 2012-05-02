package com.csc220.weather;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author Dharmdeo Singh
 * 
 */
public class DailyForecast {
	private String title; // Day's title

	private String dayDesc; // description
	private String dayPred; // prediction
	private String dayTitle; // day's daytime title

	private String nightDesc; // night time description
	private String nightPred; // night time prediction
	private String nightTitle; // night time title

	private String high; // high temperature
	private String low; // low temperature

	private String imageURLString = "http://img.weather.weatherbug.com/forecast/icons/localized/125x105/en/trans/";
	private Bitmap dayConditionImg;
	private Bitmap nightConditionImg;

	/**
	 * @param title
	 *            The name of the day
	 * @param high
	 *            The high temperature for the day
	 * @param low
	 *            The Low temperature for the day
	 * 
	 * @param title
	 *            A title for the day (Monday, Tuesday,..)
	 * @param high
	 *            The high temperature for the day
	 * @param low
	 *            The low temperature of the day
	 * @param dayCondition
	 *            A condition name (to download appropriate icon)
	 * @param nightCondition
	 *            A condition name for th night (to download appropriate icon)
	 */
	public DailyForecast(String title, String high, String low,
			String dayCondition, String nightCondition) {
		this.high = high;
		this.low = low;
		this.title = title;
		dayConditionImg = downloadImage(imageURLString + dayCondition + ".png");
		nightConditionImg = downloadImage(imageURLString + nightCondition
				+ ".png");
	}

	/**
	 * Download an image from a URL
	 * 
	 * @param urlString
	 *            A string containing the URL to the image to download
	 * @return a Bitmap object of the image downloaded
	 */
	private Bitmap downloadImage(String urlString) {
		URL url;
		BufferedInputStream bis = null;
		try {
			url = new URL(urlString);
			URLConnection con = url.openConnection();
			InputStream is = con.getInputStream();
			bis = new BufferedInputStream(is);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return BitmapFactory.decodeStream(bis);
	}

	public Bitmap getDayImage() {
		return dayConditionImg;
	}

	public Bitmap getNightImage() {
		return nightConditionImg;
	}

	/*
	 * Returns a printable version of the basic weather information for this day
	 */
	public String toString() {
		return title + "\n" + "Low: " + low + " High: " + high + "\n" + dayDesc
				+ " " + dayPred;
	}

	/**
	 * @param desc
	 *            Daytime description
	 * @param pred
	 *            Daytime prediction
	 * @param name
	 *            Title of the day (could be different than the day's name)
	 */
	public void setDayDetails(String desc, String pred, String name) {
		dayDesc = desc;
		dayPred = pred;
		dayTitle = name;
	}

	/**
	 * @param desc
	 *            Night time description
	 * @param pred
	 *            Night time prediction
	 * @param name
	 *            Night time name
	 */
	public void setNightDetails(String desc, String pred, String name) {
		nightDesc = desc;
		nightPred = pred;
		nightTitle = name;
	}

	/**
	 * @return The days name
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return The highest temperature predicted for the day
	 */
	public String getHigh() {
		return high;
	}

	/**
	 * @return The lowest temperature predicted for the day
	 */
	public String getLow() {
		return low;
	}

	/**
	 * @return A short description of the day's weather.
	 */
	public String getDayDescription() {
		return dayDesc;
	}

	/**
	 * @return A prediction about what will occur throughout the day and what to
	 *         expect.
	 */
	public String getDayPrediction() {
		return dayPred;
	}

	/**
	 * @return A short description about the weather at night
	 */
	public String getNightDescription() {
		return nightDesc;
	}

	/**
	 * @return A prediction about what will occur during the night
	 */
	public String getNightPrediction() {
		return nightPred;
	}
}
