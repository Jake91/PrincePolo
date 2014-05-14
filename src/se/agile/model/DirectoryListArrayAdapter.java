package se.agile.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import se.agile.activities.model.GitHubData.Directory;
import se.agile.activities.model.GitHubData.File;
import se.agile.activities.model.GitHubData.Folder;
import se.agile.asynctasks.RequestFiles;
import se.agile.asynctasks.RequestListener;
import se.agile.princepolo.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class DirectoryListArrayAdapter extends ArrayAdapter<Directory>{
	private final Context context;
	private ArrayList<Directory>  directoryList = new ArrayList<Directory>();
	private String logTag = "PrincePolo";
	private String currentPath = "", branchName;
	private OnClickListener checkListener;
	RequestListener<ArrayList<Directory>> requestListener;
	
	
	public DirectoryListArrayAdapter(Context context, ArrayList<Directory> directoryList, String branchName, String path, RequestListener<ArrayList<Directory>> requestListener, OnClickListener checkListener) {
		super(context, R.layout.fragment_notification_list_view,R.id.directory_name, directoryList);
		this.context = context;
		if(directoryList != null){
			this.directoryList = directoryList;
		}
		this.checkListener = checkListener;
		this.requestListener = requestListener;
		this.branchName = branchName;
		updateData(path);
	}
	
	public void changeBranch(String branchName){
		this.branchName = branchName;
	}
	
	public void changeBranchAndUpdateData(String branchName){
		this.branchName = branchName;
		updateData(currentPath);
	}
	
	public void updateData(String path){
		RequestFiles reqFiles = new RequestFiles(requestListener);
		reqFiles.execute(path, branchName);
		this.currentPath = path;
	}
	
	public void retry(){
		updateData(currentPath);
	}
	
	public void replaceList(ArrayList<Directory> directoryList){
		this.directoryList.clear();
		for(Directory dir : directoryList){
			this.directoryList.add(dir);
		}
		Collections.sort(this.directoryList);
		notifyDataSetChanged();
	}
	
	public Directory getDirectory(int position){
		return directoryList.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.fragment_directory_row_list_view, parent, false);
		
		TextView name = (TextView) rowView.findViewById(R.id.directory_name);
		TextView path = (TextView) rowView.findViewById(R.id.directory_path);
		ImageView icon = (ImageView) rowView.findViewById(R.id.directory_icon);
		
		CheckBox check = (CheckBox) rowView.findViewById(R.id.directory_check);
		Directory directory = directoryList.get(position);
		
		name.setText(directory.getName());
		path.setText(directory.getPath());
		if(directory instanceof Folder){
			icon.setImageResource(R.drawable.folder_icon);
			check.setVisibility(View.INVISIBLE);
		}else if(directory instanceof File){
			File file = (File) directory;
			check.setChecked(file.isWorkingFile());
			check.setOnClickListener(checkListener);
			check.setTag(file);
		}
		return rowView;
	}

}
