package se.agile.activities.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class HttpConnection extends AsyncTask<String, Void, Void>{
	
	private static final String logTag = "gitIntegration";
	 public static String OAUTH_URL = "https://github.com/login/oauth/authorize";
	    public static String OAUTH_ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
	    
	    public static String CLIENT_ID = "387b05f90574b6fede43";
	    public static String CLIENT_SECRET = "557392acf8c742ac6e6a3a4ff36b172f378c1633";
	    public static String CALLBACK_URL = "http://localhost";

	@Override
	protected Void doInBackground(String... params) {
		Log.d(logTag, "Param to asynctask httpconnection: " + params[0]);
//		SchemeRegistry schemeRegistry = new SchemeRegistry();
//		schemeRegistry.register(new Scheme("https", 
//		            SSLSocketFactory.getSocketFactory(), 508));
//
//		HttpParams param = new BasicHttpParams();
//
//		SingleClientConnManager mgr = new SingleClientConnManager(param, schemeRegistry);
//
//		HttpClient client = new DefaultHttpClient(mgr, param);
		HttpClient client = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(OAUTH_ACCESS_TOKEN_URL);
	    try {
	        // Add your data
	    	/**
	    	
	    	
	    	***/
	    	JSONObject holder = new JSONObject();
	    	try {
	    		holder.put("client_id", CLIENT_ID);
		    	holder.put("client_secret", CLIENT_SECRET);
				holder.put("code", params[0]);
			} catch (JSONException e1) {
				Log.d(logTag,"JSON ERROR");
				e1.printStackTrace();
			}
            StringEntity se = new StringEntity( holder.toString());  
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httppost.setEntity(se);
            Log.d(logTag, "Holder json: " + holder.toString());
	        
//	    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//	        nameValuePairs.add(new BasicNameValuePair("client_id", CLIENT_ID));
//	        nameValuePairs.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));
//	        nameValuePairs.add(new BasicNameValuePair("code", params[0]));
//	        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
//	        entity.setContentType("application/json");
////	        httppost.setHeader("Accept", "application/json");
//	        httppost.setHeader("Content-type", "application/json; charset=UTF-8");
//	        httppost.setEntity(entity);
	        
	        //"application/json"
	        // Execute HTTP Post Request
	        Log.d(logTag, "client.execute");
	        Log.d(logTag, "Post: " + getString(httppost));
	        HttpResponse response = client.execute(httppost);
	        Log.d(logTag, "response: " + response.toString());
	        HttpEntity respEntity = response.getEntity();
	        Log.d(logTag, "response Entity Utils: " + EntityUtils.toString(respEntity));
	        for(Header header : response.getAllHeaders()){
	        	Log.d(logTag,"Header: " + header.getName() + " value: " + header.getValue());
	        }
//	        BufferedReader reader = new BufferedReader(new InputStreamReader(respEntity.getContent(), "UTF-8"));
//	        String json = reader.readLine();
//        	Log.d(logTag, "json= " + json);
//        	JSONTokener tokener = new JSONTokener(json);
//	        try {
//				JSONArray finalResult = new JSONArray(tokener);
//				Log.d(logTag, "json array result: " + finalResult);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	        

	        
	    } catch (ClientProtocolException e) {
	    	Log.d(logTag, "Error in testBrowser2");
	    } catch (IOException e) {
	    	Log.d(logTag, "Error in testBrowser3");
	    }
////	    StringBuilder builder = new StringBuilder();
//		
//	    HttpClient client = new DefaultHttpClient();
//	    HttpGet httpGet = new HttpGet("https://github.com/login/oauth/authorize?client_id=387b05f90574b6fede43&redirect_uri=princepolo://oauthresponse");
//	    //client_id=387b05f90574b6fede43
//	    //response code: 90ca25b8c464fe24ef47
//	    //https://github.com/login/oauth/access_token?client_id=387b05f90574b6fede43&client_secret=557392acf8c742ac6e6a3a4ff36b172f378c1633&code=96cf4141dffdc1e6ea35
//	    //access_token=ccd1a34fd121b268d05a4452e3109722072cfb83&scope=&token_type=bearer
//	    Log.d(logTag, "");
//	    
//	    //--------------------------------
////	    HttpPost httppost = new HttpPost("http://www.yoursite.com/script.php");
////	    try {
////	        // Add your data
////	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
////	        nameValuePairs.add(new BasicNameValuePair("id", "12345"));
////	        nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
////	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
////
////	        // Execute HTTP Post Request
////	        HttpResponse response = client.execute(httppost);
////	        
////	    } catch (ClientProtocolException e) {
////	        // TODO Auto-generated catch block
////	    } catch (IOException e) {
////	        // TODO Auto-generated catch block
////	    }
//	    //--------------------------
//	    
//	    
//	    try {
//	    	Log.d(logTag, "Execute http get");
//	    	HttpResponse response = client.execute(httpGet);
//	    	StatusLine statusLine = response.getStatusLine();
//	    	Log.d(logTag, "responseline: " + statusLine.getReasonPhrase());
//	      /**
//
//OAuthClientRequest request = null;
//request = OAuthClientRequest
//       .authorizationLocation(authenticationUrl)
//       .setClientId("<your client id>").setRedirectURI("<your redirect url>")
//        .buildQueryMessage();
// 
//Intent intent = new Intent(Intent.ACTION_VIEW,
//        Uri.parse(request.getLocationUri() + "&response_type=code"));
//startActivity(intent);
//
//
//<intent-filter>
//    <action android:name="android.intent.action.VIEW"/>
//    <category android:name="android.intent.category.DEFAULT" />
//    <category android:name="android.intent.category.BROWSABLE"/>
//    <data android:scheme="myapp" android:host="oauthresponse"/>
//</intent-filter>
//
//@Override
//protected void onNewIntent(Intent intent)
//{
//    Uri uri = intent.getData();
// 
//   if (uri != null && uri.toString()
//           .startsWith("myapp://oauthresponse"))
//    {
//        String code = uri.getQueryParameter("code");
//        // ...
//   }
//}
//
//OAuthClientRequest request = null;
// 
//request = OAuthClientRequest.tokenLocation("<service request URL>")
//    .setGrantType(GrantType.AUTHORIZATION_CODE)
//   .setClientId("<your client id>")
//    .setClientSecret("<your client secret>")
//    .setRedirectURI("myapp://oauthresponse)
//    .setCode(code)
//    .buildBodyMessage();
// 
//OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
// 
//OAuthJSONAccessTokenResponse response = oAuthClient.accessToken(request);
//String token = response.getAccessToken();
//
//	       */
//	      
//	      
//	      
////	      int statusCode = statusLine.getStatusCode();
////	      	
////	      if (statusCode == 200) {
////	        HttpEntity entity = response.getEntity();
////	        
////	        InputStream content = entity.getContent();
////	        
////	        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
////	        
////	        String line;
////	        
////	        while ((line = reader.readLine()) != null) {
////	          builder.append(line);
////	        }
////	      } else {
////	        Log.e(logTag, "Failed to download file");
////	      }
//	    } catch (ClientProtocolException e) {
//	      e.printStackTrace();
//	    } catch (IOException e) {
//	      e.printStackTrace();
//	    }
////	    return builder.toString();
//		return null;
		return null;
	}

	private String getString(HttpPost post){
		StringBuilder builder = new StringBuilder();
		for(Header header : post.getAllHeaders()){
			builder.append("HeaderName: "+header.getName() + " HeaderValue: "+ header.getValue() + "    ");
		}
		
		builder.append("Entity: " + post.getEntity().toString() + "    ");
		builder.append("Method: " + post.getMethod().toString() + "    ");
		builder.append("params: " + post.getParams().toString() + "    ");
		builder.append("Uri: "+ post.getURI().toString() + " Scheme: "+ post.getURI().getScheme() + " Path: "+post.getURI().getPath()+" Host: "+post.getURI().getHost()+ "    ");
		builder.append("RequestLine URI: " + post.getRequestLine().getUri() + " RequestLine: " + post.getRequestLine().toString() + "    ");
		builder.append("ProtocolVersion: " + post.getProtocolVersion().toString() + "    ");
		return builder.toString();
	}

}
