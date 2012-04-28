package com.csc220.weather;

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
	
	public DailyForecast(String title, String high, String low) {
		this.high = high;
		this.low = low;
		this.title = title;
	}
	
	public String toString(){
		return title+"\n"+"Low: "+low+" High: "+high+"\n"+dayDesc+" "+dayPred;
	}
	
	public void setDayDetails(String desc, String pred, String name){
		dayDesc = desc;
		dayPred = pred;
		dayTitle = name;
	}
	
	public void setNightDetails(String desc, String pred, String name){
		nightDesc = desc;
		nightPred = pred;
		nightTitle = name;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getHigh(){
		return high;
	}
	
	public String getLow(){
		return low;
	}
	
	public String getDayDescription(){
		return dayDesc;
	}
	
	public String getDayPrediction(){
		return dayPred;
	}
	
	public String getNightDescription(){
		return nightDesc;
	}
	
	public String getNightPrediction(){
		return nightPred;
	}
}
