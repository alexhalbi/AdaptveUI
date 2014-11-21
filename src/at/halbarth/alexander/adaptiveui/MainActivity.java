package at.halbarth.alexander.adaptiveui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    //TODO Add user?
    private String[] users;
    //TODO save curr logged in user! to sharedpref
    private int userLoggedIn; 
    
    private FavoriteMap favoriteMap;
    private FragmentNameMap fragmntNameMap;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        loadUsers();
        
        try {
        	favoriteMap = getFavoriteMap();
        } catch(Exception e) {
        	Log.e("FavoriteMap","New",e);
        	favoriteMap = new FavoriteMap();
        }
        
        if(favoriteMap==null) {
        	Log.d("FavoriteMap","New");
        	favoriteMap = new FavoriteMap();
        }
        
        fragmntNameMap = new FragmentNameMap(getApplicationContext(), favoriteMap);
        
        setContentView(R.layout.activity_main);
        
        mTitle = getTitle();
        
        createNavigationDrawer();
        
        Configuration config = getBaseContext().getResources().getConfiguration();

        String lang = settings.getString(getString(R.string.pref_locale), "");
        if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang))
        {
            locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
        
        mNavigationDrawerFragment.updateElements(fragmntNameMap.getSortedNames(users[userLoggedIn]));
    }
	
	private void loadUsers() {
        userLoggedIn = 1;
        users = getResources().getStringArray(R.array.user_array);
	}
	
	@Override
	protected void onPause() {
		saveFavoriteMap(favoriteMap);
		super.onPause();
	}

	public void createNavigationDrawer() {
		mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		
		// Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        
        loadUsers();
	}

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(favoriteMap==null) {
        	Log.d("FavoriteMap","favoriteMap is null...");
        } else {
            Log.d("FavoriteMap","favoriteMap" + favoriteMap.toString());
        }
        Fragment frgmt = favoriteMap.getPosition(users[userLoggedIn], position);
        Log.d("FavoriteMap","frgmt="+frgmt.toString());
		fragmentManager.beginTransaction()
		.replace(R.id.container, frgmt)
		.commit();
		if(mNavigationDrawerFragment!=null)
			mNavigationDrawerFragment.updateElements(fragmntNameMap.getSortedNames(users[userLoggedIn]));
    }

    public void onSectionAttached(int number) {
    	mTitle = fragmntNameMap.getNamePos(users[userLoggedIn], number);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }
    
    public void changeUser(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose_user);
        builder.setItems(users, new DialogInterface.OnClickListener() {
            @SuppressLint("NewApi") @Override
            public void onClick(DialogInterface dialog, int id) {
                userLoggedIn = id;
                invalidateOptionsMenu();
                onNavigationDrawerItemSelected(0);
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            try {
    	        MenuItem user_indicator = menu.getItem(0);
    	        for(int i = 1; user_indicator != null;i++) {
    	        	if(user_indicator.getItemId()==R.id.user_indicator) {
    	        		user_indicator.setTitle(users[userLoggedIn]);
    	        		break;
    	        	}
    	        	user_indicator = menu.getItem(i);
    	        }
            } catch (IndexOutOfBoundsException e) {
            	Log.d("Exeption in onCreateOptionsMenu", "IndexOutOfBounds", e);
            }
            return true;
        }
        
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.change_user) {
        	changeUser();
        }
        return super.onOptionsItemSelected(item);
    }
    
    private Locale locale = null;

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        if (locale != null)
        {
            newConfig.locale = locale;
            Locale.setDefault(locale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }
    
    public void localeGER(View v) {
    	Locale locale = new Locale("de"); 
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        createNavigationDrawer();
        Toast.makeText(getApplicationContext(), getString(R.string.restart), Toast.LENGTH_LONG).show();
    }
    
    public void localeENG(View v) {
    	Locale locale = new Locale("en"); 
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        createNavigationDrawer();
        Toast.makeText(getApplicationContext(), getString(R.string.restart), Toast.LENGTH_LONG).show();
    }
    
    public void saveFavoriteMap(FavoriteMap favoriteMap) {
    	 Log.d("FavoriteMap","saveFavoriteMap");
    	 ObjectOutputStream oos = null;
    	 FileOutputStream fout = null;
    	 try{
    	        fout = openFileOutput("favoriteMap", Context.MODE_PRIVATE);
    	        oos = new ObjectOutputStream(fout);
    	        oos.writeObject(favoriteMap);
    	        Log.d("FavoriteMap","written=" + favoriteMap.toString());
    	 } catch (Exception e) {
    		 Log.e("FavoriteMap","saveFavoriteMap",e);
    	 }finally {
    	   if(oos  != null){
    	     try {
				oos.close();
			} catch (IOException e) {
				Log.e("FavoriteMap","saveFavoriteMap",e);
			}
    	   } 
    	 }
    }
    
    public FavoriteMap getFavoriteMap() {
    	ObjectInputStream objectinputstream = null;
    	FavoriteMap favoriteMap = null;
    	 try {
    		 FileInputStream streamIn = openFileInput("favoriteMap");
    	        objectinputstream = new ObjectInputStream(streamIn);
    	        favoriteMap = (FavoriteMap) objectinputstream.readObject();
    	    	 Log.d("FavoriteMap","read=" + favoriteMap.toString());

    	   } catch (Exception e) {
    		   Log.e("FavoriteMap","getFavoriteMap",e);
    	   }finally {
    	     if(objectinputstream != null){
    	        try {
					objectinputstream .close();
				} catch (IOException e) {
					Log.e("FavoriteMap","getFavoriteMap",e);
				}
    	     } 
    	   }
    	 return favoriteMap;
    }

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		saveFavoriteMap(favoriteMap);
		super.onSaveInstanceState(outState);
	}
}
