package se.agile.activities;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import se.agile.asynctasks.GetTheCommitHistory;
import se.agile.asynctasks.CountBranches;
import se.agile.model.Preferences;
import se.agile.model.Util;
import se.agile.navigator.NavDrawerItem;
import se.agile.navigator.NavDrawerListAdapter;
import se.agile.princepolo.R;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends Activity 
{
	private int notificationID = 100;
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// drawer title
	private CharSequence mDrawerTitle;

	// app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	private String logTag;
	
	// For the GetCommitHistory
	ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// At the start of the app no new commits have arrived by definition
		Util.haveNewCommitsArrived = false;
	
		logTag = getResources().getString(R.string.logtag_main);
		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// get drawer icons from resources
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// nagivagion drawer items added to array
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1), true, "50+"));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[9], navMenuIcons.getResourceId(9, -1)));
		

		// Recycle the typed array
		navMenuIcons.recycle();
		
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the navigation drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) 
			{
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) 
			{
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) 
		{
			displayView(0);
			Log.d(logTag, "Displayview 0 isconnected");
		}
		
		// Opens up the menu from the left when the app is openeds
		//mDrawerLayout.openDrawer(Gravity.LEFT);
		
		// Just a test, at the moment. Counts the number of Branches in a repo
		CountBranches task = new CountBranches();
		task.execute(new String[] { "https://api.github.com/repos/Jake91/PrincePolo/branches?access_token=aa534e873012c9a6881ee6826f31e494ad6ca6db" });
		
		// Checks every 10 sec whether new commits have arrived
		scheduler.scheduleAtFixedRate (new Runnable() 
		{
			public void run() 
			{
				GetTheCommitHistory task = new GetTheCommitHistory();
				task.execute(new String[] { "https://api.github.com/repos/" + Preferences.getSelectedRepository().getName() + "/commits?access_token=" + Preferences.getAccessToken()});
				
				if (Util.haveNewCommitsArrived == true)
				{
					issueNotification();
				}
			}
		}, 0, 10, TimeUnit.SECONDS);
	}

	
	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) 
		{
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) 
		{
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	// displays the fragment view for selected fragment
	private void displayView(int position) 
	{
		Fragment fragment = null;
		switch (position) 
		{
			case 0:
				fragment = new RepositoryOverviewFragment();
				break;
			case 1:
				fragment = new NotificationsFragment();
				break;
			case 2:
				fragment = new IssuesFragment();
				break;
			case 3:
				fragment = new BranchesFragment();
				break;
			case 4:
				fragment = new CollaboratorsFragment();
				break;
			case 5:
				fragment = new SelectRepositoryFragment();
				break;
			case 6:
				fragment = new ConnectToGitHubFragment();
				break;
			case 7:
				fragment = new SettingsFragment();
				break;
			case 8:
				fragment = new AboutFragment();
				break;
			case 9:
				fragment = new SignOutFragment();
				break;
	
			default:
				break;
		}

		if (fragment != null) 
		{
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} 
		else 
		{
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) 
	{
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) 
	{
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	private void issueNotification()
	{
		Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Uri funSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/glad");
    	long[] pattern = {500,500,500,500,500,500,500,500,500};
    	 
    	NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this);
    	mBuilder.setSmallIcon(R.drawable.ic_launcher);
    	mBuilder.setContentTitle("Incoming commit!");
    	mBuilder.setContentText("Click to view it");
    	mBuilder.setSound(alarmSound);
    	mBuilder.setVibrate(pattern);
    	
    	NotificationManager mNotificationManager =
    		    (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
    		    
    		// notificationID allows you to update the notification later on.
    		mNotificationManager.notify(notificationID, mBuilder.build());
    		
    		Util.haveNewCommitsArrived = false;
	}

}
