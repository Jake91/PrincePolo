package se.agile.asynctasks;

public interface RequestListener {
	void requestFinished();
	void whenNoInternetConnection();
	void whenNoSelectedRepository();
}
