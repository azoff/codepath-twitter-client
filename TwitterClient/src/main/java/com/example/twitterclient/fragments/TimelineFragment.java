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
import com.example.twitterclient.models.User;
import com.example.twitterclient.net.TwitterClient;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * Created on 10/23/13.
 */
public class TimelineFragment extends Fragment implements
		PullToRefreshBase.OnRefreshListener<ListView>,
		PullToRefreshBase.OnLastItemVisibleListener,
		AsyncTweetListHandler.OnTweetListListener,
		TweetListAdapter.HandlesTweet {

	TweetListAdapter listAdapter;
	PullToRefreshListView listView;

	public static enum TimelineType {
		HOME, MENTIONS, USER
	}

	public static int getNameResource(TimelineType type) {
		if (type.equals(TimelineType.HOME))
			return R.string.home;
		else if (type.equals(TimelineType.MENTIONS))
			return R.string.mentions;
		else if (type.equals(TimelineType.USER))
			return R.string.profile;
		return -1;
	}

	public static int getIconResource(TimelineType type) {
		if (type.equals(TimelineType.HOME))
			return R.drawable.ic_home_symbol;
		else if (type.equals(TimelineType.MENTIONS))
			return R.drawable.ic_at_symbol;
		else if (type.equals(TimelineType.USER))
			return R.drawable.ic_action_profile;
		return -1;
	}

	final User user;
	final TimelineType timelineType;
	private TweetListAdapter.HandlesTweet tweetHandler;

	public TimelineFragment(TimelineType type) {
		this.user = null;
		this.timelineType = type;
	}

	public TimelineFragment(User user) {
		this.user = user;
		timelineType = TimelineType.USER;
	}

	public void setTweetHandler(TweetListAdapter.HandlesTweet tweetHandler) {
		this.tweetHandler = tweetHandler;
	}

	public void recallLastTweets() {
		List<Tweet> tweets = null;
		Integer limit = Integer.valueOf(getString(R.integer.page_size));
		if (timelineType == TimelineType.HOME)
			tweets = Tweet.getRecentTweets(limit);
		else if (timelineType == TimelineType.MENTIONS)
			tweets = Tweet.getRecentMentions(limit);
		else if (timelineType == TimelineType.USER)
			tweets = Tweet.getRecentUserTweets(limit, user);
		if (tweets != null)
			onTweetList(tweets);
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
		Integer count = Integer.valueOf(getString(R.integer.page_size));
		if (timelineType == TimelineType.HOME)
			client.getHomeTimeline(count, loadBefore, loadAfter, handler);
		else if (timelineType == TimelineType.MENTIONS)
			client.getMentionsTimeline(count, loadBefore, loadAfter, handler);
		else if (timelineType == TimelineType.USER)
			client.getUserTimeline(count, user, loadBefore, loadAfter, handler);
	}

	public void endRefresh() {
		if (listView.isRefreshing())
			listView.onRefreshComplete();
	}

	@Override
	public void onResume() {
		super.onResume();
		recallLastTweets();
		loadNewerTweets();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof TweetListAdapter.HandlesTweet))
			throw new IllegalArgumentException("Activity must be able to handle Timeline tweets");
		setTweetHandler((TweetListAdapter.HandlesTweet) activity);
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
	public User getCurrentUser() {
		return tweetHandler.getCurrentUser();
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
		tweetHandler.onError(error);
	}

	@Override
	public void onProfileClick(User user) {
		tweetHandler.onProfileClick(user);
	}

	@Override
	public void onReplyClick(Tweet tweet) {
		tweetHandler.onReplyClick(tweet);
	}
}
