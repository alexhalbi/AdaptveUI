package at.halbarth.alexander.adaptiveui;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.support.v4.app.Fragment;
import android.util.Log;

@SuppressWarnings("rawtypes")
public class FavoriteMap implements Serializable {
	private static final long serialVersionUID = -6974625372446467298L;
	private Map<String, Map<Class, Integer>> user;
	private Class[] fragments;
	
	public FavoriteMap(String[] fragments) {
		this.fragments = new Class[fragments.length];
		for(int i = 0; i<fragments.length;i++) {
			try {
				this.fragments[i] = Class.forName(fragments[i]);
			} catch (ClassNotFoundException e) {
				Log.e("FragmentMap", "Fragment " + fragments[i] +" not found, skipped", e);
			}
		}
	}

	public Fragment getPosition(String user, int position) {
		return getPosition(user, position, true);
	}

	public Fragment getPositionNoIncr(String user, int position) {
		return getPosition(user, position, false);
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
