package se.agile.asynctasks;

import java.util.ArrayList;

import se.agile.activities.model.GitHubData.Branch;
import se.agile.activities.model.GitHubData.Commit;
import se.agile.activities.model.GitHubData.Directory;
import se.agile.activities.model.GitHubData.Folder;
import android.util.Log;

public class RequestAllFilesForBranch{
	
	private RequestBranches reqBranches;
	private RequestListener<ArrayList<Directory>> listener;
//	private ArrayList<Directory> directoryList = new ArrayList<Directory>();
	private ArrayList<String> shaList = new ArrayList<String>();
	private String logTag = "PrincePolo";
	private int numberOfThreads = 0;
	private Folder folder;
	
	private RequestListener<ArrayList<Directory>> getDirectoryListener(){
		RequestListener<ArrayList<Directory>> dirListener = new RequestListenerAdapter<ArrayList<Directory>>() {
			
			@Override
			public void whenNoSelectedRepository() {
				listener.whenNoSelectedRepository();
			}
			
			@Override
			public void whenNoInternetConnection() {
				listener.whenNoInternetConnection();
			}
			
			@Override
			public synchronized void requestFinished(ArrayList<Directory> directoryList) {
				numberOfThreads--;
				if(directoryList != null && directoryList.size() > 0){
					for(Directory dir : directoryList){
						if(dir instanceof Folder){
							Folder folder = (Folder) dir;
							numberOfThreads++;
							RequestFiles reqFiles = new RequestFiles(getDirectoryListener(), folder);
							reqFiles.execute();
						}
					}
				}
				Log.d(logTag, "Number of Threads: " + numberOfThreads);
				if(numberOfThreads==0){
					listener.requestFinished(getResult());
				}
			}
		};
		return dirListener;
	}
	
	public RequestAllFilesForBranch(final RequestListener<ArrayList<Directory>> listener, String branchName){
		this.listener = listener;
		this.folder = new Folder("ToppFolder");
		folder.setBranchName(branchName);
		folder.setPath("");
		numberOfThreads++;
		RequestFiles reqFiles = new RequestFiles(getDirectoryListener(), folder);
		reqFiles.execute("");
	}
	
	public ArrayList<Directory> getResult(){
		return folder.getDirectoryList();
	}
}
