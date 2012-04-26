package com.dharmdeo.compass;

import android.app.Activity;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class Main extends Activity {
	
	Compass compass;
	Handler handler;
	int GPSBearing;
	int magneticBearing;
	TextView deg1,deg2,nsew1,nsew2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        
        handler = new Handler(){
        	@Override
        	public void handleMessage(Message msg) {
        		super.handleMessage(msg);
        		switch(msg.arg1){
        		case 0:
        			//Log.i("handler","notified that sensor changed");
        			magneticBearing = compass.getMagDir();
        			break;
        		case 1:
        			GPSBearing = compass.getGPSBearing();
        			//Log.i("handler","notified that location changed");
        			break;
        		}
        		
        		updateUI();
        	}

        };
        
        compass = new Compass(lm,sm,handler);
        compass.setGPSRate(10000);
        
        deg1 = (TextView) findViewById(R.id.deg1);
        deg2 = (TextView) findViewById(R.id.deg2);
        nsew1 = (TextView) findViewById(R.id.nsew1);
        nsew2 = (TextView) findViewById(R.id.nsew2);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	compass.startMagneticCompass();
    	compass.startGPSCompass();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	compass.stopMagneticCompass();
    	compass.stopGPSCompass();
    }
    
    private void updateUI() {
		deg1.setText(magneticBearing+" degrees");
		deg2.setText(GPSBearing+" degrees");
		
		if(magneticBearing == 0){
			nsew1.setText("N");
		}
		else if(magneticBearing>0 && magneticBearing<90){
			nsew1.setText("NE");
		}
		else if(magneticBearing==90){
			nsew1.setText("E");
		}
		else if(magneticBearing>90 && magneticBearing<180){
			nsew1.setText("SE");
		}
		else if(magneticBearing==180){
			nsew1.setText("S");
		}
		else if(magneticBearing>180 && magneticBearing<270){
			nsew1.setText("SW");
		}
		else if(magneticBearing==270){
			nsew1.setText("W");
		}
		else if(magneticBearing>270 && magneticBearing<360){
			nsew1.setText("NW");
		}
		
		
		if(GPSBearing == 0){
			nsew2.setText("N");
		}
		else if(GPSBearing>0 && GPSBearing<90){
			nsew2.setText("NE");
		}
		else if(GPSBearing==90){
			nsew2.setText("E");
		}
		else if(GPSBearing>90 && GPSBearing<180){
			nsew2.setText("SE");
		}
		else if(GPSBearing==180){
			nsew2.setText("S");
		}
		else if(GPSBearing>180 && GPSBearing<270){
			nsew2.setText("SW");
		}
		else if(GPSBearing==270){
			nsew2.setText("W");
		}
		else if(GPSBearing>270 && GPSBearing<360){
			nsew2.setText("NW");
		}
	}
    
}