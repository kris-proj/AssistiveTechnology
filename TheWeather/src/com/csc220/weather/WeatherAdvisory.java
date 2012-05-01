package com.csc220.weather;

public class WeatherAdvisory {
	private long startTime;
	private long endTime;
	private String description;
	private String message;
	
	
	public WeatherAdvisory(long startTime, long endTime){
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public void setDetails(String description, String message){
		this.description = description;
		this.message = message;
	}
}
