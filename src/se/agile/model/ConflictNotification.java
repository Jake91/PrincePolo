package se.agile.model;

import se.agile.activities.model.GitHubData.Commit;
import se.agile.activities.model.GitHubData.File;

public class ConflictNotification extends Notification<Tuple<File,Commit>>{
	public ConflictNotification(File file, Commit commit){
		super(new Tuple<File, Commit>(file, commit));
		super.setContentTitle("\"" + file.getName() + "\" changed");
		super.setContentText("Edited in branch: " + commit.getBranchName() + " by: " + commit.getCommitter().getName());
	}
}
