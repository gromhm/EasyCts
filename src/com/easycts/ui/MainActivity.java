package com.easycts.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.easycts.R;
import com.easycts.ui.mainactivity.CollectionLignesFragment;
import com.easycts.ui.mainactivity.DefaultFragment;
import com.easycts.ui.mainactivity.FavoritesFragment;
import com.easycts.ui.mainactivity.MainActivityMenuItemView;

public class MainActivity extends SherlockFragmentActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mMenuArray;
	private String[] mLigneTypes;
	private int currentPosition;
	Boolean isFirstFilterCallBack=true;
	
	CollectionLignesFragment lignesFragment;
	FavoritesFragment favFragment;
	DefaultFragment planetFragment;
	
	public final static String LIGNEID = "com.easycts.ui.intent.LIGNEID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar); //Used for theme switching in samples
		super.onCreate(savedInstanceState);
		
		lignesFragment = new CollectionLignesFragment();
		favFragment = new FavoritesFragment();
		planetFragment = new DefaultFragment();
		
		setContentView(R.layout.activity_main);
		mTitle = mDrawerTitle = getTitle();
		mMenuArray = getResources().getStringArray(R.array.menu_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		//Menu filtre ligne
		mLigneTypes = getResources().getStringArray(R.array.ligne_type);

		
        // set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mMenuArray));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				supportInvalidateOptionsMenu(); // creates call to
												// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				supportInvalidateOptionsMenu(); // creates call to
												// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) 
		{
			selectItem(0);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		//MenuItem searchMenu = menu.add("Search");
		//searchMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	        
		//MenuInflater inflater = getSupportMenuInflater();
		//inflater.inflate(R.menu.main, menu);
		getSupportActionBar().setNavigationMode( ActionBar.NAVIGATION_MODE_STANDARD);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		//menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		//menu.findItem(R.id.action_filter).setVisible(!drawerOpen);
		
		if(drawerOpen)
		{
			menu.clear();
			getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		}
		
		/*if(currentPosition == 2)
		{
			getSupportActionBar().setNavigationMode(drawerOpen? ActionBar.NAVIGATION_MODE_STANDARD : ActionBar.NAVIGATION_MODE_LIST);
		}*/
		
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(getMenuItem(item))) 
		{
			return true;
		}

		// Handle action buttons
		/*switch (item.getItemId()) 
		{
			case R.id.action_websearch:
				// create intent to perform web search for this planet
				Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
				intent.putExtra(SearchManager.QUERY, getSupportActionBar()
						.getTitle());
				// catch event that there's no activity to handle intent
				if (intent.resolveActivity(getPackageManager()) != null) {
					startActivity(intent);
				} else {
					Toast.makeText(this, R.string.app_not_available,
							Toast.LENGTH_LONG).show();
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}*/
		
		return super.onOptionsItemSelected(item);
	}

	private android.view.MenuItem getMenuItem(final MenuItem item) 
	{
		return MainActivityMenuItemView.GetMenuItemView(item);
	}


	private void selectItem(int position) 
	{
		// update the main content by replacing fragments
		   FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	        // Locate Position
	        switch (position) {
	        case 2:
	            ft.replace(R.id.content_frame, lignesFragment);
	            break;
	        case 3:
	            ft.replace(R.id.content_frame, favFragment);
	            break;
	        default:
	    		Bundle args = new Bundle();
	    		args.putInt(DefaultFragment.ITEMNUMBER, position);
	    		DefaultFragment def = new DefaultFragment();
	    		def.setArguments(args);
	        	ft.replace(R.id.content_frame, def);
	        	break;
	        }
	        ft.commit();

		/*FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, currentFragment).commit();*/

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mMenuArray[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
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

	
	/*@Override
	 The click listner for filterView in the actionbar*/
	/*public boolean onNavigationItemSelected(int arg0, long arg1)
	{
		if(isFirstFilterCallBack)
			isFirstFilterCallBack = false;
		else
			currentFragment.SetLignesFragmentFilter(arg0);
		
		return true;
	}*/
	
	
	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener 
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
		{
			currentPosition = position; 
			selectItem(position);
		}
	}
}