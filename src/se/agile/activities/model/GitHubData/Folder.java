package se.agile.activities.model.GitHubData;

import java.util.ArrayList;

public class Folder implements Directory{
	
	private String name, path, sha, url, branchName;
	private int size;
	private ArrayList<Directory> directoryList;
	
	public Folder(String name){
		this.name = name;
	}
	@Override
	public String getPath() {
		return path;
	}
	@Override
	public void setPath(String path) {
		this.path = path;
	}
	@Override
	public String getSha() {
		return sha;
	}
	@Override
	public void setSha(String sha) {
		this.sha = sha;
	}
	@Override
	public String getUrl() {
		return url;
	}
	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	@Override
	public String getName(){
		return this.name;
	}
	
	public boolean addDirectory(Directory dir){
		return directoryList.add(dir);
	}
	
	public boolean removeDirectory(Directory dir){
		return directoryList.remove(dir);
	}
	
	public ArrayList<Directory> getDirectoryList(){
		return directoryList;
	}
	
	public void setDirectoryList(ArrayList<Directory> directoryList){
		this.directoryList = directoryList;
	}
	@Override
	public String getBranchName() {
		return branchName;
	}
	@Override
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	@Override
	public int compareTo(Directory another) {
		if(another instanceof Folder){
			return this.name.compareTo(another.getName());
		}else{
			return -1;
		}
	}
	
}
