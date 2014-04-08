package se.agile.activities.model.GitHubData;

public interface GitHubDataInterface {
	public static enum Type {USER, REPOSITORY, BRANCH};
	
	public String getName();
	public void setName(String name);
	
	public Type getType();
}
