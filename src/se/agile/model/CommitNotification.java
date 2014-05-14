package se.agile.model;

import se.agile.githubdata.Commit;

public class CommitNotification extends Notification<Commit>{
	public CommitNotification(Commit commit){
		super(commit);
		super.setContentTitle("New commit to \"" + commit.getBranchName() + "\"");
		super.setContentText(commit.getName());
	}
}
