package se.agile.activities;	

import se.agile.princepolo.R;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class IssuesFragment extends Fragment 
{
	private String logTag;
	
	public IssuesFragment(){}
	
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		logTag = getResources().getString(R.string.logtag_main);
        View rootView = inflater.inflate(R.layout.fragment_issues, container, false);

        android.view.View.OnClickListener bttnListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Button b = (Button) v;
				CharSequence text = b.getText();
			    Fragment fragment = new CardFragment(text);
			    
			    if (fragment != null) 
				{
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().add(R.id.frame_container, fragment).commit();
					Log.d(logTag, "got text from button");
				} 
				
			}
		};
		((Button) rootView.findViewById(R.id.bttnPok0)).setOnClickListener(bttnListener);
        ((Button) rootView.findViewById(R.id.bttnPok1)).setOnClickListener(bttnListener);
        ((Button) rootView.findViewById(R.id.bttnPok2)).setOnClickListener(bttnListener);
        ((Button) rootView.findViewById(R.id.bttnPok3)).setOnClickListener(bttnListener);
        ((Button) rootView.findViewById(R.id.bttnPok5)).setOnClickListener(bttnListener);
        ((Button) rootView.findViewById(R.id.bttnPok8)).setOnClickListener(bttnListener);
        ((Button) rootView.findViewById(R.id.bttnPok13)).setOnClickListener(bttnListener);
        ((Button) rootView.findViewById(R.id.bttnPok20)).setOnClickListener(bttnListener);
        ((Button) rootView.findViewById(R.id.bttnPok40)).setOnClickListener(bttnListener);
        ((Button) rootView.findViewById(R.id.bttnPok100)).setOnClickListener(bttnListener);
        ((Button) rootView.findViewById(R.id.bttnPokQ)).setOnClickListener(bttnListener);
        return rootView;
        
    }
	
	
}
