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
				String stuff;
				switch (msg.arg1) {
				case WeatherBug.CURRENT:
					stuff = wb.getCity() + "\n\n" + "Current Temp: "
							+ wb.getHourlyForecast().get(0).getTemp();
					tv.setText(stuff);
					tv.invalidate();
					break;
				case WeatherBug.FORECAST:
					stuff = wb.getCity() + "\n\n";
					fiveDayForecast = wb.get5DayForecast();
					for (int i = 0; i < fiveDayForecast.size(); i++) {
						stuff += fiveDayForecast.get(i).toString() + "\n\n";
					}
					tv.setText(stuff);
					break;
				}
				super.handleMessage(msg);
			}
		}, this);

		wb.updateCurrentWithLoc();
	}
}