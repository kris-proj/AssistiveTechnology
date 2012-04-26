package com.csc220.weather;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class TheWeatherActivity extends Activity {
	private WeatherBug wb;
	private ArrayList<DailyForecast> fiveDayForecast;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		final TextView tv = (TextView) findViewById(R.id.text);
		wb = new WeatherBug(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.arg1) {
				case WeatherBug.CURRENT:
					tv.setText("Current Temp: " + wb.getTemp() + " Desc: "
							+ wb.getDescription());
					tv.invalidate();

				case WeatherBug.FORECAST:
					break;
				}
				super.handleMessage(msg);
			}
		}, this);

		wb.updateForecastWithLoc();
	}
}