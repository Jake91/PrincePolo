package se.agile.activities.model;

import java.util.ArrayList;

import se.agile.activities.model.GitHubData.Repository;
import se.agile.activities.model.GitHubData.User;

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
    
    private static void setGeneral(PREF_KEY key, String value){
    	if(isInitialized()){
    		Editor edit = prefs.edit();
            edit.putString(key.getKey(), value);
            edit.apply();
            Log.d(logTag,"saved " + key + ": " + value + " to preferences");
    	}else{
    		Log.e(logTag, "Preferences is not initialized");
    	}
    }
    private static String getGeneral(PREF_KEY key) {
    	if(isInitialized()){
    		String value = prefs.getString(key.getKey(), "");
            if(value.equals("")){
            	Log.e(logTag, "You have no " + key + " saved in prefereneces");
            }else{
            	Log.d(logTag, "Got " + key + " from preferences: " + value);
            }
            return value;
    	}else{
    		Log.e(logTag, "Preferences is not initialized");
    		return "Error, preferences is not initialized";
    	}
    }
    
    public static enum PREF_KEY {
    	ACCESS_TOKEN("access_token"),
    	TOKEN_TYPE("token_type"),
    	SCOPE("scope"),
    	USER_NAME("username"),
    	USER_REPOS("repos"),
    	SELECTED_REPOSITORY("selected_repository"),
    	USER_ACCOUNT_CREATED("account_created"),
    	CLIENT_ID("387b05f90574b6fede43"),
    	CLIENT_SECRET("557392acf8c742ac6e6a3a4ff36b172f378c1633"),
    	EMPTY("");
		
		private final String key;

		private PREF_KEY(String key) {
			this.key = key;
		}

		private String getKey() {
			return key;
		}
		
		public static PREF_KEY getKey(String key){
			for(PREF_KEY pref : values()){
				if(pref.getKey().equals(key)){
					return pref;
				}
			}
			return PREF_KEY.EMPTY;
		}
	}
    
//    public static String ACCESS_TOKEN = "access_token";
    
    public static void setAccessToken(String access_token) {
    	setGeneral(PREF_KEY.ACCESS_TOKEN, access_token);
    }
    public static String getAccessToken() {
    	return getGeneral(PREF_KEY.ACCESS_TOKEN);
    }
    
    
//    public static String TOKEN_TYPE = "token_type";
    
    public static void setTokenType(String token_type) {
    	setGeneral(PREF_KEY.TOKEN_TYPE, token_type);
    }
    public static String getTokenType() {
    	return getGeneral(PREF_KEY.TOKEN_TYPE);
    }
    
    
//    public static String SCOPE = "scope";
    
    public static void setScope(String scope) {
    	setGeneral(PREF_KEY.SCOPE, scope);
    }
    public static String getScope() {
    	return getGeneral(PREF_KEY.SCOPE);
    }
    
    
//    public static String USER_NAME = "username";
    
    public static void setUser(User user) {
    	setGeneral(PREF_KEY.USER_NAME, user.getName());
    }
    public static User getUser() {
    	return new User(getGeneral(PREF_KEY.USER_NAME));
    }
    
//    public static String USER_REPOS = "repos";
    
    public static void setUserRepos(ArrayList<Repository> repos) {
    	StringBuilder builder = new StringBuilder();
    	for(Repository repo : repos){
    		builder.append(repo.getName() + ",");
    	}
    	setGeneral(PREF_KEY.USER_REPOS, builder.toString());
    }
    public static ArrayList<Repository> getUserRepos() {
    	ArrayList<Repository> list = new ArrayList<Repository>();
    	String repos = getGeneral(PREF_KEY.USER_REPOS);
    	for(String repo: repos.split(",")){
    		list.add(new Repository(repo));
    	}
    	return list;
    }
    
//    public static String USER_ACCOUNT_CREATED = "account_created";
    
    public static void setUserAcountCreated(String account_created) {
    	setGeneral(PREF_KEY.USER_ACCOUNT_CREATED, account_created);
    }
    public static String getUserAcountCreated() {
    	return getGeneral(PREF_KEY.USER_ACCOUNT_CREATED);
    }
    
    public static void setSelectedRepository(String selected_repository) {
    	setGeneral(PREF_KEY.SELECTED_REPOSITORY, selected_repository);
    }
    public static String getSelectedRepository() {
    	return getGeneral(PREF_KEY.SELECTED_REPOSITORY);
    }
    
    //-------------------------------
    
    public static String getClientId() {
    	return PREF_KEY.CLIENT_ID.getKey();
    }
    
    public static String getClientSecret() {
    	return PREF_KEY.CLIENT_SECRET.getKey();
    }
    
    private static ArrayList<PreferenceListener> prefListener = new ArrayList<PreferenceListener>();
    private static OnSharedPreferenceChangeListener onChangeListern; // Need to keep this reference, otherwise we don't get any events....
    //Wrapping the OnSharedPreferenceChangeListener since i dont want to give away the sharedpreferences....
    public static void addListener(PreferenceListener listener){
    	if(isInitialized()){
    		prefListener.add(listener);
    		if(onChangeListern == null){
    			onChangeListern = new OnSharedPreferenceChangeListener() {
    				public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
    					for(PreferenceListener list : prefListener){
    	          	    	list.preferenceChanged(PREF_KEY.getKey(key));
    	          	    }
    				}
    			};
    			prefs.registerOnSharedPreferenceChangeListener(onChangeListern);
    		}
    	}else{
    		Log.e(logTag, "Preferences is not initialized");
    	}
    }
    
    public static boolean isConnectedToGitHub()
    {
    	return getAccessToken().matches("[\\dA-z]+");
    }
}

