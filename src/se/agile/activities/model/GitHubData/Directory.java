package se.agile.activities.model.GitHubData;

public interface Directory {

	public String getPath();

	public void setPath(String path);

	public String getSha();

	public void setSha(String sha);

	public String getUrl();

	public void setUrl(String url);

	public String getName();
	
	public void setBranchName(String branchName);
	
	public String getBranchName();
}
