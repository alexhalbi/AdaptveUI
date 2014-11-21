package at.halbarth.alexander.adaptiveui.fragments;

import java.util.Locale;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import at.halbarth.alexander.adaptiveui.R;

public class ChangeLocale extends Fragment {
	
	private final Locale de = new Locale("de");
	private final Locale en = new Locale("en");
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.fragment_change_locale, container, false);
    	
    	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(v.getContext());

        Configuration config = v.getContext().getResources().getConfiguration();

        String lang = settings.getString(getString(R.string.pref_locale), "");
        if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang))
        {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            ((Button) v.findViewById(R.id.button_de)).setText(de.getDisplayLanguage(Locale.getDefault()));
            ((Button) v.findViewById(R.id.button_en)).setText(en.getDisplayLanguage(Locale.getDefault()));
        }
        
        return v;
    }
}