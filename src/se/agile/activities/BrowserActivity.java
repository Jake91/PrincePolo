package se.agile.activities;

import se.agile.activities.model.HttpConnection;
import se.agile.princepolo.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BrowserActivity extends Activity {
	public static String OAUTH_URL = "https://github.com/login/oauth/authorize";
    public static String OAUTH_ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
    public static String CLIENT_ID = "387b05f90574b6fede43";
    public static String CLIENT_SECRET = "557392acf8c742ac6e6a3a4ff36b172f378c1633";
    public static String CALLBACK_URL = "princepolo://oauthresponse";//"http://localhost";
    
	private static final String logTag = "gitIntegration";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browser);
		Log.d(logTag, "new intent started: testbrowseractivity");
		String url = OAUTH_URL + "?client_id=" + CLIENT_ID + "&redirect_uri=" + CALLBACK_URL;
		
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
            	synchronized (this) { //Seems to be threading calling the method since i got serveral HttpConnections running....
            		if(url.matches("princepolo://oauthresponse\\?code=[\\dA-z]+") && !isPostSent) {
    	        		String accessCode = url.split("code=")[1];
    	        		Log.d(logTag, "accessCode: " + accessCode);
                		HttpConnection conn = new HttpConnection();
                		conn.execute(accessCode);
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
