package se.agile.model;

import se.agile.githubdata.Commit;
import se.agile.githubdata.File;

public class ConflictNotification extends Notification<Tuple<File,Commit>>{
	public ConflictNotification(File file, Commit commit){
		super(new Tuple<File, Commit>(file, commit));
		super.setContentTitle("\"" + file.getName() + "\" changed");
		super.setContentText("Edited in branch: " + commit.getBranchName() + " by: " + commit.getCommitter().getName());
	}
}
