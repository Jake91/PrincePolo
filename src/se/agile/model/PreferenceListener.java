package se.agile.model;

import se.agile.model.Preferences.PREF_KEY;


public interface PreferenceListener{
	public void preferenceChanged(PREF_KEY key);
}
