package se.agile.activities;	

import se.agile.princepolo.R;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

@SuppressLint("ValidFragment")
public class CardFragment extends Fragment 
{
	private String logTag;
	private CharSequence text;
	private Integer clickNumber = 0;
	private Fragment previousFragment;

	public CardFragment()
	{
		Log.d(logTag, "Constructor");
	}

	@SuppressLint("ValidFragment")
	public CardFragment(CharSequence txt, Fragment prevFragment)
	{
		Log.d(logTag, "Constructor text");
		text=txt;
		previousFragment = prevFragment;
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		logTag = getResources().getString(R.string.logtag_main);
        View rootView = inflater.inflate(R.layout.fragment_card, container, false);
        
        final Button p1_button = (Button) rootView.findViewById(R.id.bttnCloseCard);
        
//        TextView tv = (TextView) rootView.findViewById(R.id.textOnCard);
//        tv.setText(text);
        
        final Fragment fragment = this;
        android.view.View.OnClickListener bttnListener = new View.OnClickListener() 
        {
        	@Override
        	public void onClick(View v) 
        	{
        		if ( clickNumber == 0){
        			p1_button.setBackgroundResource(R.drawable.whole_screen_button);
        			p1_button.setText(text);
        			clickNumber++;
        		}
        		else {
		            FragmentManager fragmentManager = getFragmentManager();
		            fragmentManager.beginTransaction().replace(R.id.frame_container, previousFragment).commit();
		            fragmentManager.beginTransaction().remove(fragment).commit();
		            Log.d(logTag, "fragment stuff");
        		}
        		
        	}
        };
        ((Button) rootView.findViewById(R.id.bttnCloseCard)).setOnClickListener(bttnListener);
         
        return rootView;
    }
}
