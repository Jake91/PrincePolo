package se.agile.activities.model.GitHubData;



public class Branch implements GitHubDataInterface{
	private String name, url;
	private Commit latestCommit;

	public Branch(String name){
		this.name = name;
	}
	
	public Commit getLatestCommit() {
		return latestCommit;
	}

	public void setLatestCommit(Commit latestCommit) {
		this.latestCommit = latestCommit;
	}

	@Override
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
	public String toString(){
		return "Branchname: " + name + " Latest commit: " + latestCommit.getName();
	}
}
