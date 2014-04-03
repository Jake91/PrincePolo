package se.agile.activities;

import se.agile.activities.model.HttpConnection;
import se.agile.activities.model.Preferences;
import se.agile.activities.model.HttpConnection.URL;
import se.agile.princepolo.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BrowserActivity extends Activity {
	private static String OAUTH_URL = "https://github.com/login/oauth/authorize";
    private static String CALLBACK_URL = "princepolo://oauthresponse";//"http://localhost";
    
	private String logTag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browser);
		logTag = getResources().getString(R.string.logtag_main);
		Log.d(logTag, "new intent started: testbrowseractivity");
		String url = OAUTH_URL + "?client_id=" + Preferences.getClientId() + "&redirect_uri=" + CALLBACK_URL;
		
		WebView webview = (WebView)findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
        	@Override
            public void onPageFinished(WebView view, String url) {
        		Log.d(logTag, "onPageFinished url: " + url);
        		super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
            	Log.d(logTag, "shouldOverrideUrlLoading url: " + url);
            	return super.shouldOverrideUrlLoading(view, url);
            }
            private boolean isPostSent;
        	@Override
        	public void onPageStarted(WebView view, String url, Bitmap favicon) {
                String accessCodeFragment = "code=";
            	// We hijack the GET request to extract the OAuth parameters
            	Log.d(logTag, "OnPageStarted url: " + url);
            	synchronized (this) { //Seems to be threading calling the method since i got several HttpConnections running....
            		if(url.matches("princepolo://oauthresponse\\?code=[\\dA-z]+") && !isPostSent) {
    	        		String accessCode = url.split("code=")[1];
    	        		Log.d(logTag, "accessCode: " + accessCode);
    	        		AsyncTask<String, Void, Void> aTask = new AsyncTask<String, Void, Void>(){
    	        			@Override
    	        			protected Void doInBackground(String... params) {
    	        				HttpConnection.requestAccessToken(params[0]);
    	        				HttpConnection.getRequestGeneral(URL.GET_USER);
    							HttpConnection.getRequestGeneral(URL.GET_USER_REPOS);
    	        				return null;
    	        			}
    	        		};
                		aTask.execute(accessCode);
                		isPostSent = true;
    	        	}else{
    	        		Log.d(logTag, "Url don't match \"princepolo://oauthresponse\\?code=[\\dA-z]+\"");
    	        	}
				}
            	if(isPostSent){
            		finish();
            	}
        		
            }});
        webview.loadUrl(url);
	}
}
