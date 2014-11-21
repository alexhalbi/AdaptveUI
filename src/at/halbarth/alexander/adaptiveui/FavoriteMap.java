package at.halbarth.alexander.adaptiveui;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.support.v4.app.Fragment;
import android.util.Log;

@SuppressWarnings("rawtypes")
public class FavoriteMap implements Serializable {
	private static final long serialVersionUID = -8423069670771879104L;
	private Map<String, Map<Class, Integer>> user;
	private Map<Class,String> names;
	private Class[] fragments;
	
	public FavoriteMap(String[] fragments, String[] fragmentNames) {
		this.fragments = new Class[fragments.length];
		for(int i = 0; i<fragments.length;i++) {
			try {
				this.fragments[i] = Class.forName(fragments[i]);
			} catch (ClassNotFoundException e) {
				Log.e("FragmentMap", "Fragment " + fragments[i] +" not found, skipped", e);
			}
		}
		names = new HashMap<Class, String>();
		updateNames(fragments, fragmentNames);
	}
	
	public void updateNames(String[] fragments, String[] fragmentNames) {
		if(fragments.length!=fragmentNames.length) {
			throw new InvalidParameterException("Resource Name and Fragment Length do not match!");
		}
		for(int i = 0; i<fragments.length;i++) {
			try {
				this.names.put(Class.forName(fragments[i]), fragmentNames[i]);
			} catch (ClassNotFoundException e) {
				Log.e("FragmentNameMap", "Fragment " + fragments[i] + " not found, skipped", e);
			}
		}
	}

	public Fragment getPosition(String user, int position) {
		return getPosition(user, position, true);
	}

	private Fragment getPosition(String user, int position, boolean incr) {
		Log.d("FavoriteMap", "getPosition" + position + '(' + user + ')');
		Map<Class, Integer> frgmntCnt = null;
		if (this.user != null) {
			frgmntCnt = this.user.get(user);
		} else {
			this.user = new HashMap<String, Map<Class, Integer>>();
		}

		if (frgmntCnt == null) {
			frgmntCnt = new HashMap<Class, Integer>(fragments.length);
			for(Class c:fragments) {
				frgmntCnt.put(c, 0);
			}
		}

		FragmentComparator bvc = new FragmentComparator(frgmntCnt);
		TreeMap<Class, Integer> sorted_map = new TreeMap<Class, Integer>(bvc);
		sorted_map.putAll(frgmntCnt);

		Entry<Class, Integer> entr = getNEntrie(position, sorted_map);

		if (incr) {
			frgmntCnt.put(entr.getKey(), entr.getValue() + 1);
		}

		this.user.put(user, frgmntCnt);

		try {
			Log.d("FavoriteMap", "newInstance");
			Fragment f = (Fragment) entr.getKey().newInstance();
			Log.d("FavoriteMap", "newInstance Class=" + f.getClass().getName());
			return f;
		} catch (IllegalAccessException e) {
			Log.e("FavoriteMap", "newInstance", e);
		} catch (IllegalArgumentException e) {
			Log.e("FavoriteMap", "newInstance", e);
		} catch (InstantiationException e) {
			Log.e("FavoriteMap", "newInstance", e);
		}
		return null;
	}

	public static Map.Entry<Class, Integer> getNEntrie(int n,
			Map<Class, Integer> source) {
		int count = 0;
		for (Map.Entry<Class, Integer> entry : source.entrySet()) {
			if (count == n) {
				return entry;
			}
			count++;
		}
		return null;
	}
	
	
	public String getNameFrgmt(Fragment frgmnt) {
		return names.get(frgmnt.getClass());
	}
	
	public String getNamePos(String user, int position) {
		return getNameFrgmt(getPosition(user, position,false));
	}
	
	public String[] getSortedNames(String user) {
		String[] list = new String[names.size()];
		for(int i = 0;list.length>i;i++) {
			list[i] = getNamePos(user, i);
		}
		return list;
	}
}

@SuppressWarnings("rawtypes")
class FragmentComparator implements Comparator<Class> {

	Map<Class, Integer> base;

	public FragmentComparator(Map<Class, Integer> frgmntCnt) {
		this.base = frgmntCnt;
	}

	@Override
	public int compare(Class a, Class b) {
		if (base.get(a) >= base.get(b)) {
			return -1;
		} else {
			return 1;
		}
	}
}
