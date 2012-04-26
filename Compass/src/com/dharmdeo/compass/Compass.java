package com.dharmdeo.compass;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Compass implements LocationListener, SensorEventListener {

	private SensorManager sensorManager;
	private LocationManager locationManager;
	private Sensor orientationSensor;
	private Location prevLoc = null;
	
	private Handler handler;
	private Message msg;
	
	private int magBearing;
	private int gpsRate = 5000;
	// Compass methods and implementations

	/**
	 * The Handler passed will be notified when there is a change in either
	 * the orientation sensor (magnetic compass) or GPS bearing. The message that
	 * the compass dispatches to the Handler will contain which compass was changed
	 * in arg1. The angle can then be obtained using other methods of Compass class
	 * @param lm LocationManager required for getting GPS location.
	 * Can be obtained in an activity using:  <code>getSystemService(LOCATION_SERVICE)</code><br>
	 * @param sm SensorManager required for getting information from orientation sensor.
	 * Can be obtained in an activity using:  <code>getSystemService(SYSTEM_SERVICE)</code><br>
	 * @param handler A handler that will be notified when changes are made
	 * 
	 */
	public Compass(LocationManager lm, SensorManager sm, Handler handler) {
		locationManager = lm;
		sensorManager = sm;
		this.handler = handler; // will be notified when changes occur
		msg = new Message(); // used to notify handler of change
		
		//Sensor.TYPE_ORIENTATION is deprecated but works best
		orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		
	}

	/**
	 * Starts the magnetic compass. Once started, the compass <strong>MUST</strong>
	 * be stopped when not needed or the Compass object will continue to listen 
	 * for changes in the magnetic sensor.
	 */
	public void startMagneticCompass() {
		// Register "this" as listener since Compass class implements the SensorEventListener
		sensorManager.registerListener(this, orientationSensor,SensorManager.SENSOR_DELAY_UI);
	}

	/**
	 * Stops the magnetic compass. This method should be called when use of the 
	 * magnetic compass is no longer needed. 
	 */
	public void stopMagneticCompass() {
		sensorManager.unregisterListener(this);
	}
	
	/**
	 * Starts the GPS compass. Once started, the compass <strong>MUST</strong>
	 * be stopped when not needed or the Compass object will continue to listen 
	 * for changes in the location sensor.
	 */
	public void startGPSCompass(){
		// Register "this" as listener since Compass class implements the LocationListener
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, gpsRate,0, this);
	}
	
	/**
	 * Stops the GPS compass. This method should be called when use of the 
	 * GPS compass is no longer needed. 
	 */
	public void stopGPSCompass(){
		locationManager.removeUpdates(this);
	}
	
	/**
	 * Sets the GPS update rate.
	 * @param rate (ms) The rate at which the compass should check the GPS.
	 * Recommended 5000 - 10000. (Default is 5000)
	 */
	public void setGPSRate(int rate){
		stopGPSCompass(); // stop the GPS compass so rate can be changed
		gpsRate = rate;
		// Start the GPS compass after rate has changed.
		// startGPSCompass uses new rate to request updates
		startGPSCompass(); 
	}
	
	/**
	 * Get the current facing relative to North using magnetic sensor
	 * @return The current facing relative to North in degrees
	 */
	public int getMagDir(){
		// magBearing holds current magnetic bearing
		return magBearing;
	}
	
	/**
	 * Get the current bearing relative to North using consecutive
	 * GPS coordinates
	 * @return The current bearing relative to North in degrees
	 */
	public int getGPSBearing(){
		// the previous location's bearing is the GPS bearing
		return (int) prevLoc.getBearing();
	}
	
	// SensorEventListener method implementations
	
	/* (non-Javadoc)
	 * @see android.hardware.SensorEventListener#onAccuracyChanged(android.hardware.Sensor, int)
	 */
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// unimplemented
	}
	
	/* (non-Javadoc)
	 * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
	 */
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			// msg is dispatched with arg1 = 0 indicating magnetic sensor changed
			msg.arg1 = 0;
			handler.dispatchMessage(msg);
			
			//event.values[0] holds the magnetic compass's azimuth
			magBearing = (int) event.values[0];
			Log.i("COMPASS", "Azimuth: " + magBearing);
		} 
		else {}
	}

	
	// LocationListener method implementations 
	/* (non-Javadoc)
	 * @see android.location.LocationListener#onLocationChanged(android.location.Location)
	 */
	public void onLocationChanged(Location location) {
		Log.i("COMPASS2","Lat: "+location.getLatitude()+" Lon: "+location.getLongitude());
		
		if (prevLoc != null) {
			// calculate the bearing from the previous location to the current
			prevLoc.setBearing(prevLoc.bearingTo(location));
			Log.i("COMPASS2","Bearing: "+prevLoc.getBearing());
			// previous location is a copy of the current location
			// (next time function called current location is previous)
			prevLoc = new Location(location);
			
			// message dispatched with arg1 = 1 indicates GPS change
			msg.arg1 = 1;
			handler.dispatchMessage(msg);
		} 
		else {
			// if the previous location has no values yet
			prevLoc = new Location(location);
		}
	}

	/* (non-Javadoc)
	 * @see android.location.LocationListener#onProviderDisabled(java.lang.String)
	 */
	public void onProviderDisabled(String provider) {
		// unimplemented
	}

	/* (non-Javadoc)
	 * @see android.location.LocationListener#onProviderEnabled(java.lang.String)
	 */
	public void onProviderEnabled(String provider) {
		// unimplemented
	}

	/* (non-Javadoc)
	 * @see android.location.LocationListener#onStatusChanged(java.lang.String, int, android.os.Bundle)
	 */
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// unimplemented
	}
}
