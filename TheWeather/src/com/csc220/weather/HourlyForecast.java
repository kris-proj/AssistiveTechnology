package com.csc220.weather;

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

	public HourlyForecast(int time,int temp, String desc, String sc, String cop,
			String fl, String fll, String wd, String ws, String dp, String h) {
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
