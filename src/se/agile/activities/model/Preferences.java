package se.agile.activities.model;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Log;

public class Preferences {
	private static String PREF_STRINGS_NAME = "princepolo";
    private static String logTag = "PrincePolo"; 
    private static SharedPreferences prefs;
    
    public static void initializePreferences(Context context){
    	prefs = context.getSharedPreferences(PREF_STRINGS_NAME, Context.MODE_PRIVATE); 
    }
    
    private static boolean isInitialized(){
    	return prefs != null;
    }
    
    private static String ACCESS_TOKEN = "access_token";
    
    public static void setAccessToken(String access_token) {
    	if(isInitialized()){
    		Editor edit = prefs.edit();
            edit.putString(ACCESS_TOKEN, access_token);
            edit.apply();
            Log.d(logTag,"saved " + ACCESS_TOKEN + ": " + access_token + " to preferences");
    	}else{
    		Log.e(logTag, "Preferences is not initialized");
    	}
    }

    public static String getAccessToken() {
    	if(isInitialized()){
    		String access_token = prefs.getString(ACCESS_TOKEN, "");
            if(access_token.equals("")){
            	access_token = "Empty";
            	Log.e(logTag, "You have no " + ACCESS_TOKEN + " saved in prefereneces");
            }else{
            	Log.d(logTag, "Got " + ACCESS_TOKEN + " from preferences: " + access_token);
            }
            return access_token;
    	}else{
    		Log.e(logTag, "Preferences is not initialized");
    		return "Error, preferences is not initialized";
    	}
    }
    
    private static String TOKEN_TYPE = "token_type";
    
    public static void setTokenType(String token_type) {
    	if(isInitialized()){
    		Editor edit = prefs.edit();
            edit.putString(TOKEN_TYPE, token_type);
            edit.apply();
            Log.d(logTag,"saved " + TOKEN_TYPE + ": " + token_type + " to preferences");
    	}else{
    		Log.e(logTag, "Preferences is not initialized");
    	}
    }

    public static String getTokenType() {
    	if(isInitialized()){
    		String token_type = prefs.getString(TOKEN_TYPE, "");
            if(token_type.equals("")){
            	token_type = "Empty";
            	Log.e(logTag, "You have no " + TOKEN_TYPE + " saved in prefereneces");
            }else{
            	Log.d(logTag, "Got " + TOKEN_TYPE + " from preferences: " + token_type);
            }
            return token_type;
    	}else{
    		Log.e(logTag, "Preferences is not initialized");
    		return "Error, preferences is not initialized";
    	}
    }
    
    private static String SCOPE = "scope";
    
    public static void setScope(String scope) {
    	if(isInitialized()){
    		Editor edit = prefs.edit();
            edit.putString(SCOPE, scope);
            edit.apply();
            Log.d(logTag,"saved " + SCOPE + ": " + scope + " to preferences");
    	}else{
    		Log.e(logTag, "Preferences is not initialized");
    	}
    }

    public static String getScope() {
    	if(isInitialized()){
    		String scope = prefs.getString(SCOPE, "");
	        if(scope.equals("")){
	        	scope = "Empty";
	        	Log.e(logTag, "You have no " + SCOPE + " saved in prefereneces");
	        }else{
	        	Log.d(logTag, "Got " + SCOPE + " from preferences: " + scope);
	        }
	        return scope;
    	}else{
    		Log.e(logTag, "Preferences is not initialized");
    		return "Error, preferences is not initialized";
    	}
    }
    
    public static String getClientId() {
    	return "387b05f90574b6fede43";
    }
    
    public static String getClientSecret() {
    	return "557392acf8c742ac6e6a3a4ff36b172f378c1633";
    }
    
    private static ArrayList<PreferenceListener> prefListener = new ArrayList<PreferenceListener>();
    private static OnSharedPreferenceChangeListener onChangeListern; // Need to keep this reference, otherwise we don't get any events....
    //Wrapping the OnSharedPreferenceChangeListener since i dont want to give away the sharedpreferences....
    public static void addListener(PreferenceListener listener){
    	Log.e(logTag, "trying to add listener");
    	if(isInitialized()){
    		prefListener.add(listener);
    		if(onChangeListern == null){
    			Log.e(logTag, "add listener");
    			onChangeListern = new OnSharedPreferenceChangeListener() {
    				public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
    					Log.d(logTag, "Listener event recieved Preference");
    					for(PreferenceListener list : prefListener){
    	          	    	list.preferenceChanged(key);
    	          	    }
    				}
    			};
    			prefs.registerOnSharedPreferenceChangeListener(onChangeListern);
    		}
    	}else{
    		Log.e(logTag, "Preferences is not initialized");
    	}
    }
}

