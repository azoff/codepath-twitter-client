package com.example.twitterclient.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.twitterclient.R;
import com.example.twitterclient.adapters.TweetListAdapter;
import com.example.twitterclient.apps.TwitterApp;
import com.example.twitterclient.handlers.AsyncTweetListHandler;
import com.example.twitterclient.models.Tweet;
import com.example.twitterclient.net.TwitterClient;
import com.example.twitterclient.utils.HandlesErrors;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * Created on 10/23/13.
 */
public class TimelineFragment extends Fragment implements
		PullToRefreshBase.OnRefreshListener<ListView>,
		PullToRefreshBase.OnLastItemVisibleListener,
		AsyncTweetListHandler.OnTweetListListener {

	PullToRefreshListView listView;
	TweetListAdapter listAdapter;

	public static enum TimelineType {
		HOME, MENTIONS
	}

	public static int getNameResource(TimelineType type) {
		if (type.equals(TimelineType.HOME))
			return R.string.home;
		else if (type.equals(TimelineType.MENTIONS))
			return R.string.mentions;
		return -1;
	}

	public static int getIconResource(TimelineType type) {
		return android.R.drawable.ic_menu_search;
	}

	final TimelineType timelineType;
	final HandlesErrors errorHandler;

	public TimelineFragment(TimelineType timelineType, HandlesErrors errorHandler) {
		this.timelineType = timelineType;
		this.errorHandler = errorHandler;
	}

	public void loadNewerTweets() {
		Tweet loadBefore = null;
		if (listAdapter.getCount() > 0)
			loadBefore = listAdapter.getItem(0);
		loadTweets(loadBefore, null);
	}

	public void loadOlderTweets() {
		Tweet loadAfter = null;
		int count = listAdapter.getCount();
		if (count > 0)
			loadAfter = listAdapter.getItem(count - 1);
		loadTweets(null, loadAfter);
	}

	public void loadTweets(Tweet loadBefore, Tweet loadAfter) {
		TwitterClient client = TwitterApp.getClient();
		AsyncTweetListHandler handler = new AsyncTweetListHandler(this);
		if (timelineType == TimelineType.HOME)
			client.getHomeTimeline(loadBefore, loadAfter, handler);
		else if (timelineType == TimelineType.MENTIONS)
			client.getMentionsTimeline(loadBefore, loadAfter, handler);
	}

	public void endRefresh() {
		if (listView.isRefreshing())
			listView.onRefreshComplete();
	}

	@Override
	public void onResume() {
		super.onResume();
		loadNewerTweets();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listAdapter = new TweetListAdapter(activity, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_timeline, container, false);
		if (v == null) {
			onError(new InflateException("Unable to inflate TweetList"));
			return null;
		}

		listView = (PullToRefreshListView) v.findViewById(R.id.listView);
		listView.setOnLastItemVisibleListener(this);
		listView.setOnRefreshListener(this);
		listView.setAdapter(listAdapter);

		return v;

	}

	@Override
	public void onTweetList(List<Tweet> tweets) {
		listAdapter.addAll(tweets);
		listAdapter.sort(new Tweet.ByDateCreatedDesc());
		endRefresh();
	}

	@Override
	public void onLastItemVisible() {
		loadOlderTweets();
	}

	@Override
	public void onRefresh(PullToRefreshBase refreshView) {
		loadNewerTweets();
	}

	@Override
	public void onError(Throwable error) {
		endRefresh();
		errorHandler.onError(error);
	}

}
