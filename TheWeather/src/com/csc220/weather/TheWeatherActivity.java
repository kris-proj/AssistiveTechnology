package com.csc220.weather;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class TheWeatherActivity extends Activity {
	private WeatherBug wb;
	private ArrayList<DailyForecast> fiveDayForecast;
	private ArrayList<HourlyForecast> hourlyForecast;
	private TextView tv;
	ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		
		// A handler is needed to receive messages from weatherbug object
		// It must have the handleMessage method overidden
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.arg1) {
				case WeatherBug.CURRENT:
					currentUpdated();
					break;
				case WeatherBug.FORECAST:
					forecastUpdated();
					break;
				}
				super.handleMessage(msg);
			}
		};

		wb = new WeatherBug(handler, this);
		
		lv = (ListView) findViewById(R.id.listview);
		
		wb.updateCurrentWithZip("10001");
	}

	public void currentUpdated() {
		// The hourly forecast can be obtained with this method call. It
		// returns an array list of hourly forecasts

		hourlyForecast = wb.getHourlyForecast();
		HourlyForecastAdapter adapter = new HourlyForecastAdapter(this, R.layout.hourly_listview_row, hourlyForecast);
		lv.setAdapter(adapter);
		
	}

	public void forecastUpdated() {
		// The weekly forecast is returned as an arraylist of daily forecasts
		fiveDayForecast = wb.get5DayForecast();

		String mock;
		mock = wb.getCity() + "\n";
		DailyForecast today = fiveDayForecast.get(0);
		mock += "Today: " + today.getTitle() + " " + today.getHigh() + "/"
				+ today.getLow() + "\n\n";
		mock += "The next 4 days: \n\n";
		DailyForecast day;
		for (int i = 1; i < 5; i++) {
			day = fiveDayForecast.get(i);
			mock += day.getTitle() + " " + day.getHigh() + "/" + day.getLow()
					+ "\n\n";
		}
		tv.setText(mock);
	}
}