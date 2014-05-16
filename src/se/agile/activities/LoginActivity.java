package se.agile.activities;

import se.agile.asynctasks.RequestAccessToken;
import se.agile.asynctasks.RequestListener;
import se.agile.asynctasks.RequestRepositories;
import se.agile.asynctasks.RequestUser;
import se.agile.model.Preferences;
import se.agile.princepolo.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LoginActivity extends Activity implements RequestListener<String>{
	private static String OAUTH_URL = "https://github.com/login/oauth/authorize";
    private static String CALLBACK_URL = "princepolo://oauthresponse";
    
	private String logTag;
	
	private RequestAccessToken accessTokenThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		logTag = getResources().getString(R.string.logtag_main);
		if(MainActivity.isNetworkConnected()){
			startWebView();
		}else{
			whenNoInternetConnection();
		}
		
		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();	
	}
	
	private void startWebView(){
		String url = OAUTH_URL + "?client_id=" + Preferences.getClientId() + "&redirect_uri=" + CALLBACK_URL;
		accessTokenThread = new RequestAccessToken(this);
		WebView webview = (WebView)findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
		WebSettings ws = webview.getSettings();
		ws.setSaveFormData(false);
        webview.setWebViewClient(new WebViewClient() {
        	@Override
            public void onPageFinished(WebView view, String url) {
        		super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
            	return super.shouldOverrideUrlLoading(view, url);
            }
            private boolean isPostSent;
        	@Override
        	public void onPageStarted(WebView view, String url, Bitmap favicon) {
                String accessCodeFragment = "code=";
            	// We hijack the GET request to extract the OAuth parameters
            	synchronized (this) { //Seems to be threading calling the method since i got several HttpConnections running....
            		if(url.matches("princepolo://oauthresponse\\?code=[\\dA-z]+") && !isPostSent) {
    	        		String accessCode = url.split("code=")[1];
    	        		accessTokenThread.execute(accessCode);
                		isPostSent = true;
    	        	}
                	if(isPostSent){
                		Preferences.setIsFirstTime(false);
                		finish();
                		overridePendingTransition(0, 0); //Removes the animation
                	}
				}        		
            }});
        webview.loadUrl(url);
	}

	@Override
	public void requestFinished(String result) {
		new RequestUser().execute();
		new RequestRepositories().execute();
	}

	@Override
	public void whenNoInternetConnection() {
		MainActivity.hasNoInternetConnection(this);
	}

	@Override
	public void whenNoSelectedRepository() {
		
	}
	
	@Override
	public void onBackPressed() {
		Log.d(logTag, "onbackpressed");
	     Intent startMain = new Intent(Intent.ACTION_MAIN);
	        startMain.addCategory(Intent.CATEGORY_HOME);
	        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        startActivity(startMain);

	}

	@Override
	public void requestUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestFailed() {
		// TODO Auto-generated method stub
		
	}
}
