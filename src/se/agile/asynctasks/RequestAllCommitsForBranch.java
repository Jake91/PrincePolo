package se.agile.asynctasks;

import java.util.ArrayList;

import se.agile.githubdata.Branch;
import se.agile.githubdata.Commit;
import android.util.Log;

public class RequestAllCommitsForBranch{
	
	private RequestBranches reqBranches;
	private RequestListener<ArrayList<Commit>> listener;
	private ArrayList<Commit> commitList = new ArrayList<Commit>();
	private ArrayList<String> shaList = new ArrayList<String>();
	private String logTag = "PrincePolo";
	private int numberOfThreads = 0;
	
	private RequestListener<Commit> getCommitListener(){
		RequestListener<Commit> commitListener = new RequestListenerAdapter<Commit>() {
			
			@Override
			public void whenNoSelectedRepository() {
				listener.whenNoSelectedRepository();
			}
			
			@Override
			public void whenNoInternetConnection() {
				listener.whenNoInternetConnection();
			}
			
			@Override
			public synchronized void requestFinished(Commit commit) {
				if(commit != null){
					numberOfThreads--;
					commitList.add(commit);
					listener.requestUpdate();
					ArrayList<Commit> parentCommitsList = commit.getParentList();
					if(parentCommitsList.size() > 0){
						for(Commit com : parentCommitsList){
							if(!shaList.contains(com.getSha())){
								shaList.add(com.getSha());
								numberOfThreads++;
								RequestShortCommit reqCommitList = new RequestShortCommit(getCommitListener());
								reqCommitList.execute(com.getSha(),commit.getBranchName());
							}
						}
					}
					Log.d(logTag,"Number of threads " + numberOfThreads);
					if(numberOfThreads < 1){
						listener.requestFinished(commitList);
					}
				}
			}
		};
		return commitListener;
	}
	
	public RequestAllCommitsForBranch(final RequestListener<ArrayList<Commit>> listener, String branchname){
		this.listener = listener;
		final String branchNameFinal = branchname;
		
		RequestListener<ArrayList<Branch>> branchListener = new RequestListenerAdapter<ArrayList<Branch>>() {
			
			@Override
			public void whenNoSelectedRepository() {
				listener.whenNoSelectedRepository();				
			}
			
			@Override
			public void whenNoInternetConnection() {
				listener.whenNoInternetConnection();
				
			}
			
			@Override
			public void requestFinished(ArrayList<Branch> branchList) {
				if(branchList != null){
					for(Branch branch : branchList){
						if(branch.getName().equals(branchNameFinal)){
							numberOfThreads++;
							RequestShortCommit reqCommit = new RequestShortCommit(getCommitListener());
							reqCommit.execute(branch.getLatestCommit().getSha(),branch.getName());
							break;
						}
					}
				}
			}
		};
		
		reqBranches = new RequestBranches(branchListener);
		reqBranches.execute();
	}
	
	public ArrayList<Commit> getResult(){
		if(commitList.size() < 1){
			Log.i("PrincePolo", "No commits recieved for branch");
		}
		return this.commitList;
	}
}
