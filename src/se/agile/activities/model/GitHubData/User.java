package se.agile.activities.model.GitHubData;


public class User implements GitHubDataInterface{
	private String name;
	public User(String name){
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
		return "Username: " + name;
	}

	@Override
	public Type getType() {
		return Type.USER;
	}
}
