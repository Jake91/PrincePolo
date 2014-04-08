package se.agile.asynctasks;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class CountBranches extends AsyncTask<String, Void, String> 
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
		String findStr = "name";
		int lastIndex = 0;
		int count =0;

		while(lastIndex != -1)
		{
			lastIndex = result.indexOf(findStr,lastIndex);

			 if( lastIndex != -1)
			 {
				 count ++;
				 lastIndex+=findStr.length();
			 }
		}
		Log.d(logTag, "numberofBranches:"+count);
    }
}
