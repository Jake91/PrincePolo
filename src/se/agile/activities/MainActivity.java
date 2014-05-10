package se.agile.activities;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import se.agile.asynctasks.RequestAccessToken;
import se.agile.model.Notification;
import se.agile.model.NotificationHandler;
import se.agile.model.Preferences;
import se.agile.navigator.NavDrawerItem;
import se.agile.navigator.NavDrawerListAdapter;
import se.agile.princepolo.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// drawer title
	private CharSequence mDrawerTitle;

	// app title
	private CharSequence mTitle;

	// slide menu items
	// private String[] navMenuTitles;
	// private TypedArray navMenuIcons;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	private static String logTag;
	private static MainActivity activity;

	private NotificationHandler notificationHandler;

	// For the GetCommitHistory
	ScheduledExecutorService scheduler = Executors
			.newSingleThreadScheduledExecutor();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Preferences.initializePreferences(this);
		activity = this;
		logTag = getResources().getString(R.string.logtag_main);

		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		navDrawerItems = new ArrayList<NavDrawerItem>();
		// nagivagion drawer items added to array
		for (VIEW view : VIEW.values()) {
			navDrawerItems.add(view.getNavDrawerItem());
		}
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the navigation drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {

			displayView(VIEW.REPOSITORY_OVERVIEW);
		}
		notificationHandler = new NotificationHandler(this);
		notificationHandler.start();

		// Opens up the menu from the left when the app is openeds
		// mDrawerLayout.openDrawer(Gravity.LEFT);

	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(VIEW.getVIEW(position));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
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

	/**
	 * Here you can add a new view. (Don't forget to add the title and the icon
	 * in /res/values/strings.xml) (And in the MainActivity.displayView(VIEW
	 * view))
	 * 
	 * @author Jacob
	 * 
	 */
	public enum VIEW {
		// The titleIconArrayIndex is to make sure you get the icon and title
		// you specified when first creating the view.
		// The position is now easy to change.
		// But if you change the position you also have to change the order that
		// they are specified! position 0 -> Specified first (in this enum).
		REPOSITORY_OVERVIEW(0, false, "", 0), 
		NOTIFICATIONS(1, true, "0", 1), 
		PLANNING_POKER(2, false, "", 2), 
		PERSONAL_NOTES(3, false, "", 3), 
		BRANCHES(4, false, "", 4),
		COLLABORATORS(5, false, "", 5), 
		SELECT_REPOSITORY(6, false, "", 6),
		CONNECT_TO_GITHUB(7, false, "", 7), 
		SETTINGS(8, false, "", 8), 
		ABOUT(9, false,"", 9), 
		SIGNOUT(10, false, "", 10);

		private final int position, titleIconArrayIndex;
		private boolean isCounterVisible;
		private String count;

		private static final String[] navMenuTitles = activity.getResources()
				.getStringArray(R.array.nav_drawer_items);
		private static final TypedArray navMenuIcons = activity.getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		private VIEW(final int position, boolean isCounterVisible,
				String count, int titleIconArrayIndex) {
			this.position = position;
			this.isCounterVisible = isCounterVisible;
			this.count = count;
			this.titleIconArrayIndex = titleIconArrayIndex;
		}

		public int getPosition() {
			return position;
		}

		public String getTitle() {
			return navMenuTitles[titleIconArrayIndex];
		}

		public int getIconValue() {
			return navMenuIcons.getResourceId(titleIconArrayIndex, -1);
		}

		public NavDrawerItem getNavDrawerItem() {
			if (isCounterVisible) {
				return new NavDrawerItem(navMenuTitles[titleIconArrayIndex],
						navMenuIcons.getResourceId(titleIconArrayIndex, -1),
						isCounterVisible, count);
			} else {
				return new NavDrawerItem(navMenuTitles[titleIconArrayIndex],
						navMenuIcons.getResourceId(titleIconArrayIndex, -1));
			}

		}

		public static VIEW getVIEW(int position) {
			for (VIEW view : values()) {
				if (view.getPosition() == position) {
					return view;
				}
			}
			return null;
		}

		public static VIEW getView(String name) {
			for (VIEW view : values()) {
				if (view.name().equals(name)) {
					return view;
				}
			}
			return null;
		}

		public String getName() {
			return this.name();
		}
	}

	// displays the fragment view for selected fragment
	protected void displayView(VIEW view) {
		Fragment fragment = null;
		switch (view) {
		case REPOSITORY_OVERVIEW:
			fragment = new RepositoryOverviewFragment();
			break;
		case NOTIFICATIONS:
			fragment = new NotificationFragmentSwitcher();
			break;
		case PLANNING_POKER:
			fragment = new PokerFragment();
			break;
		case PERSONAL_NOTES:
			fragment = new PersonalNotesFragment();
			break;
		case BRANCHES:
			fragment = new BranchesFragment();
			break;
		case COLLABORATORS:
			fragment = new CollaboratorsFragment();
			break;
		case SELECT_REPOSITORY:
			fragment = new SelectRepositoryFragment();
			break;
		case CONNECT_TO_GITHUB:
			fragment = new ConnectToGitHubFragment();
			break;
		case SETTINGS:
			fragment = new SettingsFragment();
			break;
		case ABOUT:
			fragment = new AboutFragment();
			break;
		case SIGNOUT:
			fragment = new SignOutFragment();
			break;

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(view.getPosition(), true);
			mDrawerList.setSelection(view.getPosition());
			setTitle(view.getTitle());
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public static boolean isNetworkConnected() {
		if (activity != null) {
			ConnectivityManager cm = (ConnectivityManager) activity
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			return (cm.getActiveNetworkInfo() != null);
		} else {
			Log.e(logTag,
					"isNetworkConnected: Couldn't check connection proberly");
			return false;
		}
	}

	public static void hasNoInternetConnection(Context context) {
		final Context finalContext = context;
		new AlertDialog.Builder(context)
				.setTitle("No Internet Access")
				.setMessage("Connect to Internet and then try again")
				.setPositiveButton("Retry",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								if (MainActivity.isNetworkConnected()) {
									if (Preferences.getAccessToken().equals("")) {
										Intent intent = new Intent(activity,
												LoginActivity.class);
										activity.startActivity(intent);
										activity.displayView(VIEW.SETTINGS);
									}
								} else {
									hasNoInternetConnection(finalContext);
								}
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								activity.displayView(VIEW.CONNECT_TO_GITHUB);
							}
						}).show();
	}

	public static void hasNoSelectedRepository(Context context) {
		final Context finalContext = context;
		new AlertDialog.Builder(context).setTitle("No Selected Repository")
				.setMessage("Select a Repository")
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						activity.displayView(VIEW.SELECT_REPOSITORY);
					}
				}).show();
	}

	/**
	 * Check that we have an access token and a selected repository.
	 */
	private void initialCheck() {
		Log.d(logTag, "initialCheck");
		if (!Preferences.isConnectedToGitHub()
				&& !RequestAccessToken.isRequestingAccessToken()) {
			if (isNetworkConnected()) {
				Log.d(logTag, "start login");
				Intent intent = new Intent(MainActivity.this,
						LoginActivity.class);
				startActivity(intent);
			} else {
				hasNoInternetConnection(activity);
			}
		} else if (!Preferences.hasSelectedRepository()) {
			Log.d(logTag, "Select repo");
			displayView(VIEW.SELECT_REPOSITORY);
		}
	}

	@Override
	public void onResume() {
		initialCheck();
		super.onResume();
	}

	@Override
	public void onNewIntent(Intent intent) {
		if (intent.getAction().equals(VIEW.NOTIFICATIONS.getName())) {
			displayView(VIEW.NOTIFICATIONS);
		}
		super.onNewIntent(intent);
	}
}
