package se.agile.activities.model.GitHubData;


public class Repository implements GitHubDataInterface{
	private String name;
	public Repository(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString(){
		return "Repositoryname: " + name;
	}
}
