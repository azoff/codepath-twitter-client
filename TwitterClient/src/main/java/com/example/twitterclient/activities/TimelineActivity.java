package com.example.twitterclient.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.twitterclient.R;
import com.example.twitterclient.adapters.TweetListAdapter;
import com.example.twitterclient.apps.TwitterApp;
import com.example.twitterclient.handlers.AsyncTweetListHandler;
import com.example.twitterclient.models.Tweet;

import java.util.List;

public class TimelineActivity extends Activity
		implements AsyncTweetListHandler.HandlesTweetList {

	private TweetListAdapter tweetList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		tweetList = new TweetListAdapter(this, R.layout.lv_item_tweet, R.id.lvTweets);
		getRecentTweets();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getRecentTweets();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_compose, menu);
		return true;
	}

	@Override
	public void onTweetList(List<Tweet> tweets) {
		for (int i = tweets.size() - 1; i >= 0; i--)
			tweetList.insert(tweets.get(i), 0);
	}

	@Override
	public void onError(Throwable error) {
		Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
		Log.e("FIXME", "Timeline Error", error);
	}

	public void getRecentTweets() {
		Integer count = tweetList.getCount();
		Tweet last = count > 0 ? tweetList.getItem(0) : null;
		TwitterApp.getClient().getHomeTimeline(last, new AsyncTweetListHandler(this));
	}

	public void startComposeActivity(MenuItem item) {
		Intent intent = new Intent(this, ComposeActivity.class);
		startActivity(intent);
	}
}
