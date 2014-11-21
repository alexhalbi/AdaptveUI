package at.halbarth.alexander.adaptiveui;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import at.halbarth.alexander.adaptiveui.elements.ChangeLocale;
import at.halbarth.alexander.adaptiveui.elements.ExampleEditTxt;
import at.halbarth.alexander.adaptiveui.elements.ExamplePic;
import at.halbarth.alexander.adaptiveui.elements.ExampleTxt;
import at.halbarth.alexander.adaptiveui.elements.LinksWIFI;

@SuppressWarnings("rawtypes")
public class FragmentNameMap {
	private Map<Class,Integer> names;
	private Resources ressources;
	private FavoriteMap favoriteMap;
	
	public FragmentNameMap(Context appContext, FavoriteMap favoriteMap) {
		this.ressources = appContext.getResources();
		this.favoriteMap = favoriteMap;
		names = new HashMap<Class, Integer>();
		names.put(ExamplePic.class, R.string.title_section1);
		names.put(ExampleTxt.class, R.string.title_section2);
		names.put(LinksWIFI.class, R.string.title_section3);
		names.put(ChangeLocale.class, R.string.title_section4);
		names.put(ExampleEditTxt.class, R.string.title_section5);
	}
	
	public String getNameFrgmt(Fragment frgmnt) {
		return ressources.getString(names.get(frgmnt.getClass()));
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
