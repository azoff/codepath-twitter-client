package com.example.twitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.example.twitterclient.R;
import com.example.twitterclient.fragments.TimelineFragment;
import com.example.twitterclient.utils.HandlesErrors;

public class TimelineActivity extends SherlockFragmentActivity implements
		ActionBar.TabListener,
		HandlesErrors {

	TimelineFragment activeFragment;

	public void startComposeActivity(MenuItem item) {
		Intent intent = new Intent(this, ComposeActivity.class);
		startActivity(intent);
	}

	public void createTabs(Bundle savedInstanceState) {

		ActionBar actionBar = getSupportActionBar();
		if (actionBar == null) return;

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);

		for (TimelineFragment.TimelineType type : TimelineFragment.TimelineType.values()) {
			TimelineFragment tag = new TimelineFragment(type, this);
			actionBar.addTab(actionBar
					.newTab().setText(tag.getName())
					.setIcon(tag.getIconResource())
					.setTag(tag)
					.setTabListener(this)
			);
		}

		if (savedInstanceState == null)
			actionBar.selectTab(actionBar.getTabAt(0));

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		createTabs(savedInstanceState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		activeFragment.loadNewerTweets();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_compose, menu);
		return true;
	}

	@Override
	public void onError(Throwable error) {
		Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
		Log.e("FIXME", "TimelineType Error", error);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
		TimelineFragment fragment = (TimelineFragment) tab.getTag();
		ft.replace(R.id.timelineLayout, fragment).commit();
		activeFragment = fragment;
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
		onTabSelected(tab, ft);
	}
}
