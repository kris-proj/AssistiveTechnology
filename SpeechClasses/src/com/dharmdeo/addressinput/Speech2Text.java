package com.dharmdeo.addressinput;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

public class Speech2Text implements RecognitionListener {

	public SpeechRecognizer speechRecognizer;
	private Context context;
	private String result;
	private String TAG = "SpeechRecognition";
	private Handler handler;
	private Message msg;
	boolean isListening;
	public static final int SUCCESS = 1;
	public static final int FAIL = 0;

	/**
	 * @param context context required for SpeechRecognizer. Context can be obtained
	 * from the calling activity
	 * @param handler The handler that is notified of a result. 
	 */
	public Speech2Text(Context context, Handler handler) {
		this.context = context;
		//result = text;
		speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
		speechRecognizer.setRecognitionListener(this);
		this.handler = handler;
		msg = new Message();
		msg.arg1 = FAIL;
	}

	/**
	 * Start the speech recognizer. It will notify the handler when or if 
	 * there are results. 
	 */
	public void startListening() {
		isListening = true;
		// start with an empty string
		result = "";
		
		// setup the intent for speech recognition
		Intent recognizerIntent = new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		
		// calling package is required for SpeechRecognizer
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
				"com.dharmdeo.recognizer");
		
		/* Language model the server will use to process the audio. 
		 * LANGUAGE_MODEL_FREE_FORM - free form language (probably not useful)
		 * LANGUAGE_MODEL_WEB_SEARCH - used for web searches (works for addresses)
		 */
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
		
		// Tell the server to recognize English (US)
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
		speechRecognizer.startListening(recognizerIntent);
	}
	
	/**
	 * @return Returns the results of the speech recognition. This should
	 * be called once the hander is notified of the result. If there are no
	 * results, an empty string is returned.
	 */
	public String getResult(){
		return result;
	}

	/* ======== Implemented RecognitionListener methods ========= */

	public void onBeginningOfSpeech() {
		Log.i(TAG, "Speech started..");
	}

	public void onBufferReceived(byte[] buffer) {
		// unimplemented
	}

	public void onEndOfSpeech() {
		Log.i(TAG,"Speech ended..getting results");
	}

	/*
	 * Log any errors that may occur
	 */
	public void onError(int error) {
		isListening = false;
		msg.arg1 = FAIL;
		handler.dispatchMessage(msg);
		switch (error) {
		case SpeechRecognizer.ERROR_AUDIO:
			Log.e(TAG, "Audio recording error.");
			break;
		case SpeechRecognizer.ERROR_CLIENT:
			Log.e(TAG, "Other client-side error.");
			break;

		case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
			Log.e(TAG, "Insufficient permissions.");
			break;

		case SpeechRecognizer.ERROR_NETWORK:
			Log.e(TAG, "Network related error.");
			break;

		case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
			Log.e(TAG, "Network operation timed out.");
			break;

		case SpeechRecognizer.ERROR_NO_MATCH:
			Log.e(TAG, "No results returned by speech recognizer.");
			break;

		case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
			Log.e(TAG, "The recognition service is already in use.");
			break;

		case SpeechRecognizer.ERROR_SERVER:
			Log.e(TAG, "Server has sent error status.");
			break;

		case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
			Log.e(TAG, "No speech input.");
			break;
		}
	}

	public void onEvent(int eventType, Bundle params) {

	}

	public void onPartialResults(Bundle partialResults) {

	}

	public void onReadyForSpeech(Bundle params) {
		Toast toast = Toast
				.makeText(context, "Speak now..", Toast.LENGTH_SHORT);
		toast.show();

	}

	public void onResults(Bundle results) {
		isListening = false;
		ArrayList<String> data = results
				.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		Toast toast = Toast.makeText(context, data.get(0), Toast.LENGTH_SHORT);
		toast.show();
		Log.i("TEST", data.get(0));
		result = data.get(0);
		msg.arg1 = SUCCESS;
		handler.dispatchMessage(msg);
	}

	public void onRmsChanged(float rmsdB) {
		// unimplemented
	}

}
