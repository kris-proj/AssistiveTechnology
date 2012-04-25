package com.dharmdeo.addressinput;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

public class AddressRecognizer {
	// Text to speech object to speak
	private TTS tts;
	// Speech to text object to recognize speech
	private Speech2Text s2t;
	// Layout of calling activity. Used for getting swipe inputs
	private LinearLayout UILayout;
	// String to store the address
	private String address;
	// A handler to handle the messages from the speech recognizer
	private Handler handler;
	// Handler on the UI Thread
	private Handler UIHandler;
	/*
	 * A thread that waits for user inputs Required to keep the app from force
	 * closing
	 */
	private Thread background;

	private boolean finishedListeningforAdddress = false;

	// Strings that store the parts of the address
	public String houseNumber = "";
	public String street = "";
	public String city = "";
	public String state = "";

	// Used to figure out the swipe gestures
	public int previousY = 0;

	public boolean success;
	public boolean error = false;

	// Static variables to indicate the current state
	public static final int FINISHED = 12;
	public static final int HOUSENUMBER = 2;
	public static final int STREET = 3;
	public static final int CITY = 4;
	public static final int STATE = 5;
	public static final int QUERY_DECISION = 6;

	// Indicates what is currently being listened for
	public int listeningFor = HOUSENUMBER;

	public AddressRecognizer ar = this;

	/**
	 * @param context
	 *            Context that the Address recognizer will run on
	 * @param UI
	 *            Handler running on the UI thread (main thread of the activity)
	 * @param layout
	 *            Layout of the calling Activity. This is required to implement
	 *            the swipe up and down gestures
	 */
	public AddressRecognizer(Context context, final Handler UI, LinearLayout layout) {
		UILayout = layout;
		UIHandler = UI;
		tts = new TTS(context);
		/*
		 * Implement the handler which will take care of the Speech2Text
		 * messages
		 */
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.arg1) {
				case Speech2Text.FAIL:
					error = true;
					break;
				case Speech2Text.SUCCESS:
					/*
					 * If the speech to text is successful we can save the
					 * results.
					 */
					switch (listeningFor) {
					/*
					 * save to the corresponding string based on what the
					 * AddressRecognizer is listening for
					 */
					case HOUSENUMBER:
						houseNumber = s2t.getResult();
						break;
					case STREET:
						street = s2t.getResult();
						break;
					case CITY:
						city = s2t.getResult();
						break;
					case STATE:
						state = s2t.getResult();
						break;
					}
				case AddressRecognizer.FINISHED:
					if(finishedListeningforAdddress)
						UIHandler.dispatchMessage(msg);
					break;
				}

				synchronized (s2t) {
					/*
					 * Acquire the lock on s2t and notify it that we've finished
					 */
					s2t.notify();
				}

			};
		};

		// Create the speech to text object
		s2t = new Speech2Text(context, handler);
	}

	/**
	 * Start listening for an address. Call getAddress for result (if any)
	 */
	public void listenForAddress() {
		background = new Thread(new Runnable() {

			public void run() {
				synchronized (s2t) {
					address = "";

					// Listening for the different parts of the address
					listenForHouseNumber();
					listenForStreet();
					listenForCity();
					listenForState();

					// Tell the UI Thread we are done
					Message UIMessage = new Message();
					finishedListeningforAdddress = true;
					UIMessage.arg1 = FINISHED;
					address = houseNumber + " " + street + " " + city + " "
							+ state;
					handler.dispatchMessage(UIMessage);

					
				}
			}

			/**
			 * A delay function...
			 */
			public void delay() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			public void listenForHouseNumber() {
				success = false;

				// Indicate that the house number is being listened for
				listeningFor = HOUSENUMBER;
				// Keep trying until user confirms correct input
				while (!success && !error) {
					tts.speak("Please say house number");
					delay();

					/* post the listen runnable to the UI Thread */
					UIHandler.post(listen);

					try {
						s2t.wait();
						// Confirm the recognized text with the user
						tts.speak("Did you say " + houseNumber + "?");
						UIHandler.post(query);
						s2t.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				error = false;

			}

			public void listenForStreet() {
				success = false;
				// Indicate that we are listening for the street
				listeningFor = STREET;
				while (!success && !error) {
					tts.speak("Please say street");
					delay();

					/* post the listen runnable to the UI Thread */
					UIHandler.post(listen);

					try {
						// Confirm the recognized text
						s2t.wait();
						tts.speak("Did you say " + street + "?");
						UIHandler.post(query);
						s2t.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				error = false;

			}

			public void listenForCity() {
				success = false;
				// Indicate that we are listening for the city
				listeningFor = CITY;
				while (!success && !error) {
					tts.speak("Please say city");
					delay();

					/* post the listen runnable to the UI Thread */
					UIHandler.post(listen);

					try {
						// Confirm the recognized text
						s2t.wait();
						tts.speak("Did you say " + city + "?");
						UIHandler.post(query);
						s2t.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				error = false;

			}

			public void listenForState() {
				success = false;
				// Indicate that we are listening for the city
				listeningFor = STATE;
				while (!success && !error) {
					tts.speak("Please say state");
					delay();

					/* post a runnable to the UI Thread */
					UIHandler.post(listen);

					try {
						// Confirm the recognized text
						s2t.wait();
						tts.speak("Did you say " + state + "?");
						UIHandler.post(query);
						s2t.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				error = false;

			}

		});

		// Start the background thread
		background.start();
	}

	/**
	 * @return The address (human readable)
	 */
	public String getAddress() {
		// Return the complete concatenated address
		return address;
	}

	/**
	 * This runnable will be posted to the handler on the UI thread
	 */
	private Runnable listen = new Runnable() {

		public void run() {
			// Start Speech Recognition
			s2t.startListening();

		}
	};

	/**
	 * This runnable will be posted to the handler on the UI thread This
	 * runnable's job is to allow us to determine if the user agrees whether the
	 * recognized speech is accurate or not. It must be run on the UI thread to
	 * attach the OnTouchListener to the main layout.
	 */
	private Runnable query = new Runnable() {

		public void run() {
			// Enable the layout so we can detect touch events
			UILayout.setEnabled(true);

			// Attached a new OnTouchListener
			UILayout.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					/*
					 * We can determine if the user swiped up or down depending
					 * on the motion event performed
					 */
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						/*
						 * If the user touches the screen first, store the
						 * position the touched.
						 */
						previousY = (int) event.getY();
						return true;
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						/*
						 * When the finger is lifted, we can determine if it was
						 * a swipe up or down depending on if the position is
						 * higher or lower. Higher (in value) indicates the user
						 * swiped down. Lower (in value) indicates the user
						 * swiped up. This is because the screen's (0,0)
						 * position is on the top left corner.
						 */
						int currentY = (int) event.getY();
						if (currentY - previousY < 0)
							/*
							 * If the current y position is smaller then the
							 * user swiped up. This indicates that they
							 * confirmed the recognized text The app can then
							 * move on to the next input.
							 */
							success = true;
						else {
							/*
							 * If the y position of the finger up event is
							 * higher than the finger down event, that means the
							 * user swiped down. Therefore, the user has denied
							 * the recognized text. They will then be prompted
							 * again for the same input.
							 */
							success = false;
						}
						synchronized (s2t) {
							s2t.notify();
						}
						UILayout.setEnabled(false);
						return true;
					}
					return false;
				}
			});
		}
	};

	public void finish() {
		/* Destroy the instances of the speec
		 * recognizer and the text to speech
		 * engine
		 */
		s2t.speechRecognizer.destroy();
		tts.tts.shutdown();
	}
}
