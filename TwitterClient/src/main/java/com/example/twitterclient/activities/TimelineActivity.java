package com.example.twitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.twitterclient.R;
import com.example.twitterclient.adapters.TweetListAdapter;
import com.example.twitterclient.apps.TwitterApp;
import com.example.twitterclient.fragments.TimelineFragment;
import com.example.twitterclient.models.Tweet;
import com.example.twitterclient.models.User;

public class TimelineActivity extends SherlockFragmentActivity implements
		ActionBar.TabListener,
		TweetListAdapter.HandlesTweet {

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
			if (type == TimelineFragment.TimelineType.USER) continue;
			TimelineFragment tag = new TimelineFragment(type);
			actionBar.addTab(actionBar
					.newTab().setText(TimelineFragment.getNameResource(type))
					.setIcon(TimelineFragment.getIconResource(type))
					.setTag(tag).setTabListener(this)
			);
		}

		if (savedInstanceState == null)
			actionBar.selectTab(actionBar.getTabAt(0));

	}

	public void startCurrentProfileActivity(MenuItem item) {
		Intent intent = new Intent(this, ProfileActivity.class);
		intent.putExtra("id", TwitterApp.getCurrentUser().getId());
		startActivity(intent);
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
		inflater.inflate(R.menu.menu_timeline, menu);
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
		ft.replace(R.id.timelineLayout, fragment);
		activeFragment = fragment;
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
	}

	@Override
	public User getCurrentUser() {
		return TwitterApp.getCurrentUser();
	}

	@Override
	public void onProfileClick(User user) {
		Intent intent = new Intent(this, ProfileActivity.class);
		intent.putExtra("id", user.getId());
		startActivity(intent);
	}

	@Override
	public void onReplyClick(Tweet tweet) {
		Intent intent = new Intent(this, ComposeActivity.class);
		intent.putExtra("reply_to_id", tweet.getId());
		startActivity(intent);
	}

}
