package se.agile.activities.model.GitHubData;

import java.util.ArrayList;
import java.util.Date;

import android.util.Log;

public class Commit implements GitHubDataInterface{
	private final String logTag = "PrincePolo";
	private String message, sha, url;
	private ArrayList<Commit> parentList;
	private User committer;
	private Date date;
	private ArrayList<File> changedFiles;
	private boolean isComplete;
	
	public Commit(String url){
		this.url = url;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
		this.changedFiles = new ArrayList<File>();
		this.parentList = new ArrayList<Commit>();
	}

	public String getSha() {
		return sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public User getCommitter() {
		return committer;
	}

	public void setCommitter(User committer) {
		this.committer = committer;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public ArrayList<File> getChangedFiles() {
		if(!isComplete){
			Log.e(logTag, "All the information about the commit haven't been downloaded. Check isComplete in order to know if all information exist.");
		}
		return changedFiles;
	}

	public void setChangedFiles(ArrayList<File> changedFiles) {
		this.changedFiles = changedFiles;
	}
	
	@Override
	public String getName() {
		return "Date: " + date.toString() + " Author: " + committer.getName() + " Message: " + message;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Commit [ ");
		builder.append("Committer: " + committer.getName());
		builder.append(", Date: " + date);
		builder.append(", messsage: " + message);
		builder.append(", sha: " + sha);
		builder.append(", ChangedFiles: ");
		if(isComplete){
			for(File file : changedFiles){
				builder.append(file.toString() + ", ");
			}
		}else{
			builder.append("IsComplete: " + isComplete);
		}
		
		
		builder.append("]");
		
		
		return builder.toString();
	}

	public ArrayList<Commit> getParentList() {
		return parentList;
	}

	public void setParentList(ArrayList<Commit> parentList) {
		this.parentList = parentList;
	}
	
	
	public void setIsComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}
	/**
	 * When using the GitHub Api a branch has for example a connected commit. 
	 * But when we're reading a branch and also create a commit for that branch, 
	 * that commit doesn't get all information.
	 * For example you cannot se changedFiles.
	 * 
	 * @return
	 */
	public boolean isComplete() {
		return isComplete;
	}


}
