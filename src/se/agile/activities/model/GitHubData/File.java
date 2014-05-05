package se.agile.activities.model.GitHubData;

public class File implements GitHubDataInterface{
	
	private String name, status;
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
}
