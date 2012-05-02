package com.csc220.weather;

public class WeatherAdvisory {
	private String startTime; // The start time of the advisory
	private String endTime; // When the advisory is over
	private String description; // A description about the advisory (short)
	private String message; // A detailed message about the advisory (long)

	/**
	 * Create a WeatherAdvisory with a start and end time
	 * 
	 * @param string
	 *            Start time
	 * @param string2
	 *            End time
	 */
	public WeatherAdvisory(String string, String string2) {
		this.startTime = string;
		this.endTime = string2;
	}

	/**
	 * Set the details about the weather advisory
	 * 
	 * @param description
	 *            A description of the weather advisory
	 * @param message
	 *            A more detailed message about the advisory
	 */
	public void setDetails(String description, String message) {
		this.description = description;
		this.message = message;
	}

	/*
	 * For debugging purposes
	 */
	public String toString() {
		return description + ":\n" + message;
	}

	/**
	 * @return The description about the weather advisory
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return The weather advisory message
	 */
	public String getMessage() {
		return message;
	}
}
