package se.agile.activities;	

import se.agile.model.Preferences;
import se.agile.princepolo.R;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.NumberPicker;
import android.widget.Switch;


public class SettingsFragment extends Fragment implements OnClickListener,
											OnCheckedChangeListener, NumberPicker.OnValueChangeListener {
	private String logTag;
	private Switch soundSwitch;
	private Button saveChangesButton;
	private boolean sound, changed;
	private NumberPicker minutePicker, secondPicker;
	private int updateFrequency;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		logTag = getResources().getString(R.string.logtag_main);
		View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
		sound = Preferences.getSoundIsOn();
		updateFrequency = Preferences.getTimeDuration();
		soundSwitch = (Switch) rootView.findViewById(R.id.soundSwitch);
		saveChangesButton = (Button) rootView.findViewById(R.id.saveChanges_Button);
		minutePicker = (NumberPicker) rootView.findViewById(R.id.minutePicker);
		secondPicker = (NumberPicker) rootView.findViewById(R.id.secondPicker);
		minutePicker.setOnValueChangedListener(this);
		secondPicker.setOnValueChangedListener(this);
		saveChangesButton.setOnClickListener(this);
		soundSwitch.setOnCheckedChangeListener(this);
		minutePicker.setMaxValue(59);
		minutePicker.setMinValue(0);
		minutePicker.setValue(0);
		secondPicker.setMaxValue(59);
		secondPicker.setMinValue(0);
		secondPicker.setValue(10);
		disableSaveButton();
		return rootView;
	}


	@Override
	// this currently only sets values within this class. Perhaps it should instead fire
	// some kind of changeRequest to wherever the commit update frequency and sounds are being used.
	public void onClick(View v) {
		if (v == saveChangesButton){
			sound = changed;
			updateFrequency = (minutePicker.getValue())*60 + secondPicker.getValue();
			Preferences.setTimeDuration(updateFrequency);
			Preferences.setSoundIsOn(sound);
			disableSaveButton();
		}
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		
		if (buttonView == soundSwitch) {
			changed = isChecked;
			if(minutePicker.getValue() > 0 || secondPicker.getValue() > 0){
				enableSaveButton();
			}
		}
	}

	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		if (newVal == 0){
			if (picker == minutePicker && secondPicker.getValue() == 0){
				disableSaveButton();
			}
			else if (picker == secondPicker && minutePicker.getValue() == 0) {
				disableSaveButton();
			}
		}
		else {
			enableSaveButton();
		}
	}
	private void enableSaveButton(){
		saveChangesButton.setClickable(true);
		saveChangesButton.setTextColor(Color.parseColor("#000000"));
	}
	private void disableSaveButton(){
		saveChangesButton.setClickable(false);
		saveChangesButton.setTextColor(Color.parseColor("#A7A79B"));
	}
	// returns whether or not sound is enabled
	public boolean soundEnabled(){
		return sound;
	}
	// returns the desired updatefrequency in seconds, default 10 
	public int updateFrequency(){
		return updateFrequency;
	}
}
