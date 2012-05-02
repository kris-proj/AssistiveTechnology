package com.csc220.weather;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TheWeatherActivity extends Activity {
	private WeatherBug wb;
	private ArrayList<DailyForecast> fiveDayForecast;
	private ArrayList<HourlyForecast> hourlyForecast;
	private ArrayList<WeatherAdvisory> weatherAdvisory;
	TextView tv;
	ListView lv;
	ImageView iv;
	ImageView iv2;
	TextView tv2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		tv = (TextView) findViewById(R.id.text);
		iv = (ImageView) findViewById(R.id.imageView1);
		tv2 = (TextView) findViewById(R.id.text2);
		iv2 = (ImageView) findViewById(R.id.imageView2);
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
				case WeatherBug.ADVISORY:
					weatherAdvisory = wb.getAdvisories();
					int length = weatherAdvisory.size();
					String mock = wb.getCity() + "\n\n";
					for(int i = 0;i<length;i++){
						mock += weatherAdvisory.get(i).toString() + "\n\n";
					}
					tv.setText(mock);
					break;
				}
				super.handleMessage(msg);
			}
		};

		wb = new WeatherBug(handler, this);
		
		//lv = (ListView) findViewById(R.id.listview);
		
		wb.updateForecastWithZip("10001");
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
		iv.setImageBitmap(fiveDayForecast.get(0).getDayImage());
		tv.setText(fiveDayForecast.get(0).getTitle());
		iv2.setImageBitmap(fiveDayForecast.get(0).getNightImage());
		tv2.setText(fiveDayForecast.get(0).getTitle() + " Night");
	}
}