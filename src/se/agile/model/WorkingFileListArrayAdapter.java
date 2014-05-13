package se.agile.model;


import java.util.ArrayList;

import se.agile.activities.model.GitHubData.Directory;
import se.agile.activities.model.GitHubData.File;
import se.agile.model.Preferences.PREF_KEY;
import se.agile.princepolo.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class WorkingFileListArrayAdapter extends ArrayAdapter<File>{
	private final Context context;
	private ArrayList<File>  fileList = new ArrayList<File>();
	private String logTag = "PrincePolo";
	private OnClickListener buttonRemoveListener;
	
	
	public WorkingFileListArrayAdapter(Context context, ArrayList<File> filesList) {
		super(context, R.layout.list_view,R.id.directory_name, filesList);
		this.context = context;
		if(fileList != null){
			this.fileList = filesList;
		}
		buttonRemoveListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Object ob = v.getTag();
				if(ob instanceof File){
					File file = (File) ob;
					fileList.remove(file);
					TemporaryStorage.workingFiles.remove(file);
					notifyDataSetChanged();
				}
				
			}
		};
	}
	
	public Directory getDirectory(int position){
		return fileList.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.fragment_select_working_files_overview_row, parent, false);
		
		TextView name = (TextView) rowView.findViewById(R.id.directory_name);
		TextView path = (TextView) rowView.findViewById(R.id.directory_path);
		
		File file = fileList.get(position);
		
		name.setText(file.getName());
		path.setText(file.getPath());
		
		Button done = (Button) rowView.findViewById(R.id.working_removeButton);
		done.setOnClickListener(buttonRemoveListener);
		done.setTag(file);
		
		
		return rowView;
	}
}
