package at.halbarth.alexander.adaptiveui.elements;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import at.halbarth.alexander.adaptiveui.R;

public class LinksWIFI extends Fragment {
	private Button button1;
	private Button button2;
	private Button button3;
	private TextView wifi_label;
	private NetworkInfo mWifi;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ConnectivityManager connManager = (ConnectivityManager) getActivity()
				.getApplication().getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		View v = inflater.inflate(R.layout.fragment_links, container, false);

		button1 = (Button) v.findViewById(R.id.button1);
		button2 = (Button) v.findViewById(R.id.button2);
		button3 = (Button) v.findViewById(R.id.button3);
		wifi_label = (TextView) v.findViewById(R.id.wifi_label);

		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Locale current = getResources().getConfiguration().locale;
				if (current.getISO3Country().equalsIgnoreCase("DEU"))
					startActivity(new Intent(Intent.ACTION_VIEW, Uri
							.parse("http://www.google.de")));
				else
					startActivity(new Intent(Intent.ACTION_VIEW, Uri
							.parse("http://www.google.com")));
			}
		});
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Locale current = getResources().getConfiguration().locale;
				if (current.getISO3Country().equalsIgnoreCase("DEU"))
					startActivity(new Intent(Intent.ACTION_VIEW, Uri
							.parse("http://www.amazon.de")));
				else
					startActivity(new Intent(Intent.ACTION_VIEW, Uri
							.parse("http://www.amazon.com")));
			}
		});
		button3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse("http://www.spengergasse.at")));
			}
		});
		colorButtons();

		return v;
	}

	public LinksWIFI() {
		super();
	}

	private void colorButtons() {
		colorButtons(!mWifi.isConnected());
	}

	private void colorButtons(boolean b) {
		if (b) {
			button1.setBackgroundColor(Color.RED);
			button2.setBackgroundColor(Color.RED);
			button3.setBackgroundColor(Color.RED);
			wifi_label.setVisibility(TextView.VISIBLE);
		} else {
			button1.setBackgroundColor(Color.GRAY);
			button2.setBackgroundColor(Color.GRAY);
			button3.setBackgroundColor(Color.GRAY);
			wifi_label.setVisibility(TextView.INVISIBLE);
		}
	}
}