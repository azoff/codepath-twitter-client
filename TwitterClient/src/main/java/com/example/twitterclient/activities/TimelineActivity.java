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
import com.example.twitterclient.handlers.LoadTweetHandler;
import com.example.twitterclient.models.Tweet;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.util.List;

public class TimelineActivity extends Activity implements
		AsyncTweetListHandler.HandlesTweetList,
		LoadTweetHandler.CanLoadTweets {

	private TweetListAdapter tweetList;
	private PullToRefreshListView lvTweets;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		tweetList = new TweetListAdapter(this, R.layout.lv_item_tweet);

		LoadTweetHandler handler = new LoadTweetHandler(this);
		lvTweets = (PullToRefreshListView) findViewById(R.id.lvTweets);
		lvTweets.setOnLastItemVisibleListener(handler);
		lvTweets.setOnRefreshListener(handler);
		lvTweets.setAdapter(tweetList);

		if (!recallTweets())
			loadTweets(null, null);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		loadRecentTweets();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_compose, menu);
		return true;
	}

	public void appendTweets(List<Tweet> tweets) {
		tweetList.addAll(tweets);
	}

	public void prependTweets(List<Tweet> tweets) {
		for (int i = tweets.size() - 1; i >= 0; i--)
			tweetList.insert(tweets.get(i), 0);
	}

	@Override
	public void onTweetList(List<Tweet> tweets, boolean prepend) {
		if (prepend) {
			prependTweets(tweets);
			lvTweets.onRefreshComplete();
		} else {
			appendTweets(tweets);
		}
	}

	@Override
	public void onError(Throwable error) {
		Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
		Log.e("FIXME", "Timeline Error", error);
	}

	public void loadRecentTweets() {
		Tweet loadBefore = null;
		if (tweetList.getCount() > 0)
			loadBefore = tweetList.getItem(0);
		loadTweets(loadBefore, null);
	}

	public void loadOlderTweets() {
		Tweet loadAfter = null;
		int count = tweetList.getCount();
		if (count > 0)
			loadAfter = tweetList.getItem(count - 1);
		loadTweets(null, loadAfter);
	}

	public void loadTweets(Tweet loadBefore, Tweet loadAfter) {
		JsonHttpResponseHandler handler = new AsyncTweetListHandler(this, loadBefore != null);
		TwitterApp.getClient().getHomeTimeline(loadBefore, loadAfter, handler);
	}

	public boolean recallTweets() {
		List<Tweet> tweets = Tweet.selectRecent(25);
		if (tweets.size() <= 0) return false;
		appendTweets(tweets);
		return true;
	}

	public void startComposeActivity(MenuItem item) {
		Intent intent = new Intent(this, ComposeActivity.class);
		startActivity(intent);
	}
}
