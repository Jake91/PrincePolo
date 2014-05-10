package se.agile.activities.model.GitHubData;

public class File implements GitHubDataInterface, Directory{
	
	private String name, status, path, sha, url, branchName;
	private int deletions, additions, changes;

	public File(String name){
		this.name = name;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getDeletions() {
		return deletions;
	}

	public void setDeletions(int deletions) {
		this.deletions = deletions;
	}

	public int getAdditions() {
		return additions;
	}

	public void setAdditions(int additions) {
		this.additions = additions;
	}

	public int getChanges() {
		return changes;
	}

	public void setChanges(int changes) {
		this.changes = changes;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		
	}
	
	@Override
	public String toString() {
		return "File [name=" + name + ", status=" + status + ", deletions="
				+ deletions + ", additions=" + additions + ", changes="
				+ changes + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		File other = (File) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
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

	@Override
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	@Override
	public String getBranchName() {
		return branchName;
		
	}
	
	
}
