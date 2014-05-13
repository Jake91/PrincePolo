package se.agile.model;

import se.agile.activities.model.GitHubData.Commit;
import se.agile.activities.model.GitHubData.File;

public class ConflictNotification extends Notification<File>{
	public ConflictNotification(File file, Commit commit){
		super(file);
		super.setContentTitle("\"" + file.getName() + "\" changed");
		super.setContentText("Edited in branch: " + commit.getBranchName() + " by: " + commit.getCommitter().getName());
	}
}
