package com.example.twitterclient.handlers;

import android.widget.ListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * Created on 10/22/13.
 */
public class LoadTweetHandler implements
		PullToRefreshBase.OnLastItemVisibleListener,
		PullToRefreshBase.OnRefreshListener<ListView> {

	private final CanLoadTweets loader;

	public static interface CanLoadTweets {
		public void loadRecentTweets();

		public void loadOlderTweets();
	}

	public LoadTweetHandler(CanLoadTweets loader) {
		this.loader = loader;
	}

	@Override
	public void onLastItemVisible() {
		loader.loadOlderTweets();
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		loader.loadRecentTweets();
	}

}
