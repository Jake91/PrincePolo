package se.agile.model;

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
    	if(prefs == null){
    		prefs = context.getSharedPreferences(PREF_STRINGS_NAME, Context.MODE_PRIVATE);
    	}else{
    		Log.e(logTag, "Preferences is already initialized");
    	}
    }
    
    private static boolean isInitialized(){
    	return prefs != null;
    }
    
    private static void setGeneral(PREF_KEY key, String value){
    	if(isInitialized()){
    		Editor edit = prefs.edit();
            edit.putString(key.getKey(), value);
            edit.apply();
            fireStateChanged(key);
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
    		return "";
    	}
    }
    
    public static void ClearPreferences(){
    	setAccessToken("");
    	setRepositories(new ArrayList<Repository>());
    	setScope("");
    	setTokenType("");
    	setUser(new User(""));
    	setSelectedRepository(new Repository(""));
    	setUserAcountCreated("");
    	setIsFirstTime(true);
    }
    
    public static enum PREF_KEY {
    	COMMITS_MD5_VALUE("commits_md5_value"),
    	ACCESS_TOKEN("access_token"),
    	TOKEN_TYPE("token_type"),
    	SCOPE("scope"),
    	USER_NAME("username"),
    	USER_REPOSITORIES("repos"),
    	SELECTED_REPOSITORY("selected_repository"),
    	USER_ACCOUNT_CREATED("account_created"),
    	FIRST_TIME_USING_APP("is_first_time"),
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
    
    public static void setMD5Value(String commits_md5_value) {
    	setGeneral(PREF_KEY.COMMITS_MD5_VALUE, commits_md5_value);
    }
    public static String getMD5Value() {
    	return getGeneral(PREF_KEY.COMMITS_MD5_VALUE);
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
    
    public static void setIsFirstTime(Boolean is_first_time) {
    	if(is_first_time){
    		setGeneral(PREF_KEY.FIRST_TIME_USING_APP, "True");
    	}else{
    		setGeneral(PREF_KEY.FIRST_TIME_USING_APP, "False");
    	}
    	
    }
    public static boolean isFirstTimeUsingApp() {
    	String isFirstTime = getGeneral(PREF_KEY.FIRST_TIME_USING_APP);
    		return (isFirstTime.equals("True") || isFirstTime.equals(""));
    }    

    
    public static void setUser(User user) {
    	setGeneral(PREF_KEY.USER_NAME, user.getName());
    }
    public static User getUser() {
    	return new User(getGeneral(PREF_KEY.USER_NAME));
    }
    
    
    public static void setRepositories(ArrayList<Repository> repos) {
    	StringBuilder builder = new StringBuilder();
    	for(Repository repo : repos){
    		builder.append(repo.getName() + ",");
    	}
    	setGeneral(PREF_KEY.USER_REPOSITORIES, builder.toString());
    }
    public static ArrayList<Repository> getRepositories() {
    	ArrayList<Repository> list = new ArrayList<Repository>();
    	String repos = getGeneral(PREF_KEY.USER_REPOSITORIES);
    	for(String repo: repos.split(",")){
    		if(!repo.equals("")){
    			list.add(new Repository(repo));
    		}
    	}
    	return list;
    }
    
    
    public static void setUserAcountCreated(String account_created) {
    	setGeneral(PREF_KEY.USER_ACCOUNT_CREATED, account_created);
    }
    public static String getUserAcountCreated() {
    	return getGeneral(PREF_KEY.USER_ACCOUNT_CREATED);
    }
    
    public static void setSelectedRepository(Repository selected_repository) {
    	setGeneral(PREF_KEY.SELECTED_REPOSITORY, selected_repository.getName());
    }
    
    public static Repository getSelectedRepository() {
    	return new Repository(getGeneral(PREF_KEY.SELECTED_REPOSITORY));
    }
    //-------------------------------
    
    public static String getClientId() {
    	return PREF_KEY.CLIENT_ID.getKey();
    }
    
    public static String getClientSecret() {
    	return PREF_KEY.CLIENT_SECRET.getKey();
    }

    private static ArrayList<PreferenceListener> prefListener = new ArrayList<PreferenceListener>();
    
    private static void fireStateChanged(PREF_KEY key){
    	for(PreferenceListener listener : prefListener){
    		listener.preferenceChanged(key);
    	}
    }

    public static void addListener(PreferenceListener listener){
    	prefListener.add(listener);
    }
    public static boolean removeListener(PreferenceListener listener){
    	return prefListener.remove(listener);
    }
    
    public static boolean isConnectedToGitHub()
    {
    	return getAccessToken().matches("[\\dA-z]+");
    }
    
    public static boolean hasSelectedRepository()
    {
    	return !getSelectedRepository().getName().equals("");
    }
}

