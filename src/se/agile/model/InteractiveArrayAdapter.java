package se.agile.model;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import se.agile.princepolo.R;
import se.agile.activities.model.GitHubData.Branch;
import se.agile.model.Preferences;

public class InteractiveArrayAdapter extends ArrayAdapter<BranchSelectionModel> 
{
	private final List<BranchSelectionModel> list;
	private final Activity context;
	private final static String logTag = "Thor";

	public InteractiveArrayAdapter(Activity context, List<BranchSelectionModel> list) 
	{
		super(context, R.layout.rowbuttonlayout, list);
		this.context = context;
		this.list = list;
	}

	static class ViewHolder 
	{
		protected CheckBox checkbox;
		protected TextView text;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view = null;
		if (convertView == null) 
		{
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.rowbuttonlayout, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
			viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() 
			{
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
				{
					BranchSelectionModel element = (BranchSelectionModel) viewHolder.checkbox.getTag();
					element.setSelected(buttonView.isChecked());
					
					String branchName = element.getName();
					
					ArrayList<Branch> listOfBranches = Preferences.getUnselectedBranches();
					ArrayList<String> namesOfBranches = new ArrayList<String>();
					for (int i = 0; i < listOfBranches.size(); i++)
					{
						namesOfBranches.add(listOfBranches.get(i).getName());
					}
					
		            if (!isChecked)
		            {
						Preferences.addUnselectedBranch(branchName);
		            }
		            
		            else if (isChecked)
		            {        	
		            	if (namesOfBranches.contains(branchName))
		            	{
		            		Preferences.removeUnselectedBranches(branchName);
		            	}
		            }
		            
				}
			});
			view.setTag(viewHolder);
			viewHolder.checkbox.setTag(list.get(position));
			viewHolder.text = (TextView) view.findViewById(R.id.label);
		} 
		else 
		{
			view = convertView;
			((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.checkbox.setChecked(list.get(position).isSelected());
		holder.text.setText(list.get(position).getName());
		return view;
	}
}   