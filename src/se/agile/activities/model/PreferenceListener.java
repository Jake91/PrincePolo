package se.agile.activities.model;

import se.agile.activities.model.Preferences.PREF_KEY;


public interface PreferenceListener{
	public void preferenceChanged(PREF_KEY key);
}
