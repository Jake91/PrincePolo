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
		Branch other = (Branch) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
	
	
}
