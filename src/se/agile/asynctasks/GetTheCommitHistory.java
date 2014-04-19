package se.agile.asynctasks;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import se.agile.activities.MainActivity;
import se.agile.model.Preferences;
import se.agile.model.TemporaryStorage;
import android.os.AsyncTask;
import android.util.Log;

public class GetTheCommitHistory extends AsyncTask<String, Void, String> 
{
	private String logTag;
	
	@Override
	protected String doInBackground(String... urls) 
    {
		String response = "";
		for (String url : urls) 
		{
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			try 
			{
				HttpResponse execute = client.execute(httpGet);
				InputStream content = execute.getEntity().getContent();
			
				BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
				String s = "";
				while ((s = buffer.readLine()) != null) 
				{
			        response += s;
				}

			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return response;
    }

    @Override
    protected void onPostExecute(String result) 
    {
    	String newValue = getMD5EncryptedString(result);
//	    Log.d(logTag, "Old value is: "+Preferences.getMD5Value());
//	    Log.d(logTag, "NEW value is: "+newValue);
	    
	    if (Preferences.getMD5Value().equals(""))
	    {
//	    	Log.d(logTag, "MD5 value in prefs was empty!");
	    	Preferences.setMD5Value(newValue);
	    }
	    
	    if (!newValue.equals(Preferences.getMD5Value()))
	    {
	    	Preferences.setMD5Value(newValue);
//	    	TemporaryStorage.haveNewCommitsArrived = true;
	    	
	    }
	    else return;
    }
    
	// Calculates MD5 for a file
	public static String getMD5EncryptedString(String encTarget)
	{
        MessageDigest mdEnc = null;
        try 
        {
        	mdEnc = MessageDigest.getInstance("MD5");
        } 
        catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
        } 
        // Encryption algorithm
        mdEnc.update(encTarget.getBytes(), 0, encTarget.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
        while ( md5.length() < 32 ) 
        {
            md5 = "0"+md5;
        }
        return md5;
    }
}
