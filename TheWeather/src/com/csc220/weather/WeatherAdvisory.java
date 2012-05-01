package com.csc220.weather;

public class WeatherAdvisory {
	private String startTime;
	private String endTime;
	private String description;
	private String message;
	
	
	public WeatherAdvisory(String string, String string2){
		this.startTime = string;
		this.endTime = string2;
	}
	
	public void setDetails(String description, String message){
		this.description = description;
		this.message = message;
	}
	
	public String toString(){
		return description + ":\n" + message;
	}
}
