package com.csc220.weather;

public class DailyForecast {
	private String title; // Day's title
	
	private String dayDesc; // description
	private String dayPred; // prediction
	private String dayTitle; // day's daytime title
	
	private String nightDesc; // night time description
	private String nightPred; // night time prediction
	private String nightTitle; // night time title

	private int high = 0; // high temperature
	private int low = 0; // low temperature
	
	public DailyForecast(String title, int hight, int low) {
		this.high = hight;
		this.low = low;
		this.title = title;
	}
	
}
