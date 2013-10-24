package com.example.twitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.twitterclient.R;
import com.example.twitterclient.fragments.TimelineFragment;
import com.example.twitterclient.utils.HandlesErrors;

public class TimelineActivity extends FragmentActivity implements HandlesErrors {

	TimelineFragment homeTimelineFragment;
	TimelineFragment mentionsTimelineFragment;
	TimelineFragment currentTimelineFragment;

	public void showHomeTimelineFragment() {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.timelineLayout, homeTimelineFragment)
				.commit();
		markAsCurrentTimelineFragment(homeTimelineFragment);
	}

	public void showMentionsTimelineFragment() {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.timelineLayout, mentionsTimelineFragment)
				.commit();
		markAsCurrentTimelineFragment(mentionsTimelineFragment);
	}

	public void markAsCurrentTimelineFragment(TimelineFragment currentTimelineFragment) {
		this.currentTimelineFragment = currentTimelineFragment;
	}

	public void startComposeActivity(MenuItem item) {
		Intent intent = new Intent(this, ComposeActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		homeTimelineFragment = new TimelineFragment(TimelineFragment.TimelineType.HOME, this);
		mentionsTimelineFragment = new TimelineFragment(TimelineFragment.TimelineType.MENTIONS, this);
		if (savedInstanceState == null)
			showHomeTimelineFragment();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		this.currentTimelineFragment.loadNewerTweets();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_compose, menu);
		return true;
	}

	@Override
	public void onError(Throwable error) {
		Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
		Log.e("FIXME", "TimelineType Error", error);
	}

}
