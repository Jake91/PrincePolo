package se.agile.activities.model.GitHubData;

import android.os.AsyncTask;


public class Branch implements GitHubDataInterface{
	private String name;
	private String url;
	private String sha;

	public Branch(String name){
		this.name = name;
	}
	
	public String getSha() {
		return sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public Type getType() {
		return Type.BRANCH;
	}
	
	@Override
	public String toString(){
		return "Branchname: " + name + ", sha: " + sha + ", url: " + url;
	}
}
