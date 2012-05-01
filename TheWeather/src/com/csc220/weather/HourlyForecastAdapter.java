package com.csc220.weather;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class HourlyForecastAdapter extends ArrayAdapter<HourlyForecast> {
	private int layoutResourceID; // the resource id of the custom row
	private Context context; // the activity context to get inflater
	private ArrayList<HourlyForecast> hourlyData; // a list of the hourly data
	private static TextView time, temp;

	public HourlyForecastAdapter(Context context, int layoutResourceID, ArrayList<HourlyForecast> objects) {
		super(context, layoutResourceID, objects);
		
		// Make copies of the input parameters for later use
		this.layoutResourceID = layoutResourceID;
		this.context = context;
		hourlyData = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView; // A View for each row
		if(row == null){
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceID, parent, false);
			// Get the corresponding textviews for time and temperature
			time = (TextView) row.findViewById(R.id.time);
			temp = (TextView) row.findViewById(R.id.temp);
		}
		else{
			// dunno
		}
		HourlyForecast hour = hourlyData.get(position);
		
		// Set the time and temperature for the corresponding row
		time.setText(hour.getTime());
		temp.setText(hour.getTemp());
	
		return row;
	}

}
