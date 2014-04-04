package se.agile.activities.model;

import se.agile.activities.model.HttpConnection.URL;

public interface HttpConnectionResponseListener {
	public void receivedResponse(URL url, String response);
}
