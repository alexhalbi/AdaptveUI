package at.halbarth.alexander.adaptiveui;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.util.Log;

@SuppressWarnings("rawtypes")
public class FragmentNameMap {
	private Map<Class,String> names;
	private Resources ressources;
	private FavoriteMap favoriteMap;
	
	public FragmentNameMap(Context appContext, FavoriteMap favoriteMap) {
		this.ressources = appContext.getResources();
		this.favoriteMap = favoriteMap;
		names = new HashMap<Class, String>();
		
		String[] frgmts = ressources.getStringArray(R.array.fragments);
		String[] names = ressources.getStringArray(R.array.fragment_names);
		
		if(frgmts.length!=names.length) {
			throw new InvalidParameterException("Resource Name and Fragment Length do not match!");
		}
		for(int i = 0; i<frgmts.length;i++) {
			try {
				this.names.put(Class.forName(frgmts[i]), names[i]);
			} catch (ClassNotFoundException e) {
				Log.e("FragmentNameMap", "Fragment " + frgmts[i] + " not found, skipped", e);
			}
		}
	}
	
	public String getNameFrgmt(Fragment frgmnt) {
		return names.get(frgmnt.getClass());
	}
	
	public String getNamePos(String user, int position) {
		return getNameFrgmt(favoriteMap.getPositionNoIncr(user, position));
	}
	
	public String[] getSortedNames(String user) {
		String[] list = new String[names.size()];
		for(int i = 0;list.length>i;i++) {
			list[i] = getNamePos(user, i);
		}
		return list;
	}
}
