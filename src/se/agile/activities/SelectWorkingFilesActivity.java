package se.agile.activities;

import se.agile.princepolo.R;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class SelectWorkingFilesActivity extends Activity{
	private String logTag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_directory_switcher);
		logTag = getResources().getString(R.string.logtag_main);
		Fragment fragment = new SelectWorkingFilesFragment();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content_directory_holder, fragment);
        transaction.commit();
	}
	
	public void onDestroy(){
		super.onDestroy();
		SelectWorkingFilesFragment.resetStaticVariables();
	}
}
