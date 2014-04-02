package se.agile.activities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import se.agile.activities.model.HttpConnection;
import se.agile.princepolo.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TestBrowserActivity extends Activity {
	 public static String OAUTH_URL = "https://github.com/login/oauth/authorize";
    public static String OAUTH_ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
    
    public static String CLIENT_ID = "387b05f90574b6fede43";
    public static String CLIENT_SECRET = "557392acf8c742ac6e6a3a4ff36b172f378c1633";
    public static String CALLBACK_URL = "http://localhost";
    
	private static final String logTag = "gitIntegration";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_browser);
		Log.d(logTag, "new intent started: testbrowseractivity");
		
//		WebView browser = (WebView) findViewById(R.id.webview);
//		browser.loadUrl("http://www.google.com");
		String url = OAUTH_URL + "?client_id=" + CLIENT_ID;
		
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
            private boolean once = true;
        	@Override
        	public void onPageStarted(WebView view, String url, Bitmap favicon) {
                String accessTokenFragment = "access_token";
                String accessCodeFragment = "code=";
                
            	// We hijack the GET request to extract the OAuth parameters
            	Log.d(logTag, "OnPageStarted url: " + url);
//	        	if (url.contains(accessTokenFragment)) {
//	        		// the GET request contains directly the token
//	        		String accessToken = url.substring(url.indexOf(accessTokenFragment));
//	        		//TokenStorer.setAccessToken(accessToken);
//	        		Log.d(logTag,"accessToken: " + accessToken);
//	        		
//	        		
//	                
//					
//	        	} else 
	        		if(url.contains(accessCodeFragment) && once) {
	        		// the GET request contains an authorization code
	        		String accessCode = url.substring(url.indexOf(accessCodeFragment)+5);//+5 in order to remove 'code='
//            		TokenStorer.setAccessCode(accessCode);
	        		Log.d(logTag, "accessCode: " + accessCode);
	        		
            		String query = "client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&" + accessCode;
            		Log.d(logTag, "Query: " + query);
            		
            		HttpConnection conn = new HttpConnection();
            		conn.execute(accessCode);
            		once =false;
//            		Log.d(logTag, "finish activity");
//            		view.loadUrl(OAUTH_ACCESS_TOKEN_URL + "?" + query);
//            		view.postUrl(OAUTH_ACCESS_TOKEN_URL, query.getBytes());
	        	}else{
	        		Log.d(logTag, "Error in testBrowser");
	        	}
            }});
        webview.loadUrl(url);
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
//		Uri uri = this.getIntent().getData();
//		Log.d(logTag, "Repsonse" + uri.toString());
//		if (uri != null && uri.toString().startsWith("princepolo://oauthresponse")){
//			String code = uri.getQueryParameter("code");
//			Log.d(logTag, "code recieved: " + code);
//	   }else{	
//		   Log.d(logTag, "error in uri");
//	   }
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
	

}
