package com.csc220.weather;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

/**
 * @author Dharmdeo Singh
 *
 */
public class HourlyForecast {
	private int temp;
	private String time;

	private String description;
	private String skyCover;
	private String chanceOfPrecip;
	private String feelsLike;
	private String feelsLikeLabel;
	private String windDirection;
	private String windSpeed;
	private String dewPoint;
	private String humidity;

	public HourlyForecast(long time,int temp, String desc, String sc, String cop,
			String fl, String fll, String wd, String ws, String dp, String h) {
		Date date = new Date(time);
		this.time = date.toLocaleString();
		int len = this.time.length();
		this.time = this.time.substring(len-10,len);
		
		
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
	
	public String getTemp(){
		return Integer.toString(temp);
	}
	
	public String getTime(){
		return time;
	}
	
	public String getDescription(){
		return description;
	}
	
	public String getSkyCover(){
		return skyCover;
	}
	
	public String getChanceOfPrecip(){
		return chanceOfPrecip;
	}
	
	public String getFeelsLike(){
		return feelsLike;
	}
	
	public String getFeelsLikeLabel(){
		return feelsLikeLabel;
	}
	
	public String getWindDirection(){
		return windDirection;
	}
	
	public String getWindSpeed(){
		return windSpeed;
	}
	
	public String getDewPoint(){
		return dewPoint;
	}
	
	public String getHumidity(){
		return humidity;
	}
}
