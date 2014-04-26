package se.agile.model;

import se.agile.activities.model.GitHubData.Commit;

public class CommitNotification extends Notification<Commit>{
	public CommitNotification(Commit commit){
		super(commit);
		super.setContentTitle("New commit");
		super.setContentText(commit.getName());
	}
}
