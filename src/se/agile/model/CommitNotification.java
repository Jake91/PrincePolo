package se.agile.model;

import java.util.Date;

import se.agile.activities.model.GitHubData.Commit;

public class CommitNotification extends Notification<Commit>{
	public CommitNotification(Commit commit){
		super(commit);
		super.setContentTitle("New commit to \"" + commit.getBranchName() + "\"");
		super.setContentText(commit.getName());
	}
}
