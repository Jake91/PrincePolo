package se.agile.activities;

import se.agile.princepolo.R;
import se.agile.princepolo.R.layout;
import se.agile.princepolo.R.menu;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class BrowserActivity extends Activity {

	private static final String logTag = "gitIntegration";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browser);
		Log.d(logTag, "new intent started: browseractivity");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.browser, menu);
		return true;
	}
	@Override
	protected void onStart(){
		Log.d(logTag, "onStart");
		super.onStart();
	}
	@Override
    protected void onRestart(){
		Log.d(logTag, "onRestart");
		super.onRestart();
    }
	@Override
    protected void onResume(){
		Log.d(logTag, "onResume: ");
		Uri uri = this.getIntent().getData();
		Log.d(logTag, "Repsonse" + uri.toString());
		if (uri != null && uri.toString().startsWith("princepolo://oauthresponse")){
			String code = uri.getQueryParameter("code");
			Log.d(logTag, "code recieved: " + code);
	   }else{	
		   Log.d(logTag, "error in uri");
	   }
		super.onResume();
    }
	@Override
    protected void onPause(){
		Log.d(logTag, "onPause");
		super.onPause();
    }
	@Override
    protected void onStop(){
		Log.d(logTag, "onStop");
		super.onStop();
    }
	@Override
    protected void onDestroy(){
    	Log.d(logTag, "onDestroy");
    	super.onDestroy();
    }
	
	@Override
	protected void onNewIntent(Intent intent){
		Log.d(logTag, "On new intent : browseractivity");
		Uri uri = intent.getData();
		Log.d(logTag, "get uri data");
		Log.d(logTag, "uri recieved: " + uri.toString());
		if (uri != null && uri.toString().startsWith("princepolo://oauthresponse")){
			String code = uri.getQueryParameter("code");
			
			Log.d(logTag, "code recieved: " + code);
	   }else{	
		   Log.d(logTag, "error in uri");
	   }
	}

}
