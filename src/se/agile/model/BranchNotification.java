package se.agile.model;

import se.agile.githubdata.Branch;

public class BranchNotification extends Notification<Branch>{

	public BranchNotification(Branch branch) {
		super(branch);
		super.setContentTitle("New branch");
		super.setContentText("A new branch named \"" + branch.getName() + "\" has been created.");
	}

}
