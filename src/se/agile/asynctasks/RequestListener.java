package se.agile.asynctasks;

public interface RequestListener<T> {
	void requestFinished(T result);
	/**
	 * Called if some stage of the request is finished that is necessary for the other part.
	 * Not necessary called for all request!
	 */
	void requestUpdate();
	void whenNoInternetConnection();
	void whenNoSelectedRepository();
	void requestFailed();
}
