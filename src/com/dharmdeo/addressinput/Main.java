package com.dharmdeo.addressinput;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 * ===============================================================
 * This activity does not require any user interaction to start up.
 * It will be called from another activity for results. The result
 * will be the recognized address which the user will speak. The 
 * address will be passed back via an Intent with an Extra String 
 * with the name "address". The user will be prompted via synthesized
 * speech to speak the various parts of an address. They will then
 * be asked to confirm the recognized part. Should they not confirm,
 * they will be prompted again until the Recognizer gets an accurate
 * result. 
 * ================================================================
 */

public class Main extends Activity{
    private TextView text;
    private String address;
    private Button button;
    private Button speak;
    private AddressRecognizer ar;
    private Handler handler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        text = (TextView) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);
        speak = (Button) findViewById(R.id.button1);
        
        /*
         * Layout that will take care of all the swipe up and down
         * controls. This is passed to the AddressRecognizer. 
         */
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        
        
        /*
         * Handler required by the AddressRecognizer to perform tasks
         * that must be run on the UI (getting user input via swipe
         * controls. The handler is also notified when the AddressRecognizer
         * is finished. 
         */
        handler = new Handler(){
        	@Override
        	public void handleMessage(Message msg) {
        		Log.i("TEST","Message received");
        		/*
        		 * If the message received indicates that the AddressRecognizer
        		 * is complete, we can handle the message accordingly.
        		 */
        		if(msg.arg1 == AddressRecognizer.FINISHED){
        			/*
        			 * The Activity should end now. This should be done via
        			 * the finish method on the UI thread. Therefore a new
        			 * runnable is run on the UI Thread to take care of passing
        			 * the result and finishing the activity
        			 */
        			runOnUiThread(test);
        		}
        		super.handleMessage(msg);
        	}
        	
        };
        
        /*
         * Instantiate the AddressRecogizer object
         */
        ar = new AddressRecognizer(this, handler,layout);
        
        
        try {
        	/*
        	 * Temporary fix. This allows the TextToSpeech
        	 * engine enough time to start up.
        	 */
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        /*
         * Start the AddressRecognition right away.
         */
        ar.listenForAddress();
        
        button.setClickable(false);
		speak.setClickable(false);
		
        
/*        button.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				button.setClickable(false);
				speak.setClickable(false);
				ar.listenForAddress();
				button.setClickable(true);
				speak.setClickable(true);
			}
		});
        
        speak.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				button.setClickable(false);
				speak.setClickable(false);
				address = ar.getAddress();
				text.setText(address);
				tts.speak(address);
				button.setClickable(true);
				speak.setClickable(true);
			}
		});*/
        
    }
    
    
    /*
     * (non-Javadoc)
     * @see android.app.Activity#onDestroy()
     * The onDestroy method must be overridden to ensure the
     * resources used by the AddressRecognizer (TextToSpeech
     * and SpeechToText engines) are released. This is done by
     * calling the AddressRecognizer's finish method.
     */
    protected void onDestroy() {
    	super.onDestroy();
    	// Frees up all resources
    	ar.finish();
    };
    
    
    /*
     * A Runnable that will be run on the UI thread which will
     * take care of passing the address back to the Activity which
     * started this activity for a result. After setting the result,
     * the Activity will then end.
     */
    Runnable test = new Runnable() {
		
		public void run() {
			// ===== Testing =====
			address = ar.getAddress();
			text.setText(address);
			// ===================
			
			/*
			 * Create an Intent to pass back to the calling activity.
			 * Put the address into the intent as an Extra. Set the 
			 * result and finish the activity.
			 */
			Intent intent = new Intent();
			intent.putExtra("address", address);
			setResult(RESULT_OK,intent);
			finish();
		}
	};
}