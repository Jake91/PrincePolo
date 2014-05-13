package se.agile.asynctasks;

import java.util.ArrayList;

import se.agile.activities.model.GitHubData.Directory;
import se.agile.activities.model.GitHubData.Folder;
import se.agile.model.JSONParser;

public class RequestFiles extends RequestTask<String, Void, ArrayList<Directory>>{
	private final String logTag = "PrincePolo";
	
	private Folder folder;
	
	private String url = "https://api.github.com/repos/";
	//https://api.github.com/repos/Jake91/PrincePolo/contents
	
	public RequestFiles(){
		super();
	}
	
	public RequestFiles(RequestListener<ArrayList<Directory>> listener){
		this(null, listener);
	}
	
	public RequestFiles(Folder folder, RequestListener<ArrayList<Directory>> listener){
		super(listener);
		this.folder = folder;
		url +=  super.getSelectedRepositoryName() + "/contents";
	}
	
	
	@Override
	protected ArrayList<Directory> doInBackground(String... params) {
		
		String path = "";
		String branch = "";
		if(folder != null){
			branch = folder.getBranchName();
			path = folder.getPath().equals("") ? "" : "/" + folder.getPath();
			
		}else{
			path = params[0].equals("") ? "" : "/" + params[0];
			branch = params[1];
		}
		ArrayList<Directory> list = JSONParser.parseDirectories(generalGETRequest(url + path + "?ref=" + branch), branch);
		if(folder != null){
			folder.setDirectoryList(list);
		}
		return list;
	}
	@Override
	protected void onPostExecute(ArrayList<Directory> directoryList){
		if(!isCancelled()){
			finishedWithRequest(directoryList);
		}
	}
}