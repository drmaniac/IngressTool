package de.pieczewski.ingresstool;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CommActivity extends FragmentActivity {
	
	private static final String TAG = CommActivity.class.getName();

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comm);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.comm, menu);
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new CommView();
			Bundle args = new Bundle();
			
			switch (position) {
				case 0:
					args.putString(CommView.ARG_COMM_LAYER, CommView.COMM_LAYER_FULL);
					fragment.setArguments(args);
				break;
				
				case 1:
					args.putString(CommView.ARG_COMM_LAYER, CommView.COMM_LAYER_ALL_CHAT);
					fragment.setArguments(args);
					break;
				case 2:
					args.putString(CommView.ARG_COMM_LAYER, CommView.COMM_LAYER_FACTION_CHAT);
					fragment.setArguments(args);
					break;
				case 3:
					args.putString(CommView.ARG_COMM_LAYER, CommView.COMM_LAYER_ACTION);
					fragment.setArguments(args);
					break;
			}
			
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.comm_section1).toUpperCase(l);
			case 1:
				return getString(R.string.comm_section2).toUpperCase(l);
			case 2:
				return getString(R.string.comm_section3).toUpperCase(l);
			case 3:
				return getString(R.string.comm_section4).toUpperCase(l);
			}
			return null;
		}
	}

}
